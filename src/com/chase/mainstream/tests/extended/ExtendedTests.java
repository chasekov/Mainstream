package com.chase.mainstream.tests.extended;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import com.chase.mainstream.api.ExtendedClient;
import com.chase.mainstream.api.enums.SortType;
import com.chase.mainstream.api.extended.LyricScraperThread;
import com.chase.mainstream.models.inners.Song;

public class ExtendedTests {

	public static void main(String[] args) throws Exception {
		ExtendedClient mainstream = new ExtendedClient(
				"PaBGoZpaVpHDc3_-JtGQ4I3OHuIOPk-4M0OyRzHcrYxK8LHdVEr_4Qf20DPadqtk");

		// Oliver tree is a hip hop artist
		int artistId = mainstream.getArtistId("Oliver Tree");

		// Get every song where Oliver tree is the primary artist
		// No songs where oliver tree is a feature will be included
		List<Song> songs = mainstream.getArtistSongs(artistId, SortType.POPULARITY, true);

		RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
		CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom().setDefaultRequestConfig(requestConfig).build();

		httpclient.start();

		// Allow for 15 concurrent connections
		// AKA there will be a maximum of 15 songs being scraped at the same time
		ExecutorService pool = Executors.newFixedThreadPool(15);

		// Lyric scraper threads, request song html and parse for the lyrics
		// Lyrics are then placed into song.lyrics attribute
		// Work in progress, obviously will be modularized/abstracted. Very specific
		// usage as of now.
		LyricScraperThread[] threads = new LyricScraperThread[songs.size()];

		for (int i = 0; i < threads.length; i++) {
			pool.execute(new LyricScraperThread(httpclient, songs.get(i)));
		}

		// All scraping threads have been added, no more will be accepted
		// Pool has been shutdown, and now we wait for completion
		pool.shutdown();

		try {
			// Wait a maximum of 120 seconds
			if (pool.awaitTermination(120, TimeUnit.SECONDS)) {

				System.out.println("All threads have ended");

				// Output all song titles, and length of lyrics for POW
				for (Song song : songs) {
					System.out.println(song.title + " , " + song.lyrics.length());
				}
			}
		} catch (Exception ex) {

		} finally {
			// Close our http client
			httpclient.close();
		}
	}
}
