package semantifier

import ws.palladian.extraction.entity.ner.Annotations
import ws.palladian.extraction.entity.ner.tagger.PalladianNer
import ws.palladian.helper.nlp.Tokenizer;
import org.codehaus.groovy.grails.commons.ApplicationHolder

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


/**
 */
class LearningNerService {

	volatile PalladianNer palladianNer

	public LearningNerService() {
		palladianNer = createNer()
	}

	private PalladianNer createNer() {
		return new ws.palladian.extraction.entity.ner.tagger.PalladianNer(
			ws.palladian.extraction.entity.ner.tagger.PalladianNer.LanguageMode.English,
			ws.palladian.extraction.entity.ner.tagger.PalladianNer.TrainingMode.Sparse
		);
	};

	public void learn(String textToLearn, def metadata) {
		metadata.each { singleEntity ->
			LearnedEntity.createNew(singleEntity.id, singleEntity.type, textToLearn, singleEntity.offset, singleEntity.length)
		}
	}
	public void learnEntity(uri, entityString, type) {

		def entityWithSameUriExists = false

		LearnedEntity.search(entityString, [result: 'every']).every { learnedEntity ->
			if (learnedEntity.linkedDataUrl == uri) {
				entityWithSameUriExists = true
			}
		}
		if (!entityWithSameUriExists) {
			LearnedEntity.createNew(uri, type, entityString, 0, entityString.length())
		}
	}

	public Annotations getAnnotations(String text) {
		def annotations = palladianNer.getAnnotations(text)
		println annotations
		return annotations
	}
	public void rebuild() {
		def newPalladianNer = createNer()

		File file = new File("/tmp/ner_training.txt")
		file.delete()
		file << ApplicationHolder.application.parentContext.getResource("train.txt").inputStream.text
		// Learn palladianNER the following facts: "type", "entityString", "sourceText"
		// <http://foaf.blas/>...</http://foaf.blas/> -> Palladian converter -> Column Based
		LearnedEntity.findAll().each { entity ->
			def tokens = Tokenizer.tokenize(entity.sourceText)
			println(entity.sourceText);

			tokens.each { token ->
				file << token
				file << "\t"
				if (entity.name.contains(token)) {
					// TODO: the check above is not very nice -> better idea: generate XML format and parse this?
					file << entity.type
				} else {
					file << "O" // "O" buchstabe "Oh"
				}
				file << "\n"
			}
			file << "\n"
		}
		newPalladianNer.train("/tmp/ner_training.txt", "/tmp/nerModel_1")

		palladianNer = newPalladianNer
	}
}