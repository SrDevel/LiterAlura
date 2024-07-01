package org.aluralatam.literalura.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Author> author;
    @Enumerated(EnumType.STRING)
    private Language language;
    private Double downloads;
    // Le da un valor por defecto a la propiedad favorite
    @Column(columnDefinition = "boolean default false")
    private Boolean favorite;

    public Book(String title, List<Author> author, Language language, Double downloads) {
        this.title = title;
        this.author = author;
        this.language = language;
        this.downloads = downloads;
    }

    @Override
    public String toString() {
        return "Titulo: " + title
                + "\nAutores: " + author
                + "\nIdioma: " + language
                + "\nDownloads: " + downloads;
    }
}
