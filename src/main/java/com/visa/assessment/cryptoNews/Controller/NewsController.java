package com.visa.assessment.cryptoNews.Controller;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.visa.assessment.cryptoNews.Model.Article;
import com.visa.assessment.cryptoNews.Model.ArticleList;
import com.visa.assessment.cryptoNews.Service.NewsService;

@Controller
public class NewsController {
    
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    private List<Article> currentList;

    @Autowired
    NewsService service;

    @GetMapping(path="/")
    public String displayNews(Model model){

        currentList = service.getArticles();

        logger.info( "Inside controller check List size >>>> " + Integer.toString(currentList.size()) );
        ArticleList articleList = new ArticleList();
        articleList.setArticles(currentList);
        model.addAttribute("Articles", articleList);
        return "display2";
    }

    @RequestMapping("/save")
    public String save(@ModelAttribute Article article, @ModelAttribute ArticleList articleList, Model model){
        
        List<Article> ListOfSave = new LinkedList<>();      // list of indexes to save

        for(String s:articleList.getSaveStatus())
        {   logger.info("check save status >>>>> " + s);                     
            ListOfSave.add(currentList.get(Integer.parseInt(s)));
        }
        service.saveArticles(ListOfSave);
                
        currentList = service.getArticles();     // get articles again   

        //ArticleList articleList = new ArticleList();
        articleList.setArticles(currentList);

        model.addAttribute("Articles", articleList);
        return "display2";
    }
}
