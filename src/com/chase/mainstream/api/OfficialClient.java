package com.chase.mainstream.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import com.chase.mainstream.api.enums.SortType;
import com.chase.mainstream.models.ApiResponse;
import com.chase.mainstream.models.inners.Artist;
import com.chase.mainstream.models.inners.SearchHit;
import com.chase.mainstream.models.inners.Song;
import com.chase.mainstream.models.responses.ArtistSongsResponse;
import com.chase.mainstream.models.responses.ArtistsResponse;
import com.chase.mainstream.models.responses.SearchResponse;
import com.chase.mainstream.models.responses.SongsResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class OfficialClient {

	String ACCESS_TOKEN;

	private final String apiEndpoint = "http://api.genius.com";
	private final String songsEndpoint = "songs";
	private final String artistsEndpoint = "artists";
	private final String searchEndpoint = "search";

	private HttpClient httpClient;

	public OfficialClient(String ACCESS_TOKEN) {
		this.ACCESS_TOKEN = ACCESS_TOKEN;

		List<Header> defaultHeaders = createDefaultHeaders();
		httpClient = HttpClients.custom().setDefaultHeaders(defaultHeaders).build();
	}

	private List<Header> createDefaultHeaders() {
		List<Header> headers = new ArrayList<Header>();

		Header useragentHeader = new BasicHeader("User-Agent", "CompuServe Classic/1.22");
		Header contentAcceptHeader = new BasicHeader("Accept", "application/json");
		Header authHeader = new BasicHeader("Authorization", "Bearer " + ACCESS_TOKEN);

		headers.add(useragentHeader);
		headers.add(contentAcceptHeader);
		headers.add(authHeader);
		return headers;
	}

	protected String createEndpoint(Object... pieces) {
		StringBuilder endpointBuilder = new StringBuilder(apiEndpoint);

		for (int i = 0; i < pieces.length; i++) {
			endpointBuilder.append("/");
			endpointBuilder.append(String.valueOf(pieces[i]));
		}

		return endpointBuilder.toString();
	}

	protected String createParameters(Map<String, String> parameters) throws UnsupportedEncodingException {
		StringBuilder parameterBuilder = new StringBuilder("?");
		Iterator<Map.Entry<String, String>> iterator = parameters.entrySet().iterator();

		int index = 0;

		while (iterator.hasNext()) {
			Map.Entry<String, String> entry = iterator.next();

			parameterBuilder.append(entry.getKey());
			parameterBuilder.append("=");
			parameterBuilder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));

			if (index != parameters.entrySet().size() - 1) {
				parameterBuilder.append("&");
			}

			index++;
		}

		return parameterBuilder.toString();
	}

	protected String getResponseBody(String endpoint) throws ParseException, ClientProtocolException, IOException {
		HttpGet getRequest = new HttpGet(endpoint);
		String responseBody = EntityUtils.toString(httpClient.execute(getRequest).getEntity());
		return responseBody;
	}

	public Artist getArtist(int artistId) throws ClientProtocolException, IOException {
		String endpoint = createEndpoint(artistsEndpoint, artistId);
		String responseBody = getResponseBody(endpoint);

		Type type = new TypeToken<ApiResponse<ArtistsResponse>>() {
		}.getType();

		ApiResponse<ArtistsResponse> jsonResponse = new Gson().fromJson(responseBody, type);
		return jsonResponse.response.artist;
	}

	public Song getSong(int songId) throws ClientProtocolException, IOException {
		String endpoint = createEndpoint(songsEndpoint, songId);
		String responseBody = getResponseBody(endpoint);

		Type type = new TypeToken<ApiResponse<SongsResponse>>() {
		}.getType();

		ApiResponse<SongsResponse> jsonResponse = new Gson().fromJson(responseBody, type);
		return jsonResponse.response.song;
	}

	public List<SearchHit> getSearchHits(String query) throws ClientProtocolException, IOException {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("q", query);

		String endpoint = createEndpoint(searchEndpoint);
		String parameters = createParameters(paramMap);
		String url = endpoint + parameters;

		System.out.println(url);
		String responseBody = getResponseBody(url);

		Type type = new TypeToken<ApiResponse<SearchResponse>>() {
		}.getType();

		ApiResponse<SearchResponse> jsonResponse = new Gson().fromJson(responseBody, type);
		return jsonResponse.response.hits;
	}

	protected ApiResponse<ArtistSongsResponse> getArtistSongsResponse(int artistId, SortType sortType, int songsPerPage,
			int page) throws ParseException, ClientProtocolException, IOException {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("sort", sortType.value);
		paramMap.put("per_page", String.valueOf(songsPerPage));
		paramMap.put("page", String.valueOf(page));

		String endpoint = createEndpoint(artistsEndpoint, artistId, songsEndpoint);
		String parameters = createParameters(paramMap);
		String url = endpoint + parameters;

		System.out.println(url);

		String responseBody = getResponseBody(url);

		Type type = new TypeToken<ApiResponse<ArtistSongsResponse>>() {
		}.getType();

		ApiResponse<ArtistSongsResponse> jsonResponse = new Gson().fromJson(responseBody, type);
		return jsonResponse;
	}

	public List<Song> getArtistSongs(int artistId, SortType sortType, int songsPerPage, int page)
			throws ParseException, ClientProtocolException, IOException {
		ApiResponse<ArtistSongsResponse> jsonResponse = getArtistSongsResponse(artistId, sortType, songsPerPage, page);
		return jsonResponse.response.songs;
	}

}
