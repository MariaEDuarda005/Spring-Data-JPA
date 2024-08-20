package com.bookstore.jpa.doman;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TB_BOOK")
@Data
// implements Serializable -> é para mostrar está habilitada e pode ser serializada
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // não pode ser nulo e tem que ser unico
    private String title;

    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    // a chave estrangeira ligada a outra entidade
    private Publisher publisher;

    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // remover junto com o FetchType.LAZY
    @ManyToMany//(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @JsonIgnore
    private Set<Author> authors = new HashSet<>();

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL) // mappedBy: passa o dono do relacionamento
    //@JsonIgnore
    private Review review;
}
