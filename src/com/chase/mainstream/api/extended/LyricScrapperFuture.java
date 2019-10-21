package com.chase.mainstream.api.extended;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class LyricScrapperFuture implements FutureCallback<HttpResponse> {

	@Override
	public void completed(HttpResponse result) {
		try {
			String responseBody = EntityUtils.toString(result.getEntity());
			Document doc = Jsoup.parse(responseBody);
			Element element = doc.selectFirst(".lyrics");
			String lyrics = element.wholeText().trim();
			System.out.println(lyrics);

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void failed(Exception ex) {
	}

	@Override
	public void cancelled() {
	}

}
