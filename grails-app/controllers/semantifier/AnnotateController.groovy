package semantifier

import ws.palladian.extraction.entity.ner.tagger.OpenCalaisNER;

class AnnotateController {

    def index = {
	    def x = new OpenCalaisNER();
    }
}
