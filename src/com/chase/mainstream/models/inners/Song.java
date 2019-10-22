package com.chase.mainstream.models.inners;

public class Song {

	public int id;
	public String full_title;
	public String title;
	public String url;

	public String header_image_thumbnail_url;
	public String header_image_url;

	public String release_date;

	/* These will never exist unless, discography has been created */
	public String lyrics;
	public String cleaned_lyrics;

	public Artist primary_artist;
	public Album album;
}
