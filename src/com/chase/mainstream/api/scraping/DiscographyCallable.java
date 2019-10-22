package com.chase.mainstream.api.scraping;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;

import com.chase.mainstream.api.ExtendedClient;
import com.chase.mainstream.api.enums.SortType;
import com.chase.mainstream.models.custom.Discography;
import com.chase.mainstream.models.inners.Artist;
import com.chase.mainstream.models.inners.Song;

public class DiscographyCallable implements Callable<Discography> {

	private CloseableHttpAsyncClient httpClient;
	private ExtendedClient extendedClient;
	private Artist artist;

	public DiscographyCallable(CloseableHttpAsyncClient httpClient, ExtendedClient extendedClient, Artist artist) {
		this.httpClient = httpClient;
		this.extendedClient = extendedClient;
		this.artist = artist;
	}

	@Override
	public Discography call() throws Exception {

		if (!httpClient.isRunning()) {
			throw new Exception("Discography Callable was provided a non running http async client.");
		}

		List<Song> songs = extendedClient.getArtistSongs(artist.id, SortType.POPULARITY, true);

		ExecutorService pool = Executors.newFixedThreadPool(15);

		LyricScraperThread[] threads = new LyricScraperThread[songs.size()];

		for (int i = 0; i < threads.length; i++) {

			// Provides no value.
			songs.get(i).primary_artist = null;

			pool.execute(new LyricScraperThread(httpClient, songs.get(i)));
		}

		pool.shutdown();

		try {
			if (!pool.awaitTermination(120, TimeUnit.SECONDS)) {
				throw new Exception("Discography callable has exceeded max time.");
			}
		} catch (Exception ex) {
			throw ex;
		}

		return new Discography(artist, songs);
	}

}
