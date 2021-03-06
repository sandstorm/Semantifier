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

/**
 * controller responsible for showing the help screen.
 */
class HelpController {

    def index = {
		def configurationFileExists = grailsApplication.config.grails.config.locations.any { fileName ->
			def url = new URL(fileName)
			try {
				return (url.text.length() > 0)
			} catch (FileNotFoundException e) {
				return false
			}
		}
		return [
			settings: grailsApplication.config,
			configurationFileExists: configurationFileExists]
    }
}
