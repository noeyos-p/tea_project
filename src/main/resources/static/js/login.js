/*************************************************
 * 로그인 폼 클라이언트 검증 스크립트
 * - 이메일: 비어있음/형식 오류 모두 차단
 * - 비밀번호: 비어있음/최소 길이 미만 차단
 * - blur 시 즉각 피드백, submit 시 최종 검증
 *************************************************/

/* ========== 요소 참조 ========== */
// 이메일 입력 필드와 에러 메시지 표시 영역
const emailInput = document.getElementById('email');
const emailHelp  = document.getElementById('emailHelp');

// 비밀번호 입력 필드와 에러 메시지 표시 영역
const passwordInput = document.getElementById('password');
const passwordHelp  = document.getElementById('passwordHelp');

// 로그인 폼 (문서 내 첫 번째 <form>을 가져옴; 폼이 여러 개라면 더 구체적인 셀렉터 사용 권장)
const form = document.querySelector('form');


/* ========== 에러 메시지 유틸리티 (이메일) ========== */
/**
 * 이메일 입력과 관련된 에러 메시지를 화면에 표시
 * - textContent로 메시지 바인딩
 * - .show / .error-input 클래스로 가시적 상태 및 입력 필드 에러 스타일링
 */
function showEmailError(msg) {
  emailHelp.textContent = msg;
  emailHelp.classList.add('show');
  emailInput.classList.add('error-input');
}

/** 이메일 관련 에러 상태 초기화 */
function clearEmailError() {
  emailHelp.textContent = '';
  emailHelp.classList.remove('show');
  emailInput.classList.remove('error-input');
}


/* ========== 에러 메시지 유틸리티 (비밀번호) ========== */
/**
 * 비밀번호 입력과 관련된 에러 메시지를 화면에 표시
 */
function showPasswordError(msg) {
  passwordHelp.textContent = msg;
  passwordHelp.classList.add('show');
  passwordInput.classList.add('error-input');
}

/** 비밀번호 관련 에러 상태 초기화 */
function clearPasswordError() {
  passwordHelp.textContent = '';
  passwordHelp.classList.remove('show');
  passwordInput.classList.remove('error-input');
}


/* ========== 실시간(blur) 검증 ========== */
/**
 * 이메일 입력 칸에서 포커스를 잃었을 때 실행
 * 1) 값 공백 제거 → 빈 값 여부 확인
 * 2) 정규식으로 이메일 형식 검증
 *   - ^[^\s@]+  : 공백/ @ 제외 1글자 이상 (로컬파트)
 *   - @         : 리터럴 @
 *   - [^\s@]+   : 공백/ @ 제외 1글자 이상 (도메인)
 *   - \.        : 리터럴 점(.)
 *   - [^\s@]+$  : 공백/ @ 제외 1글자 이상 (최상위 도메인)
 */
emailInput.addEventListener('blur', () => {
  const v = emailInput.value.trim();
  const ok = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v);

  if (!v) {
    // 아무것도 입력하지 않음
    showEmailError('이메일을 입력해주세요.');
  } else if (!ok) {
    // 입력은 했지만 형식이 맞지 않음
    showEmailError('입력하신 이메일이 올바르지 않습니다.');
  } else {
    // 정상 입력
    clearEmailError();
  }
});

/**
 * 비밀번호 입력 칸에서 포커스를 잃었을 때 실행
 * - 공백 제거 후 빈 값/최소 길이(여기서는 4 초과) 확인
 */
passwordInput.addEventListener('blur', () => {
  const v = passwordInput.value.trim();

  if (!v) {
    showPasswordError('비밀번호를 입력해주세요.');
  } else if (v.length <= 4) {
    // 현재 조건: 길이가 4 이하일 때 에러 → 즉, 5자리부터 통과
    showPasswordError('비밀번호는 최소 4자리 이상이어야 합니다.');
  } else {
    clearPasswordError();
  }
});


/* ========== 제출(submit) 시 최종 검증 ========== */
/**
 * 폼 제출 시 모든 필드 유효성 재검사
 * - 하나라도 실패하면 e.preventDefault()로 제출 차단
 * - blur 검사에서 통과했더라도 사용자가 재편집했을 수 있으므로 재확인
 */
form.addEventListener('submit', e => {
  const email = emailInput.value.trim();
  const pass  = passwordInput.value.trim();
  const emailOk = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);

  let valid = true;

  // 이메일 최종 검사
  if (!email) {
    showEmailError('이메일을 입력해주세요.');
    valid = false;
  } else if (!emailOk) {
    showEmailError('입력하신 이메일이 올바르지 않습니다.');
    valid = false;
  } else {
    clearEmailError();
  }

  // 비밀번호 최종 검사
  if (!pass) {
    showPasswordError('비밀번호를 입력해주세요.');
    valid = false;
  } else if (pass.length <= 4) {
    // 현재 조건: 4 이하 → 실패 (즉, 5자리 이상 통과)
    showPasswordError('비밀번호는 최소 4자리 이상이어야 합니다.');
    valid = false;
  } else {
    clearPasswordError();
  }

  // 하나라도 실패 시 폼 제출 막기
  if (!valid) {
    e.preventDefault();
  }
});
