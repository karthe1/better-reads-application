package com.betterreads.search;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Controller
public class SearchController {

	private static String COVER_IMAGE_URL = "https://covers.openlibrary.org/b/id/";

	private final WebClient webClient;

	public SearchController(WebClient.Builder weBuilder) {
		this.webClient = weBuilder
				.exchangeStrategies(ExchangeStrategies.builder()
						.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)).build())
				.baseUrl("https://openlibrary.org/search.json").build();
	}

	@GetMapping(value = "/search/books")
	public String searchBooks(@RequestParam String query, Model model) {
		Mono<SearchResult> result = this.webClient.get().uri("?q={query}", query).retrieve()
				.bodyToMono(SearchResult.class);
		List<SearchResultBook> searchResults = result.block().getDocs().stream().limit(10).map(resultBook -> {
			SearchResultBook searchResultBook = resultBook;
			searchResultBook.setKey(searchResultBook.getKey().replaceAll("/works/", ""));
			String coverId = searchResultBook.getCover_i();
			if (coverId != null) {
				coverId = COVER_IMAGE_URL + coverId + "-M.jpg";
			} else {
				coverId = "/images/no-image.jpg";
			}
			searchResultBook.setCover_i(coverId);
			return searchResultBook;
		}).collect(Collectors.toList());
		model.addAttribute("searchResults", searchResults);

		return "search";
	}

}
