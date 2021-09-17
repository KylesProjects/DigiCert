package com.digicert.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digicert.domain.Book;

// This interface provides an API for CRUD operations as well as pagination and sorting. 
@Repository
public interface BookRepository extends JpaRepository<Book, Long>{

	Book findByTitle(String title);

}
