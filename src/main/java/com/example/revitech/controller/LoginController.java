package com.example.revitech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.revitech.form.SignupForm;
import com.example.revitech.service.UserService;

import jakarta.validation.Valid;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/signup")
    public String showSignup(Model model) {
        model.addAttribute("signupForm", new SignupForm());
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute("signupForm") @Valid SignupForm form,
                                BindingResult bindingResult,
                                Model model) {

        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", null, "パスワードが一致しません");
        }
        if (userService.isUsernameTaken(form.getUsername())) {
            bindingResult.rejectValue("username", null, "このユーザー名はすでに使われています");
        }
        if (userService.isEmailTaken(form.getEmail())) {
            bindingResult.rejectValue("email", null, "このメールはすでに使われています");
        }
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        userService.registerUser(form);
        return "redirect:/login";
    }
}
