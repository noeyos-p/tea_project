package com.project.tea.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class ChoiceController {

    @GetMapping("/choice")
    public String  choice(){
        return "/tea/mood-state";
    }


}