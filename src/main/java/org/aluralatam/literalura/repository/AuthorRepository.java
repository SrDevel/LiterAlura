package org.aluralatam.literalura.repository;

import org.aluralatam.literalura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("SELECT a FROM Author a WHERE a.deathYear IS NULL OR a.deathYear > :year")
    List<Author> listAuthorsAliveUntilYear(int year);
}
