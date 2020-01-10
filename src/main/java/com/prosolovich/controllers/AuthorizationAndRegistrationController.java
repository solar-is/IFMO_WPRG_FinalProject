package com.prosolovich.controllers;

import com.prosolovich.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static com.prosolovich.controllers.Utils.*;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Controller
public class AuthorizationAndRegistrationController {


    @GetMapping("/")
    public String getAuthorizationPage(Model model, HttpSession httpSession) {
        if (httpSession.getAttribute("userLogin") != null &&
                httpSession.getAttribute("userPassword") != null &&
                httpSession.getAttribute("userRole") != null) {
            return "redirect:/mainPage";
        } else {
            model.addAttribute("user", new User());
            return "authorization";
        }
    }


    @PostMapping("/authorize")
    public String authorizationClicked(@ModelAttribute("user") User user, Model model, HttpSession httpSession) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        PreparedStatement preparedStatement = connectionSource
                .createConnection()
                .prepareStatement("select * from main.users where login=?");
        preparedStatement.setString(1, user.getLogin());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            if (check(user.getPassword(), resultSet.getString("password"))) {
                httpSession.setMaxInactiveInterval(604800);
                httpSession.setAttribute("userLogin", user.getLogin());
                httpSession.setAttribute("userPassword", user.getPassword());
                httpSession.setAttribute("userRole", resultSet.getString("role"));
                return "redirect:/mainPage";
            }
        }
        model.addAttribute("error", true);
        return "authorization";
    }


    @PostMapping("/register")
    public String registrationClicked(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registerNewUser")
    public String registrationOfNewUser(@ModelAttribute("user") User user, Model model) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        PreparedStatement userByLogin = getPreparedStatement("select * from main.users where login=?");
        userByLogin.setString(1, user.getLogin());
        ResultSet resultSet = userByLogin.executeQuery();
        if (resultSet.next()) {
            model.addAttribute("equalUser", true);
        } else {
            PreparedStatement insertGenre = getPreparedStatement("insert into main.genre values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
            int genreIdx = getGoodIdForGenreTable();
            insertGenre.setInt(1, genreIdx);
            for (int i = 2; i < 30; ++i) {
                insertGenre.setInt(i, user.getGenrePrefs()[i - 2]);
            }
            insertGenre.executeUpdate();

            PreparedStatement insertUser = getPreparedStatement("insert into main.users (role, login, password, genrepref) values (?,?,?,?);");
            insertUser.setString(1, "USUAL");
            insertUser.setString(2, user.getLogin());
            insertUser.setString(3, getSaltedHash(user.getPassword()));
            insertUser.setInt(4, genreIdx);
            insertUser.executeUpdate();
            model.addAttribute("successRegister", true);
        }
        return "authorization";
    }


}
