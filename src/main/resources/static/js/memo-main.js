(function () {
      const dow = ['일', '월', '화', '수', '목', '금', '토'];
      const dowRow = document.getElementById('dowRow');
      const daysGrid = document.getElementById('daysGrid');
      const calTitle = document.getElementById('calTitle');
      const prevBtn = document.getElementById('prevBtn');
      const nextBtn = document.getElementById('nextBtn');

      const selectedDateEl = document.getElementById('selectedDate');
  function fmtTitle(year, monthIndex) {
    return `${year}년 ${monthIndex + 1}월`;
  }

  function fmtRecordDateKR(date) {
    const y2 = String(date.getFullYear()).slice(2); // '2025' -> '25'
    const m = date.getMonth() + 1;
    const d = date.getDate();
    return `${y2}년 ${m}월 ${d}일의 기록`;
  }
  function updateRecordHeader(date) {
    if (!selectedDateEl) return;
    selectedDateEl.textContent = fmtRecordDateKR(date);
  }

  function sameDate(a, b) {
    return a && b &&
      a.getFullYear() === b.getFullYear() &&
      a.getMonth() === b.getMonth() &&
      a.getDate() === b.getDate();
  }

  // 초기 뷰: 이번 달 1일
  let view = new Date();
  view.setDate(1);

  let selected = new Date();
  selected.setHours(0, 0, 0, 0);

  updateRecordHeader(selected);

  dow.forEach(d => {
    const el = document.createElement('div');
    el.className = 'dow';
    el.textContent = d;
    dowRow.appendChild(el);
  });

      function fmtTitle(year, monthIndex) {
        return `${year}년 ${monthIndex + 1}월`;
      }

      function sameDate(a, b) {
        return a && b && a.getFullYear() === b.getFullYear() && a.getMonth() === b.getMonth() && a.getDate() === b.getDate();
      }

      function render() {
        const year = view.getFullYear();
        const month = view.getMonth();
        calTitle.textContent = fmtTitle(year, month);

        daysGrid.innerHTML = '';

        const firstDay = new Date(year, month, 1).getDay();
        const daysInMonth = new Date(year, month + 1, 0).getDate();
        const prevMonthDays = new Date(year, month, 0).getDate();

        for (let i = 0; i < 42; i++) {
          const cell = document.createElement('button');
          cell.type = 'button';
          cell.className = 'day';

          let dayNum, cellDate, isCurrent = true;

          if (i < firstDay) {
            dayNum = prevMonthDays - firstDay + 1 + i;
            cell.classList.add('muted');
            cellDate = new Date(year, month - 1, dayNum);
            isCurrent = false;
          } else if (i >= firstDay + daysInMonth) {
            dayNum = i - (firstDay + daysInMonth) + 1;
            cell.classList.add('muted');
            cellDate = new Date(year, month + 1, dayNum);
            isCurrent = false;
          } else {
            dayNum = i - firstDay + 1;
            cellDate = new Date(year, month, dayNum);
          }

          cell.textContent = dayNum;

          const today = new Date();
          today.setHours(0, 0, 0, 0);
          const td = new Date(cellDate);
          td.setHours(0, 0, 0, 0);
          if (sameDate(td, today)) cell.classList.add('today');
          if (selected && sameDate(td, selected)) cell.classList.add('selected');

          cell.addEventListener('click', () => {
            selected = new Date(cellDate);

            document.querySelectorAll('.day.selected').forEach(el => el.classList.remove('selected'));
            cell.classList.add('selected');

            if (!isCurrent) {
              view = new Date(cellDate.getFullYear(), cellDate.getMonth(), 1);
              render();
            }
          });

          daysGrid.appendChild(cell);
        }
      }

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