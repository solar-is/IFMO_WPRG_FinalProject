package com.prosolovich.controllers;

import com.prosolovich.domain.Film;
import com.prosolovich.domain.User;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.prosolovich.controllers.Utils.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Controller
public class MainPageController {


    @RequestMapping("/mainPage")
    public String mainPage(@ModelAttribute("user") User user, Model model, HttpSession httpSession) throws SQLException {
        User cur = new User();
        if (httpSession.getAttribute("userLogin") != null &&
                httpSession.getAttribute("userPassword") != null) {
            fillUserFromDb(cur, (String) httpSession.getAttribute("userLogin"), (String) httpSession.getAttribute("userPassword"));
        } else {
            fillUserFromDb(cur, user.getLogin(), user.getPassword());
        }
        model.addAttribute("user", cur);
        return "mainPage";
    }


    @PostMapping("/editProfile")
    public String editProfile(Model model) {
        model.addAttribute("user", new User());
        return "editProfilePage";
    }

    @PostMapping("/editUserProfile")
    public String editUserProfile(@ModelAttribute("user") User user,
                                  @RequestParam("oldPassword") String oldPassword,
                                  HttpSession httpSession,
                                  Model model) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        if (httpSession.getAttribute("userPassword").equals(oldPassword)) {
            String newLogin = user.getLogin();
            String newPassword = user.getPassword();
            PreparedStatement insertGenre = getPreparedStatement("insert into main.genre values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
            int genreIdx = getGoodIdForGenreTable();
            insertGenre.setInt(1, genreIdx);
            for (int i = 2; i < 30; ++i) {
                insertGenre.setInt(i, user.getGenrePrefs()[i - 2]);
            }
            insertGenre.executeUpdate();

            PreparedStatement updateUser = getPreparedStatement(
                    "update main.users set login=?, password=?, genrepref=? where login=?");
            updateUser.setString(1, newLogin);
            updateUser.setString(2, getSaltedHash(newPassword));
            updateUser.setInt(3, genreIdx);
            updateUser.setString(4, (String) httpSession.getAttribute("userLogin"));
            updateUser.executeUpdate();
            httpSession.setAttribute("userLogin", newLogin);
            httpSession.setAttribute("userPassword", newPassword);
            return "redirect:/mainPage";
        } else {
            user.setGenrePrefs(null);
            model.addAttribute("user", user);
            model.addAttribute("incorrectInput", true);
            return "editProfilePage";
        }
    }

    @PostMapping("/")
    public String exitButton(HttpSession httpSession) {
        httpSession.setAttribute("userLogin", null);
        httpSession.setAttribute("userPassword", null);
        httpSession.setAttribute("userRole", null);
        httpSession.setAttribute("prevRecs", null);
        httpSession.invalidate();
        return "redirect:/";
    }

    @PostMapping("/findFilmByIdOrTitle")
    public String findFilm(@RequestParam("idOrTitle") String string,
                           Model model,
                           RedirectAttributes redirectAttributes) throws IOException, SQLException {
        Film cur = new Film();
        String query;
        if (isImdbId(string)) {
            StringBuilder stringBuilder = new StringBuilder(string);
            if (stringBuilder.length() != 7) {
                while (stringBuilder.length() != 7)
                    stringBuilder.insert(0, "0");
            }
            string = stringBuilder.toString();
            query = "http://www.omdbapi.com/?apikey=af25d893&i=tt" + string;
        } else {
            string = string.replaceAll(" ", "%20");
            query = "http://www.omdbapi.com/?apikey=af25d893&t=" + string;
        }
        JSONObject obj = getResponseJsonObject(query);
        if (obj.getString("Response").equals("True")) {
            fillFilmFromJson(cur, obj);
            model.addAttribute("film", cur);
            addFilmToDbIfNeeded(cur, Integer.parseInt(obj.getString("imdbID").substring(2)));
            return "filmPage";
        } else {
            redirectAttributes.addFlashAttribute("noSuchFilm", true);
            return "redirect:/mainPage";
        }
    }


    @PostMapping("/getRecommendation")
    public String getRecommendation(HttpSession httpSession, Model model) throws SQLException, IOException {
        String login = (String) httpSession.getAttribute("userLogin");
        String password = (String) httpSession.getAttribute("userPassword");
        Set<Integer> prevRecs = (HashSet<Integer>) httpSession.getAttribute("prevRecs");
        if (prevRecs == null)
            prevRecs = new HashSet<>();
        User cur = new User();
        fillUserFromDb(cur, login, password);
        int[] userGenrePrefs = cur.getGenrePrefs();

        ResultSet resultSet = connectionSource.createConnection().createStatement().executeQuery(
                "select * from main.movie inner join main.genre on movie.genresmapping=genre.id " +
                        "order by movie.imdbrating desc limit 18000;"
        );

        Map<Integer, Double> idToDistance = new HashMap<>();
        while (resultSet.next()) {
            if (prevRecs.contains(resultSet.getInt("imdbid"))) {
                continue;
            }
            double distance = 0;
            for (int i = 0; i < 28; ++i) {
                distance += Math.abs(userGenrePrefs[i] - resultSet.getInt(i + 7));
            }
            distance /= resultSet.getDouble("imdbrating");
            idToDistance.put(resultSet.getInt("imdbid"), distance);
        }
        double lowestDistance = 9999;
        int id = -1;
        for (Integer e : idToDistance.keySet()) {
            if (idToDistance.get(e) < lowestDistance) {
                lowestDistance = idToDistance.get(e);
                id = e;
            }
        }
        model.addAttribute("film", getFilmById(id));
        prevRecs.add(id);
        httpSession.setAttribute("prevRecs", prevRecs);
        return "filmPage";
    }

