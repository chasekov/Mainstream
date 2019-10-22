package com.chase.nlp.processing.stages;

public class Lowercase implements TextProcessingStage {

	@Override
	public String process(String text) {
		return text.toLowerCase();
	}

}
