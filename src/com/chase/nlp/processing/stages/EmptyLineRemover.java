package com.chase.nlp.processing.stages;

public class EmptyLineRemover implements TextProcessingStage {

	@Override
	public String process(String text) {
		String processed = "";

		for (String line : text.split("\n")) {
			if (!line.equals(""))
				processed += line + "\n";
		}

		// TODO Auto-generated method stub
		return processed;
	}

}