    @PostMapping("/changeRole")
    public String changeRole() {
        return "changeRole";
    }

    @PostMapping("/changeUserRole")
    public String changeUserRole(@RequestParam("userLogin") String login,
                                 @RequestParam("role") String role,
                                 RedirectAttributes redirectAttributes) throws SQLException {
        PreparedStatement userByLogin = getPreparedStatement(
                "select * from main.users where login=?"
        );
        userByLogin.setString(1, login);
        ResultSet resultSet = userByLogin.executeQuery();
        if (resultSet.next()){
            PreparedStatement updateUser = getPreparedStatement(
                    "update main.users set role=? where login=?");
            updateUser.setString(1,role.toUpperCase());
            updateUser.setString(2, login);
            updateUser.executeUpdate();
            redirectAttributes.addFlashAttribute("roleUpdated", true);
        } else {
            redirectAttributes.addFlashAttribute("noSuchUser", true);
        }
        return "redirect:/mainPage";
    }


    @RequestMapping("/resolveReqs")
    public String resolveReqs(Model model,
                              RedirectAttributes redirectAttributes) throws SQLException {
        ResultSet resultSet = connectionSource.createConnection().createStatement().executeQuery("select * from main.request");
        if (resultSet.next()) {
            int reqId = resultSet.getInt("id");
            String userLogin = resultSet.getString("userlogin");
            String title = resultSet.getString("movietitle");
            int genreChangeId = resultSet.getInt("genrechange");
            int[] olds = new int[28];
            int[] news = new int[28];
            PreparedStatement genreById = getPreparedStatement("select * from main.genre where id=?");
            genreById.setInt(1, genreChangeId);
            ResultSet resultSet1 = genreById.executeQuery();
            resultSet1.next();
            for (int i = 0; i<28; ++i){
                news[i] = resultSet1.getInt(i + 2);
            }
            PreparedStatement movieWithGenreByTitle = getPreparedStatement(
                    "select * from main.movie inner join main.genre on movie.genresmapping=genre.id where title=?");
            movieWithGenreByTitle.setString(1, title);
            ResultSet resultSet2 = movieWithGenreByTitle.executeQuery();
            resultSet2.next();
            for (int i = 0; i<28; ++i){
                olds[i] = resultSet2.getInt(i + 7);
            }
            model.addAttribute("userLogin", userLogin);
            model.addAttribute("title", title);
            model.addAttribute("news", news);
            model.addAttribute("olds", olds);
            model.addAttribute("reqId", reqId);
            return "resolveReqs";
        } else {
            redirectAttributes.addFlashAttribute("noRequests", true);
            return "redirect:/mainPage";
        }
    }

    @PostMapping("/acceptReq/{reqId}")
    public String acceptReq(@PathVariable Integer reqId) throws SQLException {
        PreparedStatement reqById = getPreparedStatement("select * from main.request where id=?");
        reqById.setInt(1, reqId);
        ResultSet req = reqById.executeQuery();
        req.next();
        int genreChangeId = req.getInt("genrechange");
        String title = req.getString("movietitle");
        PreparedStatement movieGenreByTitle = getPreparedStatement("select genresmapping from main.movie where title=?");
        movieGenreByTitle.setString(1, title);
        ResultSet movieGenre = movieGenreByTitle.executeQuery();
        movieGenre.next();
        int oldMapping = movieGenre.getInt(1);
        PreparedStatement updateMovie = getPreparedStatement(
                "update main.movie set genresmapping=? where title=?");
        updateMovie.setInt(1, genreChangeId);
        updateMovie.setString(2, title);
        updateMovie.executeUpdate();
        PreparedStatement deleteOldGenre = getPreparedStatement("delete from main.genre where id=?");
        deleteOldGenre.setInt(1, oldMapping);
        deleteOldGenre.executeUpdate();
        PreparedStatement deleteReqById = getPreparedStatement("delete from main.request where id=?");
        deleteReqById.setInt(1, reqId);
        deleteReqById.executeUpdate();
        return "redirect:/resolveReqs";
    }


    @PostMapping("/declineReq/{reqId}")
    public String declineReq(@PathVariable Integer reqId) throws SQLException {
        PreparedStatement reqById = getPreparedStatement("select * from main.request where id=?");
        reqById.setInt(1, reqId);
        ResultSet resultSet = reqById.executeQuery();
        resultSet.next();
        int genreChangeId = resultSet.getInt("genrechange");
        PreparedStatement deleteGenreById = getPreparedStatement("delete from main.genre where id=?");
        deleteGenreById.setInt(1, genreChangeId);
        deleteGenreById.executeUpdate();
        PreparedStatement deleteReqById = getPreparedStatement("delete from main.request where id=?");
        deleteReqById.setInt(1, reqId);
        deleteReqById.executeUpdate();
        return "redirect:/resolveReqs";
    }
}
