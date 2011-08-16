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
import groovyx.gpars.ParallelEnhancer


/**
 * Service which can do annotation of longer text. This annotation happens in multiple steps:
 * <ol><li>Find out the language of the input text</li>
 * <li>Determine the NER service to use using <tt>ner.annotation.languageToNerServiceMapping</tt>, and call this NER service.</li>
 * <li>Then, use the linkification services in the order specified by <tt>ner.linkification.linkificationOrder</tt>. The first returned results will be used.</li>
 * </ol>
 */
class AnnotationService {

	def grailsApplication
	def learningNerService

	LanguageClassifier languageClassifier

	/**
	 * Annotate the given text.
	 *
	 * @param text
	 * @return Map
	 */
	public def annotate(String text) {
		println "Start annotation";
		def language = languageClassifier.classify(text);
		Annotations rawAnnotations = doNamedEntityRecognition(text, language)
		println "NER finished";
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
		if (grailsApplication.config.ner.annotation.learning) {
			annotations.addAll(learningNerService.getAnnotations(text))
		}
		def languageToNerServiceMapping = grailsApplication.config.ner.annotation.languageToNerServiceMapping
		def nerName = languageToNerServiceMapping[language]
		def ner = grailsApplication.mainContext.getBean(nerName)
		annotations.addAll(ner.getAnnotations(text))
		return annotations
	}

	/**
	 * Enrich the raw annotations in a multi-threaded way.
	 *
	 * @param rawAnnotations
	 * @return Map annotations
	 */
	private def enrichAndNormalizeRawAnnotations(Annotations rawAnnotations) {
		println "Start enrichment"
		def processedAnnotations = []

		def numberOfThreads = rawAnnotations.size()
		if (numberOfThreads > 20) numberOfThreads = 20

		GParsPool.withPool(numberOfThreads) {
			processedAnnotations = rawAnnotations.collectParallel { annotation ->
				def ret = linkifySingleAnnotation(annotation);
				println "stopping linkify single"
				return ret
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
		println "starting linkify single"
		def output = [
			entity: annotation.entity,
			offset: annotation.offset,
			length: annotation.length,
			mostLikelyTagName: annotation.mostLikelyTagName,
		]


		GParsPool.withPool(5) {
			def linkificationResults = grailsApplication.config.ner.linkification.linkificationOrder.tokenize(',').collectParallel { linkifierName ->
				println "single linkifier start " + linkifierName
				AbstractLinkifier linkifier = grailsApplication.mainContext.getBean(linkifierName + 'LinkificationService')
				if (!linkifier) throw new Exception("TODO: Linkifier ${linkifierName} not found!")

				def linkificationResult = linkifier.linkify(annotation)
				if (linkificationResult != null) {
					linkificationResult.each {
						it.linkifierName = linkifier.name
					}
					/*if (linkificationResult.size() > 0 && linkifier.shouldAbortWhenResultsFound()) {
						return output;
					}*/
					// TODO fix that
				}
				println "single linkifier stop " + linkifierName
				return linkificationResult
			}

			def flattenedLinkificationResults = []
			linkificationResults.each { linkificationResult ->
				flattenedLinkificationResults.addAll(linkificationResult)
			}
			output.links = flattenedLinkificationResults
			return output
		}
		//for (String linkifierName in ) {
		/*
		}*/


	//	return output;
	}
}