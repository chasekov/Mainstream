# Mainstream
Extension of official genius client with lyric scraping capabilities
Created in order to scrape and analyze lyrical discographies for artist's


### Example of purpose
-------------
#### Scraping an artist's lyrical discography
```Java
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
```

#### Excerpt of output
```Java
http://api.genius.com/search?q=Oliver+Tree
http://api.genius.com/artists/1020188/songs?per_page=50&sort=popularity&page=1
http://api.genius.com/artists/1020188/songs?per_page=50&sort=popularity&page=2
All threads have ended
Alien Boy , 1243
Hurt , 1351
All That , 1591
Miracle Man , 1294
Movement , 1799
All I Got , 1272
Fuck , 1774
Welcome to LA , 1214
Cheapskate , 1271
Introspective , 1384
All Bets Are Off , 1583
Upside Down , 811
Do You Feel Me? , 1553
All That x Alien Boy , 1338
This is Separation , 1072
On N On* , 1673
Demons , 1174
Karma Police , 1281
```







### Models
-------------
#### Artist
```Java
public class Artist {
	public String api_path;
	public String name;
	public int id;
	public String url;
}
```

#### Album
```Java
public class Album {

	public String api_path;
	public String cover_art_url;
	public String full_title;
	public int id;
	public String name;
	public String url;

	public Artist artist;
}
```

#### Song
```Java
public class Song {

	public String full_title;
	public String title;
	public String url;

	public String header_image_thumbnail_url;
	public String header_image_url;
	public int id;
	public int lyrics_owner_id;
	public String lyrics_state;
	public String lyrics;

	public Artist primary_artist;
	public Album album;
}
```

#### Search Hit
```Java
public class SearchHit {
	public String index;
	public String type;
	public SearchHitResult result;

	public class SearchHitResult {
		public String full_title;
		public Artist primary_artist;
	}
}
```

### Enums
-------------
#### SortType
> Used when getting an artist's songs
```Java
public enum SortType {

	TITLE("title"), POPULARITY("popularity");

	public String value;

	SortType(String value) {
		this.value = value;
	}

}
```

### Official Client
-------------
#### getArtist
> Returns an artist based on provided artistId
```Java
public Artist getArtist(int artistId)
```

#### getSong
> Returns a song based on provided songId
```Java
public Song getSong(int songId)
```

#### getSearchHits
> Returns a list of search hits for a query, limited use aside from searching for an artist's name in order to find their id
```Java
public List<SearchHit> getSearchHits(String query)
```

#### getArtistSongs
> Returns a list of songs for an artistId, limited use officially. Extended client uses this in order to accumulate artist's full discography
```Java
public List<Song> getArtistSongs(int artistId, SortType sortType, int songsPerPage, int page)
```

### Extended Client
-------------
#### getArtistId
> Attempts to find an artist's id based on their name
```Java
public int getArtistId(String artistName)
```

#### getArtistSongs
> Collects every song from an artist to create complete tracklists
> Expects a sort type
> primaryOnly indicates whether songs where the artist is featured should be included
```Java
public List<Song> getArtistSongs(int artistId, SortType sortType, boolean primaryOnly)
```
