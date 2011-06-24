package semantifier.disambigurator

import ws.palladian.extraction.entity.ner.Annotation

abstract class AbstractDisambigurator {
	abstract public def disambigurate(Annotation annotation);
}
