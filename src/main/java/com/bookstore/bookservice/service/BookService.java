package com.bookstore.bookservice.service;

import com.bookstore.bookservice.model.Book;
import com.bookstore.bookservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired(required = false)
    private BookRepository bookRepository;

    private final Map<String, Book> store = new ConcurrentHashMap<>();
    private volatile boolean mongoAvailable = false;
    private volatile boolean checkedMongo = false;

    public BookService() {
        initSampleBooks();
    }

    private void initSampleBooks() {
        // --- 1. TECHNOLOGY & CLOUD ARCHITECTURE ---
        addSampleBook("b101", "Cloud-Native Java Microservices", "Josh Long", "978-0134685991", "Technology",
                new BigDecimal("4500.00"), 25,
                "Hands-on guide to building reactive and resilient microservices with Spring Boot, Spring Cloud, and Google Cloud Platform.",
                "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop&q=80",
                List.of("cloud", "java", "microservices", "spring", "gcp"));

        addSampleBook("b102", "Designing Data-Intensive Applications", "Martin Kleppmann", "978-1449373320", "Technology",
                new BigDecimal("5200.00"), 18,
                "The fundamental ideas behind reliable, scalable, and maintainable data systems in modern enterprise cloud computing.",
                "https://images.unsplash.com/photo-1532012164546-f432f2e37b29?w=600&auto=format&fit=crop&q=80",
                List.of("databases", "distributed-systems", "nosql", "mysql"));

        addSampleBook("b103", "Clean Code: A Handbook of Agile Software", "Robert C. Martin", "978-0132350884", "Technology",
                new BigDecimal("3800.00"), 30,
                "A code-centric masterclass on writing clean, readable, testable, and maintainable software in enterprise teams.",
                "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=600&auto=format&fit=crop&q=80",
                List.of("clean-code", "refactoring", "best-practices", "java"));

        addSampleBook("b104", "Building Microservices: Designing Fine-Grained Systems", "Sam Newman", "978-1492034025", "Technology",
                new BigDecimal("4800.00"), 15,
                "Covers key aspects of microservice design including service boundaries, inter-service communication, deployment, and security.",
                "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=600&auto=format&fit=crop&q=80",
                List.of("microservices", "architecture", "devops", "cloud"));

        // --- 2. LITERATURE & CLASSICS ---
        addSampleBook("b105", "Madol Doova (මඩොල් දූව)", "Martin Wickramasinghe", "978-9550201010", "Literature",
                new BigDecimal("1200.00"), 40,
                "Classic adventure novel portraying the life of Upali Gammampila and Jinadasa on an isolated island in southern Sri Lanka.",
                "https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=600&auto=format&fit=crop&q=80",
                List.of("sinhala", "literature", "adventure", "classic"));

        addSampleBook("b106", "Gamperaliya (ගම්පෙරළිය)", "Martin Wickramasinghe", "978-9550201027", "Literature",
                new BigDecimal("1450.00"), 35,
                "The monumental novel depicting the transformation of traditional feudal village society into modern mercantile culture in Sri Lanka.",
                "https://images.unsplash.com/photo-1476275466078-4007374efbbe?w=600&auto=format&fit=crop&q=80",
                List.of("sinhala", "novel", "culture", "heritage"));

        addSampleBook("b107", "To Kill a Mockingbird", "Harper Lee", "978-0061120084", "Literature",
                new BigDecimal("2200.00"), 20,
                "The unforgettable novel of childhood in a sleepy Southern town and the crisis of conscience that rocked it.",
                "https://images.unsplash.com/photo-1495640388908-05fa85288e61?w=600&auto=format&fit=crop&q=80",
                List.of("classic", "fiction", "justice"));

        // --- 3. SELF-HELP & PSYCHOLOGY ---
        addSampleBook("b108", "Atomic Habits", "James Clear", "978-0735211292", "Self-Help",
                new BigDecimal("2900.00"), 28,
                "An easy and proven way to build good habits and break bad ones with small changes that deliver remarkable results.",
                "https://images.unsplash.com/photo-1589829085413-56de8ae18c73?w=600&auto=format&fit=crop&q=80",
                List.of("productivity", "psychology", "habits", "success"));

        addSampleBook("b109", "Deep Work: Rules for Focused Success", "Cal Newport", "978-1455586691", "Self-Help",
                new BigDecimal("3100.00"), 22,
                "Deep work is the ability to focus without distraction on a cognitively demanding task—a super-power in our distracted world.",
                "https://images.unsplash.com/photo-1506784983877-45594efa4cbe?w=600&auto=format&fit=crop&q=80",
                List.of("focus", "productivity", "career"));

        // --- 4. BUSINESS & FINANCE ---
        addSampleBook("b110", "The Psychology of Money", "Morgan Housel", "978-0857197689", "Business",
                new BigDecimal("3200.00"), 25,
                "Timeless lessons on wealth, greed, and happiness doing well with money is not necessarily about what you know. It's about how you behave.",
                "https://images.unsplash.com/photo-1554415707-9e4966772740?w=600&auto=format&fit=crop&q=80",
                List.of("finance", "investing", "wealth", "mindset"));

        addSampleBook("b111", "Zero to One: Notes on Startups", "Peter Thiel", "978-0804139298", "Business",
                new BigDecimal("3400.00"), 18,
                "The great secret of our time is that there are still uncharted frontiers to explore and new inventions to create.",
                "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?w=600&auto=format&fit=crop&q=80",
                List.of("startups", "entrepreneurship", "innovation"));

        // --- 5. SCIENCE & ASTRONOMY ---
        addSampleBook("b112", "Cosmos", "Carl Sagan", "978-0345539434", "Science",
                new BigDecimal("3600.00"), 14,
                "The story of fifteen billion years of cosmic evolution transforming matter and life into consciousness.",
                "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=600&auto=format&fit=crop&q=80",
                List.of("space", "astronomy", "universe", "science"));
    }

    private void addSampleBook(String id, String title, String author, String isbn, String category,
                               BigDecimal price, Integer stock, String desc, String img, List<String> tags) {
        Book b = new Book(id, title, author, isbn, category, price, stock, desc, img, tags, 2024, LocalDateTime.now(), LocalDateTime.now());
        store.put(id, b);
    }

    private boolean isMongoLive() {
        if (checkedMongo) return mongoAvailable;
        if (bookRepository == null) {
            mongoAvailable = false;
            checkedMongo = true;
            return false;
        }
        try {
            bookRepository.count();
            mongoAvailable = true;
        } catch (Exception e) {
            mongoAvailable = false;
        }
        checkedMongo = true;
        return mongoAvailable;
    }

    public List<Book> findBooks(String category, String search) {
        if (isMongoLive()) {
            try {
                if (category != null && !category.isBlank()) {
                    return bookRepository.findByCategoryIgnoreCase(category.trim());
                }
                if (search != null && !search.isBlank()) {
                    return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(search.trim(), search.trim());
                }
                List<Book> dbList = bookRepository.findAll();
                if (!dbList.isEmpty()) return dbList;
            } catch (Exception ignored) {
                mongoAvailable = false;
            }
        }

        return store.values().stream()
                .filter(b -> category == null || category.isBlank() || b.getCategory().equalsIgnoreCase(category.trim()))
                .filter(b -> search == null || search.isBlank() ||
                        b.getTitle().toLowerCase().contains(search.toLowerCase().trim()) ||
                        b.getAuthor().toLowerCase().contains(search.toLowerCase().trim()) ||
                        (b.getDescription() != null && b.getDescription().toLowerCase().contains(search.toLowerCase().trim())))
                .sorted(Comparator.comparing(Book::getId))
                .collect(Collectors.toList());
    }

    public Optional<Book> getBookById(String id) {
        if (isMongoLive()) {
            try {
                Optional<Book> opt = bookRepository.findById(id);
                if (opt.isPresent()) return opt;
            } catch (Exception ignored) {
                mongoAvailable = false;
            }
        }
        return Optional.ofNullable(store.get(id));
    }

    public Book saveBook(Book book) {
        if (book.getId() == null || book.getId().isBlank()) {
            book.setId("b" + (System.currentTimeMillis() % 100000));
        }
        if (book.getCreatedAt() == null) {
            book.setCreatedAt(LocalDateTime.now());
        }
        book.setUpdatedAt(LocalDateTime.now());

        if (isMongoLive()) {
            try {
                return bookRepository.save(book);
            } catch (Exception ignored) {
                mongoAvailable = false;
            }
        }

        store.put(book.getId(), book);
        return book;
    }
}
