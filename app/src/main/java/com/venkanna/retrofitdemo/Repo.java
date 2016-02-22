package com.venkanna.retrofitdemo;

/**
 * Created by pcs-30 on 22/2/16.
 */
public class Repo {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "repo name :"+name;
    }
}
