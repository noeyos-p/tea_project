package com.project.tea.controller;

import com.project.tea.dto.ResultDto;
import com.project.tea.dto.TeaDto;
import com.project.tea.service.MoodService;
import com.project.tea.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Mood 체크리스트 컨트롤러
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/mood")
public class MoodController {

    private final MoodService moodService;
    private final ResultService resultService;

    // 체크리스트 폼 페이지
    @GetMapping("/checklist")
    public String showMoodChecklist() {
        return "moodCheck";
    }

    // 체크리스트 제출 처리
    @PostMapping("/submit")
    public String submitMoodChecklist(
            @RequestParam(name = "checkedIds", required = false) List<Long> checkedIds,
            RedirectAttributes redirectAttributes) {

        try {
            // 체크 안 했거나 5개 선택 안했으면 예외
            if (checkedIds == null || checkedIds.size() != 5) {
                throw new IllegalArgumentException("체크리스트는 반드시 5개 선택해야 합니다.");
            }

            // 체크리스트 분석 → 추천 차 조회
            ResultDto analysisResult = moodService.analyzeMood(checkedIds);

            // 분석 결과 DB에 저장 (analysisResult가 null이면 예외)
            if (analysisResult == null) {
                throw new IllegalArgumentException("선택한 체크리스트 항목이 DB에 존재하지 않습니다.");
            }

            ResultDto savedResult = resultService.saveMoodResult(
                    analysisResult.getResultId(),
                    analysisResult.getTeas(),
                    analysisResult.getMessage()
            );

            // Flash Attribute로 전달
            redirectAttributes.addFlashAttribute("result", savedResult);
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
