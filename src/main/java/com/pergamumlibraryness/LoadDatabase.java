package com.pergamumlibraryness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(BookRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new Book( "To Kill a Mockingbird", "Harper Lee")));
            log.info("Preloading " + repository.save(new Book( "The Fault in Our Stars", "John Green")));
            log.info("Preloading " + repository.save(new Book( "The Alchemist", "Paulo Coelho")));
            log.info("Preloading " + repository.save(new Book( "The Book Thief", "Markus Zusak")));
            log.info("Preloading " + repository.save(new Book( "The Seven Husbands of Evelyn Hugo", "Taylor Jenkins Reid")));
            log.info("Preloading " + repository.save(new Book( "The Hunger Games", "Suzanne Collins")));
            log.info("Preloading " + repository.save(new Book( "Harry Potter and the Philosopher's Stone", "J.K. Rowling")));
            log.info("Preloading " + repository.save(new Book( "The Da Vinci Code", "Dan Brown ")));
        };
    }
}