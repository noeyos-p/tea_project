// AuthController.java
package com.project.tea.controller;

import com.project.tea.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/login")  public String loginPage()  { return "tea/login"; }   // ★
    @GetMapping("/signup") public String signupForm() { return "tea/signup"; } // ★

    @PostMapping("/signup")
    public String signup(@RequestParam String email,
                         @RequestParam String password,
                         @RequestParam String confirmPassword,
                         @RequestParam String nickname,
                         RedirectAttributes ra) {
        try {
            userService.signup(email, password, confirmPassword, nickname);
            ra.addFlashAttribute("msg", "회원가입이 완료되었습니다. 로그인 해주세요!");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/signup";
        }
    }


}
