package com.project.tea.controller;

import com.project.tea.entity.QnaEntity;
import com.project.tea.entity.ResultEntity;
import com.project.tea.entity.TeaEntity;
import com.project.tea.entity.UserEntity;
import com.project.tea.repository.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final TeaRepository teaRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager em;
    private final ResultRepository resultRepository;
    private final MoodRepository moodRepository;
    private final StateRepository stateRepository;
    private final QnaRepository qnaRepository;
    // 목록
    @GetMapping
    public String adminHome(Model model) {
        List<UserEntity> users = userRepository.findAll();
        List<TeaEntity> teas = teaRepository.findAll();
        List<ResultEntity> ments = resultRepository.findAll();
        List<QnaEntity> qnas = qnaRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("teas", teas);
        model.addAttribute("ments", ments);
        model.addAttribute("qnas", qnas);
        return "tea/admin-page";
    }

    // 유저 생성 폼/생성
    @GetMapping("/users/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new UserEntity());
        model.addAttribute("mode", "create");
        return "tea/admin-user-form";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute("user") UserEntity form, RedirectAttributes ra) {
        if (!StringUtils.hasText(form.getEmail()) || !StringUtils.hasText(form.getNickname())) {
            ra.addFlashAttribute("error", "닉네임과 이메일은 필수입니다.");
            return "redirect:/admin/users/new";
        }
        form.setPassword(passwordEncoder.encode("1111")); // 비밀번호 입력 없이 1111 저장
        userRepository.save(form);
        ra.addFlashAttribute("notice", "유저가 생성되었습니다.");
        return "redirect:/admin";
    }

    // 유저 수정 폼/수정
    @GetMapping("/users/{id}/edit")
    public String editUserForm(@PathVariable Long id, Model model) {
        UserEntity user = userRepository.findById(id).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("mode", "edit");
        return "tea/admin-user-form";
    }

    @PutMapping("/users/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") UserEntity form, RedirectAttributes ra) {
        UserEntity user = userRepository.findById(id).orElseThrow();
        user.setNickname(form.getNickname());
        user.setEmail(form.getEmail());
        userRepository.save(user);
        ra.addFlashAttribute("notice", "유저 정보가 수정되었습니다.");
        return "redirect:/admin";
    }

    //  차 생성 폼/생성
    @GetMapping("/teas/new")
    public String newTeaForm(Model model) {
        model.addAttribute("tea", new TeaEntity());
        model.addAttribute("mode", "create");
        return "tea/admin-tea-form";
    }

    @PostMapping("/teas")
    public String createTea(@ModelAttribute("tea") TeaEntity form, RedirectAttributes ra) {
        // 필요한 필드만 사용(name, content, eat, caution)
        TeaEntity tea = new TeaEntity();
        tea.setName(form.getName());
        tea.setContent(form.getContent());
        tea.setEat(form.getEat());
        tea.setCaution(form.getCaution());
        teaRepository.save(tea);
        ra.addFlashAttribute("notice", "차가 생성되었습니다.");
        return "redirect:/admin";
    }

    // 차 수정 폼/수정
    @GetMapping("/teas/{id}/edit")
    public String editTeaForm(@PathVariable Long id, Model model) {
        TeaEntity tea = teaRepository.findById(id).orElseThrow();
        model.addAttribute("tea", tea);
        model.addAttribute("mode", "edit");
        return "tea/admin-tea-form";
    }

    @PutMapping("/teas/{id}")
    public String updateTea(@PathVariable Long id, @ModelAttribute("tea") TeaEntity form, RedirectAttributes ra) {
        TeaEntity tea = teaRepository.findById(id).orElseThrow();
        tea.setName(form.getName());
        tea.setContent(form.getContent());
        tea.setEat(form.getEat());
        tea.setCaution(form.getCaution());
        teaRepository.save(tea);
        ra.addFlashAttribute("notice", "차 정보가 수정되었습니다.");
        return "redirect:/admin";
    }


    // 사용자 삭제
    private String doDeleteUser(Long id, String adminPassword, RedirectAttributes ra) {
        if (!"1111".equals(adminPassword)) {
            ra.addFlashAttribute("error", "관리자 비밀번호가 일치하지 않습니다.");
            return "redirect:/admin";
        }
        try {
            if (!userRepository.existsById(id)) {
                ra.addFlashAttribute("error", "해당 유저가 이미 없거나 존재하지 않습니다. (id=" + id + ")");
                return "redirect:/admin";
            }
            userRepository.deleteById(id); // FK 제약 있으면 아래 catch 진입
            ra.addFlashAttribute("notice", "유저가 삭제되었습니다.");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "연관 데이터 때문에 삭제할 수 없습니다. 관련 데이터를 먼저 삭제하세요.");
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            ra.addFlashAttribute("error", "삭제 대상이 없습니다. (이미 삭제됨)");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "삭제 중 오류: " + e.getClass().getSimpleName());
        }
        return "redirect:/admin";
    }

    // 차 삭제
    private String doDeleteTea(Long id, String adminPassword, RedirectAttributes ra) {
        if (!"1111".equals(adminPassword)) {
            ra.addFlashAttribute("error", "관리자 비밀번호가 일치하지 않습니다.");
            return "redirect:/admin";
        }
        try {
            if (!teaRepository.existsById(id)) {
                ra.addFlashAttribute("error", "해당 차 데이터가 이미 없거나 존재하지 않습니다. (id=" + id + ")");
                return "redirect:/admin";
            }
            teaRepository.deleteById(id);
            ra.addFlashAttribute("notice", "차가 삭제되었습니다.");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "연관 데이터 때문에 삭제할 수 없습니다. 관련 데이터를 먼저 삭제하세요.");
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            ra.addFlashAttribute("error", "삭제 대상이 없습니다. (이미 삭제됨)");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "삭제 중 오류: " + e.getClass().getSimpleName());
        }
        return "redirect:/admin";
    }

    // 기존 DELETE
    @Transactional
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id,
                             @RequestParam(value = "adminPassword", required = false) String adminPassword,
                             RedirectAttributes ra) {
        return doDeleteUser(id, adminPassword, ra);
    }

    @Transactional
    @DeleteMapping("/teas/{id}")
    public String deleteTea(@PathVariable Long id,
                            @RequestParam(value = "adminPassword", required = false) String adminPassword,
                            RedirectAttributes ra) {
        return doDeleteTea(id, adminPassword, ra);
    }

    // 삭제우회용 POST
    @Transactional
    @PostMapping("/users/{id}/delete")
    public String deleteUserPost(@PathVariable Long id,
                                 @RequestParam(value = "adminPassword", required = false) String adminPassword,
                                 RedirectAttributes ra) {
        return doDeleteUser(id, adminPassword, ra);
    }

    @Transactional
    @PostMapping("/teas/{id}/delete")
    public String deleteTeaPost(@PathVariable Long id,
                                @RequestParam(value = "adminPassword", required = false) String adminPassword,
                                RedirectAttributes ra) {
        return doDeleteTea(id, adminPassword, ra);
    }

    // 수정 우회용 POST
    @PostMapping("/users/{id}/edit")
    @Transactional
    public String updateUserPost(@PathVariable Long id,
                                 @ModelAttribute("user") UserEntity form,
                                 RedirectAttributes ra) {
        UserEntity user = userRepository.findById(id).orElseThrow();
        user.setNickname(form.getNickname());
        user.setEmail(form.getEmail());
        userRepository.save(user);
        ra.addFlashAttribute("notice", "유저 정보가 수정되었습니다.");
        return "redirect:/admin";
    }

    @PostMapping("/teas/{id}/edit")
    @Transactional
    public String updateTeaPost(@PathVariable Long id,
                                @RequestParam String name,
                                @RequestParam(required = false) String content,
                                @RequestParam(required = false) String eat,
                                @RequestParam(required = false) String caution,
                                RedirectAttributes ra) {
        try {
            if (name == null || name.isBlank()) {
                ra.addFlashAttribute("error", "이름은 필수입니다.");
                return "redirect:/admin/teas/" + id + "/edit";
            }

            TeaEntity tea = teaRepository.findById(id).orElseThrow();
            tea.setName(name);
            tea.setContent(content);
            tea.setEat(eat);
            tea.setCaution(caution);
            teaRepository.save(tea); // 영속 상태면 생략해도 되지만 명시 유지

            ra.addFlashAttribute("notice", "차 정보가 수정되었습니다.");
            return "redirect:/admin";
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "DB 제약 위반으로 수정할 수 없습니다.");
            return "redirect:/admin/teas/" + id + "/edit";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "수정 중 오류: " + e.getClass().getSimpleName());
            return "redirect:/admin/teas/" + id + "/edit";
        }
    }
    //                                                   멘트 관련 POST
    @GetMapping("/ments/new")
    public String newMentForm(Model model) {
        model.addAttribute("ment", new ResultEntity());
        model.addAttribute("mode", "create");
        return "tea/admin-ment-form"; // 별도 폼 템플릿을 쓰는 경우
    }

    @PostMapping("/ments")
    @Transactional
    public String createMent(@RequestParam String result,
                             @RequestParam(required = false) Long moodId,
                             @RequestParam(required = false) Long stateId,
                             RedirectAttributes ra) {
        try {
            if (result == null || result.isBlank()) {
                ra.addFlashAttribute("error", "result(멘트 내용)은 필수입니다.");
                return "redirect:/admin/ments/new";
            }
            ResultEntity r = new ResultEntity();
            r.setResult(result);
            if (moodId != null)  r.setMood(moodRepository.findById(moodId).orElse(null));
            if (stateId != null) r.setState(stateRepository.findById(stateId).orElse(null));
            resultRepository.save(r);

            ra.addFlashAttribute("notice", "멘트가 추가되었습니다.");
            return "redirect:/admin";
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "DB 제약 위반으로 추가 실패");
            return "redirect:/admin/ments/new";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "추가 중 오류: " + e.getClass().getSimpleName());
            return "redirect:/admin/ments/new";
        }
    }
    @PostMapping("/ments/{id}/delete")
    @Transactional
    public String deleteMent(@PathVariable Long id,
                             @RequestParam(value = "adminPassword", required = false) String adminPassword,
                             RedirectAttributes ra) {
        if (!"1111".equals(adminPassword)) {
            ra.addFlashAttribute("error", "관리자 비밀번호가 일치하지 않습니다.");
            return "redirect:/admin";
        }
        try {
            if (!resultRepository.existsById(id)) {
                ra.addFlashAttribute("error", "이미 없거나 존재하지 않는 멘트입니다. (id=" + id + ")");
                return "redirect:/admin";
            }
            resultRepository.deleteById(id);
            ra.addFlashAttribute("notice", "멘트가 삭제되었습니다.");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "연관 데이터 때문에 삭제할 수 없습니다.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "삭제 중 오류: " + e.getClass().getSimpleName());
        }
        return "redirect:/admin";
    }
    @GetMapping("/ments/{id}/edit")
    public String editMentForm(@PathVariable Long id, Model model) {
        ResultEntity ment = resultRepository.findById(id).orElseThrow();
        model.addAttribute("ment", ment);
        model.addAttribute("mode", "edit");
        return "tea/admin-ment-form"; // 파일 경로와 일치
    }
    @PostMapping("/ments/{id}/edit")
    @Transactional
    public String updateMent(@PathVariable Long id,
                             @RequestParam String result,
                             @RequestParam(required = false) Long moodId,
                             @RequestParam(required = false) Long stateId,
                             RedirectAttributes ra) {
        try {
            if (result == null || result.isBlank()) {
                ra.addFlashAttribute("error", "result(멘트 내용)은 필수입니다.");
                return "redirect:/admin/ments/" + id + "/edit";
            }

            ResultEntity r = resultRepository.findById(id).orElseThrow();
            r.setResult(result);
            r.setMood(moodId  != null ? moodRepository.findById(moodId).orElse(null)   : null);
            r.setState(stateId != null ? stateRepository.findById(stateId).orElse(null) : null);
            resultRepository.save(r);

            ra.addFlashAttribute("notice", "멘트가 수정되었습니다.");
            return "redirect:/admin";
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "DB 제약 위반으로 수정할 수 없습니다.");
            return "redirect:/admin/ments/" + id + "/edit";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "수정 중 오류: " + e.getClass().getSimpleName());
            return "redirect:/admin/ments/" + id + "/edit";
        }
    }
    //                       QNA 관련 POST
