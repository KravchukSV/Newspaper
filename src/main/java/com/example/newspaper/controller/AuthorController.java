package com.example.newspaper.controller;

import com.example.newspaper.entity.Author;
import com.example.newspaper.repository.AuthorRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

@Tag(name = "Author Controller", description = "CRUD operations with Author")
@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorRepository authorRepository;

    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Operation(summary = "Get by all authors", description = "Returns a list of all authors from the database")
    @ApiResponse(responseCode = "200", description = "All ok")
    @GetMapping
    Collection<Author> findAll(){
        Collection<Author> authors =  authorRepository.findAll();
        return authors;
    }

    @Operation(summary = "Get by ID", description = "Returns the author by the specified id " +
            "or returns the status code 404(not found) if the author by the specified id is not found")
    @ApiResponse(responseCode = "200", description = "All ok")
    @ApiResponse(responseCode = "404", description = "Not found(According to the author ID not found)",
            content = @Content)
    @GetMapping("/{id}")
    ResponseEntity<Author> findById(@PathVariable Integer id){
        Optional<Author> authorOptional =  authorRepository.findById(id);
        if(authorOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(authorOptional.get());
    }

    @Operation(summary = "Delete by ID", description = "Deletes the author by the specified id " +
            "or returns the status code 404(not found) if the author by the specified id is not found")
    @ApiResponse(responseCode = "204", description = "No content(the author's deletion was successful)")
    @ApiResponse(responseCode = "404", description = "Not found(According to the author ID not found)",
            content = @Content)
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable Integer id){
        if(!authorRepository.existsById(id)){
            ResponseEntity.notFound();
        }
        authorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update by ID", description = "Updates the author by the specified id " +
            "or returns the status code 404(not found) if the author by the specified id is not found")
    @ApiResponse(responseCode = "200", description = "All ok")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "404", description = "Not found(According to the author ID not found)",
            content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Author author){
        if(id == null || !id.equals(author.getId()))
            ResponseEntity.badRequest();
        if(!authorRepository.existsById(id))
           ResponseEntity.notFound();
        authorRepository.save(author);
        return ResponseEntity.ok().body(author);
    }

    @Operation(summary = "Create", description = "Create new author")
    @ApiResponse(responseCode = "201", description = "Created(creation was successful)")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @PostMapping
    public ResponseEntity<Author> create(@RequestBody Author author){
        if(author == null || author.getId() != null){
            ResponseEntity.badRequest();
        }
        Author newAuthor = authorRepository.save(author);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newAuthor.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newAuthor);
    }
}
