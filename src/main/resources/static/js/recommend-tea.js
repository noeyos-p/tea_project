document.addEventListener("DOMContentLoaded", () => {
  const viewport = document.getElementById("teaViewport");
  const prevBtn  = document.getElementById("ptPrev");
  const nextBtn  = document.getElementById("ptNext");
  const teaMenu  = document.getElementById("teaMenu");
  const saveForm = document.getElementById("saveForm");
  const selectedTeaInput = document.getElementById("selectedTeaId");

  const alreadyToday = String(saveForm?.dataset?.already) === "true";
  const currentTeaId = (saveForm?.dataset?.currentTeaId || "").trim();

  // 버튼 5개 단위 스크롤
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

  // tea 선택 표시
  function showTea(teaId) {
    document.querySelectorAll(".tea-detail").forEach(el => el.style.display = "none");
    const detail = document.querySelector(`.tea-detail[data-tea-id="${teaId}"]`);
    if (detail) detail.style.display = "block";

    teaMenu.querySelectorAll(".tea-btn").forEach(btn => {
      const active = btn.dataset.teaId === String(teaId);
      btn.classList.toggle("is-active", active);
      btn.setAttribute("aria-selected", active ? "true" : "false");
    });

    selectedTeaInput.value = teaId;
  }

  // 네비게이션 버튼
  prevBtn.addEventListener("click", () => {
    viewport.scrollBy({ left: -getStep(), behavior: "smooth" });
  });
  nextBtn.addEventListener("click", () => {
    viewport.scrollBy({ left:  getStep(), behavior: "smooth" });
  });

  viewport.addEventListener("scroll", updateButtons);
  window.addEventListener("resize", updateButtons);

  // 메뉴 클릭
  teaMenu.addEventListener("click", (e) => {
    const btn = e.target.closest(".tea-btn");
    if (!btn) return;
    const teaId = btn.dataset.teaId;
    if (teaId) showTea(teaId);
  });

  // 첫 번째 차 자동 선택
  (function selectFirstTeaByDefault() {
    const firstBtn = teaMenu.querySelector(".tea-btn");
    if (!firstBtn) return;
    showTea(firstBtn.dataset.teaId);
  })();

  // 초기 버튼 상태
  updateButtons();

  // ✅ 차 선택 버튼 confirm 처리
  document.querySelectorAll(".choose-btn").forEach(btn => {
    btn.addEventListener("click", (e) => {
      e.preventDefault();
      const teaId = selectedTeaInput.value;

      if (alreadyToday && currentTeaId && currentTeaId !== teaId) {
        const yes = window.confirm("이미 선택된 차가 있습니다.\n예: 저장하고 이동\n아니요: 저장 없이 이동");
        if (yes) {
          saveForm.submit();
        } else {
          // 👉 preview가 필요 없다면 여기서 그냥 skipSave 히든필드 추가해서 submit
          let skipField = saveForm.querySelector("input[name='skipSave']");
          if (!skipField) {
            skipField = document.createElement("input");
            skipField.type = "hidden";
            skipField.name = "skipSave";
            saveForm.appendChild(skipField);
          }
          skipField.value = "true";
          saveForm.submit();
        }
      } else {
        saveForm.submit();
      }
    });
  });
});
