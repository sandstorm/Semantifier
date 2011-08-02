package semantifier

/*
 * This file is part of "Semantifier".
 *
 * Copyright 2011 Sebastian Kurfürst
 *
 * Semantifier is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Semantifier is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Semantifier.  If not, see <http://www.gnu.org/licenses/>.
 */

import groovyx.gpars.GParsPool
import semantifier.linkification.*
import ws.palladian.extraction.entity.ner.Annotation
import ws.palladian.extraction.entity.ner.Annotations
import ws.palladian.extraction.entity.ner.tagger.OpenCalaisNER
import ws.palladian.extraction.entity.ner.tagger.AlchemyNER
import ws.palladian.classification.language.LanguageClassifier


/**
 * Service which can do annotation of longer text. This annotation happens in multiple steps:
 * <ol><li>Find out the language of the input text</li>
 * <li>Determine the NER service to use using <tt>ner.annotation.languageToNerServiceMapping</tt>, and call this NER service.</li>
 * <li>Then, use the linkification services in the order specified by <tt>ner.linkification.linkificationOrder</tt>. The first returned results will be used.</li>
 * </ol>
 */
class AnnotationService {

	def grailsApplication

	LanguageClassifier languageClassifier

	/**
	 * Annotate the given text.
	 *
	 * @param text
	 * @return Map
	 */
	public def annotate(String text) {
		def language = languageClassifier.classify(text);
		Annotations rawAnnotations = doNamedEntityRecognition(text, language)

		return [language: language, entities: enrichAndNormalizeRawAnnotations(rawAnnotations)];
	}

	/**
	 * Do named entity recognition, based on the language. 
	 * @param text
	 * @param language
	 * @return Annotations the annotations found
	 */
	private Annotations doNamedEntityRecognition(def text, def language) {
		Annotations annotations = []
		def languageToNerServiceMapping = grailsApplication.config.ner.annotation.languageToNerServiceMapping
		def nerName = languageToNerServiceMapping[language]
		def ner = grailsApplication.mainContext.getBean(nerName)
		return ner.getAnnotations(text)
	}

	/**
	 * Enrich the raw annotations in a multi-threaded way.
	 * 
	 * @param rawAnnotations
	 * @return Map annotations
	 */
	private def enrichAndNormalizeRawAnnotations(Annotations rawAnnotations) {
		def processedAnnotations = []

		def numberOfThreads = rawAnnotations.size()
		if (numberOfThreads > 10) numberOfThreads = 10
		
		GParsPool.withPool(numberOfThreads) {
			processedAnnotations = rawAnnotations.collectParallel { annotation ->
				return linkifySingleAnnotation(annotation);
			}
		}
		return processedAnnotations;
	}
	
	/**
	 * Helper function which should linkify a single annotation.
	 *
	 * @param annotation
	 * @return Map linkified annotation, if successful.
	 */
	private def linkifySingleAnnotation(Annotation annotation) {
		def output = [
			entity: annotation.entity,
			offset: annotation.offset,
			length: annotation.length,
			mostLikelyTagName: annotation.mostLikelyTagName,
		]
		def linkificationResults = []
		output.links = linkificationResults
		for (String linkifierName in grailsApplication.config.ner.linkification.linkificationOrder.tokenize(',')) {
			AbstractLinkifier linkifier = grailsApplication.mainContext.getBean(linkifierName + 'LinkificationService')
			if (!linkifier) throw new Exception("TODO: Linkifier ${linkifierName} not found!")

			def linkificationResult = linkifier.linkify(annotation)
			if (linkificationResult != null) {
				linkificationResult.each {
					it.linkifierName = linkifier.name
				}
				linkificationResults.addAll(linkificationResult)
				if (linkificationResult.size() > 0 && linkifier.shouldAbortWhenResultsFound()) {
					return output;
				}
			}
		}

		return output;
	}
}