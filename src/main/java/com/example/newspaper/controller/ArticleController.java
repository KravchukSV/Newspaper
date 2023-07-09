package com.example.newspaper.controller;

import com.example.newspaper.entity.Article;
import com.example.newspaper.repository.ArticleRepository;
import com.example.newspaper.server.ArticleService;
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

@Tag(name = "Article Controller", description = "CRUD operations with Article")
@RestController
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;
    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository, ArticleService articleService) {
        this.articleRepository = articleRepository;
        this.articleService = articleService;
    }

    @Operation(summary = "Get by all article", description = "Returns a list of all articles from the database")
    @ApiResponse(responseCode = "200", description = "All ok")
    @GetMapping
    Collection<Article> findAll(){
        Collection<Article> articles =  articleRepository.findAll();
        return articles;
    }

    @Operation(summary = "Get by ID", description = "returns the article by the specified id " +
                "or returns the status code 404(not found) if the article by the specified id is not found")
    @ApiResponse(responseCode = "200", description = "All ok")
    @ApiResponse(responseCode = "404", description = "Not found(According to the article ID not found)",
            content = @Content)
    @GetMapping("/{id}")
    ResponseEntity<Article> findById(@PathVariable Integer id){
        Optional<Article> articleOptional =  articleRepository.findById(id);
        if(articleOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(articleOptional.get());
    }

    @Operation(summary = "Delete by ID", description = "Deletes the article by the specified id " +
            "or returns the status code 404(not found) if the article by the specified id is not found")
    @ApiResponse(responseCode = "204", description = "No content(the article's deletion was successful)")
    @ApiResponse(responseCode = "404", description = "Not found(According to the article ID not found)",
            content = @Content)
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable Integer id){
        if(!articleRepository.existsById(id)){
            ResponseEntity.notFound();
        }
        articleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update by ID", description = "Updates the article by the specified id " +
            "or returns the status code 404(not found) if the article by the specified id is not found")
    @ApiResponse(responseCode = "200", description = "All ok")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "404", description = "Not found(According to the article ID not found)",
            content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Article article){
        if(id == null || !id.equals(article.getId()))
            ResponseEntity.badRequest();
        if(!articleRepository.existsById(id))
           ResponseEntity.notFound();
        articleRepository.save(article);
        return ResponseEntity.ok().body(article);
    }

    @Operation(summary = "Create", description = "Create new article")
    @ApiResponse(responseCode = "201", description = "Created(creation was successful)")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @PostMapping
    public ResponseEntity<Article> create(@RequestBody Article article){
        if(article == null || article.getId() != null){
            ResponseEntity.badRequest();
        }
        Article newArticle = articleRepository.save(article);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newArticle.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newArticle);
    }

    @Operation(summary = "Search for duplicates", description = "Returns a list of articles that duplicate this article")
    @ApiResponse(responseCode = "200", description = "All ok")
    @GetMapping("{id}/duplicates")
    public ResponseEntity<Collection<Article>> findDuplicateArticles(@PathVariable Integer id) {
        Article article = articleRepository.findById(id).get();
        Collection<Article> duplicateArticles = articleService.listDuplicates(article);
        return ResponseEntity.ok(duplicateArticles);
    }
}
