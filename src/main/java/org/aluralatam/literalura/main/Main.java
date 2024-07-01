package org.aluralatam.literalura.main;

import org.aluralatam.literalura.dto.AuthorDTO;
import org.aluralatam.literalura.dto.BookDTO;
import org.aluralatam.literalura.dto.BookDataDTO;
import org.aluralatam.literalura.model.Author;
import org.aluralatam.literalura.model.Book;
import org.aluralatam.literalura.model.Language;
import org.aluralatam.literalura.repository.AuthorRepository;
import org.aluralatam.literalura.repository.BookRepository;
import org.aluralatam.literalura.service.ApiConnection;
import org.aluralatam.literalura.service.BookService;
import org.aluralatam.literalura.service.DataConverterImpl;
import org.aluralatam.literalura.validations.DataValidator;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Main {
    private final DataConverterImpl dataConverter = new DataConverterImpl();
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final Scanner sc = new Scanner(System.in);

    private Book bookEntity;

    public Main(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void showMenu(){
        do {
            var menu = """
                    --- Bienvenido a Literalura ---
                    
                    1. Buscar un libro por título
                    2. Listar todos los libros buscados
                    3. Listar libros por idioma
                    4. Autores vivos hasta un año específico
                    5. Estadísticas de libros
                    6. Marcar libro como favorito
                    7. Listar libros favoritos
                    8. Salir
                    
                    Ingrese una opción:
                    """;
            System.out.println(menu);
            String option = sc.nextLine();

            if (!DataValidator.isPositiveInteger(option)){
                System.out.println("Opción \"" + option + "\" no válida\n");
                System.out.println(menu);
                option = sc.next();
                sc.nextLine();
            }
            
            switch (Integer.parseInt(option)){
                case 1 -> searchBookByTitle();
                case 2 -> {
                    System.out.println("------------ Lista de libros ------------");
                    bookRepository.findAll().forEach(b -> {
                        System.out.println("Título: " + b.getTitle()
                                + "\nAutores: " + String.valueOf(b.getAuthor().stream().map(Author::getName).toList()).replace("[", "").replace("]", "")
                                + "\nIdioma: "
                                + b.getLanguage().getSpanishName()
                                + "\nDescargas: " + b.getDownloads() + "\n");
                        System.out.println("----------------------------------------");
                    });
                }
                case 3 -> listBooksByLanguage();
                case 4 -> listAuthorsAliveUntilYear();
                case 5 -> statistics();
                case 6 -> markBookAsFavorite();
                case 7 -> listFavorites();
                case 8 -> {
                    System.out.println("¡Hasta luego!");
                    System.exit(0);
                }
                default -> System.out.println("Opción: \"" + option + "\" inválida");
            }

        } while (true);
    }

    private void searchBookByTitle() {

        System.out.println("Ingrese el título del libro que desea buscar: ");

        var title = sc.nextLine();

        String URL_BASE = "https://gutendex.com/books/?search=";

        if (DataValidator.isValidString(title)){
            System.out.println("Título \"" + title + "\" no válido");
            title = sc.nextLine();
        }

        var json = ApiConnection.getApiData(URL_BASE + title.replace(" ", "+"));

        var bookData = dataConverter.convert(json, BookDataDTO.class);

        String finalTitle = title;
        
        Optional<BookDTO> book = bookData.books().stream()
                .filter(l -> l.title().toUpperCase().contains(finalTitle.toUpperCase()))
                .findFirst();

        if (book.isPresent() &&
                !bookRepository.existsByTitleIgnoreCase(book.get().title())){

            System.out.println("---- Libro encontrado ----");
            System.out.println("Título: " + book.get().title()
                    + "\nAutores: " + String.valueOf(book.get().authors().stream().map(AuthorDTO::name).toList()).replace("[", "").replace("]", "")
                    + "\nIdioma: " + book.get().languages().get(0)
                    + "\nDescargas: " + book.get().downloads());
            System.out.println("--------------------------");


            bookEntity = BookService.convertToEntity(book.get());

            List<Author> authors = getAuthorsFromBook();

            bookEntity.setAuthor(authors);
            bookRepository.save(bookEntity);

        } else {
            System.out.println("Libro no encontrado o ya existente en la base de datos");
        }
    }

    private void listBooksByLanguage() {
        System.out.println("Idiomas disponibles: ");
        for (var language : Language.values()){
            System.out.println(language.getCode() + " - " + language.getSpanishName());
        }

        System.out.println("Ingrese el idioma de los libros que desea buscar: ");
        var userLanguage = sc.nextLine();

        while (DataValidator.isValidString(userLanguage)){
            System.out.println("Idioma \"" + userLanguage + "\" no válido");
            userLanguage = sc.nextLine();
        }

        var language = Language.fromCode(userLanguage);

        var books = bookRepository.findByLanguage(language);

        assert language != null;
        if (books.isEmpty()){
            System.out.println("No se encontraron libros en el idioma: " + language.getSpanishName());
        } else {
            System.out.println("------------ Libros en " + language.getSpanishName() + " ------------");
            books.forEach(b -> {
                System.out.println("Título: " + b.getTitle()
                        + "\nAutores: " + String.valueOf(b.getAuthor().stream().map(Author::getName).toList()).replace("[", "").replace("]", "")
                        + "\nDescargas: " + b.getDownloads() + "\n");
                System.out.println("----------------------------------------");
            });
        }
        
    }

    private void listAuthorsAliveUntilYear(){
        System.out.println("Ingrese el año hasta el cual desea buscar autores vivos: ");
        var year = sc.nextLine();

        if (DataValidator.isPositiveInteger(year)){
            System.out.println("Año \"" + year + "\" no válido");
            year = sc.nextLine();
        }

        var authors = authorRepository.listAuthorsAliveUntilYear(Integer.parseInt(year));

        if (authors.isEmpty()){
            System.out.println("No se encontraron autores vivos hasta el año: " + year);
        } else {
            System.out.println("------------ Autores vivos hasta el año " + year + " ------------");
            authors.forEach(a -> {
                System.out.println("Nombre: " + a.getName()
                        + "\nAño de nacimiento: " + a.getBirthYear()
                        + "\nAño de muerte: " + a.getDeathYear() + "\n");
                System.out.println("----------------------------------------");
            });
        }
    }

    private List<Author> getAuthorsFromBook(){
        List<Author> authors = new ArrayList<>(bookEntity.getAuthor());

        for (int i = 0; i < authors.size(); i++) {
            Author author = authors.get(i);
            Optional<Author> existingAuthor = authorRepository.findByNameIgnoreCase(author.getName());
            if (existingAuthor.isPresent()) {
                authors.set(i, existingAuthor.get());
            } else {
                Author savedAuthor = authorRepository.save(author);
                authors.set(i, savedAuthor);
            }
        }
        return authors;
    }

    private void statistics(){
        List<Book> books = bookRepository.findAll();
        DoubleSummaryStatistics stats = books.stream()
                .mapToDouble(Book::getDownloads)
                .summaryStatistics();

        System.out.println("Estadísticas de descargas: ");
        System.out.println("Descargas totales: " + stats.getSum()
                + "\nPromedio de descargas: " + stats.getAverage()
                + "\nMáximo de descargas: " + stats.getMax()
                + "\nMínimo de descargas: " + stats.getMin());

    }

    private void markBookAsFavorite(){
        bookRepository.findAll().forEach(b -> System.out.println(b.getTitle()));

        System.out.println("Ingrese el título del libro que desea marcar como favorito: ");
        var title = sc.nextLine();

        if (DataValidator.isValidString(title)){
            System.out.println("Título \"" + title + "\" no válido");
            title = sc.nextLine();
        }

        var book = bookRepository.findByTitleIgnoreCase(title);

        if (book.isPresent()){
            book.get().setFavorite(true);
            bookRepository.save(book.get());
            System.out.println("Libro marcado como favorito");
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    private void listFavorites() {
        var favorites = bookRepository.findByFavoriteTrue();

        if (favorites.isEmpty()){
            System.out.println("No hay libros marcados como favoritos");
        } else {
            System.out.println("------------ Libros favoritos ------------");
            favorites.forEach(b -> {
                System.out.println("Título: " + b.getTitle()
                        + "\nAutores: " + String.valueOf(b.getAuthor().stream().map(Author::getName).toList()).replace("[", "").replace("]", "")
                        + "\nIdioma: "
                        + b.getLanguage().getSpanishName()
                        + "\nDescargas: " + b.getDownloads() + "\n");
                System.out.println("----------------------------------------");
            });
        }
    }

}
