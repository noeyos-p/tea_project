(function () {
  const dow = ['일', '월', '화', '수', '목', '금', '토'];
  const dowRow   = document.getElementById('dowRow');
  const daysGrid = document.getElementById('daysGrid');
  const calTitle = document.getElementById('calTitle');
  const prevBtn  = document.getElementById('prevBtn');
  const nextBtn  = document.getElementById('nextBtn');

  const selectedDateEl = document.getElementById('selectedDate'); // 선택한 날짜 타이틀
  const recordsEl      = document.getElementById('records');      // 메모 카드 컨테이너

  // ---------- util ----------
  const pad2 = n => String(n).padStart(2, '0');
  const toISODate = (d) => `${d.getFullYear()}-${pad2(d.getMonth()+1)}-${pad2(d.getDate())}`;

  function fmtTitle(year, monthIndex) {
    return `${year}년 ${monthIndex + 1}월`;
  }
  function fmtRecordDateKR(date) {
    const y2 = String(date.getFullYear()).slice(2);
    const m  = date.getMonth() + 1;
    const d  = date.getDate();
    return `${y2}년 ${m}월 ${d}일의 기록`;
  }
  function updateRecordHeader(date) {
    if (selectedDateEl) selectedDateEl.textContent = fmtRecordDateKR(date);
  }
  function sameDate(a, b) {
    return a && b &&
      a.getFullYear() === b.getFullYear() &&
      a.getMonth()    === b.getMonth() &&
      a.getDate()     === b.getDate();
  }

  // ---------- API ----------
  async function loadRecords(date) {
    if (!recordsEl) return;
    const iso = toISODate(date);

    try {
      const res = await fetch(`/userdata/memo/list?date=${iso}`, { method: 'GET' });
      if (!res.ok) return;

      const list = await res.json();

      // 결과 새로 그리기: 기존 제거
      recordsEl.innerHTML = '';

      // 결과가 없으면 아무것도 표시하지 않음
      if (!Array.isArray(list) || list.length === 0) return;

      // CHANGED: 최신 1개만 표시 (백엔드 정렬이 되어있지 않다면 현재 들어온 첫 항목만)
      const items = list.slice(0, 1); // 최신 1건만

      // 결과 렌더
      items.forEach(item => {
        const card = document.createElement('div');
        card.className = 'record';
        card.innerHTML = `
          <div class="diary">
            <!-- CHANGED: 날짜 라벨 제거 -->
            <p class="tea">${item.teaName ?? ''}</p>
          </div>
          ${item.memo ? `
            <div class="memo">
              <textarea id="memo-${item.id}" readonly>${item.memo}</textarea>
            </div>` : ``}
          <form action="/userdata/memo/edit/${item.id}" method="get">
            <button type="submit" class="update-btn">수정</button>
          </form>
        `;
        recordsEl.appendChild(card);
      });
    } catch (e) {
      console.error(e);
    }
  }

  // ---------- state ----------
  let view = new Date(); view.setDate(1);                 // 이번 달 1일
  let selected = new Date(); selected.setHours(0,0,0,0);  // 오늘

  // 초기: 헤더 + 오늘 기록 로드
  updateRecordHeader(selected);
  loadRecords(selected);

  // 요일 헤더
  dow.forEach(d => {
    const el = document.createElement('div');
    el.className = 'dow';
    el.textContent = d;
    dowRow.appendChild(el);
  });

  // ---------- 달력 렌더 ----------
  function render() {
    const year  = view.getFullYear();
    const month = view.getMonth();
    calTitle.textContent = fmtTitle(year, month);

    daysGrid.innerHTML = '';

    const firstDay      = new Date(year, month, 1).getDay();
    const daysInMonth   = new Date(year, month + 1, 0).getDate();
    const prevMonthDays = new Date(year, month, 0).getDate();

    for (let i = 0; i < 42; i++) {
      const cell = document.createElement('button');
      cell.type = 'button';
      cell.className = 'day';

      let dayNum, cellDate, isCurrent = true;

      if (i < firstDay) {
        dayNum   = prevMonthDays - firstDay + 1 + i;
        cellDate = new Date(year, month - 1, dayNum);
        cell.classList.add('muted');
        isCurrent = false;
      } else if (i >= firstDay + daysInMonth) {
        dayNum   = i - (firstDay + daysInMonth) + 1;
        cellDate = new Date(year, month + 1, dayNum);
        cell.classList.add('muted');
        isCurrent = false;
      } else {
        dayNum   = i - firstDay + 1;
        cellDate = new Date(year, month, dayNum);
      }

      cell.textContent = dayNum;

      const today = new Date(); today.setHours(0,0,0,0);
      const td = new Date(cellDate); td.setHours(0,0,0,0);

      if (sameDate(td, today))    cell.classList.add('today');
      if (sameDate(td, selected)) cell.classList.add('selected');

      cell.addEventListener('click', () => {
        selected = new Date(cellDate);

        // 선택 표시 갱신
        document.querySelectorAll('.day.selected')
          .forEach(el => el.classList.remove('selected'));
        cell.classList.add('selected');

        // 달 바깥 날짜면 해당 달로 이동 후 재렌더
        if (!isCurrent) {
          view = new Date(cellDate.getFullYear(), cellDate.getMonth(), 1);
          render();
        }

        // 헤더 + 데이터 로드
        updateRecordHeader(selected);
        loadRecords(selected);
      });

      daysGrid.appendChild(cell);
    }
  }

  // ---------- 네비게이션 ----------
  prevBtn.addEventListener('click', () => {
    view.setMonth(view.getMonth() - 1);
    render();
  });
  nextBtn.addEventListener('click', () => {
    view.setMonth(view.getMonth() + 1);
    render();
  });

  render();
})();
