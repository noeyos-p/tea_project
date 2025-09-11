// src/main/java/com/project/tea/controller/QnaController.java
package com.project.tea.controller;

import com.project.tea.dto.QnaDto;
import com.project.tea.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tea/mypage/qna") // ✅ 원본 유지
public class QnaController {

    private final QnaService qnaService;

    /** 목록: 데이터 없으면 main-none, 있으면 main-list (원본 유지) */
    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {
        Page<QnaDto> qnaPage = qnaService.list(page, size);
        model.addAttribute("page", qnaPage);
        return (qnaPage.getTotalElements() == 0)
                ? "tea/mypage/qna/main-none"
                : "tea/mypage/qna/main-list";
    }

    /** 상세 */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("qna", qnaService.get(id));
        return "tea/mypage/qna/post";
    }

    /** 작성 폼  */
    @GetMapping("/new")
    public String form() {
        return "tea/mypage/qna/new-question";
    }

    /** 작성 처리 (원본 유지) */
    @PostMapping
    public String create(@RequestParam String title,
                         @RequestParam String post) {
        qnaService.create(title, post);
        return "redirect:/tea/mypage/qna" ;
    }

}
