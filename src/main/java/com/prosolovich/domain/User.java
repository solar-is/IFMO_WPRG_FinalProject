package com.prosolovich.domain;

public class User {
    String login;
    String password;
    int[] genrePrefs;
    Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int[] getGenrePrefs() {
        return genrePrefs;
    }

    public void setGenrePrefs(int[] genrePrefs) {
        this.genrePrefs = genrePrefs;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
