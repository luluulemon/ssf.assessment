package com.visa.assessment.cryptoNews.Controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RestController;

import com.visa.assessment.cryptoNews.Model.Article;
import com.visa.assessment.cryptoNews.Service.NewsService;

import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
public class NewsRESTController {
    private static final Logger logger = LoggerFactory.getLogger(NewsRESTController.class);

    @Autowired
    NewsService service;

    @GetMapping(path="/news/{id}", produces="application/json")
    public ResponseEntity<String> GetArticle(@PathVariable String id){
        if(!service.hasKey(id))
        {   
            JsonObject errorJson = Json.createObjectBuilder()
            .add("error", "Cannot find news article " + id).build();
            return ResponseEntity.status(404).body(errorJson.toString());
            
        }
        logger.info("Inside REST >>>>>> check id" + id);
        Article article = service.loadArticle(id);
        
        JsonObject body = Json.createObjectBuilder().add("id", article.getId())
                            .add("title", article.getTitle())
                            .add("body", article.getBody())
                            .add("published_on", article.getPublished_on())
                            .add("url", article.getUrl())
                            .add("imageurl", article.getImageurl())
                            .add("tags", article.getTags())
                            .add("categories", article.getCategories()).build();

        return ResponseEntity.ok(body.toString());
    }


}
