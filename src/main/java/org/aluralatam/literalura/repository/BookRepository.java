package org.aluralatam.literalura.repository;

import org.aluralatam.literalura.model.Book;
import org.aluralatam.literalura.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByLanguage(Language language);
}
