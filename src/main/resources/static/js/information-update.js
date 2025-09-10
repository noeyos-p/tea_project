document.addEventListener('DOMContentLoaded', () => {
      const form = document.getElementById('signupForm');
      const pw = document.getElementById('pw');
      const cpw = document.getElementById('confirm-pw');

      if (!form) return;

      form.addEventListener('submit', (e) => {
        const p = pw.value.trim();
        const c = cpw.value.trim();

        // 비밀번호를 변경하려는 경우에만 클라이언트 체크
        if (p.length > 0 || c.length > 0) {
          if (p.length < 4) {
            e.preventDefault();
            alert('비밀번호는 4자 이상이어야 합니다.');
            pw.focus();
            return;
          }
          if (p !== c) {
            e.preventDefault();
            alert('비밀번호가 일치하지 않습니다.');
            cpw.focus();
            return;
          }
        }
        // 닉네임 중복은 서버에서 최종 검증. (existsByNickname)
      });
    });
    document.addEventListener('DOMContentLoaded', () => {
      const body = document.body;
      const msg = body.dataset.message;
      const err = body.dataset.error;

      if (err && err.trim()) {
        alert(err);
      } else if (msg && msg.trim()) {
        alert(msg);
      }
    });
