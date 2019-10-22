package com.chase.nlp.tests;

import java.io.FileReader;
import java.util.List;

import com.chase.mainstream.models.custom.Discography;
import com.chase.mainstream.models.inners.Song;
import com.chase.nlp.models.Topics;
import com.chase.nlp.models.Topics.Topic;
import com.chase.nlp.processing.TextProcessor;
import com.chase.nlp.processing.stages.BalancedParenthesesCondenser;
import com.chase.nlp.processing.stages.EmptyLineCondenser;
import com.chase.nlp.processing.stages.EmptyLineRemover;
import com.chase.nlp.processing.stages.Lowercase;
import com.chase.nlp.processing.stages.ParentheseRepeatIndicatorRemover;
import com.chase.nlp.processing.stages.PunctuationRemover;
import com.chase.nlp.processing.stages.RepeatedLinesRemover;
import com.chase.nlp.processing.stages.SectionHeaderRemover;
import com.chase.nlp.processing.stages.Trimmer;
import com.chase.nlp.tests.utils.DiscLoader;
import com.google.gson.Gson;

public class TopicsTest {

	public static void main(String[] args) throws Exception {
		Gson gson = new Gson();
		Topics t = gson.fromJson(new FileReader("test_data/topic_test.json"), Topics.class);

		String artist = "Felly";
		System.out.println("Artist : " + artist);

		int max_songs = 10;
		for (int i = 0; i < max_songs; i++) {
			testSong(t.topics, getCleanedSong(artist, i));
		}
	}

	public static void testSong(List<Topic> topics, Song song) {
		System.out.println("Song : " + song.title);
		System.out.println("Lyrics:");
		int i = 0;
		int max_lines = 10;
		for (String line : song.lyrics.split("\n")) {
			System.out.println("  > " + line);

			if (++i == max_lines) {
				System.out.println();
				break;
			}
		}

		for (Topic topic : topics)
			testTopic(topic, song.lyrics);
	}

	public static void testTopic(Topic topic, String lyrics) {
		System.out.println("Topic : " + topic.name);

		for (String keyword : topic.keywords) {
			System.out.println("  > " + keyword + ": " + getOccurenceCount(lyrics, keyword));
		}

		System.out.println();
	}

	public static int getOccurenceCount(String hayStack, String needle) {
		int lastIndex = 0;
		int count = 0;

		while (lastIndex != -1) {

			lastIndex = hayStack.indexOf(needle, lastIndex);

			if (lastIndex != -1) {
				count++;
				lastIndex += hayStack.length();
			}
		}

		return count;
	}

	public static Song getCleanedSong(String artist, int index) throws Exception {
		Discography disc = DiscLoader.getDiscography(artist);
		Song song = disc.getSongs().get(index);

		song.lyrics = new TextProcessor().add(new PunctuationRemover()).add(new Lowercase())
				.add(new SectionHeaderRemover()).add(new ParentheseRepeatIndicatorRemover())
				.add(new BalancedParenthesesCondenser()).add(new EmptyLineCondenser()).add(new Trimmer())
				.add(new RepeatedLinesRemover()).add(new EmptyLineRemover()).process(song.lyrics);

		return song;
	}

}
