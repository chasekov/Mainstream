package com.chase.nlp.processing.stages;

public class PunctuationRemover implements TextProcessingStage {

	@Override
	public String process(String text) {
		text = text.replaceAll("'", "");
		text = text.replaceAll("’", "");
		text = text.replaceAll(",", "");
		text = text.replaceAll("\\!", "");
		text = text.replaceAll("\\?", "");

		return text;
	}

}
