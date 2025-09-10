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

    // ------------------ 공통 유저 데이터 조회 ------------------
    private List<UserDataEntity> getCurrentUserData() {
        Long userId = userService.getCurrentUserId();
        return userDataService.getUserDataByUserId(userId);
    }

    // ------------------ 차 선택 후 저장 ------------------
    @PostMapping("/save")
    public String saveUserData(
            @RequestParam Long teaId,
            @RequestParam(required = false) Long moodId,
            @RequestParam(required = false) Long stateId
    ) {
        Long userId = userService.getCurrentUserId();
        Long userDataId = userDataService.saveUserData(userId, teaId, moodId, stateId);
        return "redirect:/userdata/memo/" + userDataId;
    }

    // ------------------ 마이페이지 ------------------
    @GetMapping("/mypage")
    public String showUserData(Model model) {
        model.addAttribute("userDataList", getCurrentUserData());
        return "tea/mypage/information/main";
    }

    // ------------------ 메모 작성 ------------------
    @GetMapping("/memo/{userDataId}")
    public String showMemoForm(@PathVariable Long userDataId, Model model) {
        UserDataEntity data = userDataService.getUserDataById(userDataId);
        model.addAttribute("userData", data);
        model.addAttribute("tea", data.getTea());
        return "tea/tea-memo";
    }

    @PostMapping("/memo")
    public String createMemo(@ModelAttribute("userData") UserDataEntity userData, Model model) {
        userDataService.saveMemo(userData.getId(), userData.getMemo());
        UserDataEntity updatedData = userDataService.getUserDataById(userData.getId());
        model.addAttribute("userData", updatedData);
        model.addAttribute("tea", updatedData.getTea());
        model.addAttribute("message", "메모가 작성되었습니다.");
        return "tea/tea-memo";
    }

    // ------------------ 메모 수정 ------------------
    @GetMapping("/memo/edit/{userDataId}")
    public String showMemoEditForm(@PathVariable Long userDataId, Model model) {
        UserDataEntity data = userDataService.getUserDataById(userDataId);
        model.addAttribute("userData", data);
        return "tea/mypage/memo/update";
    }

    @PostMapping("/memo/{userDataId}")
    public String updateMemo(@PathVariable Long userDataId,
                             @RequestParam String memo,
                             RedirectAttributes redirectAttributes) {
        userDataService.saveMemo(userDataId, memo);
        redirectAttributes.addFlashAttribute("message", "메모가 수정되었습니다.");
        return "redirect:/userdata/mypage/memo/main";
    }

    // ------------------ 메모 메인 페이지 ------------------
    @GetMapping("/mypage/memo/main")
    public String showMemoMain(Model model) {
        model.addAttribute("userDataList", getCurrentUserData());
        return "tea/mypage/memo/main";
    }



}
