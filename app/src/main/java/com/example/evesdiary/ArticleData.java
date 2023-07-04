package com.example.evesdiary;

public class ArticleData {
    private String title;
    private String content;
    private int image_id;

    public ArticleData(String title, String content, int image_id){
        this.title = title;
        this.content = content;
        this.image_id = image_id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }
}
