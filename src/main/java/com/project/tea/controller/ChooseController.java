package com.project.tea.controller;

import com.project.tea.service.ChooseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Pageable;

@Controller
@RequiredArgsConstructor
public class ChooseController {

    private final ChooseService chooseService;

    /* main */
    @GetMapping("/")
    public String root() {
        return "redirect:/main";
    }

    /** 메인: 집계 상위 N개 노출 (기본 10) */
    @GetMapping("/main")
    public String main(@RequestParam(defaultValue = "10") int limit, Model model) {
        model.addAttribute("topTeas", chooseService.findTopChoices(limit));
        model.addAttribute("limit", limit);
        // 템플릿 경로는 프로젝트 구조에 맞게 조정하세요 (예: "tea/main")
        return "tea/main";
    }

    /** 결과화면에서 "이 차로 할게요" 클릭 시 */
    @PostMapping("/choose")
    public String choose(@RequestParam("teaId") Long teaId, RedirectAttributes ra) {
        chooseService.increment(teaId);
        ra.addFlashAttribute("message", "선택이 반영되었습니다.");
        return "redirect:/main";
    }
}
