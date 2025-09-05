package com.project.tea.controller;

import com.project.tea.dto.TeaDto;
import com.project.tea.service.StateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/state")
public class StateController {

    private final StateService stateService;
    private final ResultService resultService;

    @GetMapping("/select")
    public String showStateSelection() {
        return "state/select";
    }

    @PostMapping("/recommend")
    public String recommendTea(@RequestParam Long stateId, Model model) {
        List<TeaDto> teas = stateService.recommendByState(stateId);
        resultService.saveStateResult("추천 티 결과", stateId);
        model.addAttribute("teas", teas);
        return "state/result";
    }
}
