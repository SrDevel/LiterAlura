package org.aluralatam.literalura.service;

import org.aluralatam.literalura.dto.AuthorDTO;
import org.aluralatam.literalura.dto.BookDTO;
import org.aluralatam.literalura.model.Author;
import org.aluralatam.literalura.model.Book;
import org.aluralatam.literalura.model.Language;
import org.aluralatam.literalura.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;

    public void saveBook(BookDTO bookDTO) {
        repository.save(convertToEntity(bookDTO));
    }

    public List<BookDTO> getAllBooks(){
        return convertToDTO(repository.findAll());
    }

    private List<BookDTO> convertToDTO(List<Book> book) {
        return book.stream()
                .map(b -> new BookDTO(
                        b.getTitle(),
                        convertAuthorToDTO(b.getAuthor()),
                        List.of(b.getLanguage().toString()),
                        b.getDownloads()
                ))
                .toList();
    }

    private BookDTO convertToDTO(Book book) {
        return new BookDTO(
                book.getTitle(),
                convertAuthorToDTO(book.getAuthor()),
                List.of(book.getLanguage().toString()),
                book.getDownloads()
        );
    }

    private Book convertToEntity(BookDTO bookDTO) {
        return new Book(
                bookDTO.title(),
                bookDTO.authors().stream()
                        .map(a ->
                                new Author(a.name(),
                                        a.birthYear(),
                                        a.deathYear())).toList(),
                Language.fromCode(String.valueOf(bookDTO.languages().get(0))),
                bookDTO.downloads()
        );
    }

    private List<AuthorDTO> convertAuthorToDTO(List<Author> authors) {
        return authors.stream()
                .map(a -> new AuthorDTO(
                        a.getName(),
                        a.getBirthYear(),
                        a.getDeathYear()
                ))
                .toList();
    }

}
