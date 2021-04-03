package com.example.campus_work1.Model;

public class Comment {

    private String comment;
    private String publisher;

    public Comment(String comment, String publisger) {
        this.comment = comment;
        this.publisher = publisger;
    }

    public Comment() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisger(String publisger) {
        this.publisher = publisger;
    }
}
