// DOM 트리가 모두 파싱된 뒤 실행 (이미지/스타일 로딩까지 기다릴 필요 X)
document.addEventListener("DOMContentLoaded", () => {
  // 가로 스크롤이 적용된 뷰포트 요소 (#teaViewport)
  const viewport = document.getElementById("teaViewport");
  // 좌/우 네비게이션 버튼
  const prevBtn  = document.getElementById("ptPrev");
  const nextBtn  = document.getElementById("ptNext");

  // ---------------------------------------------
  // STEP 계산 함수: "차 버튼 5개" 단위로 넘기기
  //  - 첫 번째 .tea-btn의 실제 렌더링 너비(offsetWidth)를 읽어옴
  //  - CSS에서 버튼 사이 gap이 12px이므로, 그 값을 더해 한 칸의 총 폭으로 계산
  //  - 해당 폭 × 5(=원하시는 이동 단위) 로 스크롤 step을 반환
  //  - 버튼이 없을 경우(비어있을 때)는 기본값 240px 사용
  // ---------------------------------------------
  function getStep() {
    // 차 버튼 하나의 실제 너비 계산
    const firstBtn = viewport.querySelector(".tea-btn");
    if (!firstBtn) return 240; // 기본값(폴백). 버튼이 없으면 픽셀 단위로라도 움직일 수 있게.
    const btnWidth = firstBtn.offsetWidth + 12; // 버튼 너비 + gap(12px)  ※CSS의 .tea-menu { gap: 12px; } 와 반드시 일치시킬 것
    return btnWidth * 5; // 버튼 5개 크기만큼
  }

  // ---------------------------------------------
  // 현재 가로 스크롤 가능한 "최대 스크롤 값"을 계산
  //  - scrollWidth: 컨텐츠 총 가로 길이
  //  - clientWidth: 현재 보이는 영역의 가로 길이
  //  → 최대 스크롤 가능 범위 = (총길이 - 보이는길이)
  // ---------------------------------------------
  const maxScroll = () =>
    Math.max(0, viewport.scrollWidth - viewport.clientWidth);

  // ---------------------------------------------
  // 좌/우 화살표 버튼의 표시/숨김 상태를 갱신
  //  - 맨 왼쪽이면 prev 숨김
  //  - 맨 오른쪽이면 next 숨김
  //  - 소수점/반올림 오차를 대비해 끝 판정에 -1 보정
  //  - maxScroll() === 0 → 스크롤할 컨텐츠가 없으면 next도 숨김
  //  - CSS에서 .pt-nav.hidden { display:none; } 로 숨김 처리해야 깔끔
  // ---------------------------------------------
  function updateButtons(){
    const atStart = viewport.scrollLeft <= 0;
    const atEnd   = viewport.scrollLeft >= maxScroll() - 1; // -1은 미세 오차 보정
    prevBtn.classList.toggle("hidden", atStart);
    nextBtn.classList.toggle("hidden", atEnd || maxScroll() === 0);
  }

  // ---------------------------------------------
  // 클릭 시 왼쪽/오른쪽으로 "버튼 5개 분량"만큼 스크롤
  //  - behavior: "smooth" 로 부드럽게 이동
  // ---------------------------------------------
  prevBtn.addEventListener("click", () => {
    viewport.scrollBy({ left: -getStep(), behavior: "smoot
