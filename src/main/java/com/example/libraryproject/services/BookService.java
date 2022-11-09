package com.example.libraryproject.services;

import com.example.libraryproject.Models.Book;
import com.example.libraryproject.Models.CategoryBook;
import com.example.libraryproject.Repository.BookRepository;
import com.example.libraryproject.Repository.CategoryBookRepository;
import com.example.libraryproject.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class BookService {


    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryBookRepository categoryBookRepository;


    @PostConstruct
    public void addPostBook() {


        if (categoryBookRepository.findByCategoryName("Akcja") == null)
            categoryBookRepository.save(new CategoryBook("Akcja"));

        if (categoryBookRepository.findByCategoryName("Powieść") == null)
            categoryBookRepository.save(new CategoryBook("Powieść"));

        if (categoryBookRepository.findByCategoryName("Science-Fiction") == null)
            categoryBookRepository.save(new CategoryBook("Science-Fiction"));

        if (categoryBookRepository.findByCategoryName("Thriller") == null)
            categoryBookRepository.save(new CategoryBook("Thriller"));

        if (categoryBookRepository.findByCategoryName("Dramat") == null)
            categoryBookRepository.save(new CategoryBook("Dramat"));

        if (categoryBookRepository.findByCategoryName("Kryminał") == null)
            categoryBookRepository.save(new CategoryBook("Kryminał"));

        if (categoryBookRepository.findByCategoryName("Komedia") == null)
            categoryBookRepository.save(new CategoryBook("Komedia"));


        if (bookRepository.findAll().isEmpty()) {
            bookRepository.save(new Book("W pustyni i w puszczy", "Henryk Sienkiewicz", 1911,
                    "8205236266118", categoryBookRepository.findByCategoryName("Powieść")));
            bookRepository.save(new Book("Zbrodnia i kara", "Fiodor Dostojewski", 1866,
                    "1732659511374", categoryBookRepository.findByCategoryName("Kryminał")));
            bookRepository.save(new Book("Lalka", "Bolesław Prus", 1889,
                    "3464671469218", categoryBookRepository.findByCategoryName("Powieść")));
            bookRepository.save(new Book("Wesele", "Stanisław Wyspiański", 1901,
                    "6090851170392", categoryBookRepository.findByCategoryName("Dramat")));
            bookRepository.save(new Book("Hamlet", "William Shakespeare", 1901,
                    "2686765240001", categoryBookRepository.findByCategoryName("Dramat")));
            bookRepository.save(new Book("Kamienie na Szaniec", "Aleksander Kamiński",
                    1943, "1569634489760", categoryBookRepository.findByCategoryName("Akcja")));
            bookRepository.save(new Book("Balladyna", "Juliusz Słowacki", 1839,
                    "4440014035818", categoryBookRepository.findByCategoryName("Dramat")));
        }
    }

    public ResponseEntity<?> addBook(Book book) {
        if (bookRepository.existsByISBN(book.getISBN())) {
            Book id = bookRepository.findByISBN(book.getISBN()).orElseThrow(() -> null);
            if (id.getBookName().equals(book.getBookName()) && id.getAuthor().equals(book.getAuthor()) && id.getYearOfPublication() == book.getYearOfPublication()) {

                bookRepository.addBookCapacity(book.getCapacity(), id.getIdBook());
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse("Copy of book added successfully!"));

            } else {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: ISBN is already taken!"));
            }

        } else {
            bookRepository.save(book);
            return ResponseEntity
                    .ok(new MessageResponse("Book added successfully!"));
        }

    }


    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getBooks(int capacity) {
        return bookRepository.findByCapacityGreaterThan(capacity);
    }

    public List<Book> getFilterBooks(String name) {
        return bookRepository.findByBookNameContainsIgnoreCase(name);
    }

    public Book getBook(long idBook) {
        return bookRepository.findById(idBook).orElse(null);
    }

    public List<Book> getBooksByCategory(String category) {
        CategoryBook categoryName = categoryBookRepository.findByCategoryName(category);

        return bookRepository.findByCategoryBook(categoryName);
    }

    public List<Book> getFilterBooksByCategory(String category, String filterName) {
        List<Book> booksByCategory = getBooksByCategory(category);

        return booksByCategory.stream().filter(book -> book.getBookName().toLowerCase().contains(filterName)).toList();
    }

    public List<CategoryBook> getCategoriesBookName() {
        return categoryBookRepository.findAll();
    }

    ;


}
