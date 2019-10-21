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

public class LyricScraperThread extends Thread {

	private CloseableHttpAsyncClient client;
	private HttpContext context;
	private HttpGet request;
	private Song song;

	public LyricScraperThread(CloseableHttpAsyncClient client, Song song) {
		this.client = client;
		context = HttpClientContext.create();
		this.song = song;
		this.request = new HttpGet(song.url);
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
			System.out.println("Completed " + song.title);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
