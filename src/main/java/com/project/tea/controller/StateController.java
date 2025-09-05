package com.project.tea.controller;

import com.project.tea.dto.ResultDto;
import com.project.tea.dto.TeaDto;
import com.project.tea.service.ResultService;
import com.project.tea.service.StateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * State 체크리스트 컨트롤러
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/state")
public class StateController {

    private final StateService stateService;
    private final ResultService resultService;

    // State 선택 폼 페이지
    @GetMapping("/checklist")
    public String showStateChecklist() {
        return "stateCheckList"; // stateCheckList.html
    }

    // State 선택 제출 처리
    @PostMapping("/submit")
    public String submitStateChecklist(@RequestParam Long stateId,
                                       RedirectAttributes redirectAttributes) {
        try {
            // 추천 티 리스트 조회
            List<TeaDto> teas = stateService.recommendByState(stateId);

            // 추천 결과를 DB에 저장
            ResultDto savedResult = resultService.saveStateResult(stateId, teas, "상태에 따른 추천 티입니다.");

            // Flash Attribute로 전달
            redirectAttributes.addFlashAttribute("result", savedResult);

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
