package com.project.tea.controller;

import com.project.tea.dto.ChooseDto;
import com.project.tea.service.ChooseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChooseController {

    private final ChooseService chooseService;

    /**
     * 메인 페이지
     * - 집계(count) 내림차순 TOP N을 모델로 내려서 main.html에서 렌더
     * - 사진은 템플릿에서 <img src="/img/background.jpg"> 로 고정 사용
     */
    @GetMapping("/main")
    public String main(@RequestParam(defaultValue = "10") int limit, Model model) {
        List<ChooseDto> topTeas = chooseService.findTopChoices(limit);
        model.addAttribute("topTeas", topTeas);
        model.addAttribute("limit", limit); // 필요시 템플릿에서 사용
        return "tea/main";
    }

//    /**
//     * 선택: 루트로 들어오면 /main 으로 보내기 원할 때 사용
//     */
//    @GetMapping("/")
//    public String redirectRoot() {
//        return "redirect:/main";
//    }
}
