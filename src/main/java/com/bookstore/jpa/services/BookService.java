package com.bookstore.jpa.services;

import com.bookstore.jpa.doman.Author;
import com.bookstore.jpa.doman.Book;
import com.bookstore.jpa.doman.Publisher;
import com.bookstore.jpa.doman.Review;
import com.bookstore.jpa.dtos.BookRecordDto;
import com.bookstore.jpa.repository.AuthorRepository;
import com.bookstore.jpa.repository.BookRepository;
import com.bookstore.jpa.repository.PublisherRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    @Transactional // se der erro em algumas das partes tem um rollback e volta tudo para a estaca zero
    public Book saveBook(BookRecordDto dados){
        try {
            Book book = new Book();
            book.setTitle(dados.title());

            // Verifica se o Publisher existe
            Publisher publisher = publisherRepository.findById(dados.publisherId())
                    .orElseThrow(() -> new RuntimeException("Publisher not found"));
            book.setPublisher(publisher);

            // Verifica se os Authors existem
            Set<Author> authors = authorRepository.findAllById(dados.authorIds()).stream()
                    .collect(Collectors.toSet());
            if (authors.isEmpty()) {
                throw new RuntimeException("No authors found with provided IDs");
            }
            book.setAuthors(authors);

            Review review = new Review();
            review.setComment(dados.reviewComment());
            review.setBook(book);
            book.setReview(review);

            return bookRepository.save(book);
        } catch (Exception e) {
            // Log e rethrow para debugging
            e.printStackTrace();
            throw new RuntimeException("Error saving book: " + e.getMessage(), e);
        }
    }

    // Novo método para obter títulos de livros e nomes de autores
    public List<String> getBookAndAuthorNames() {
        List<Object[]> results = bookRepository.findBookAndAuthorNames();
        return results.stream()
                .map(row -> "Book Title: " + row[0] + ", Author Name: " + row[1])
                .collect(Collectors.toList());
    }

    // metodo para deletar um livro
    @Transactional // para fazer a deleção em cascata
    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }

    // fazer uma tratativa de erros, pois se der algum erro na base de dados, pois senão encontrar
    // por exemplo a editora, então implementar nesse codigo para inserir tratativas de erro como
    // try/catch para que na hora que não encotrar ele cria um exception para enviar os dados
    // falando que os dados não foram encotrados, entre outras tratativas que podem ser usadas
    // nesse cenario
}
