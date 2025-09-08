const form = document.getElementById("signupForm");
const password = document.getElementById("pw");
const confirmPassword = document.getElementById("confirm-pw");

form.addEventListener("submit", function (e) {
  if (password.value !== confirmPassword.value) {
    e.preventDefault(); // 제출 막기
    alert("비밀번호가 일치하지 않습니다.");
  }
});