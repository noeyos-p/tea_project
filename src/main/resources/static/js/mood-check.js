// 사용자가 선택할 수 있는 '기분' 항목 목록
const MOOD_ITEMS = [
  "아침에 일어나 상쾌함을 느낀다.",
  "사소한 일에도 답답함을 느낀다",
  "작은 일에도 웃음이 난다",
  "주변 사람들과 대화하는 게 즐겁다",
  "자존감이 낮아진 것 같다",
  "쉽게 눈물이 난다",
  "미래가 불안하게 느껴진다",
  "아무것도 하기 싫다",
  "내 마음이 이해받지 못해 속상하다",
  "혼자 있고 싶을 때가 많다",

  "내 마음이 이해받지 못해 속상하다",
  "혼자 있고 싶을 때가 많다",
  "무기력하게 하루를 보낸다",
  "스트레스를 자주 받는다",
  "과거 일을 떠올리면 속상하다",
  "과거가 그리워 자주 생각난다",
  "주변 상황이 답답하게 느껴진다",
  "하루하루 버티는 느낌이다",
  "아침에 일어나 상쾌함을 느낀다.",
  "오늘 하루가 기대된다",

  "아침에 일어나 상쾌함을 느낀다.",
  "아침에 일어나 상쾌함을 느낀다.",
  "오늘 하루가 기대된다",
  "오늘 하루가 기대된다",
  "작은 일에도 웃음이 난다",
  "웃을 일이 별로 없다",
  "주변의 위로도 크게 도움이 되지 않는다",
  "집중이 잘 되지 않는다",
  "외롭다고 느낀다",
  "사람들과의 관계가 힘들다고 느낀다",
];

// DOM 요소 캐싱: 매번 querySelector를 호출하지 않도록 변수에 보관해 성능/가독성 개선
const grid = document.getElementById('moodGrid');           // 체크박스들이 들어갈 그리드 컨테이너
const form = document.getElementById('moodForm');            // 제출을 담당하는 폼
const submitBtn = document.getElementById('moodSubmit');     // 제출 버튼 (현재 로직에서는 직접 제어는 안 하지만 확장 여지)
const countEl = document.querySelector('.mood-count');       // "(선택수/MAX)" 카운트가 표시되는 엘리먼트
const MAX = 5;                                               // 최대 선택 개수 제한

// 초기 렌더링 함수: MOOD_ITEMS를 기반으로 체크박스 라벨 세트를 만들어 그리드에 삽입
function render() {
  // 기존 렌더링 내용 제거(초기화)
  grid.innerHTML = '';

  // 항목마다 label + input[type=checkbox] + span 텍스트 구조를 생성
  MOOD_ITEMS.forEach((text, i) => {
    const id = `mood_${i}`;              // 각 input에 유니크 id 부여(라벨-인풋 연결용)
    const item = document.createElement('label'); // 접근성/클릭 편의성 위해 label로 감싸기
    item.className = 'mood-item';
    // template literal 내부에서 굳이 \" 로 이스케이프할 필요는 없지만, 현재 로직을 유지합니다.
    item.innerHTML = `
      <input type=\"checkbox\" id=\"${id}\" name=\"mood\" value=\"${text}\">
      <span class=\"mood-label\">${text}</span>
    `;
    grid.appendChild(item);
  });

  // 동적 생성된 체크박스에 이벤트 연결
  attachHandlers();

  // 초기 상태(선택 수, 제한 여부 등) 동기화
  updateState();
}

// 체크박스 변경(change) 이벤트를 모든 항목에 바인딩
// - 사용자가 체크/언체크할 때마다 상태를 재계산해 UI 반영
function attachHandlers() {
  grid.querySelectorAll('input[type="checkbox"]').forEach(chk => {
    chk.addEventListener('change', updateState);
  });
}

// 현재 선택 상태를 계산하고, UI/속성을 업데이트
function updateState() {
  // 모든 체크박스 수집
  const all = Array.from(grid.querySelectorAll('input[type="checkbox"]'));
  // 체크된 체크박스만 필터링
  const checked = all.filter(c => c.checked);

  // 선택된 항목에 스타일을 주기 위한 클래스 토글
  // - .mood-item.is-checked 등에 CSS를 주면 '선택됨' 시각 효과를 만들 수 있습니다.
  all.forEach(input => {
    const wrap = input.closest('.mood-item');
    wrap.classList.toggle('is-checked', input.checked);
  });

  // 최대 개수에 도달했는지 여부
  const capped = checked.length >= MAX;

  // 그리드 레벨 클래스 토글: .mood-grid.is-capped에 '더는 선택 불가' 스타일을 줄 수 있음(예: 커서, 안내문 등)
  grid.classList.toggle('is-capped', capped);

  // capped일 때, 아직 선택되지 않은 나머지 체크박스를 비활성화하여 추가 선택을 막음
  // - 이미 체크된 항목은 계속 해제할 수 있어야 하므로 disabled=false 유지
  all.forEach(input => {
    if (!input.checked) input.disabled = capped; else input.disabled = false;
  });

  // (현재/최대) 텍스트 갱신
  // - 접근성을 강화하려면 span에 aria-live="polite"를 두어도 좋습니다(필수는 아님).
  countEl.textContent = `(${checked.length}/${MAX})`;
}

// 폼 제출 시 검증 및 페이지 이동
form.addEventListener('submit', (e) => {
  // 현재 선택된 체크박스 개수 계산
  const selectedCount = grid.querySelectorAll('input[type="checkbox"]:checked').length;

  // 정확히 MAX개를 선택해야만 통과
  if (selectedCount !== MAX) {
    e.preventDefault();                 // 기본 제출(이동) 동작 취소
    alert("총 5개를 선택해야 합니다."); // 사용자 안내
    return;
  }

  // 검증 통과 시 다음 페이지로 이동
  // - 필요하다면 쿼리스트링/POST 제출 등으로 선택값을 전달하는 로직을 추가할 수 있습니다.
  window.location.href = "/next.html";
});

// 최초 1회 렌더링 호출
render();
