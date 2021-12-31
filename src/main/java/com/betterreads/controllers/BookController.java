package com.betterreads.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.betterreads.book.Book;
import com.betterreads.book.BookRepository;

@Controller
public class BookController {

	@Autowired
	BookRepository bookRepository;

	private static String COVER_IMAGE_URL = "https://covers.openlibrary.org/b/id/";

	@GetMapping(value = "/books/{bookId}")
	public String getBookById(@PathVariable String bookId, Model model) {
		Optional<Book> optionalBook = bookRepository.findById(bookId);
		String coverImage = "";
		if (optionalBook.isPresent()) {
			Book book = optionalBook.get();
			model.addAttribute("book", book);
			if (book.getCoverIds() != null && !book.getCoverIds().isEmpty()) {
				coverImage = COVER_IMAGE_URL + book.getCoverIds().get(0) + "-L.jpg";
			} else {
				coverImage = "/images/no-image.jpg";
			}
			model.addAttribute("coverImage", coverImage);
			return "book";
		} else {
			return "book-not-found";
		}
	}

}
