package com.prosolovich.controllers;

import com.prosolovich.domain.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.prosolovich.controllers.Utils.*;

@Controller
public class FilmPageController {


    @PostMapping("/sendRequest/{title}")
    public String sendRequest(HttpSession httpSession, Model model, @PathVariable("title") String title) {
        Request request = new Request();
        request.setUserLogin((String) httpSession.getAttribute("userLogin"));
        request.setMovieTitle(title);
        httpSession.setAttribute("userLogin", request.getUserLogin());
        httpSession.setAttribute("movieTitle", request.getMovieTitle());
        model.addAttribute("request", request);
        return "sendRequest";
    }

    @PostMapping("/postRequest")
    public String postRequest(@ModelAttribute("request") Request request, HttpSession httpSession) throws SQLException {
        PreparedStatement insertGenre = getPreparedStatement(
                "insert into main.genre values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
        int genreIdx = getGoodIdForGenreTable();
        insertGenre.setInt(1, genreIdx);
        for (int i = 2; i < 30; ++i) {
            insertGenre.setInt(i, request.getGenrePrefs()[i - 2]);
        }
        insertGenre.executeUpdate();

        request.setUserLogin((String) httpSession.getAttribute("userLogin"));
        request.setMovieTitle((String) httpSession.getAttribute("movieTitle"));

        PreparedStatement insertReq = getPreparedStatement(
                "insert into main.request (userLogin, movieTitle, genrechange) values (?,?,?)");
        insertReq.setString(1, request.getUserLogin());
        insertReq.setString(2, request.getMovieTitle());
        insertReq.setInt(3, genreIdx);
        insertReq.executeUpdate();
        return "redirect:/mainPage";
    }

    @RequestMapping("/deleteFilm/{title}")
    public String deleteFilm(@PathVariable String title,
                             RedirectAttributes redirectAttributes) throws SQLException {
        PreparedStatement deleteMovie = getPreparedStatement(
                "delete from main.movie where title=?");
        deleteMovie.setString(1, title);
        deleteMovie.executeUpdate();
        redirectAttributes.addFlashAttribute("deleted", true);
        redirectAttributes.addFlashAttribute("title", title);
        return "redirect:/mainPage";
    }
}
