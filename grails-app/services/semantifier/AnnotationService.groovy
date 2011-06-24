package semantifier

class AnnotationService {

	def openCalaisConnector

	public def annotate(String text) {
		def annotations = [];

		def language = "en"

		if (language == "en") {
			annotations = openCalaisConnector.getAnnotations(text);
		} else {
			//annotations = alchemyConnector.getAnnotations(text);
		}

		def processedAnnotations = []

		println annotations
		return [language: language, entities: processedAnnotations];
	}
}