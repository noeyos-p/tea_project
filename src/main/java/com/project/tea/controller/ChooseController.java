package com.project.tea.controller;

import com.project.tea.service.ChooseService;
import com.project.tea.service.UserDataService;
import com.project.tea.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChooseController {

    private final ChooseService chooseService;
    private final UserService userService;
    private final UserDataService userDataService;

    @GetMapping("/")
    public String root() {
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String main(@RequestParam(defaultValue = "10") int limit, Model model) {
        model.addAttribute("topTeas", chooseService.findTopChoices(limit));
        model.addAttribute("limit", limit);
        return "tea/main";
    }

    @PostMapping("/choose")
    public String choose(@RequestParam("teaId") Long teaId, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        // 집계 +1
        chooseService.increment(teaId);

        //  오늘 기록 생성/조회 후 그 ID로 이동
        Long userId = userService.getCurrentUserId();
        // moodId/stateId가 있으면 같이 넘기면 더 정확 (없으면 null로)
        var result = userDataService.saveOrGetToday(userId, teaId, null, null);

        return "redirect:/userdata/memo/" + result.getId();
    }

}
