package com.example.libraryproject.services;

import com.example.libraryproject.Models.Book;
import com.example.libraryproject.Models.BookCategory;
import com.example.libraryproject.Repository.BookCategoryRepository;
import com.example.libraryproject.Repository.BookRepository;
import com.example.libraryproject.payload.response.MessageResponse;
import lombok.SneakyThrows;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;

import static com.example.libraryproject.services.BorrowBookService.AVAILABLE_PATH;

@Service
public class BookService {


    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookCategoryRepository bookCategoryRepository;


    @PostConstruct
    public void addPostBook() {


        if (bookCategoryRepository.findByCategoryName("Akcja") == null)
            bookCategoryRepository.save(new BookCategory("Akcja"));

        if (bookCategoryRepository.findByCategoryName("Powieść") == null)
            bookCategoryRepository.save(new BookCategory("Powieść"));

        if (bookCategoryRepository.findByCategoryName("Science-Fiction") == null)
            bookCategoryRepository.save(new BookCategory("Science-Fiction"));

        if (bookCategoryRepository.findByCategoryName("Thriller") == null)
            bookCategoryRepository.save(new BookCategory("Thriller"));

        if (bookCategoryRepository.findByCategoryName("Dramat") == null)
            bookCategoryRepository.save(new BookCategory("Dramat"));

        if (bookCategoryRepository.findByCategoryName("Kryminał") == null)
            bookCategoryRepository.save(new BookCategory("Kryminał"));

        if (bookCategoryRepository.findByCategoryName("Komedia") == null)
            bookCategoryRepository.save(new BookCategory("Komedia"));


        if (bookRepository.findAll().isEmpty()) {
            bookRepository.save(new Book("Wielka wyprawa księcia Racibora", "Artur Szreiter", 1911,
                    "8205236266118", 7, bookCategoryRepository.findByCategoryName("Powieść")));
            bookRepository.save(new Book("Zaginione królestwa", "Norman Davies", 1866,
                    "1732659511374", bookCategoryRepository.findByCategoryName("Kryminał")));
            bookRepository.save(new Book("Pod pogańskim sztandarem", "Artur Szreiter", 1889,
                    "3464671469218", bookCategoryRepository.findByCategoryName("Powieść")));
            bookRepository.save(new Book("Zdobywcy", "Roger Crowley", 1901,
                    "6090851170392", bookCategoryRepository.findByCategoryName("Dramat")));
            bookRepository.save(new Book("Bohaterowie Słowian Połabskich", "Jerzy Strzelczyk", 1901,
                    "2686765240001", bookCategoryRepository.findByCategoryName("Dramat")));
            bookRepository.save(new Book("Zabójcze układy", "Przemysław Gasztold",
                    1943, "1569634489760", bookCategoryRepository.findByCategoryName("Akcja")));
            bookRepository.save(new Book("Data science od podstaw", "Josel Grus", 1839,
                    "4440014035818", bookCategoryRepository.findByCategoryName("Dramat")));

            try {

                FileUtils.deleteDirectory(AVAILABLE_PATH.toFile());
                Files.createDirectory(AVAILABLE_PATH);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ResponseEntity<?> addBook(Book book) {

        if (book.getBookCategory() == null) {
            book.setBookCategory(bookCategoryRepository.findByCategoryName("Akcja"));
        }

        if (bookRepository.existsByISBN(book.getISBN())) {
            Book id = bookRepository.findByISBN(book.getISBN()).orElseThrow(() -> null);
            if (id.getBookName().equals(book.getBookName()) && id.getAuthor().equals(book.getAuthor())
                    && id.getYearOfPublication() == book.getYearOfPublication()) {

                bookRepository.addBookCapacity(book.getCapacity(), id.getIdBook());
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse("Kopia książki została dodana!"));

            } else {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Błąd! Ten numer ISBN jest już wykorzystany"));
            }

        } else {
            BookCategory byCategoryName = bookCategoryRepository.findByCategoryName(book.getBookCategory().getCategoryName());
            book.setBookCategory(byCategoryName);
            bookRepository.save(book);
            return ResponseEntity
                    .ok(new MessageResponse("Poprawnie udało się dodać nową książkę!"));
        }

    }

    public void addImageBook(MultipartFile image) {
        File file = new File(FileSystems.getDefault().getPath("src", "main", "resources", "images").toAbsolutePath()
                + "\\" + image.getOriginalFilename());


        try (OutputStream os = new FileOutputStream(file)) {
            os.write(image.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public void addEBook(MultipartFile book) {
        File file = new File(FileSystems.getDefault().getPath("src", "main", "resources", "books", "blocked").toAbsolutePath()
                + "\\" + book.getOriginalFilename());

        try (OutputStream os = new FileOutputStream(file)) {
            os.write(book.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public ResponseEntity<MessageResponse> deleteBook(Book book) {
        bookRepository.delete(book);

        return ResponseEntity
                .ok()
                .body(new MessageResponse("Poprawnie usunięto książke"));
    }

    public ResponseEntity<MessageResponse> deleteBook(Long idBook) {
        Book book = bookRepository.findById(idBook).orElse(null);

        if (book == null)
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Wystąpił błąd."));


        if (book.getCapacity() == 1)
            bookRepository.delete(book);
        else {
            book.setCapacity(book.getCapacity() - 1);
            bookRepository.save(book);
        }


        return ResponseEntity
                .ok()
                .body(new MessageResponse("Poprawnie usunięto książke"));
    }

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getBooks(int capacity) {

        return bookRepository.findByCapacityGreaterThanOrderByIdBookDesc(capacity);
    }

    public List<Book> getFilterBooks(String name) {
        return bookRepository.findByBookNameContainsIgnoreCase(name);
    }

    public Book getBook(long idBook) {
        return bookRepository.findById(idBook).orElse(null);
    }

    public List<Book> getBooksByCategory(String category) {
        BookCategory categoryName = bookCategoryRepository.findByCategoryName(category);

        return bookRepository.findByBookCategory(categoryName);
    }

    public List<Book> getFilterBooksByCategory(String category, String filterName) {
        List<Book> booksByCategory = getBooksByCategory(category);

        return booksByCategory.stream().filter(book -> book.getBookName().toLowerCase().contains(filterName)).toList();
    }

    public List<BookCategory> getCategoriesBookName() {
        return bookCategoryRepository.findAll();
    }

    ;


}
