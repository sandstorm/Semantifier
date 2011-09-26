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
 * Grails URL Mappings
 */
 class UrlMappings {

	static mappings = {
		// as $path will contain slashes, we need to use "**" at the end to consume the whole path.
		"/type/isLiteral/$path**" {
			controller = "type"
			action = "isLiteral"
		}
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
		"/linkify" {
			 controller = "annotate"
			 action = "linkify"
		}

		"/learnEntity" {
			 controller = "annotate"
			 action = "learnEntity"
		}

		"/"(view:"/index")
		"500"(view:'/error')
	}
}
