package com.visa.assessment.cryptoNews.Model;

import java.util.List;

public class ArticleList {
    private List<Article> articles;
    private String saveStatus;
    private boolean save;
    
    public List<Article> getArticles() {
        return articles;
    }
    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
    public String getSaveStatus() {
        return saveStatus;
    }
    public void setSaveStatus(String saveStatus) {
        this.saveStatus = saveStatus;
    }
    public boolean isSave() {
        return save;
    }
    public void setSave(boolean save) {
        this.save = save;
    }

}
