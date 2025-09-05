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

/**
 * UserDataController
 * - 사용자가 선택한 티 + Mood 또는 State 결과를 마이페이지에 저장
 * - 마이페이지에서 유저 기록 조회
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/userdata")
public class UserDataController {

    private final UserDataService userDataService;
    private final UserService userService; // 현재 로그인한 유저 정보 가져오기

    /**
     * UserData 저장
     *
     * @param teaId 선택한 티 ID
     * @param moodId Mood 결과 ID (Mood 체크리스트에서 온 경우)
     * @param stateId State 결과 ID (State 체크리스트에서 온 경우)
     * @param memo 사용자 메모 (선택 사항)
     * @param redirectAttributes 플래시 메시지 전달용
     * @return 마이페이지 리다이렉트
     */
    @PostMapping("/save")
    public String saveUserData(
            @RequestParam Long teaId,
            @RequestParam(required = false) Long moodId,
            @RequestParam(required = false) Long stateId,
            @RequestParam(required = false) String memo,
            RedirectAttributes redirectAttributes
    ) {
        Long userId = userService.getCurrentUserId(); // 로그인한 유저 ID
        userDataService.saveUserData(userId, teaId, moodId, stateId, memo);
        redirectAttributes.addFlashAttribute("message", "마이페이지에 저장되었습니다.");
        return "redirect:/userdata/mypage";
    }

    /**
     * 마이페이지
     * - 로그인한 유저의 UserData 목록을 보여줌
     *
     * @param model Model 객체
     * @return mypage.html
     */
    @GetMapping("/mypage")
    public String showUserData(Model model) {
        Long userId = userService.getCurrentUserId(); // 로그인한 유저 ID
        List<UserDataEntity> userDataList = userDataService.getUserDataByUserId(userId);
        model.addAttribute("userDataList", userDataList);
        return "mypage";
    }
}
