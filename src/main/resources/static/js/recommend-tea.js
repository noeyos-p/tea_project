// DOM 트리가 모두 파싱된 뒤 실행
document.addEventListener("DOMContentLoaded", () => {
  const viewport = document.getElementById("teaViewport");
  const prevBtn  = document.getElementById("ptPrev");
  const nextBtn  = document.getElementById("ptNext");
  const teaMenu  = document.getElementById("teaMenu");
  const saveForm = document.getElementById("saveForm");
  const selectedTeaInput = document.getElementById("selectedTeaId");

  // 버튼 5개 단위로 스크롤 (gap 12px 가정: CSS .tea-menu { gap:12px; })
  function getStep() {
    const firstBtn = teaMenu.querySelector(".tea-btn");
    if (!firstBtn) return 240;
    const gap = 12;
    return (firstBtn.offsetWidth + gap) * 5;
  }

  const maxScroll = () => Math.max(0, viewport.scrollWidth - viewport.clientWidth);

  function updateButtons() {
    const atStart = viewport.scrollLeft <= 0;
    const atEnd = viewport.scrollLeft >= maxScroll() - 1;
    prevBtn.classList.toggle("hidden", atStart);
    nextBtn.classList.toggle("hidden", atEnd || maxScroll() === 0);
  }

  // tea 선택 표시 & hidden input 세팅
  function showTea(teaId) {
    // 모든 디테일 숨김
    document.querySelectorAll(".tea-detail").forEach(el => el.style.display = "none");

    // 해당 디테일 표시
    const detail = document.querySelector(`.tea-detail[data-tea-id="${teaId}"]`);
    if (detail) detail.style.display = "block";

    // 버튼 active 표시
    teaMenu.querySelectorAll(".tea-btn").forEach(btn => {
      const active = btn.dataset.teaId === String(teaId);
      btn.classList.toggle("is-active", active);
      btn.setAttribute("aria-selected", active ? "true" : "false");
    });

    // hidden input 값 설정
    selectedTeaInput.value = teaId;
  }

  // 좌/우 네비게이션
  prevBtn.addEventListener("click", () => {
    viewport.scrollBy({ left: -getStep(), behavior: "smooth" });
  });
  nextBtn.addEventListener("click", () => {
    viewport.scrollBy({ left:  getStep(), behavior: "smooth" });
  });

  // 스크롤/리사이즈 시 버튼 상태 갱신
  viewport.addEventListener("scroll", updateButtons);
  window.addEventListener("resize", updateButtons);

  // 메뉴 클릭(이벤트 위임)
  teaMenu.addEventListener("click", (e) => {
    const btn = e.target.closest(".tea-btn");
    if (!btn) return;
    const teaId = btn.dataset.teaId;
    if (teaId) showTea(teaId);
  });

  // ✅ 첫 번째 차 자동 선택
  (function selectFirstTeaByDefault() {
    const firstBtn = teaMenu.querySelector(".tea-btn");
    if (!firstBtn) return;
    showTea(firstBtn.dataset.teaId);
  })();

  // 초기 버튼 상태
  updateButtons();

  // (선택) 제출 전 검증: teaId 없으면 막기
  saveForm.addEventListener("submit", (e) => {
    if (!selectedTeaInput.value) {
      e.preventDefault();
      alert("차를 먼저 선택해주세요.");
    }
  });
});
