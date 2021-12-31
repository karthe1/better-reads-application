package com.betterreads.search;

import java.util.List;

public class SearchResult {

	private int numfound;
	private List<SearchResultBook> docs;

	public int getNumfound() {
		return numfound;
	}

	public void setNumfound(int numfound) {
		this.numfound = numfound;
	}

	public List<SearchResultBook> getDocs() {
		return docs;
	}

	public void setDocs(List<SearchResultBook> docs) {
		this.docs = docs;
	}

}
