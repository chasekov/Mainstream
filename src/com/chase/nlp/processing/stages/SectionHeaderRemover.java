package com.chase.nlp.processing.stages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SectionHeaderRemover implements TextProcessingStage {

	private static Pattern BETWEEN_BRACKET_PATTERN = Pattern.compile("\\[(.*?)\\]");

	@Override
	public String process(String text) {
		Matcher matcher = BETWEEN_BRACKET_PATTERN.matcher(text);
		return matcher.replaceAll("");
	}

}
