package com.chase.mainstream.tests.extended;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import com.chase.mainstream.api.ExtendedClient;
import com.chase.mainstream.api.scraping.DiscographyCallable;
import com.chase.mainstream.models.custom.Discography;
import com.chase.mainstream.models.inners.Artist;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DiscScrapeTest {

	public static void main(String[] args) throws Exception {
		ExtendedClient mainstream = new ExtendedClient(
				"PaBGoZpaVpHDc3_-JtGQ4I3OHuIOPk-4M0OyRzHcrYxK8LHdVEr_4Qf20DPadqtk");

		RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
		CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom().setDefaultRequestConfig(requestConfig).build();
		httpclient.start();

		ExecutorService executorService = Executors.newSingleThreadExecutor();

		int artistId = mainstream.getArtistId(new BufferedReader(new InputStreamReader(System.in)).readLine());
		Artist artist = mainstream.getArtist(artistId);

		System.out.println("Submitting Discography Callable");
		Future<Discography> future = executorService.submit(new DiscographyCallable(httpclient, mainstream, artist));
		System.out.println("Submitted Discography Callable");

		Discography discography = future.get();
		executorService.shutdown();

		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(discography);
			FileWriter writer = new FileWriter("discographies/" + artist.name + ".json");
			writer.write(json);
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			httpclient.close();
		}
	}
}
