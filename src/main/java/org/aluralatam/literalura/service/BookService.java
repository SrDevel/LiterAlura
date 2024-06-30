package org.aluralatam.literalura.service;

import org.aluralatam.literalura.dto.AuthorDTO;
import org.aluralatam.literalura.dto.BookDTO;
import org.aluralatam.literalura.model.Author;
import org.aluralatam.literalura.model.Book;
import org.aluralatam.literalura.model.Language;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private List<BookDTO> convertToDTO(@NotNull List<Book> book) {
        return book.stream()
                .map(b -> new BookDTO(
                        b.getTitle(),
                        convertAuthorToDTO(b.getAuthor()),
                        List.of(b.getLanguage().toString()),
                        b.getDownloads()
                ))
                .toList();
    }

    @NotNull
    @Contract("_ -> new")
    private BookDTO convertToDTO(Book book) {
        return new BookDTO(
                book.getTitle(),
                convertAuthorToDTO(book.getAuthor()),
                List.of(book.getLanguage().toString()),
                book.getDownloads()
        );
    }

    @NotNull
    @Contract("_ -> new")
    public static Book convertToEntity(BookDTO bookDTO) {
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

    private List<AuthorDTO> convertAuthorToDTO(@NotNull List<Author> authors) {
        return authors.stream()
                .map(a -> new AuthorDTO(
                        a.getName(),
                        a.getBirthYear(),
                        a.getDeathYear()
                ))
                .toList();
    }

}
