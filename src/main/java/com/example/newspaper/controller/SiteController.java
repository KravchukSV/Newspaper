package com.example.newspaper.controller;

import com.example.newspaper.entity.Article;
import com.example.newspaper.entity.Site;
import com.example.newspaper.repository.SiteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Tag(name = "Site Controller", description = "CRUD operations with Site")
@RestController
@RequestMapping("/sites")
public class SiteController {
    private final SiteRepository siteRepository;

    public SiteController(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    @Operation(summary = "Get by all site", description = "Returns a list of all sites from the database")
    @ApiResponse(responseCode = "200", description = "All ok")
    @GetMapping
    Collection<Site> findAll(){
        Collection<Site> sites =  siteRepository.findAll();
        return sites;
    }

    @Operation(summary = "Get by ID", description = "Returns the site by the specified id " +
            "or returns the status code 404(not found) if the site by the specified id is not found")
    @ApiResponse(responseCode = "200", description = "All ok")
    @ApiResponse(responseCode = "404", description = "Not found(According to the site ID not found)",
            content = @Content)
    @GetMapping("/{id}")
    ResponseEntity<Site> findById(@PathVariable Integer id){
        Optional<Site> siteOptional =  siteRepository.findById(id);
        if(siteOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(siteOptional.get());
    }

    @Operation(summary = "Delete by ID", description = "Deletes the site by the specified id " +
            "or returns the status code 404(not found) if the site by the specified id is not found")
    @ApiResponse(responseCode = "204", description = "No content(the site's deletion was successful)")
    @ApiResponse(responseCode = "404", description = "Not found(According to the site ID not found)",
            content = @Content)
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable Integer id){
        if(!siteRepository.existsById(id)){
            ResponseEntity.notFound();
        }
        siteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update by ID", description = "Updates the site by the specified id " +
            "or returns the status code 404(not found) if the site by the specified id is not found")
    @ApiResponse(responseCode = "200", description = "All ok")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "404", description = "Not found(According to the site ID not found)",
            content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Site site){
        if(id == null || !id.equals(site.getId()))
            ResponseEntity.badRequest();
        if(!siteRepository.existsById(id))
           ResponseEntity.notFound();
        siteRepository.save(site);
        return ResponseEntity.ok().body(site);
    }

    @Operation(summary = "Create", description = "Create new site")
    @ApiResponse(responseCode = "201", description = "Created(creation was successful)")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @PostMapping
    public ResponseEntity<Site> create(@RequestBody Site site){
        if(site == null || site.getId() != null){
            ResponseEntity.badRequest();
        }
        Site newSite = siteRepository.save(site);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newSite.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newSite);
    }

    @Operation(summary = "Returns all articles", description = "Returns all articles of the specified site " +
            "or returns the status code 404(not found) if the site by the specified id is not found")
    @ApiResponse(responseCode = "200", description = "All ok")
    @ApiResponse(responseCode = "404", description = "Not found(According to the site ID not found)",
            content = @Content)
    @GetMapping("/{id}/articles")
    ResponseEntity<Collection<Article>> allArticlesSite(@PathVariable Integer id){
        Optional<Site> siteOptional =  siteRepository.findById(id);
        if(siteOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Collection<Article> articlesSite = siteOptional.get().getArticles();
        return ResponseEntity.ok(articlesSite);
    }

    @Operation(summary = "Updating the list of articles", description = "Updates the list of site articles by the specified id " +
            "or returns the status code 404(not found) if the site by the specified id is not found")
    @ApiResponse(responseCode = "200", description = "All ok")
    @ApiResponse(responseCode = "404", description = "Not found(According to the site ID not found)",
            content = @Content)
    @Transactional
    @PutMapping("/{id}/update-articles")
    public ResponseEntity<?> updateListArticle(@PathVariable Integer id, @RequestBody Set<Article> articles){
        Optional<Site> site = siteRepository.findById(id);
        if(site.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        site.get().setArticles(articles);
        siteRepository.save(site.get());

        return ResponseEntity.ok().body(site);
    }
}
