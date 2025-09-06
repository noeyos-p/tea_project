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

    const scrollBox = document.getElementById('ptScroll');
    const prevBtn = document.getElementById('ptPrev');
    const nextBtn = document.getElementById('ptNext');

    function createCard({ rank, name, img }) {
      const card = document.createElement('article');
      card.className = 'pt-card';
      card.innerHTML = `
      <span class=\"pt-rank\">${rank}위</span>
      <img class=\"pt-media\" src=\"${img}\" alt=\"${name} 차 이미지\" loading=\"lazy\"/>
      <div class=\"pt-body\"><h3 class=\"pt-name\">${name}</h3></div>`;
      return card;
    }

    function render() {
      const frag = document.createDocumentFragment();
      PT_DATA.forEach(item => frag.appendChild(createCard(item)));
      scrollBox.appendChild(frag);
      updateNav();
    }

    function step() {
      return Math.max(1, Math.floor(scrollBox.clientWidth * 0.9));
    }

    function maxScroll() {
      return Math.max(0, scrollBox.scrollWidth - scrollBox.clientWidth - 1);
    }

    function updateNav() {
      const max = maxScroll();
      const atStart = scrollBox.scrollLeft <= 0;
      const atEnd = scrollBox.scrollLeft >= max;
      prevBtn.classList.toggle('hidden', atStart);
      nextBtn.classList.toggle('hidden', atEnd);
    }

    prevBtn.addEventListener('click', () => { scrollBox.scrollBy({ left: -step(), behavior: 'smooth' }); });
    nextBtn.addEventListener('click', () => { scrollBox.scrollBy({ left: step(), behavior: 'smooth' }); });
    scrollBox.addEventListener('scroll', updateNav, { passive: true });
    window.addEventListener('resize', updateNav);

    render();

    document.getElementById('ptLoginLink').addEventListener('click', e => {
      const btn = document.getElementById('loginNowBtn');
      if (btn) { e.preventDefault(); btn.scrollIntoView({ behavior: 'smooth' }); btn.focus({ preventScroll: true }); }
    });