document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("memoForm");
  if (!form) return;

  // 서버에서 내려준 today-memo 여부 (true/false)
  const alreadyWritten = form.dataset.already === "true";

  const memoField = document.getElementById("memoField");
  const saveBtn = document.getElementById("saveBtn");
  const notice = document.getElementById("memoNotice");

  function showBlockMessage() {
    if (notice) {
      notice.textContent =
        "오늘 메모는 이미 작성하셨습니다. 수정은 마이페이지의 기록 보관함에서 하실 수 있어요.";
      notice.style.display = "block";
      notice.style.color = "#b91c1c"; // 빨간 안내문
      notice.style.fontWeight = "500";
    } else {
      // 혹시 notice div가 없을 경우 새로 추가
      const newNotice = document.createElement("div");
      newNotice.className = "message";
      newNotice.textContent =
        "오늘 메모는 이미 작성하셨습니다. 수정은 마이페이지의 기록 보관함에서 하실 수 있어요.";
      form.insertBefore(newNotice, form.firstChild);
    }
  }

  if (alreadyWritten) {
    // 입력창 / 버튼 비활성화
    if (memoField) {
      memoField.disabled = true;
      memoField.placeholder =
        "오늘 메모는 이미 작성하셨어요. 수정은 마이페이지의 기록 보관함에서 하실 수 있어요.";
    }
    if (saveBtn) {
      saveBtn.disabled = true;
    }
    showBlockMessage();
  }

  // 폼 제출 막기
  form.addEventListener("submit", function (e) {
    if (alreadyWritten) {
      e.preventDefault();
      alert(
        "오늘 메모는 이미 작성하셨습니다.\n수정하시려면 마이페이지의 기록 보관함에서 확인해주세요."
      );
    }
  });
});
