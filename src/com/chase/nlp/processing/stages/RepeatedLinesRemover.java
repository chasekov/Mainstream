package com.chase.nlp.processing.stages;

import java.util.LinkedHashSet;
import java.util.Set;

public class RepeatedLinesRemover implements TextProcessingStage {

	@Override
	public String process(String text) {

		Set<String> uniques = new LinkedHashSet<String>();
		String[] lines = text.split("\n");

		for (String line : lines)
			uniques.add(line);

		String uniqueText = "";

		for (String line : uniques) {
			uniqueText += line + "\n";
		}

		return uniqueText;
	}

}
