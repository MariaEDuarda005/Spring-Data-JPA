package com.bookstore.jpa.repository;

import com.bookstore.jpa.doman.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Book findBookByTitle(String title); // pode ter mais de um parametro como findBookByTitleANDReview

    // @Query -> usa quando precisa de uma consulta um pouco mais elaborada
    @Query(value = "SELECT * FROM tb_book WHERE publisher_id = :id", nativeQuery = true)
    List<Book> findBooksByPublisherId(@Param("id") Long id);


    // função teste para ver se o inner join funciona
    @Query(value = "SELECT b.title AS bookTitle, a.name AS authorName " +
            "FROM tb_book_author ba " +
            "INNER JOIN tb_book b ON ba.book_id = b.id " +
            "INNER JOIN tb_author a ON ba.author_id = a.id",
            nativeQuery = true)
    List<Object[]> findBookAndAuthorNames();

}
