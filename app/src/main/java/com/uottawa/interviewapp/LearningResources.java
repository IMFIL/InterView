package com.uottawa.interviewapp;

import java.io.Serializable;

/**
 * Created by filipslatinac on 2017-07-17.
 */

public class LearningResources implements Serializable {
    private String url;
    private String image;
    private String title;
    private String description;
    private String [] author = null;

    public LearningResources(String url, String title, String image, String description){
        this.description = description;
        this.image = image;
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String [] getAuthor(){
        if (author == null){
            return null;
        }

        else{
            return author;
        }
    }

    public void setAuthor(String [] authors){
        author = authors;
    }

}
