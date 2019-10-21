package com.chase.mainstream.tests.official;

import com.chase.mainstream.api.OfficialClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SearchTests {

	public static Gson builder = new GsonBuilder().setPrettyPrinting().create();

	public static String getPretty(Object object) {
		return builder.toJson(object);
	}

	public static void main(String[] args) throws Exception {
		OfficialClient mainstream = new OfficialClient(
				"PaBGoZpaVpHDc3_-JtGQ4I3OHuIOPk-4M0OyRzHcrYxK8LHdVEr_4Qf20DPadqtk");
		System.out.println(getPretty(mainstream.getSearchHits("Mac Miller")));
	}

}
