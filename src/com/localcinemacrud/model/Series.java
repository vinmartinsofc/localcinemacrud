package com.localcinemacrud.model;

public class Series extends Title {
    private String creator;
    private boolean isFinished;

    public Series(Title title, String creator, boolean isFinished) {
        super();
        this.creator = creator;
        this.isFinished = isFinished;
    }

}


