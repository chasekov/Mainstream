package com.chase.mainstream.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import com.chase.mainstream.api.enums.SortType;
import com.chase.mainstream.models.ApiResponse;
import com.chase.mainstream.models.inners.SearchHit;
import com.chase.mainstream.models.inners.Song;
import com.chase.mainstream.models.responses.ArtistSongsResponse;

public class ExtendedClient extends OfficialClient {

	public ExtendedClient(String ACCESS_TOKEN) {
		super(ACCESS_TOKEN);
	}

	public int getArtistId(String artistName) throws Exception {
		List<SearchHit> hits = getSearchHits(artistName);

		for (SearchHit hit : hits) {
			if (hit.result.primary_artist.name.equals(artistName)) {
				return hit.result.primary_artist.id;
			}
		}

		return -1;
	}

	public List<Song> getArtistSongs(int artistId, SortType sortType)
			throws ParseException, ClientProtocolException, IOException {

		List<Song> songs = new ArrayList<Song>();
		boolean moreSongs = true;
		int page = 1;

		while (moreSongs) {
			ApiResponse<ArtistSongsResponse> jsonResponse = super.getArtistSongsResponse(artistId, sortType, 50, page);
			songs.addAll(jsonResponse.response.songs);

			if (jsonResponse.response.next_page == null) {
				moreSongs = false;
			} else {
				page++;
			}
		}

		return songs;
	}

}
