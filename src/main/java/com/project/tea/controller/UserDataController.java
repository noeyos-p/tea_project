package com.project.tea.controller;

import com.project.tea.dto.UserDataSimpleDto;
import com.project.tea.entity.UserDataEntity;
import com.project.tea.entity.UserEntity;
import com.project.tea.service.UserDataService;
import com.project.tea.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.ZoneId;
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
    public String saveUserData(@RequestParam Long teaId,
                               @RequestParam(required = false) Long moodId,
                               @RequestParam(required = false) Long stateId,
                               @RequestParam(required = false) Boolean skipSave,
                               RedirectAttributes ra) {
        Long userId = userService.getCurrentUserId();

        if (Boolean.TRUE.equals(skipSave)) {
            UserDataEntity result = userDataService.getOrCreateToday(userId);
            result.setTea(userDataService.getTeaReference(teaId));
            userDataService.saveOnlyTea(result);

            // ✅ 새 tea 이름 FlashAttribute로 전달
            ra.addFlashAttribute("teaName",
                    userDataService.getTeaReference(teaId).getName());

            ra.addFlashAttribute("message", "저장하지 않고 이동합니다.");
            return "redirect:/userdata/memo/" + result.getId();

        } else {
            UserDataService.SaveResult saveResult =
                    userDataService.saveOrGetToday(userId, teaId, moodId, stateId);

            if (saveResult.isAlready()) {
                ra.addAttribute("already", true);
            }

            // ✅ 새 tea 이름 FlashAttribute로 전달
            ra.addFlashAttribute("teaName",
                    userDataService.getTeaReference(teaId).getName());

            return "redirect:/userdata/memo/" + saveResult.getId();
        }
    }


    // ------------------ 메모 작성/보기 (폼) ------------------
    @GetMapping("/memo/{userDataId}")
    public String showMemoForm(@PathVariable("userDataId") Long userDataId,
                               @RequestParam(required = false) Boolean already,
                               Model model) {
        UserDataEntity data = userDataService.getUserDataById(userDataId);
        model.addAttribute("userData", data);

        // ✅ FlashAttribute에서 teaName이 있으면 우선 사용
        if (model.containsAttribute("teaName")) {
            // tea 객체도 있지만, 우선순위로 teaName 별도 전달
            model.addAttribute("teaName", model.getAttribute("teaName"));
        } else {
            model.addAttribute("tea", data.getTea());
        }

        // 오늘 메모 여부
        Long userId = userService.getCurrentUserId();
        boolean alreadyWritten = userDataService.existsMemoToday(userId);
        model.addAttribute("already", alreadyWritten);

        boolean isToday = LocalDate.now(ZoneId.of("Asia/Seoul")).equals(data.getDate());
        model.addAttribute("isToday", isToday);

        return "tea/tea-memo";
    }


    // ------------------ 메모 저장 ------------------
    @PostMapping("/memo")
    public String createMemo(@ModelAttribute("userData") UserDataEntity userData,
                             RedirectAttributes ra) {
        Long userId = userService.getCurrentUserId();

        boolean exists = userDataService.existsMemoToday(userId);
        if (exists) {
            // JS에서 이미 막지만, 안전장치로 한 번 더
            ra.addFlashAttribute("message", "오늘 저장된 메모가 있습니다. 수정하시려면 마이페이지에서 수정해주세요.");
            return "redirect:/userdata/memo/" + userData.getId();
        }

        // ✅ 메모 저장
        userDataService.saveMemo(userData.getId(), userData.getMemo());
        ra.addFlashAttribute("message", "메모가 저장되었습니다.");
        return "redirect:/userdata/memo/" + userData.getId(); // 같은 메모 페이지로 redirect
    }


    // ------------------ 메모 수정 ------------------
    @GetMapping("/memo/edit/{userDataId}")
    public String showMemoEditForm(@PathVariable("userDataId") Long userDataId, Model model) {
        UserDataEntity data = userDataService.getUserDataById(userDataId);
        model.addAttribute("userData", data);
        return "tea/mypage/memo/update";
    }

    @PostMapping("/memo/{userDataId}")
    public String updateMemo(@PathVariable("userDataId") Long userDataId,
                             @RequestParam String memo,
                             RedirectAttributes ra) {
        userDataService.saveMemo(userDataId, memo);
        ra.addFlashAttribute("message", "메모가 수정되었습니다.");
        return "redirect:/userdata/mypage/memo/main";
    }

    // ------------------ 메모 메인 페이지 ------------------
    @GetMapping("/mypage/memo/main")
    public String showMemoMain(Model model) {
        Long userId = userService.getCurrentUserId();

        // 오늘 작성한 메모 최신 1개만
        UserDataEntity todayMemo = userDataService.getTodayMemo(userId);

        List<UserDataEntity> displayList = (todayMemo != null) ? List.of(todayMemo) : List.of();
        model.addAttribute("userDataList", displayList);
        return "tea/mypage/memo/main";
    }

    @GetMapping("/memo/list")
    @ResponseBody
    public List<UserDataSimpleDto> getMemosByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = userService.getCurrentUserId();
        return userDataService.findByUserAndDate(userId, date)
                .stream()
                .map(UserDataSimpleDto::from)
                .toList();
    }

    /* ---------------------------------- 마이페이지 ------------------------------*/

    @GetMapping("/mypage")
    public String showUserData(Model model) {
        Long userId = userService.getCurrentUserId();
        List<UserDataEntity> userDataList = userDataService.getUserDataByUserId(userId);

        UserEntity me = userService.getById(userId); // 최신 닉네임/이메일
        model.addAttribute("user", me);
        model.addAttribute("userDataList", userDataList);
        return "tea/mypage/information/main";
    }

    @GetMapping("/mypage/update")
    public String showUpdatePage(Model model) {
        Long userId = userService.getCurrentUserId();
        UserEntity user = userService.getById(userId);
        model.addAttribute("user", user);
        return "tea/mypage/information/update";
    }

    @PostMapping("/mypage/update")
    public String updateUser(@RequestParam(required = false) String nickname,
                             @RequestParam(required = false) String password,
                             @RequestParam(required = false) String confirmPassword,
                             RedirectAttributes ra) {
        Long userId = userService.getCurrentUserId();
        boolean changed = false;

        try {
            if (nickname != null && !nickname.isBlank()) {
                userService.updateNickname(userId, nickname.trim());
                changed = true;
            }
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
                userService.updatePassword(userId, password);
                changed = true;
            }
        } catch (IllegalArgumentException e) {
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
