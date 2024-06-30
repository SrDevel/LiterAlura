package org.aluralatam.literalura;

import org.aluralatam.literalura.repository.AuthorRepository;
import org.aluralatam.literalura.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.aluralatam.literalura.main.Main;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {

    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    BookRepository bookRepository;


    public static void main(String[] args) {
        SpringApplication.run(LiterAluraApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Main main = new Main(bookRepository, authorRepository);
        main.showMenu();
    }
}
