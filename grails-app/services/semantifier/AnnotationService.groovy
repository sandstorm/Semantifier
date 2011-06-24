package semantifier

class AnnotationService {

	def openCalaisConnector
	def languageClassifier

	public def annotate(String text) {
		def annotations = [];

		def language = languageClassifier.classify(text);

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