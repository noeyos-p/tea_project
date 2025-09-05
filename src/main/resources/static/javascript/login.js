<script>
  const emailInput = document.getElementById('email');
  const emailHelp  = document.getElementById('emailHelp');

  function showEmailError(msg) {
    emailHelp.textContent = msg;
    emailHelp.classList.add('show');
    emailInput.classList.add('error-input');
  }

  function clearEmailError() {
    emailHelp.textContent = '';
    emailHelp.classList.remove('show');
    emailInput.classList.remove('error-input');
  }

  // 입력 포커스 아웃 시 간단 형식검사 (원하시면 제거 가능)
  emailInput.addEventListener('blur', () => {
    const v = emailInput.value.trim();
    const ok = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v);
    if (v && !ok) {
      showEmailError('입력하신 이메일이 올바르지 않습니다.');
    } else {
      clearEmailError();
    }
  });
</script>
