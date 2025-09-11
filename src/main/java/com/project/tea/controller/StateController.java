package com.project.tea.controller;

import com.project.tea.dto.ResultDto;
import com.project.tea.entity.StateEntity;
import com.project.tea.entity.UserDataEntity;
import com.project.tea.service.StateService;
import com.project.tea.repository.StateRepository;
import com.project.tea.service.UserDataService;
import com.project.tea.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/state")
public class StateController {

    private final StateService stateService;
    private final StateRepository stateRepository;
    private final UserService userService;
    private final UserDataService userDataService;

    // 상태 체크리스트 페이지
    @GetMapping("/checklist")
    public String showStateChecklist(Model model) {
        // DB에서 상태 목록 가져오기 (예: 두통, 복통 등)
        List<StateEntity> states = stateRepository.findAll();
        model.addAttribute("states", states);
        System.out.println("states size = " + states.size());
        states.forEach(s -> System.out.println("State: " + s.getId() + ", " + s.getState()));
        return "/tea/state-check";
//        return "stateCheck";
    }

    // 체크 제출 처리 → DB 저장 없이 결과 페이지로 바로 포워드
    @PostMapping("/submit")
    public String submitStateChecklist(
            @RequestParam(required = false) Long stateId,
            Model model) {

        if (stateId == null) {
            model.addAttribute("error", "체크리스트는 반드시 1개 선택해야 합니다.");
            model.addAttribute("states", stateRepository.findAll());
            return "/tea/state-check";
        }

        try {
            ResultDto resultDto = stateService.recommendByState(stateId);
            model.addAttribute("result", resultDto);

            // ✅ 여기 추가
            Long userId = userService.getCurrentUserId();
            UserDataEntity today = userDataService.getOrCreateToday(userId);
            boolean alreadyToday = (today.getTea() != null);
            Long currentTeaId = alreadyToday ? today.getTea().getId() : null;

            model.addAttribute("alreadyToday", alreadyToday);
            model.addAttribute("currentTeaId", currentTeaId);

            return "tea/recommend-tea";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("states", stateRepository.findAll());
            return "/tea/state-check";
        }
    }


    // 결과 페이지 직접 접근 시
    @GetMapping("/result")
    public String showStateResult() {
        return "tea/recommend-tea";
    }
}
