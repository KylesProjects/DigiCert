package com.digicert.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.digicert.domain.Book;
import com.digicert.service.BookService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

// This class handles the client request in order to respond with required JSON data and HTTP statuses.  
@RestController
public class BookController {

	@Autowired
	BookService bookService;
	
	/* 
	 * This method checks to see if the book already exists in the database. 
	 * If it is, returns 409 error. 
	 * If its not, creates a new Book object and sets the JSON attributes to the book fields and then returns the Book object back in JSON format.
	*/ 
	@RequestMapping(value="/addBook", method=RequestMethod.POST)
	public ResponseEntity<JsonNode> addBook(@RequestBody JsonNode jBook){
		Book bookCheck = bookService.getBook(jBook.get("title").asText());
		if(bookCheck!=null) {
			return new ResponseEntity<JsonNode>(HttpStatus.CONFLICT);
		}else {
			Book book = new Book();
			book.setAuthor(jBook.get("author").asText());
			book.setTitle(jBook.get("title").asText());
			Book newBook = bookService.addBook(book);
			ObjectMapper mapper = new ObjectMapper();
			return new ResponseEntity<JsonNode>(mapper.convertValue(newBook, JsonNode.class), HttpStatus.CREATED);
		}
		
		
	}
	/* 
	 * This method gets all available books from the database.
	 * Converts the List of Books into a List of JsonNodes and returns the list of JsonNodes.
	 */
	@RequestMapping(value="/getBooks", method=RequestMethod.GET)
	public ResponseEntity<List<JsonNode>> getBooks(){
		List<Book> listOfBooks = bookService.getBooks();
		ObjectMapper mapper = new ObjectMapper();
		List<JsonNode> listOfJson = new ArrayList<>();
		for(Book b : listOfBooks) {
			listOfJson.add(mapper.convertValue(b, JsonNode.class));
		}
		
		return new ResponseEntity<List<JsonNode>>(listOfJson, HttpStatus.OK);
	}
	
	/* This method checks to see if the book is available via Book id. 
	 * If it is, converts the Book object into a JsonNode and sends the JsonNode back to the client.
	 * If it is not available, it returns a 400 error.
	 */ 
	@RequestMapping(value="/getBook/{id}", method=RequestMethod.GET)
	public ResponseEntity<JsonNode> getBook(@PathVariable long id) {
		Book book = bookService.getBook(id);
		if(book!=null) {
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode oNode = mapper.createObjectNode();
			oNode.put("id", book.getId());
			oNode.put("title", book.getTitle());
			oNode.put("author", book.getAuthor());		
			JsonNode jBook = mapper.convertValue(oNode, JsonNode.class);
			return new ResponseEntity<JsonNode>(jBook, HttpStatus.OK);
		}else {
			return new ResponseEntity<JsonNode>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	/* This method checks to see if the book is available to update if the Book id exists. 
	 * If it is, sets the original Book fields to its new values and converts the Book object into a JsonNode and returns the 
	 * updated Book back to the client. 
	 * If it is not available, returns a 400 error. 
	 */
	@RequestMapping(value="/updateBook/{id}", method=RequestMethod.PUT)
	public ResponseEntity<JsonNode> updateBook(@RequestBody JsonNode jBook, @PathVariable long id){
		Book book = bookService.getBook(id);
		if(book!=null) {
			book.setTitle(jBook.get("title").asText());
			book.setAuthor(jBook.get("author").asText());
			Book updatedBook = bookService.updateBook(book);
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode oNode = mapper.createObjectNode();
			oNode.put("title", updatedBook.getTitle());
			oNode.put("author", updatedBook.getAuthor());
			JsonNode jNode = mapper.convertValue(oNode, JsonNode.class);
			return new ResponseEntity<JsonNode>(mapper.convertValue(jNode, JsonNode.class), HttpStatus.OK);
		}else {
			return new ResponseEntity<JsonNode>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	/* This method checks to see if the book is available to remove from database via Book id search. 
	 * If it is available, deletes the Book object from the database and sends a 200 status back to the client. 
	 * If it is not available, sends a 400 error back to the client. 
	 */
	@RequestMapping(value="/deleteBook/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<JsonNode> deleteBook(@PathVariable long id){
		Book book = bookService.getBook(id);
		if(book!=null) {
			bookService.deleteById(id);
			return new ResponseEntity<JsonNode>(HttpStatus.OK);
		}else {
			return new ResponseEntity<JsonNode>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
}
