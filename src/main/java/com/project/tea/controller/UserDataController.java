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

    // ------------------ 메모 관련 ------------------

    /**
     * 메모 작성 페이지
     */
    @GetMapping("/memo/{userDataId}")
    public String showMemoForm(@PathVariable Long userDataId, Model model) {
        UserDataEntity data = userDataService.getUserDataById(userDataId);
        model.addAttribute("userData", data);
        return "tea/tea-memo"; // 작성용 템플릿
    }

    /**
     * 메모 작성 POST
     */
    @PostMapping("/memo")
    public String createMemo(@ModelAttribute("userData") UserDataEntity userData, Model model) {
        // id와 memo를 이용해 저장
        userDataService.saveMemo(userData.getId(), userData.getMemo());

        UserDataEntity updatedData = userDataService.getUserDataById(userData.getId());
        model.addAttribute("userData", updatedData);
        model.addAttribute("message", "메모가 작성되었습니다.");

        return "tea/tea-memo";
    }


    /**
     * 메모 수정 페이지
     */
    @GetMapping("/memo/edit/{userDataId}")
    public String showMemoEditForm(@PathVariable Long userDataId, Model model) {
        UserDataEntity data = userDataService.getUserDataById(userDataId);
        model.addAttribute("userData", data);
        return "tea/mypage/memo/update"; // 수정용 템플릿
    }

    /**
     * 메모 수정 POST
     */
    @PostMapping("/memo/{userDataId}")
    public String updateMemo(@PathVariable Long userDataId,
                             @RequestParam String memo,
                             RedirectAttributes redirectAttributes) {
        userDataService.saveMemo(userDataId, memo);
        redirectAttributes.addFlashAttribute("message", "메모가 수정되었습니다.");
        return "redirect:/tea/mypage/memo/main"; // 수정 후 메인으로 이동
    }
}
