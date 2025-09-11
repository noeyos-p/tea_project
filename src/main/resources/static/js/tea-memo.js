document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("memoForm");
  if (!form) return;

  const alreadyWritten = form.dataset.already === "true";
  const memoField = document.getElementById("memoField");

  // ✅ 항상 메모 칸은 비워두기
  if (memoField) {
    memoField.value = "";
  }

  // ✅ 폼 제출 이벤트
  form.addEventListener("submit", function (e) {
    if (alreadyWritten) {
      e.preventDefault(); // 서버로 안 보냄
      alert("이미 저장된 기록이 있습니다.\n수정하시려면 마이페이지로 이동해주세요.");
    }
    // else → 그냥 submit → 서버가 DB 저장 처리 후 같은 memo 페이지로 redirect
  });
});
