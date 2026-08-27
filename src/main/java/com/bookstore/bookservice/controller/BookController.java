package com.bookstore.bookservice.controller;

import com.bookstore.bookservice.model.Book;
import com.bookstore.bookservice.service.BookService;
import com.bookstore.bookservice.service.GcsStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {

    private final BookService bookService;
    private final GcsStorageService gcsStorageService;

    public BookController(BookService bookService, GcsStorageService gcsStorageService) {
        this.bookService = bookService;
        this.gcsStorageService = gcsStorageService;
    }

    @GetMapping
    public List<Book> getBooks(@RequestParam(required = false) String category,
                               @RequestParam(required = false) String search) {
        return bookService.findBooks(category, search);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable String id) {
        return bookService.getBookById(id)
                .map(book -> ResponseEntity.ok((Object) book))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Book not found with ID: " + id)));
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.saveBook(book));
    }

    @PostMapping("/upload-cover")
    public ResponseEntity<Map<String, Object>> uploadCover(@RequestParam("file") MultipartFile file) {
        try {
            String url = gcsStorageService.uploadCoverImage(file);
            Map<String, Object> resp = new LinkedHashMap<>();
            resp.put("success", true);
            resp.put("fileName", file.getOriginalFilename());
            resp.put("coverImageUrl", url);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/reduce-stock")
    public ResponseEntity<?> reduceStock(@PathVariable String id, @RequestParam int quantity) {
        Optional<Book> opt = bookService.getBookById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Book not found"));
        }
        Book book = opt.get();
        if (book.getStockQuantity() == null || book.getStockQuantity() < quantity) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "error", "Insufficient stock. Available: " + (book.getStockQuantity() != null ? book.getStockQuantity() : 0)
            ));
        }

        book.setStockQuantity(book.getStockQuantity() - quantity);
        bookService.saveBook(book);

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("success", true);
        resp.put("bookId", book.getId());
        resp.put("title", book.getTitle());
        resp.put("remainingStock", book.getStockQuantity());
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}/restore-stock")
    public ResponseEntity<?> restoreStock(@PathVariable String id, @RequestParam int quantity) {
        Optional<Book> opt = bookService.getBookById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Book not found"));
        }
        Book book = opt.get();
        book.setStockQuantity((book.getStockQuantity() != null ? book.getStockQuantity() : 0) + quantity);
        bookService.saveBook(book);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "bookId", book.getId(),
                "updatedStock", book.getStockQuantity()
        ));
    }
}
