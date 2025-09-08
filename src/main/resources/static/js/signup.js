(() => {
  "use strict";
  /**
   * IIFE(즉시 실행 함수 표현식)로 스크립트를 감쌉니다.
   * - 전역 변수/함수 오염 방지
   * - 이 파일에 정의된 모든 함수와 변수는 외부에서 접근 불가
   * 'use strict' 모드 활성화:
   * - 암묵적 전역 변수 생성 방지
   * - 더 엄격한 오류 검사로 버그를 조기에 발견 가능
   */

  // id로 요소를 가져오는 간단한 헬퍼 함수
  // 예: $id("email") → document.getElementById("email")
  const $id = (x) => document.getElementById(x);

  /**
   * CSRF 토큰 가져오기
   * - 스프링 시큐리티는 CSRF 보호를 기본적으로 활성화합니다.
   * - 이때 POST 요청 시 토큰이 없으면 403 Forbidden 발생
   *
   * 우선순위:
   * 1) <meta name="_csrf">, <meta name="_csrf_header"> 태그에서 토큰/헤더 이름 읽기
   *    (타임리프 layout에 보통 삽입)
   * 2) fallback: <input type="hidden" name="...csrf..."> 값 읽기
   */
  function getCsrf() {
    const t = document.querySelector('meta[name="_csrf"]')?.content;
    const h = document.querySelector('meta[name="_csrf_header"]')?.content;
    if (t && h) return { header: h, token: t };

    const hidden = document.querySelector('input[type="hidden"][name*="csrf"]');
    if (hidden) return { header: "X-CSRF-TOKEN", token: hidden.value };

    return null; // 둘 다 없으면 서버 설정에 따라 요청이 거절될 수 있음
  }

  /**
   * 상단 에러 메시지 표시
   * - 현재 HTML에는 에러 메시지 박스가 없으므로 alert()로 대체
   * - 필요하면 <div id="formError"></div> 를 HTML에 추가해서 여기서 메시지를 넣도록 수정 가능
   */
  function showTopError(msg) {
    alert(msg);
  }

  /**
   * 폼 제출 이벤트 핸들러
   * - 기본 submit 동작(페이지 새로고침) 방지
   * - 입력값 검증 후 서버로 비동기 요청(fetch) 전송
   */
  async function onSubmit(e) {
    e.preventDefault(); // 기본 동작 방지

    // 현재 submit된 form 객체
    const form = e.currentTarget;

    // 각 입력 필드 값 읽기 (올려주신 HTML의 id 속성과 동일)
    const email = $id("email")?.value.trim() || "";
    const password = $id("password")?.value || "";
    const confirmPassword = $id("confirmPassword")?.value || "";
    const nickname = $id("nickname")?.value.trim() || "";

    // ===== 1단계: 간단한 프론트엔드 검증 =====
    if (!email || !password || !confirmPassword || !nickname) {
      return showTopError("필수 항목을 모두 입력해 주세요.");
    }
    if (password !== confirmPassword) {
      return showTopError("비밀번호가 일치하지 않습니다.");
    }
    if (password.length < 4) {
      return showTopError("비밀번호는 4자 이상이어야 합니다.");
    }

    // ===== 2단계: 중복 제출 방지 =====
    const btn = form.querySelector('button[type="submit"]');
    if (btn) btn.disabled = true; // 버튼 잠금

    try {
      // 요청 헤더 구성
      const headers = { "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8" };
      const csrf = getCsrf(); // CSRF 토큰 가져오기
      if (csrf?.token) headers[csrf.header] = csrf.token;

      // 요청 body (confirmPassword는 서버 전송하지 않음)
      const body = new URLSearchParams({
        email,
        password,
        nickname
      }).toString();

      // ===== 3단계: 서버에 POST 요청 =====
      const res = await fetch(form.getAttribute("action") || "/signup", {
        method: "POST",
        headers,
        body,
        credentials: "same-origin" // JSESSIONID 같은 쿠키 포함
      });

      // ===== 4단계: 응답 처리 =====
      if (res.ok) {
        // 성공 시: 로그인 페이지로 이동
        location.href = "/login?signup=success";
        return;
      }

      // 실패 시: 상태 코드에 따라 메시지 구분
      showTopError(
        res.status === 409
          ? "이미 등록된 이메일입니다."
          : "회원가입에 실패했습니다. 다시 시도해 주세요."
      );
    } catch {
      // 네트워크 오류 (서버 접속 불가 등)
      showTopError("네트워크 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
    } finally {
      // ===== 5단계: 항상 버튼 잠금 해제 =====
      if (btn) btn.disabled = false;
    }
  }

  /**
   * 초기화 함수
   * - .signup-card 내부의 form 요소를 찾아 submit 이벤트 핸들러 등록
   */
  function init() {
    const form = document.querySelector(".signup-card form");
    if (!form) return; // 폼이 없으면 아무것도 안 함
    form.addEventListener("submit", onSubmit);
  }

  /**
   * 문서 로딩 상태에 따라 init() 실행 시점 조정
   * - 아직 DOM이 준비되지 않았다면 DOMContentLoaded 이벤트 때 실행
   * - 이미 준비된 경우 즉시 실행
   */
  document.readyState === "loading"
    ? document.addEventListener("DOMContentLoaded", init)
    : init();
})();
