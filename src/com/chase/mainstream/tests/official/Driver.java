package com.chase.mainstream.tests.official;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.chase.mainstream.api.OfficialClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Driver {

	public static Gson builder = new GsonBuilder().setPrettyPrinting().create();

	public static String getPretty(Object object) {
		return builder.toJson(object);
	}

	public static void main(String[] args) throws Exception {
		OfficialClient mainstream = new OfficialClient(
				"PaBGoZpaVpHDc3_-JtGQ4I3OHuIOPk-4M0OyRzHcrYxK8LHdVEr_4Qf20DPadqtk");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String command = reader.readLine();

		while (!command.equals("exit")) {
			String command_args[] = command.split(" ");

			switch (command_args[0]) {
			case "song":
				System.out.println(getPretty(mainstream.getSong(Integer.parseInt(command_args[1]))));
				break;
			case "artist":
				System.out.println(getPretty(mainstream.getArtist(Integer.parseInt(command_args[1]))));
				break;
			case "search":
				System.out.println(getPretty(mainstream.getSearchHits(command_args[1])));
				break;
			}

			System.out.println();
			command = reader.readLine();
		}
	}

}
