package com.chase.nlp.tests;

import java.io.FileNotFoundException;

import com.chase.mainstream.models.custom.Discography;
import com.chase.mainstream.models.inners.Song;
import com.chase.nlp.processing.TextProcessor;
import com.chase.nlp.processing.stages.BalancedParenthesesCondenser;
import com.chase.nlp.processing.stages.EmptyLineCondenser;
import com.chase.nlp.processing.stages.Lowercase;
import com.chase.nlp.processing.stages.ParentheseRepeatIndicatorRemover;
import com.chase.nlp.processing.stages.PunctuationRemover;
import com.chase.nlp.processing.stages.RepeatedLinesRemover;
import com.chase.nlp.processing.stages.SectionHeaderRemover;
import com.chase.nlp.processing.stages.Trimmer;
import com.chase.nlp.tests.utils.DiscLoader;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class CleaningTest {

	public static void main(String[] args) throws JsonSyntaxException, JsonIOException, FileNotFoundException {

		Discography disc = DiscLoader.getDiscography("Felly");

		Song song = disc.getSongs().get(0);
		System.out.println(song.lyrics);

		String cleaned = new TextProcessor().add(new PunctuationRemover()).add(new Lowercase())
				.add(new SectionHeaderRemover()).add(new ParentheseRepeatIndicatorRemover())
				.add(new BalancedParenthesesCondenser()).add(new EmptyLineCondenser()).add(new Trimmer())
				.add(new RepeatedLinesRemover()).process(song.lyrics);

		System.out.println(cleaned);

	}

}
