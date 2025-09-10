package com.project.tea.controller;

import com.project.tea.dto.ResultDto;
import com.project.tea.entity.MoodCheckEntity;
import com.project.tea.service.MoodService;
import com.project.tea.repository.MoodCheckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mood")
public class MoodController {

    private final MoodService moodService;
    private final MoodCheckRepository moodCheckRepository;

    // 체크리스트 페이지
    @GetMapping("/checklist")
    public String showMoodChecklist(Model model) {
        List<MoodCheckEntity> checkItems = moodCheckRepository.findAllRandomOrder();
        model.addAttribute("checkItems", checkItems); // HTML에서 th:each="c : ${checkItems}" 사용
        return "/tea/mood-check";
    }

    // 체크리스트 제출 처리
    @PostMapping("/submit")
    public String submitMoodChecklist(
            @RequestParam(name = "moodId", required = false) List<Long> moodId,
            Model model) {

        List<MoodCheckEntity> checkItems = moodCheckRepository.findAllRandomOrder();
        model.addAttribute("checkItems", checkItems);

        // 체크 안했거나 5개 선택 안했으면
       if (moodId == null || moodId.size() != 5) {
            /* model.addAttribute("error", "체크리스트는 반드시 5개 선택해야 합니다."); */
            return "/tea/mood-check";
        }

        ResultDto resultDto = moodService.analyzeMood(moodId);
        model.addAttribute("result", resultDto);

        return "result";
    }

}
