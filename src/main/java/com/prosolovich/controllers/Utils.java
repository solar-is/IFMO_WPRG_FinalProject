package com.prosolovich.controllers;

import com.prosolovich.ConnectionSource;
import com.prosolovich.domain.Film;
import com.prosolovich.domain.Role;
import com.prosolovich.domain.User;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Utils {

    static ConnectionSource connectionSource = ConnectionSource.instance();

    static PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return connectionSource.createConnection().prepareStatement(sql);
    }

    static boolean isImdbId(String s) {
        return (s.charAt(0) >= '0' && s.charAt(0) <= '9');
    }


    public static Film getFilmById(int imdbId) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(imdbId));
        if (stringBuilder.length() != 7) {
            while (stringBuilder.length() != 7)
                stringBuilder.insert(0, "0");
        }
        String string = stringBuilder.toString();
        String query = "http://www.omdbapi.com/?apikey=af25d893&i=tt" + string;
        JSONObject response = getResponseJsonObject(query);
        Film cur = new Film();
        fillFilmFromJson(cur, response);
        return cur;
    }

    public static void fillFilmFromJson(Film cur, JSONObject obj) {
        cur.setTitle(obj.getString("Title"));
        cur.setYear(obj.getString("Year"));
        cur.setRuntime(obj.getString("Runtime"));
        cur.setGenres(obj.getString("Genre"));
        cur.setDirector(obj.getString("Director"));
        cur.setActors(obj.getString("Actors"));
        cur.setPlotAndAwards(obj.getString("Plot") + " " + obj.getString("Awards"));
        cur.setCountry(obj.getString("Country"));
        cur.setPoster(obj.getString("Poster"));
        cur.setImdbRating(obj.getDouble("imdbRating"));
    }

    private static int sizeOfMovieTable() throws SQLException {
        ResultSet resultSet = connectionSource.createConnection().createStatement().executeQuery("select max(id) from main.movie");
        resultSet.next();
        return resultSet.getInt(1);
    }

    public static int getGoodIdForGenreTable() throws SQLException {
        ResultSet resultSet = connectionSource.createConnection().createStatement()
                .executeQuery("select max(id) from main.genre");
        resultSet.next();
        return resultSet.getInt(1) + 1;
    }

    public static void fillUserFromDb(User cur, String userLogin, String userPassword) throws SQLException {
        PreparedStatement userByLoginAndPassword = getPreparedStatement("select * from main.users where login=?");
        userByLoginAndPassword.setString(1, userLogin);
        ResultSet userResultSet = userByLoginAndPassword.executeQuery();
        userResultSet.next();
        cur.setLogin(userResultSet.getString("login"));
        cur.setPassword(userPassword);
        cur.setRole(Role.valueOf(userResultSet.getString("role")));
        int idOfGenreMap = userResultSet.getInt("genrepref");
        PreparedStatement genreById = getPreparedStatement("select * from main.genre where id=?");
        genreById.setInt(1, idOfGenreMap);
        ResultSet genreResultSet = genreById.executeQuery();
        genreResultSet.next();
        int[] genrePrefs = new int[28];
        for (int i = 0; i < 28; ++i) {
            genrePrefs[i] = genreResultSet.getInt(i + 2);
        }
        cur.setGenrePrefs(genrePrefs);
    }


    public static JSONObject getResponseJsonObject(String query) throws IOException {
        URL url = new URL(query);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String inputLine;
        String response = "";
        while ((inputLine = in.readLine()) != null) {
            response = inputLine;
        }
        in.close();
        return new JSONObject(response);
    }

    private static boolean filmAlreadyInDb(int imdbID) throws SQLException {
        PreparedStatement movieByImdbId = getPreparedStatement("select * from main.movie where imdbid=?");
        movieByImdbId.setInt(1, imdbID);
        return movieByImdbId.executeQuery().next();
    }


    public static void addFilmToDbIfNeeded(Film cur, int imdbID) throws SQLException {
        if (filmAlreadyInDb(imdbID)) {
            return;
        }
        String genres = cur.getGenres();

        ArrayList<String> genreArray = new ArrayList<>();
        StringTokenizer stringTokenizer = new StringTokenizer(genres, ",");
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();
            token = token.trim();
            genreArray.add(token);
        }

        HashMap<String, String> mapToNormalName = new HashMap<>();
        mapToNormalName.put("Film Noir", "noir");
        mapToNormalName.put("Reality-TV", "reality");
        mapToNormalName.put("Sci-Fi", "scifi");
        mapToNormalName.put("Talk-Show", "talkshow");
        mapToNormalName.put("Game-Show", "gameshow");

        for (int i = 0; i < genreArray.size(); ++i) {
            if (mapToNormalName.containsKey(genreArray.get(i)))
                genreArray.set(i, mapToNormalName.get(genreArray.get(i)));
            genreArray.set(i, genreArray.get(i).toLowerCase());
        }

        HashMap<String, Integer> mapping = new HashMap<>();
        for (String a : genreArray) {
            mapping.put(a, 8);
        }

        PreparedStatement insertGenre = getPreparedStatement(
                "insert into main.genre values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
        int genreIdx = getGoodIdForGenreTable();
        insertGenre.setInt(1, genreIdx);
        insertGenre.setInt(2, mapping.getOrDefault("action", 3));
        insertGenre.setInt(3, mapping.getOrDefault("adult", 3));
        insertGenre.setInt(4, mapping.getOrDefault("adventure", 3));
        insertGenre.setInt(5, mapping.getOrDefault("animation", 3));
        insertGenre.setInt(6, mapping.getOrDefault("biography", 3));
        insertGenre.setInt(7, mapping.getOrDefault("comedy", 3));
        insertGenre.setInt(8, mapping.getOrDefault("crime", 3));
        insertGenre.setInt(9, mapping.getOrDefault("documentary", 3));
        insertGenre.setInt(10, mapping.getOrDefault("drama", 3));
        insertGenre.setInt(11, mapping.getOrDefault("family", 3));
        insertGenre.setInt(12, mapping.getOrDefault("fantasy", 3));
        insertGenre.setInt(13, mapping.getOrDefault("noir", 3));
        insertGenre.setInt(14, mapping.getOrDefault("gameshow", 3));
        insertGenre.setInt(15, mapping.getOrDefault("history", 3));
        insertGenre.setInt(16, mapping.getOrDefault("horror", 3));
        insertGenre.setInt(17, mapping.getOrDefault("musical", 3));
        insertGenre.setInt(18, mapping.getOrDefault("music", 3));
        insertGenre.setInt(19, mapping.getOrDefault("mystery", 3));
        insertGenre.setInt(20, mapping.getOrDefault("news", 3));
        insertGenre.setInt(21, mapping.getOrDefault("reality", 3));
        insertGenre.setInt(22, mapping.getOrDefault("romance", 3));
        insertGenre.setInt(23, mapping.getOrDefault("scifi", 3));
        insertGenre.setInt(24, mapping.getOrDefault("short", 3));
        insertGenre.setInt(25, mapping.getOrDefault("sport", 3));
        insertGenre.setInt(26, mapping.getOrDefault("talkshow", 3));
        insertGenre.setInt(27, mapping.getOrDefault("thriller", 3));
        insertGenre.setInt(28, mapping.getOrDefault("war", 3));
        insertGenre.setInt(29, mapping.getOrDefault("western", 3));
        insertGenre.executeUpdate();

        PreparedStatement insertMovie = getPreparedStatement("insert into main.movie values (?,?,?,?,?)");
        insertMovie.setInt(1, sizeOfMovieTable() + 1);
        insertMovie.setString(2, cur.getTitle());
        insertMovie.setInt(3, imdbID);
        insertMovie.setInt(4, genreIdx);
        insertMovie.setDouble(5, cur.getImdbRating());
        insertMovie.executeUpdate();
    }

    private static final int iterations = 20*1000;
    private static final int saltLen = 32;
    private static final int desiredKeyLen = 256;

    public static String getSaltedHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
        return Base64.encodeBase64String(salt) + "$" + hash(password, salt);
    }

    public static boolean check(String password, String stored) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String[] saltAndHash = stored.split("\\$");
        if (saltAndHash.length != 2) {
            throw new IllegalStateException(
                    "The stored password must have the form 'salt$hash'");
        }
        String hashOfInput = hash(password, Base64.decodeBase64(saltAndHash[0]));
        return hashOfInput.equals(saltAndHash[1]);
    }

    private static String hash(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (password == null || password.length() == 0)
            throw new IllegalArgumentException("Empty passwords are not supported.");
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(new PBEKeySpec(
                password.toCharArray(), salt, iterations, desiredKeyLen));
        return Base64.encodeBase64String(key.getEncoded());
    }
}
