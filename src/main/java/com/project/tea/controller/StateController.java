package com.project.tea.controller;

import com.project.tea.dto.ResultDto;
import com.project.tea.entity.StateEntity;
import com.project.tea.service.StateService;
import com.project.tea.repository.StateRepository;
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
//            return "stateCheck";
        }

        try {
            // State ID 기반 추천 메시지 + 추천 티 리스트 조회
            ResultDto resultDto = stateService.recommendByState(stateId);

            // 결과를 Model에 담아 바로 결과 페이지로 전달
            model.addAttribute("result", resultDto);

            return "result";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("states", stateRepository.findAll()); // 다시 상태 목록 전달
            return "/tea/state-check";
//            return "stateCheck";
        }
    }

    // 결과 페이지 직접 접근 시
    @GetMapping("/result")
    public String showStateResult() {
        return "result";
    }
}
