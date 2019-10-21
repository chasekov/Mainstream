package com.chase.mainstream.tests.extended;

import java.io.FileWriter;

import com.chase.mainstream.models.custom.Discography;
import com.chase.mainstream.models.inners.Artist;
import com.google.gson.Gson;

public class WriteTest {

	public static void main(String[] args) {

		try {
			Artist artist = new Artist();
			artist.name = "test";

			Gson gson = new Gson();

			String json = gson.toJson(new Discography(artist, null));
			FileWriter writer = new FileWriter("test.json");
			writer.write(json);
			writer.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static class Test {
		public String value;
	}
}
