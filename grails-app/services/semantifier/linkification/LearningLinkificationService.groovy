package semantifier.linkification

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

import semantifier.LearnedEntity;
import ws.palladian.extraction.entity.ner.Annotation


/**
 * Linkification service which uses Freebase.
 */
class LearningLinkificationService extends AbstractLinkifier {
	public String getName() {
		return "learning"
	}
	public def linkify(String text, String entityType) {
		def result = LearnedEntity.search(text, [result: 'every']).collect { learnedEntity ->
			return [
				id: learnedEntity.linkedDataUrl,
				type: learnedEntity.type,
				name: learnedEntity.name
			]
		}
		return result
	}
}
