package com.chase.mainstream.api.scraping;

import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.chase.mainstream.models.inners.Song;
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

public class LyricScraperThread extends Thread {

	private CloseableHttpAsyncClient client;
	private HttpContext context;
	private HttpGet request;
	private Song song;
	private TextProcessor lyricProcessor;

	public LyricScraperThread(CloseableHttpAsyncClient client, Song song) {
		this.client = client;
		context = HttpClientContext.create();
		this.song = song;
		this.request = new HttpGet(song.url);

		lyricProcessor = new TextProcessor().add(new PunctuationRemover()).add(new Lowercase())
				.add(new SectionHeaderRemover()).add(new ParentheseRepeatIndicatorRemover())
				.add(new BalancedParenthesesCondenser()).add(new EmptyLineCondenser()).add(new Trimmer())
				.add(new RepeatedLinesRemover()).add(new EmptyLineRemover());
	}

	public Song getSong() {
		return song;
	}

	@Override
	public void run() {
		try {
			Future<HttpResponse> future = client.execute(request, context, null);
			HttpResponse result = future.get();
			String responseBody = EntityUtils.toString(result.getEntity());
			Document doc = Jsoup.parse(responseBody);
			Element element = doc.selectFirst(".lyrics");

			String lyrics = element.wholeText().trim();
			song.lyrics = lyrics;
			song.cleaned_lyrics = lyricProcessor.process(song.lyrics);

			System.out.println("Completed " + song.title);
		} catch (Exception ex) {
			System.out.println("Failed " + song.title);
			ex.printStackTrace();
		}
	}

}
