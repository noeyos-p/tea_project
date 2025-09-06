package com.project.tea.controller;

import com.project.tea.dto.ResultDto;
import com.project.tea.service.MoodService;
import com.project.tea.service.UserDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mood")
public class MoodController {

    private final MoodService moodService;
    private final UserDataService userDataService;

    // 체크리스트 폼 페이지
    @GetMapping("/checklist")
    public String showMoodChecklist() {
        return "moodCheck"; // moodCheck.html
    }

    // 체크리스트 제출 처리
    @PostMapping("/submit")
    public String submitMoodChecklist(
            @RequestParam Map<Long, Integer> checkedIds, // 체크된 Mood 점수 맵
            @RequestParam Long userId,                   // 로그인 유저 ID
            RedirectAttributes redirectAttributes) {

        try {
            // 체크 개수 검증 (5개 선택 필수)
            if (checkedIds == null || checkedIds.size() != 5) {
                throw new IllegalArgumentException("체크리스트는 반드시 5개 선택해야 합니다.");
            }

            // Mood 분석 → 추천 티 + 메시지
            ResultDto resultDto = moodService.analyzeMood(checkedIds);

            // UserData에 저장 (마이페이지용)
            userDataService.saveUserData(
                    userId,
                    resultDto.getTeas().get(0).getId(), // 선택 티 (임시 첫 번째)
                    resultDto.getResultId(),
                    null,
                    null
            );

            // Flash Attribute로 결과 전달
            redirectAttributes.addFlashAttribute("result", resultDto);
            return "redirect:/mood/result";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/mood/checklist";
        }
    }

    // 결과 페이지
    @GetMapping("/result")
    public String showMoodResult() {
        return "result"; // result.html
    }
}
