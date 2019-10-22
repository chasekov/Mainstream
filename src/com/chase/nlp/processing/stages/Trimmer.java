package com.chase.nlp.processing.stages;

public class Trimmer implements TextProcessingStage {

	@Override
	public String process(String text) {

		String trimmed = "";

		for (String line : text.split("\n")) {
			trimmed += line.trim() + "\n";
		}

		return trimmed;
	}

}
