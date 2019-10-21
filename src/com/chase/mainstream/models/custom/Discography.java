package com.chase.mainstream.models.custom;

import java.util.List;

import com.chase.mainstream.models.inners.Artist;
import com.chase.mainstream.models.inners.Song;

public class Discography {

	private Artist artist;
	private List<Song> songs;

	public Discography(Artist artist, List<Song> songs) {
		this.artist = artist;
		this.songs = songs;

		System.out.println("Discography for " + artist.name + " created with " + songs.size() + " songs");
	}

	public Artist getArtist() {
		return artist;
	}

	public List<Song> getSongs() {
		return songs;
	}

}
