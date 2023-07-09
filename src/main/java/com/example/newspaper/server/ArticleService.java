package com.example.newspaper.server;

import com.example.newspaper.entity.Article;
import com.example.newspaper.entity.Author;
import com.example.newspaper.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;


@Service
public class ArticleService {
    private final int ASSESSMENT_DUPLICATION_TITLE = 20;
    private final int ASSESSMENT_DUPLICATION_ISSN_NUMBER = 10;
    private final int ASSESSMENT_DUPLICATION_ISBN_NUMBER = 10;
    private final int ASSESSMENT_DUPLICATION_AUTHOR = 15;
    private final int ASSESSMENT_DUPLICATION_YEAR_PUBLICATION = 5;
    private final int ASSESSMENT_DUPLICATION_EDITION_NUMBER = 15;
    private final int MIN_DUPLICATION_SCORE = 50;
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Collection<Article> listDuplicates(Article article){
        Collection<Article> allArticles = articleRepository.findAll();
        Collection<Article> duplicatesArticle = new ArrayList<>();

        for(Article checkArticle : allArticles){
            boolean check = checkDuplicate(article, checkArticle);
            if(check){
                duplicatesArticle.add(checkArticle);
            }
        }

        return duplicatesArticle;
    }

    private boolean checkDuplicate(Article originalArticle, Article checkArticle){
        int matchingScore = 0;

        if(originalArticle.getTitleEnglish().equals(checkArticle.getTitleEnglish())){
            matchingScore += ASSESSMENT_DUPLICATION_TITLE;
        }
        else if(originalArticle.getTitleEnglish().equals(checkArticle.getTitleEnglish())){
            matchingScore += ASSESSMENT_DUPLICATION_TITLE;
        }

        if(originalArticle.getIssnNumber().equals(checkArticle.getIssnNumber())){
            matchingScore += ASSESSMENT_DUPLICATION_ISSN_NUMBER;
        }


        if(originalArticle.getIsbnNumber().equals(checkArticle.getIsbnNumber())){
            matchingScore += ASSESSMENT_DUPLICATION_ISBN_NUMBER;
        }


        if(checkAuthor(originalArticle.getAuthors(), checkArticle.getAuthors())){
            matchingScore += ASSESSMENT_DUPLICATION_AUTHOR;
        }


        if(originalArticle.getYearPublication().equals(checkArticle.getYearPublication())){
            matchingScore += ASSESSMENT_DUPLICATION_YEAR_PUBLICATION;
        }


        if(originalArticle.getEditionNumber().equals(checkArticle.getEditionNumber())){
            matchingScore += ASSESSMENT_DUPLICATION_EDITION_NUMBER;
        }


        return matchingScore >= MIN_DUPLICATION_SCORE;
    }

    private boolean checkAuthor(Collection<Author> originalAuthors, Collection<Author> checkAuthors){

        for(Author originalAuthor : originalAuthors){
            for(Author checkAuthor : checkAuthors){
                if(originalAuthor.equals(checkAuthor)){
                    return true;
                }
            }
        }

        return false;
    }
}
