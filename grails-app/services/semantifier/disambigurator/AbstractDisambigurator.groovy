package semantifier.disambigurator
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
 * Abstract base class for the disambiguration step.
 */
abstract class AbstractDisambigurator {
	
	/**
	 * TODO: refine this method
	 *
	 * @param annotation
	 * @return
	 */
	abstract public def disambigurate(Annotation annotation);
}
