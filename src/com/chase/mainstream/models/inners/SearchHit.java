package com.chase.mainstream.models.inners;

public class SearchHit {
	public String index;
	public String type;
	public SearchHitResult result;

	public class SearchHitResult {
		public String full_title;
		public Artist primary_artist;
	}
}
