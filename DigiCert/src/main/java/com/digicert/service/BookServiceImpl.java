package com.digicert.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digicert.domain.Book;
import com.digicert.repository.BookRepository;

// This class will provide business logic performed on the objects passed through.
@Service
public class BookServiceImpl implements BookService{

	
	@Autowired
	BookRepository bookRepository;


	@Override
	public List<Book> getBooks() {
		return bookRepository.findAll();
	}

	@Override
	public Book getBook(long id) {
		Optional<Book> option =bookRepository.findById(id);
		return option.orElse(null);
	}

	@Override
	public Book addBook(Book book) {
		return bookRepository.save(book);
	}

	@Override
	public Book getBook(String title) {
		return bookRepository.findByTitle(title);
	}

	@Override
	public void deleteById(long id) {
		bookRepository.deleteById(id);
		
	}

	@Override
	public Book updateBook(Book book) {
		return bookRepository.save(book);
		
	}
}
