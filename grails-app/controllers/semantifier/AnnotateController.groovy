package semantifier

/**
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

import java.awt.GraphicsConfiguration.DefaultBufferCapabilities;

import ws.palladian.extraction.entity.ner.tagger.OpenCalaisNER;

class AnnotateController {
	def annotationService

    def index = {
		def text;
		
		if (params.lang =="de") {
			text = "In der Politik, erst recht der Außenpolitik, sind alle Handlungen Signale. Es kommt oft weniger auf den Inhalt, als auf die Präsentation an, wenn Missverständnisse vermieden werden sollen. Erst recht, wenn ein Land anders handelt, als bisher zu erwarten war. So war es bei der Abstimmung im UN-Sicherheitsrat vor genau drei Monaten am 17. März 2011, als der Vertreter Deutschlands nicht etwa zusammen mit engen Verbündeten für einen Einsatz in Libyen votierte, sondern sich der Stimme enthielt. Flugs wurde dies weithin als grundsätzliche Kehrtwendung deutscher Außenpolitik gedeutet.Jedoch schießt diese Kritik am eigentlichen Verfehlen Berlins vorbei. Das liegt nämlich nicht darin, über die Libyen-Politik anderer Meinung gewesen zu sein als enge Verbündete. Sondern es liegt in der Ungelenktheit und Herumdruckserei, welche die Entscheidung prägte. Erst dadurch untermauerte sie den Eindruck außenpolitischer Ziellosigkeit und Gleichgültigkeit, den das Tandem Merkel-Westerwelle seit geraumer Zeit drinnen und draußen geschaffen hat. Und das eine muss vom anderen unterschieden werden, um die schon jetzt wuchernde Legendenbildung einzuhegen.";
		} else {
			text = """Bill Gates, Founder of Microsoft, is a rich man. He lives with his wife in Berlin, a beautiful city.
			 TYPO3 is a Content Management System, where the founder, Kasper Skaarhoj, lives in Denmark.
			 A good friend of him is Sebastian Kurfuerst, who is CFO at Sandstorm Media UG. One of his TYPO3 friends is Thomas Maroschik, who also owns an agency.
			"""
		}
		
		def annotatedText = annotationService.annotate(text)
    	render(contentType:"text/json") {
    		annotatedText
    	}
    }
}
