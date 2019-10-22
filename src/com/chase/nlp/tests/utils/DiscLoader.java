package com.chase.nlp.tests.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.chase.mainstream.models.custom.Discography;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class DiscLoader {

	public static Gson gson = new GsonBuilder().create();

	public static Discography getDiscography(String artistName)
			throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Discography disc = gson.fromJson(new FileReader("discographies/" + artistName + ".json"), Discography.class);
		return disc;
	}

}
