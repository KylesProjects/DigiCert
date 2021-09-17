package com.digicert.service;

import java.util.List;

import com.digicert.domain.Book;

// This interface is used to make sure all necessary contracts are implemented
public interface BookService {

	Book addBook(Book book);

	List<Book> getBooks();

	Book getBook(long id);

	Book getBook(String title);

	void deleteById(long id);

	Book updateBook(Book book);

}
