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
import ws.palladian.extraction.entity.ner.Annotation

/**
 * Abstract base class for the linkification step.
 */
abstract class AbstractLinkifier {

	def grailsApplication

	/**
	 * TODO: refine this method
	 *
	 * @param annotation
	 * @return
	 */
	abstract public def linkify(String text, String entityType);

	abstract public String getName();

	public boolean shouldAbortWhenResultsFound() {
		return config.abortWhenResultsFound
	}

	public def getConfig() {
		return grailsApplication.config.ner.linkification[this.getName()]
	}
}
