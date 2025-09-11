<script>
  const ITEMS_PER_PAGE = 5;

  // 예시 데이터
  const qnaData = [
    { date: "25.12.31", title: "차가 너무 맛이 없어요. 어떻게 해야 할까요?", status: "답변 완료" },
    { date: "25.12.30", title: "차 온도를 잘 모르겠어요", status: "답변 대기" },
    { date: "25.12.29", title: "차 보관 방법 알려주세요", status: "답변 완료" },
    { date: "25.12.28", title: "차 종류별 특징이 궁금해요", status: "답변 대기" },
    { date: "25.12.27", title: "차와 잘 어울리는 음식 추천해주세요", status: "답변 완료" },
    { date: "25.12.26", title: "녹차와 홍차 차이 알려주세요", status: "답변 완료" },
    { date: "25.12.25", title: "차를 끓이는 물 온도는 몇 도가 좋나요?", status: "답변 대기" },
    { date: "25.12.24", title: "차를 하루에 몇 잔 마셔도 되나요?", status: "답변 완료" },
    { date: "25.12.23", title: "차와 커피의 카페인 차이는?", status: "답변 대기" },
    { date: "25.12.22", title: "차 맛을 잘 내는 법 알려주세요", status: "답변 완료" },
    { date: "25.12.21", title: "차 보관할 때 주의할 점이 있나요?", status: "답변 대기" },
    { date: "25.12.20", title: "차 선물 추천 좀 해주세요", status: "답변 완료" }
  ];

  let currentPage = 1;
  let pageCount = Math.ceil(qnaData.length / ITEMS_PER_PAGE);

  function renderList() {
    const listWrap = document.querySelector(".qna");
    listWrap.innerHTML = ""; // 비우기

    const start = (currentPage - 1) * ITEMS_PER_PAGE;
    const end = Math.min(start + ITEMS_PER_PAGE, qnaData.length);
    const pageSlice = qnaData.slice(start, end);

    pageSlice.forEach((item, idx) => {
      const row = document.createElement("div");
      row.className = "qna-list";

      row.innerHTML = `
        <p class="date">${item.date}</p>
        <p class="title">${item.title}</p>
        <p class="${item.status === "답변 완료" ? "answer-c" : "answer-w"}">${item.status}</p>
      `;

      listWrap.appendChild(row);

      // 마지막 항목이 아니면 hr 추가
      if (idx < pageSlice.length - 1) {
        const hr = document.createElement("hr");
        listWrap.appendChild(hr);
      }
    });
  }

  function renderPagination() {
    const prev = document.querySelector(".pagination a[data-role='prev']");
    const next = document.querySelector(".pagination a[data-role='next']");
    const numbers = document.getElementById("page-numbers");

    numbers.innerHTML = "";

    for (let i = 1; i <= pageCount; i++) {
      const a = document.createElement("a");
      a.href = "#";
      a.textContent = i;
      if (i === currentPage) a.classList.add("active");
      a.addEventListener("click", (e) => {
        e.preventDefault();
        goTo(i);
      });
      numbers.appendChild(a);
    }

    // prev/next
    prev.onclick = (e) => {
      e.preventDefault();
      if (currentPage > 1) goTo(currentPage - 1);
    };
    next.onclick = (e) => {
      e.preventDefault();
      if (currentPage < pageCount) goTo(currentPage + 1);
    };
  }

  function goTo(page) {
    currentPage = page;
    renderList();
    renderPagination();
  }

  document.addEventListener("DOMContentLoaded", () => {
    renderList();
    renderPagination();
  });
</script>
