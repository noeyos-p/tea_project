 document.getElementById("delete-btn").addEventListener("click", function(e) {
    const result = confirm("정말 탈퇴하시겠습니까?");
    if(result) {
      alert("탈퇴가 정상적으로 처리되었습니다.");
      // 실제 탈퇴 로직 실행
    } else {
    }
  });