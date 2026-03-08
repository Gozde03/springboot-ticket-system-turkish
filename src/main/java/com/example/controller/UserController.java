package com.example.controller;

import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("pageTitle", "Yeni Kullanıcı Kaydı");
        model.addAttribute("content", "pages/register");
        return "layout/layout";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        // Basic validation
        if (user.getUsername() == null || user.getUsername().isBlank()
                || user.getPassword() == null || user.getPassword().isBlank()) {

            model.addAttribute("error", "Kullanıcı adı ve şifre boş olamaz.");
            model.addAttribute("pageTitle", "Yeni Kullanıcı Kaydı");
            model.addAttribute("content", "pages/register");
            return "layout/layout";
        }

        try {
            userService.registerUser(user);
            return "redirect:/login";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("pageTitle", "Yeni Kullanıcı Kaydı");
            model.addAttribute("content", "pages/register");
            return "layout/layout";
        }
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("pageTitle", "Giriş Yap");
        model.addAttribute("content", "pages/login");
        return "layout/layout";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model) {
        User foundUser = userService.findByUsername(user.getUsername());

        if (foundUser != null && foundUser.getPassword() != null
                && foundUser.getPassword().equals(user.getPassword())) {
            return "redirect:/event";
        }

        model.addAttribute("error", "Geçersiz kullanıcı adı veya şifre");
        model.addAttribute("pageTitle", "Giriş Yap");
        model.addAttribute("content", "pages/login");
        return "layout/layout";
    }
}