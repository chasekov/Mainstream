package com.chase.nlp.processing.stages;

public class ParentheseRepeatIndicatorRemover implements TextProcessingStage {

	@Override
	public String process(String text) {
		return text.replaceAll("\\(((x)\\d+)\\)", "");
	}

}