// QnA 답변 폼 열기
    @GetMapping("/qna/{id}/answer")
    public String qnaAnswerForm(@PathVariable Long id, Model model) {
        QnaEntity q = qnaRepository.findById(id).orElseThrow();
        model.addAttribute("qna", q);
        model.addAttribute("mode", "answer");
        return "tea/admin-qna-form"; // 새 템플릿 (아래 5) 참고)
    }

    // QnA 답변 저장 (등록/수정 공통)
    @PostMapping("/qna/{id}/answer")
    @Transactional
    public String qnaAnswerSubmit(@PathVariable Long id,
                                  @RequestParam String answer,
                                  RedirectAttributes ra) {
        try {
            QnaEntity q = qnaRepository.findById(id).orElseThrow();
            q.setAnswer(answer);
            qnaRepository.save(q);
            ra.addFlashAttribute("notice", "QnA 답변이 저장되었습니다.");
            return "redirect:/admin";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "답변 저장 중 오류: " + e.getClass().getSimpleName());
            return "redirect:/admin/qna/" + id + "/answer";
        }
    }
    @PostMapping("/qna/{id}/delete")
    @Transactional
    public String qnaDelete(@PathVariable Long id,
                            @RequestParam(value = "adminPassword", required = false) String adminPassword,
                            RedirectAttributes ra) {
        if (!"1111".equals(adminPassword)) {
            ra.addFlashAttribute("error", "관리자 비밀번호가 일치하지 않습니다.");
            return "redirect:/admin";
        }
        try {
            if (!qnaRepository.existsById(id)) {
                ra.addFlashAttribute("error", "이미 없거나 존재하지 않는 QnA입니다. (id=" + id + ")");
                return "redirect:/admin";
            }
            qnaRepository.deleteById(id);
            ra.addFlashAttribute("notice", "QnA가 삭제되었습니다.");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "연관 데이터 때문에 삭제할 수 없습니다.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "삭제 중 오류: " + e.getClass().getSimpleName());
        }
        return "redirect:/admin";
    }
}
