package semantifier

/**
 * Domain class for all *learned* entities, i.e. which have been
 * confirmed by the user.
 */
class LearnedEntity {

    static constraints = {
    }
	
	static void createNew(String linkedDataUrl, String type, String surroundingText, int offset, int length) {
		def entityString = surroundingText.substring(offset, offset + length)
		def learnedEntity = new LearnedEntity(
			name: entityString,
			linkedDataUrl: linkedDataUrl,
			type: type,
			sourceText: surroundingText,
			offset: offset,
			length: length
		)
		learnedEntity.save();
	}
	
	String name
	String linkedDataUrl
	String type

	// name == sourceText[offset..offset+length]
	String sourceText
	int offset
	int length
}