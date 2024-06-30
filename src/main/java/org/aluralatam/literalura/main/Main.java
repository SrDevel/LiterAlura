package org.aluralatam.literalura.main;

import org.aluralatam.literalura.dto.BookDTO;
import org.aluralatam.literalura.service.DataConverterImpl;

import java.util.Scanner;

public class Main {
    private final DataConverterImpl dataConverter = new DataConverterImpl();
    private final String URL_BASE = "https://gutendex.com/books/";

    private final Scanner sc = new Scanner(System.in);

    public void showMenu(){
        do {
            System.out.println("""
                    --- Bienvenido a Literalura ---
                    
                    1. Buscar un libro por título
                    2. Listar todos los libros buscados
                    3. Listar libros por idioma
                    4. Salir
                    """);

            System.out.print("Ingrese una opción: ");
            int option = sc.nextInt();
            sc.nextLine();

            switch (option){
                case 1 -> {

                }
                case 2:
                    searchBookByTitle();
                    break;
                case 3:
                    break;
                case 4:
                    System.out.println("¡Hasta luego!");
                    return;
                default:
                    System.out.println("Opción inválida");
            }
        } while (true);
        }

    }

    private BookDTO searchBookByTitle() {
        System.out.print("Ingrese el título del libro que desea buscar: ");
        String title = sc.nextLine();

        String json = apiConnection.getApiData(URL_BASE + "?search=" + title.replace(" ", "+"));
        var bookData = dataConverter.convert(json, Data.class);
        Optional<BookData> book = bookData.books().stream()
                .filter(l -> l.title().toUpperCase().contains(title.toUpperCase()))
                .findFirst();

        if (book.isPresent()) {
            System.out.println("Libro encontrado: " + book.get());
        } else {
            System.out.println("Libro no encontrado");
        }

        return book;
    }
}
