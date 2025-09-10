package com.project.tea.controller;

import com.project.tea.entity.UserDataEntity;
import com.project.tea.service.ChooseService;
import com.project.tea.service.UserDataService;
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
    private final UserDataService userDataService;

    // main
    @GetMapping("/")
    public String root() {
        return "redirect:/main";
    }

    //    메인: 집계 상위 N개 노출 (기본 10)
    @GetMapping("/main")
    public String main(@RequestParam(defaultValue = "10") int limit, Model model) {
        model.addAttribute("topTeas", chooseService.findTopChoices(limit));
        model.addAttribute("limit", limit);
        return "tea/main";
    }

    // 차 선택 시
    @PostMapping("/choose")
    public String choose(@RequestParam("teaId") Long teaId, RedirectAttributes ra) {
        chooseService.increment(teaId);
        ra.addFlashAttribute("message", "선택이 반영되었습니다.");
        return "redirect:/main";
    }


    // 메모 작성 페이지
    @GetMapping("/memo/new")
    public String showMemoCreateForm(Model model) {
        model.addAttribute("userData", new UserDataEntity());
        return "memo-create";
    }
    // 메모 수정 페이지
    @GetMapping("/memo/edit/{id}")
    public String showMemoEditForm(@PathVariable("id") Long userDataId, Model model) {
        UserDataEntity data = userDataService.getUserDataById(userDataId);
        model.addAttribute("userData", data);
        return "memo-edit";
    }

    //작성
    @PostMapping("/memo")
    public String createMemo(
            @RequestParam Long userDataId,
            @RequestParam String memo,
            RedirectAttributes redirectAttributes
    ) {
        userDataService.saveMemo(userDataId, memo);
        redirectAttributes.addFlashAttribute("message", "메모가 작성되었습니다.");
        return "redirect:/memo/new";
    }


    // 수정
    @PostMapping("/memo/{id}")
    public String updateMemo(
            @PathVariable Long id,
            @RequestParam String memo,
            RedirectAttributes redirectAttributes
    ) {
        userDataService.saveMemo(id, memo);
        redirectAttributes.addFlashAttribute("message", "메모가 수정되었습니다.");
        return "redirect:/memo/edit/{id}";
    }


}