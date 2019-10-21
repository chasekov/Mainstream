package com.chase.mainstream.tests.extended;

import java.util.List;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import com.chase.mainstream.api.ExtendedClient;
import com.chase.mainstream.api.enums.SortType;
import com.chase.mainstream.api.extended.LyricScrapperThread;
import com.chase.mainstream.models.inners.Song;

public class ExtendedTests {

	public static void main(String[] args) throws Exception {
		ExtendedClient mainstream = new ExtendedClient(
				"PaBGoZpaVpHDc3_-JtGQ4I3OHuIOPk-4M0OyRzHcrYxK8LHdVEr_4Qf20DPadqtk");

		int artistId = mainstream.getArtistId("Oliver Tree");
		List<Song> songs = mainstream.getArtistSongs(artistId, SortType.POPULARITY, 25, 1);
		System.out.println(songs.size());

		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
		CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom().setDefaultRequestConfig(requestConfig).build();

		httpclient.start();
		
		LyricScrapperThread[] threads = new LyricScrapperThread[songs.size()];
		
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new LyricScrapperThread(httpclient, songs.get(i));
		}
		
	    for (LyricScrapperThread thread : threads) {
	        thread.start();
	    }
	    
	    for (LyricScrapperThread thread : threads) {
	        thread.join();
	    }
	    
	    System.out.println("All threads have ended");
	    for(Song song : songs) {
	    	System.out.println(song.full_title);
	    	System.out.println(song.lyrics.length());
	    }
	}
}
