package semantifier

import ws.palladian.extraction.entity.ner.Annotations
import ws.palladian.extraction.entity.ner.tagger.PalladianNer

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
	
	public void learn() {
		println "Learn called"
		println palladianNer.getName()
	}
	public Annotations getAnnotations(String text) {
		return palladianNer.getAnnotations(text)
	}
	public void rebuild() {
		return;
		def newPalladianNer = createNer()
		newPalladianNer.train(trainingPath, "/tmp/nerModel_1")

		palladianNer = newPalladianNer
	}
}