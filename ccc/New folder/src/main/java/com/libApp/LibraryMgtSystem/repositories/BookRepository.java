package com.libApp.LibraryMgtSystem.repositories;

import com.libApp.LibraryMgtSystem.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {

}