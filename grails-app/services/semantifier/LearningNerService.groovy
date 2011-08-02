package semantifier

import ws.palladian.extraction.entity.ner.Annotations
import ws.palladian.extraction.entity.ner.tagger.PalladianNer
import ws.palladian.helper.nlp.Tokenizer;

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
	public Annotations getAnnotations(String text) {
		def annotations = palladianNer.getAnnotations(text)
		println annotations
		return annotations
	}
	public void rebuild() {
		def newPalladianNer = createNer()
		
		File file = new File("/tmp/ner_training.txt")
		
		// Learn palladianNER the following facts: "type", "entityString", "sourceText"
		LearnedEntity.findAll().each { entity ->
			def tokens = Tokenizer.tokenize(entity.sourceText)
			println(entity.sourceText);
			
			tokens.each { token ->
				file << token
				file << "\t"
				if (entity.name.contains(token)) {
					file << entity.type
				} else {
					file << "0"
				}
				file << "\n"
			}
			file << "\n"
		}
		newPalladianNer.train("/tmp/ner_training.txt", "/tmp/nerModel_1")
		
		palladianNer = newPalladianNer
	}
}