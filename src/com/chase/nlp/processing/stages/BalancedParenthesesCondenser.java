package com.chase.nlp.processing.stages;

public class BalancedParenthesesCondenser implements TextProcessingStage {

	@Override
	public String process(String text) {
		return text.replaceAll("\\((.+?)\\)", "$1");
	}

}
