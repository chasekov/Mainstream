package com.chase.nlp.processing;

import java.util.LinkedList;
import java.util.List;

import com.chase.nlp.processing.stages.TextProcessingStage;

public class TextProcessor {

	private List<TextProcessingStage> stages;

	public TextProcessor() {
		stages = new LinkedList<TextProcessingStage>();
	}

	public TextProcessor add(TextProcessingStage stage) {
		stages.add(stage);
		return this;
	}

	public String process(String text) {

		String compiled = text;

		for (TextProcessingStage stage : stages) {
			// System.out.println(stage.getClass().toGenericString());
			compiled = stage.process(compiled);
		}

		return compiled;
	}

}
