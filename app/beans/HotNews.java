package beans;

import java.util.List;

/**
 * Created by zhangshl on 16/9/5.
 */
public class HotNews {
    private List<String> baiduHotWord;
    private String news_url;
    private String title;

    public List<String> getBaiduHotWord() {
        return baiduHotWord;
    }

    public void setBaiduHotWord(List<String> baiduHotWord) {
        this.baiduHotWord = baiduHotWord;
    }

    public String getNews_url() {
        return news_url;
    }

    public void setNews_url(String news_url) {
        this.news_url = news_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
