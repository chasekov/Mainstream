package com.chase.nlp.processing.stages;

public class EmptyLineCondenser implements TextProcessingStage {

	@Override
	public String process(String text) {
		return text.replaceAll("\n+", "\n");
	}

}
