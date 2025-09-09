let currentPage = 1;
        const itemsPerPage = 5;

        // 실제 Q&A 데이터 (예시로 12개 넣음)
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

        function renderPage(page) {
            const qna = document.querySelector(".qna");
            qna.innerHTML = ""; // 초기화

            // 시작/끝 인덱스 구하기
            const start = (page - 1) * itemsPerPage;
            const end = start + itemsPerPage;
            const pageData = qnaData.slice(start, end);

            pageData.forEach(item => {
                const statusClass = item.status === "답변 완료" ? "answer-c" : "answer-w";
                qna.innerHTML += `
      <div class="qna-list">
        <p class="date">${item.date}</p>
        <p class="title">${item.title}</p>
        <p class="${statusClass}">${item.status}</p>
      </div>
      <hr>
    `;
            });

            renderPagination();
        }

        function renderPagination() {
            const pageCount = Math.ceil(qnaData.length / itemsPerPage);
            const pageNumbers = document.getElementById("page-numbers");
            pageNumbers.innerHTML = "";

            for (let i = 1; i <= pageCount; i++) {
                pageNumbers.innerHTML += `
      <a href="#" onclick="changePage(${i})"
         style="margin:0 5px; ${i === currentPage ? 'font-weight:bold;color:#BFAE9F;' : ''}">
         ${i}
      </a>
    `;
            }
        }

        function changePage(page) {
            const pageCount = Math.ceil(qnaData.length / itemsPerPage);
            if (page < 1 || page > pageCount) return;
            currentPage = page;
            renderPage(currentPage);
        }

        // 첫 페이지 렌더링
        renderPage(currentPage);