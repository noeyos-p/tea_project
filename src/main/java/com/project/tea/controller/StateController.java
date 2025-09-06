package com.project.tea.controller;

import com.project.tea.dto.ResultDto;
import com.project.tea.service.StateService;
import com.project.tea.service.UserDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/state")
public class StateController {

    private final StateService stateService;
    private final UserDataService userDataService;

    // 상태 체크리스트 페이지
    @GetMapping("/checklist")
    public String showStateChecklist() {
        return "stateCheck"; // stateCheck.html
    }

    // 체크 제출 처리
    @PostMapping("/submit")
    public String submitStateChecklist(
            @RequestParam Long stateId,
            @RequestParam Long userId,
            RedirectAttributes redirectAttributes) {

        try {
            // 추천 티 조회 + 랜덤 메시지
            ResultDto resultDto = stateService.recommendByState(stateId);

            // UserData에 저장
            userDataService.saveUserData(userId,
                    resultDto.getTeas().get(0).getId(), // 선택 티(임시 첫 번째)
                    null,
                    resultDto.getResultId(),
                    null);

            redirectAttributes.addFlashAttribute("result", resultDto);
            return "redirect:/state/result";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/state/checklist";
        }
    }

    // 결과 페이지
    @GetMapping("/result")
    public String showStateResult() {
        return "result"; // result.html
    }
}
