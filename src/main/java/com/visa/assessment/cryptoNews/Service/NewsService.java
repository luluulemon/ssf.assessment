package com.visa.assessment.cryptoNews.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.visa.assessment.cryptoNews.Model.Article;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class NewsService {
    
    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);

    private String API_KEY = System.getenv("CRYPTO_COMPARE_API_KEY");

    @Autowired
    RedisTemplate<String, Object> template;

    public List<Article> getArticles(){
        String endPoint = "https://min-api.cryptocompare.com/data/v2/news/";
        /* categories allowed values: 1INCH, AAVE, ADA, ALGO, ALTCOIN, AR, ASIA, ATOM, AVAX,
           AXS, BAL, BAT, BCH, BLOCKCHAIN, BTC, BUSINESS, COMMODITY, COMP, CRV, DASH, DOGE, DOT,
           ENJ, ETC, ETH, EXCHANGE, FIAT, FIL, FTM, FX, ICO, KNC, LINK, LRC, LTC, LUNA, LUNC, MANA,
           MARKET, MATIC, MINING, MKR, MV, NU, OP, REGULATION, REN, SAND, SHIB, SNX, SOL, SPONSORED,
           SUSHI, TECHNOLOGY, TRADING, TRX, UMA, UNI, USDT, WALLET, XLM, XMR, XRP, XTZ, YFI, ZEC, ZRX

           **To change categories & excludeCategories as required**
        */
   
        String categories = "ETH,BTC";
        String excludeCategories = "1INCH";
        String fullUrl = UriComponentsBuilder.fromUriString(endPoint)
                        .queryParam("categories", categories)
                        .queryParam("excludeCategories", excludeCategories ).toUriString();
        
        RequestEntity req = RequestEntity.get(fullUrl)
                        .header("authorization", "Apikey " + API_KEY )
                        .build();

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);

        List<Article> articlesList = new LinkedList<>();
        try (InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())) 
        {
                JsonReader reader = Json.createReader(is);
                JsonObject data = reader.readObject();
                data.getJsonArray("Data").stream()      // put each JSON obj into article obj
                    .map(v -> (JsonObject) v)
                    .forEach((JsonObject v) -> 
                    {   Article article = new Article();
                        article.setId(v.getString("id"));
                        article.setTitle(v.getString("title"));
                        article.setUrl(v.getString("url"));
                        article.setImageurl(v.getString("imageurl"));
                        article.setPublished_on(v.getInt("published_on"));
                        article.setBody(v.getString("body"));
                        article.setTags(v.getString("tags"));
                        article.setCategories(v.getString("categories"));
                        articlesList.add(article);                 
                    }); 
            return articlesList;
        }
        catch(IOException e)
        {    logger.info(e.getMessage());      }


        return articlesList;

    }


    public void saveArticles(List<Article> articles){

        // iterate through article List and save each of them to redis
        for(Article article: articles){
            template.opsForValue().set(article.getId(), article);
            logger.info(article.getId());
        }
    }

    public Article loadArticle(String id){
        return (Article) template.opsForValue().get(id);
    }

    public boolean hasKey(String id){
        return template.hasKey(id);
    }


}







