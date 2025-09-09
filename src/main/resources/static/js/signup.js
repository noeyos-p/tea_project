function showTopError(msg) {
    // 간단: alert 사용 (원하시면 상단 배너 방식으로 바꿔드려요)
    alert(msg);
  }

  function validateSignup(e) {
    // 안전하게 제출 중단은 'return false'로 제어 (onsubmit과 궁합 좋음)
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const nickname = document.getElementById('nickname').value.trim();

    if (!email || !password || !confirmPassword || !nickname) {
      showTopError("필수 항목을 모두 입력해 주세요.");
      return false;
    }
    if (password !== confirmPassword) {
      showTopError("비밀번호가 일치하지 않습니다.");
      // 포커스까지 주면 UX 좋아져요
      document.getElementById('confirmPassword').focus();
      return false;
    }
    if (password.length < 4) {
      showTopError("비밀번호는 4자 이상이어야 합니다.");
      document.getElementById('password').focus();
      return false;
    }
    return true; // 통과 → 제출 허용
  }