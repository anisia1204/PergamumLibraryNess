package com.pergamumlibraryness;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
class BookController {

    private final BookRepository repository;

    private final BookModelAssembler assembler;

    BookController(BookRepository repository, BookModelAssembler assembler) {

        this.repository = repository;
        this.assembler = assembler;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/books")
    CollectionModel<EntityModel<Book>> all() {

        List<EntityModel<Book>> Books = repository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());

        return CollectionModel.of(Books, linkTo(methodOn(BookController.class).all()).withSelfRel());
    }
    // end::get-aggregate-root[]

    @PostMapping("/books")
    ResponseEntity<?> newBook(@RequestBody Book newBook) {

        EntityModel<Book> entityModel = assembler.toModel(repository.save(newBook));

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    // Single item

    @GetMapping("/books/{id}")
    EntityModel<Book> one(@PathVariable Long id) {

        Book Book = repository.findById(id) //
                .orElseThrow(() -> new BookNotFoundException(id));

        return assembler.toModel(Book);
    }

    @PutMapping("/books/{id}")
    ResponseEntity<?> replaceBook(@RequestBody Book newBook, @PathVariable Long id) {

        Book updatedBook = repository.findById(id) //
                .map(Book -> {
                    Book.setTitle(newBook.getTitle());
                    Book.setAuthor(newBook.getAuthor());
                    return repository.save(Book);
                }) //
                .orElseGet(() -> {
                    newBook.setId(id);
                    return repository.save(newBook);
                });

        EntityModel<Book> entityModel = assembler.toModel(updatedBook);

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @DeleteMapping("/books/{id}")
    ResponseEntity<?> deleteBook(@PathVariable Long id) {

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}