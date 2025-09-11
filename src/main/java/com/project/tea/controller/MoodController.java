package com.project.tea.controller;

import com.project.tea.dto.ResultDto;
import com.project.tea.entity.MoodCheckEntity;
import com.project.tea.entity.UserDataEntity;
import com.project.tea.service.MoodService;
import com.project.tea.repository.MoodCheckRepository;
import com.project.tea.service.UserDataService;
import com.project.tea.service.UserService;
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
    private final UserService userService;
    private final UserDataService userDataService;

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

        // ✅ 추천 결과 생성
        ResultDto resultDto = moodService.analyzeMood(moodId);
        model.addAttribute("result", resultDto);

        // ✅ 오늘 데이터 여부/선택 차 정보도 추가
        Long userId = userService.getCurrentUserId();
        UserDataEntity today = userDataService.getOrCreateToday(userId);
        boolean alreadyToday = (today.getTea() != null);
        Long currentTeaId = alreadyToday ? today.getTea().getId() : null;

        model.addAttribute("alreadyToday", alreadyToday);
        model.addAttribute("currentTeaId", currentTeaId);

        return "tea/recommend-tea";
    }
}
