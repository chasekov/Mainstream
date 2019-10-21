# Mainstream
Extension of official genius client with lyric scraping capabilities
Created in order to scrape and analyze lyrical discographies for artist's

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
