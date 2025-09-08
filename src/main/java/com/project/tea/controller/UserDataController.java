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
     * 메모 작성/수정 페이지
     */
    @GetMapping("/memo/{id}")
    public String showMemoForm(@PathVariable("id") Long userDataId, Model model) {
        UserDataEntity data = userDataService.getUserDataById(userDataId);
        model.addAttribute("userData", data);
        return "memo";
    }

    /**
     * 메모 저장 (작성/수정)
     */
    @PostMapping("/memo/save")
    public String saveMemo(
            @RequestParam Long userDataId,
            @RequestParam String memo,
            RedirectAttributes redirectAttributes
    ) {
        userDataService.saveMemo(userDataId, memo); // 여기서 작성/수정 둘 다 처리
        redirectAttributes.addFlashAttribute("message", "메모가 저장되었습니다.");
        return "redirect:/userdata/mypage";
    }

    /**
     * 마이페이지 조회
     */
    @GetMapping("/mypage")
    public String showUserData(Model model) {
        Long userId = userService.getCurrentUserId();
        List<UserDataEntity> userDataList = userDataService.getUserDataByUserId(userId);
        model.addAttribute("userDataList", userDataList);
        return "mypage";
    }
}
