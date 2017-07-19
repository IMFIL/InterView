package com.uottawa.interviewapp;

import java.io.Serializable;

/**
 * Created by filipslatinac on 2017-06-24.
 */

public class QuestionPost implements Serializable {
    private String answerUrl;
    private String question;


    public QuestionPost(String questionAsked, String ansUrl){
        answerUrl = ansUrl;
        question = questionAsked;
    }

    public String getAnswerUrl() {
        return answerUrl;
    }

    public String getQuestion() {
        return question;
    }

}
