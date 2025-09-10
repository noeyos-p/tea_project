package com.project.tea.controller;

import com.project.tea.entity.UserDataEntity;
import com.project.tea.service.UserDataService;
import com.project.tea.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/userdata")
public class UserDataController {

    private final UserDataService userDataService;
    private final UserService userService;

    /**
     * 결과 페이지에서 차 선택 + 저장
     * 저장 후 메모 작성 페이지로 이동
     */
    @PostMapping("/save")
    public String saveUserData(
            @RequestParam Long teaId,
            @RequestParam(required = false) Long moodId,
            @RequestParam(required = false) Long stateId,
            RedirectAttributes redirectAttributes
    ) {
        Long userId = userService.getCurrentUserId();
        Long userDataId = userDataService.saveUserData(userId, teaId, moodId, stateId);
        return "redirect:/userdata/memo/" + userDataId;
    }



    /**
     * 마이페이지 조회
     */
    @GetMapping("/mypage")
    public String showUserData(Model model) {
        Long userId = userService.getCurrentUserId();
        List<UserDataEntity> userDataList = userDataService.getUserDataByUserId(userId);
        model.addAttribute("userDataList", userDataList);
        return "tea/mypage/information/main";
    }
}
