// DOMContentLoaded 이벤트가 발생하면, 즉 HTML이 모두 로드되고
// DOM 트리가 준비되었을 때 안의 함수를 실행합니다.
// (이미지·CSS 같은 리소스까지 다 불러올 필요는 없음)
document.addEventListener("DOMContentLoaded", () => {

  // 모든 증상 버튼(.pill 클래스)을 가져옵니다. → NodeList 형태
  const pills = document.querySelectorAll(".pill");

  // "선택 완료" 버튼을 가져옵니다. id="submit"
  const submitBtn = document.getElementById("submit");

  // 현재 어떤 버튼이 선택되었는지 저장할 변수 (처음엔 null)
  let selected = null;

  // 각각의 pill 버튼에 클릭 이벤트를 등록합니다.
  pills.forEach(pill => {
    pill.addEventListener("click", () => {
      // 1) 다른 모든 버튼의 선택 상태를 해제합니다.
      pills.forEach(p => {
        p.classList.remove("selected");            // 시각적 선택 스타일 제거
        p.setAttribute("aria-pressed", "false");   // 접근성 속성 false로 변경
      });

      // 2) 현재 클릭한 버튼을 선택 상태로 바꿉니다.
      pill.classList.add("selected");              // 선택 스타일 추가
      pill.setAttribute("aria-pressed", "true");   // 보조기기용 속성 true로 변경
      selected = pill.textContent.trim();          // 선택된 텍스트를 변수에 저장
    });
  });

  // "선택 완료" 버튼을 눌렀을 때 실행되는 코드
  submitBtn.addEventListener("click", () => {
    if (selected) {
      // 어떤 버튼이든 선택된 상태라면 → 다음 페이지로 이동
      // TODO: 실제 이동할 경로를 "/nextpage" 대신 원하는 URL로 교체하세요
      window.location.href = "/nextpage";
    } else {
      // 선택된 버튼이 없다면 → 안내 메시지 출력
      alert("원하시는 항목을 하나 선택해 주세요.");
    }
  });
});
