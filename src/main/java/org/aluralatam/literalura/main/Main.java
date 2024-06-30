package org.aluralatam.literalura.main;

import org.aluralatam.literalura.dto.AuthorDTO;
import org.aluralatam.literalura.dto.BookDTO;
import org.aluralatam.literalura.dto.BookDataDTO;
import org.aluralatam.literalura.model.Author;
import org.aluralatam.literalura.model.Language;
import org.aluralatam.literalura.repository.AuthorRepository;
import org.aluralatam.literalura.repository.BookRepository;
import org.aluralatam.literalura.service.ApiConnection;
import org.aluralatam.literalura.service.BookService;
import org.aluralatam.literalura.service.DataConverterImpl;
import org.aluralatam.literalura.validations.DataValidator;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Scanner;

@Service
public class Main {
    private final DataConverterImpl dataConverter = new DataConverterImpl();
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final Scanner sc = new Scanner(System.in);

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
                    5. Salir
                    
                    Ingrese una opción:
                    """;
            System.out.println(menu);
            String option = sc.next();

            while (!DataValidator.isPositiveInteger(option)){
                System.out.println("Opción \"" + option + "\" no válida\n");
                System.out.println(menu);
                option = sc.next();
                sc.nextLine();
            }

            switch (Integer.parseInt(option)){
                case 1 -> {
                    var book = searchBookByTitle();
                    if (book.isPresent()){
                        System.out.println("---- Libro encontrado ----");
                        System.out.println("Título: " + book.get().title()
                                + "\nAutores: " + String.valueOf(book.get().authors().stream().map(AuthorDTO::name).toList()).replace("[", "").replace("]", "")
                                + "\nIdioma: " + book.get().languages().get(0)
                                + "\nDescargas: " + book.get().downloads());
                        System.out.println("--------------------------");

                        bookRepository.save(BookService.convertToEntity(book.get()));
                    } else {
                        System.out.println("Libro no encontrado");
                    }
                }
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
                case 5 -> {
                    System.out.println("¡Hasta luego!");
                    System.exit(0);
                }
                default -> System.out.println("Opción: \"" + option + "\" inválida");
            }

        } while (true);
    }

    @NotNull
    private Optional<BookDTO> searchBookByTitle() {

        System.out.println("Ingrese el título del libro que desea buscar: ");

        var title = sc.nextLine();

        String URL_BASE = "https://gutendex.com/books/?search=";

        // Nos aseguramos de la consulta a la api no se haga hasta que el usuario haya ingresado un título
        while (title.isBlank()){
            title = sc.nextLine();
        }

        var json = ApiConnection.getApiData(URL_BASE + title.replace(" ", "+"));

        var bookData = dataConverter.convert(json, BookDataDTO.class);

        String finalTitle = title;
        return bookData.books().stream()
                .filter(l -> l.title().toUpperCase().contains(finalTitle.toUpperCase()))
                .findFirst();
    }

    private void listBooksByLanguage() {
        // Obtenemos los idiomas del enum Language
        System.out.println("Idiomas disponibles: ");
        for (var language : Language.values()){
            System.out.println(language.getCode() + " - " + language.getSpanishName());
        }
        System.out.println("Ingrese el idioma de los libros que desea buscar: ");
        var userLanguage = sc.nextLine();

        // Nos aseguramos de que el usuario haya ingresado un idioma válido
        while (!DataValidator.isValidString(userLanguage)){
            System.out.println("Idioma \"" + userLanguage + "\" no válido");
            userLanguage = sc.nextLine();
        }

        // Obtenemos el idioma seleccionado
        var language = Language.fromCode(userLanguage);

        // Obtenemos los libros que coincidan con el idioma seleccionado
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

        while (!DataValidator.isPositiveInteger(year)){
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

}
