DELETE FROM article_author;
DELETE FROM article_site;

DELETE FROM article;
DELETE FROM site;
DELETE FROM author;

INSERT INTO author(id, full_name)
            VALUES(10, 'AUTHOR');
INSERT INTO site(id, weblink)
            VALUES(10, 'SITE');
INSERT INTO article(id, edition_number, isbn_number, issn_number, title_english, title_german, year_publication) VALUES
            (10, 11, 12, 13, 'ARTICLE', 'ARTICLE', 2023),
            (20, 21, 12, 23, 'ARTICLE', 'ARTICLE2', 2022),
            (30, 11, 12, 13, 'ARTICLE_ENG', 'ARTICLE_GER', 2023),
            (40, 11, 12, 13, 'ARTICLE', 'ARTICLE_GER', 2020);

INSERT INTO article_author(article_id, author_id) VALUES(10, 10);
INSERT INTO article_site(article_id, site_id) VALUES(10, 10);