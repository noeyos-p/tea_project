package com.project.tea.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class MainController {

    @GetMapping({"/", " main"})
    public String mainPage(){
        return "main";
    }

    // Mood 체크 화면
    @GetMapping("/moodCheck")
    public String moodCheck() {
        return "moodCheck";
    }

    // State 체크 화면
    @GetMapping("/stateCheck")
    public String stateCheck() {
        return "stateCheck";
    }
}