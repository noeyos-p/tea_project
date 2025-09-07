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
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mood")
public class MoodController {

    private final MoodService moodService;
    private final MoodCheckRepository moodCheckRepository;

    // 체크리스트 페이지
    @GetMapping("/checkList")
    public String showMoodChecklist(Model model) {
        List<MoodCheckEntity> checkItems = moodCheckRepository.findAll();
        model.addAttribute("checkItems", checkItems); // HTML에서 th:each="c : ${checkItems}" 사용
        return "moodCheck";
    }

    // 체크리스트 제출 처리 → 결과 페이지로 포워드
    @PostMapping("/submit")
    public String submitMoodChecklist(
            @RequestParam(required = false) List<Long> moodId,
            Model model) {

        if (moodId == null || moodId.size() != 5) {
            model.addAttribute("error", "체크리스트는 반드시 5개 선택해야 합니다.");
            List<MoodCheckEntity> checkItems = moodCheckRepository.findAll();
            model.addAttribute("checkItems", checkItems);
            return "moodCheck";
        }

        // 점수는 기본 1점으로 맵 생성
        Map<Long, Integer> scores = moodId.stream()
                .collect(Collectors.toMap(id -> id, id -> 1));

        ResultDto resultDto = moodService.analyzeMood(scores);
        model.addAttribute("result", resultDto);

        return "result";
    }

    // 결과 페이지
    @GetMapping("/result")
    public String showMoodResult() {
        return "result";
    }
}
