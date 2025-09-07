// 차(tea) 데이터를 담은 배열 (순위, 이름, 이미지 경로)
const PT_DATA = [
  { rank: 1, name: "카모마일", img: "/img/background.jpg" },
  { rank: 2, name: "라벤더", img: "/img/background.jpg" },
  { rank: 3, name: "루이보스", img: "/img/background.jpg" },
  { rank: 4, name: "레몬밤", img: "/img/background.jpg" },
  { rank: 5, name: "녹차", img: "/img/background.jpg" },
  { rank: 6, name: "홍차", img: "/img/background.jpg" },
  { rank: 7, name: "페퍼민트", img: "/img/background.jpg" },
  { rank: 8, name: "쟈스민", img: "/img/background.jpg" },
  { rank: 9, name: "생강차", img: "/img/background.jpg" },
  { rank: 10, name: "우롱차", img: "/img/background.jpg" },
];

// 주요 DOM 요소 가져오기
const scrollBox = document.getElementById('ptScroll'); // 카드들을 감싸는 스크롤 박스
const prevBtn   = document.getElementById('ptPrev');   // "이전" 버튼
const nextBtn   = document.getElementById('ptNext');   // "다음" 버튼

/**
 * 카드(article 요소)를 생성하는 함수
 * @param {Object} param0 - 차 데이터 (rank, name, img)
 * @returns {HTMLElement} article 요소
 */
function createCard({ rank, name, img }) {
  const card = document.createElement('article');
  card.className = 'pt-card'; // 카드 스타일 클래스
  // 카드 내부 구조: 순위, 이미지, 이름
  card.innerHTML = `
    <span class="pt-rank">${rank}위</span>
    <img class="pt-media" src="${img}" alt="${name} 차 이미지" loading="lazy"/>
    <div class="pt-body"><h3 class="pt-name">${name}</h3></div>`;
  return card;
}

/**
 * 스크롤 박스 안에 모든 카드 렌더링
 */
function render() {
  const frag = document.createDocumentFragment(); // DOM 성능 최적화를 위한 fragment 사용
  PT_DATA.forEach(item => frag.appendChild(createCard(item)));
  scrollBox.appendChild(frag);
  updateNav(); // 처음 렌더링 시 버튼 표시 상태 갱신
}

/**
 * 한 번에 스크롤 이동할 거리 계산
 * - 기본적으로 화면 너비의 90%를 이동
 * - 최소 1픽셀 이상
 */
function step() {
  return Math.max(1, Math.floor(scrollBox.clientWidth * 0.9));
}

/**
 * 스크롤 가능한 최대 거리 계산
 * - scrollWidth(전체 너비) - clientWidth(보이는 영역)
 */
function maxScroll() {
  return Math.max(0, scrollBox.scrollWidth - scrollBox.clientWidth - 1);
}

/**
 * "이전/다음" 버튼 표시 상태 업데이트
 */
function updateNav() {
  const max = maxScroll();
  const atStart = scrollBox.scrollLeft <= 0;  // 스크롤 맨 앞 여부
  const atEnd   = scrollBox.scrollLeft >= max; // 스크롤 맨 끝 여부
  // 시작 지점이면 "이전" 버튼 숨김
  prevBtn.classList.toggle('hidden', atStart);
  // 끝 지점이면 "다음" 버튼 숨김
  nextBtn.classList.toggle('hidden', atEnd);
}

// "이전" 버튼 클릭 시 → 왼쪽으로 step() 만큼 부드럽게 스크롤
prevBtn.addEventListener('click', () => {
  scrollBox.scrollBy({ left: -step(), behavior: 'smooth' });
});

// "다음" 버튼 클릭 시 → 오른쪽으로 step() 만큼 부드럽게 스크롤
nextBtn.addEventListener('click', () => {
  scrollBox.scrollBy({ left: step(), behavior: 'smooth' });
});

// 스크롤 중에도 버튼 표시 상태 갱신 (성능 위해 passive 옵션 사용)
scrollBox.addEventListener('scroll', updateNav, { passive: true });

// 창 크기가 바뀌면 버튼 상태 갱신 (반응형 대응)
window.addEventListener('resize', updateNav);

// 최초 실행: 카드 렌더링
render();

/**
 * 로그인 링크 클릭 시 → 페이지 내의 "지금 로그인" 버튼으로 부드럽게 이동
 */
document.getElementById('ptLoginLink').addEventListener('click', e => {
  const btn = document.getElementById('loginNowBtn');
  if (btn) {
    e.preventDefault(); // 기본 링크 이동 동작 막기
    btn.scrollIntoView({ behavior: 'smooth' }); // 버튼 위치로 스크롤 이동
    btn.focus({ preventScroll: true }); // 버튼에 포커스 주기 (접근성 향상)
  }
});
