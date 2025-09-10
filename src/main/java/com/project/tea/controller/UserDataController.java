package com.project.tea.controller;

import com.project.tea.entity.UserDataEntity;
import com.project.tea.entity.UserEntity;
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
    // ------------------ 메모 관련 ------------------

    /**
     * 메모 작성 페이지
     */
    @GetMapping("/memo/{userDataId}")
    public String showMemoForm(@PathVariable Long userDataId, Model model) {
        UserDataEntity data = userDataService.getUserDataById(userDataId);
        model.addAttribute("userData", data);
        model.addAttribute("tea", data.getTea());
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
        model.addAttribute("tea", updatedData.getTea());
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

    /* ----------------------------------마이페이지------------------------------*/
// 마이페이지 조회 (최신 닉네임을 렌더하기 위해 user도 함께 내려줌)
    @GetMapping("/mypage")
    public String showUserData(Model model) {
        Long userId = userService.getCurrentUserId();
        List<UserDataEntity> userDataList = userDataService.getUserDataByUserId(userId);

        UserEntity me = userService.getById(userId); // 최신 닉네임/이메일
        model.addAttribute("user", me);
        model.addAttribute("userDataList", userDataList);

        return "tea/mypage/information/main";
    }

    // 업데이트 페이지
    @GetMapping("/mypage/update")
    public String showUpdatePage(Model model) {
        Long userId = userService.getCurrentUserId();
        UserEntity user = userService.getById(userId);
        model.addAttribute("user", user);
        return "tea/mypage/information/update"; // templates/tea/mypage/information/update.html
    }

    // 닉네임/비밀번호 변경 처리
    @PostMapping("/mypage/update")
    public String updateUser(
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String confirmPassword,
            RedirectAttributes ra
    ) {
        Long userId = userService.getCurrentUserId();
        boolean changed = false;

        try {
            // 닉네임 변경 (입력된 경우에만)
            if (nickname != null && !nickname.isBlank()) {
                userService.updateNickname(userId, nickname.trim());
                changed = true;
            }

            // 비밀번호 변경 (입력된 경우에만)
            if (password != null && !password.isBlank()) {
                if (confirmPassword == null || confirmPassword.isBlank()) {
                    ra.addFlashAttribute("error", "비밀번호 확인을 입력해 주세요.");
                    return "redirect:/userdata/mypage/update";
                }
                if (!password.equals(confirmPassword)) {
                    ra.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
                    return "redirect:/userdata/mypage/update";
                }
                if (password.length() < 4) {
                    ra.addFlashAttribute("error", "비밀번호는 4자 이상이어야 합니다.");
                    return "redirect:/userdata/mypage/update";
                }
                userService.updatePassword(userId, password); // 재사용 금지 검사 포함
                changed = true;
            }

        } catch (IllegalArgumentException e) {
            // 닉네임 중복 / 비밀번호 재사용 등 서비스 레벨 유효성 실패
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/userdata/mypage/update";
        }

        if (!changed) {
            ra.addFlashAttribute("error", "변경할 닉네임 또는 비밀번호를 입력해 주세요.");
            return "redirect:/userdata/mypage/update";
        }

        ra.addFlashAttribute("message", "정보가 변경되었습니다.");
        return "redirect:/userdata/mypage";
    }
}
