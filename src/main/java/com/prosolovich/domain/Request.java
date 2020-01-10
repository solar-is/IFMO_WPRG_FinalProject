package com.prosolovich.domain;

public class Request {
    String userLogin;
    String movieTitle;
    int[] genrePrefs;

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public int[] getGenrePrefs() {
        return genrePrefs;
    }

    public void setGenrePrefs(int[] genrePrefs) {
        this.genrePrefs = genrePrefs;
    }
}
