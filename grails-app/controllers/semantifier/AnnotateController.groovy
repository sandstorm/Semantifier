package semantifier

import ws.palladian.extraction.entity.ner.tagger.OpenCalaisNER;

class AnnotateController {
	def annotationService

    def index = {
		//def text = "Bill Gates, Founder of Microsoft, is a rich man. He lives with his wife in Berlin, a beautiful city. A good friend of him is Sebastian Kurfürst, who is CFO at Sandstorm Media UG. TYPO3 is a Content Management System, where the founder, Kasper Skaarhoj, lives in Denmark. Sebastian's birthday is on the 25th of June 1988.";
		def text = "In der Politik, erst recht der Außenpolitik, sind alle Handlungen Signale. Es kommt oft weniger auf den Inhalt, als auf die Präsentation an, wenn Missverständnisse vermieden werden sollen. Erst recht, wenn ein Land anders handelt, als bisher zu erwarten war. So war es bei der Abstimmung im UN-Sicherheitsrat vor genau drei Monaten am 17. März 2011, als der Vertreter Deutschlands nicht etwa zusammen mit engen Verbündeten für einen Einsatz in Libyen votierte, sondern sich der Stimme enthielt. Flugs wurde dies weithin als grundsätzliche Kehrtwendung deutscher Außenpolitik gedeutet.Jedoch schießt diese Kritik am eigentlichen Verfehlen Berlins vorbei. Das liegt nämlich nicht darin, über die Libyen-Politik anderer Meinung gewesen zu sein als enge Verbündete. Sondern es liegt in der Ungelenktheit und Herumdruckserei, welche die Entscheidung prägte. Erst dadurch untermauerte sie den Eindruck außenpolitischer Ziellosigkeit und Gleichgültigkeit, den das Tandem Merkel-Westerwelle seit geraumer Zeit drinnen und draußen geschaffen hat. Und das eine muss vom anderen unterschieden werden, um die schon jetzt wuchernde Legendenbildung einzuhegen.";

    	render(contentType:"text/json") {
    		annotationService.annotate(text)
    	}
    }
}
