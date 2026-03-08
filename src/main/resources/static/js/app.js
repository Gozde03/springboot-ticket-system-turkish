// ===== Login Page JS =====
document.addEventListener("submit", (e) => {
  const form = e.target;
  if (!form.matches('form[data-form="login"]')) return;

  const username = form.querySelector('input[name="username"]');
  const password = form.querySelector('input[name="password"]');
  const btn = form.querySelector('button[type="submit"]');

  const u = (username?.value || "").trim();
  const p = (password?.value || "").trim();

  if (!u || !p) {
    e.preventDefault();
    alert("Kullanıcı adı ve şifre boş olamaz.");
    return;
  }

  if (btn) {
    btn.disabled = true;
    btn.dataset.oldText = btn.textContent || "Giriş Yap";
    btn.textContent = "Giriş yapılıyor...";
  }
});

window.addEventListener("pageshow", () => {
  const btn = document.querySelector('form[data-form="login"] button[type="submit"]');
  if (btn && btn.disabled) {
    btn.disabled = false;
    btn.textContent = btn.dataset.oldText || "Giriş Yap";
  }
});



document.addEventListener("click", async (e) => {
  const btn = e.target.closest("[data-add-to-cart]");
  if (!btn) return;

  e.preventDefault();

  const eventId = btn.dataset.eventId;
  try {
    const res = await fetch(`/event/add-to-cart/${eventId}`, { method: "POST" });
    const text = await res.text();
    alert(text); 
  } catch (err) {
    alert("Sepete eklenemedi.");
  }
});


// =======================
// EVENTS PAGE (localStorage cart) - CARDS + POSTERS 
// =======================
(function () {
  if (window.__PAGE__ !== "events") return;

  const events = [
    {id:1,  name:"Hayko Cepkin An Epic Senfoni", type:"Konser", date:"05.05.2026 20:00", city:"İstanbul", price:1200.0, img:"/img/events/hayko.jpg"},
    {id:2,  name:"Scorpions", type:"Konser", date:"10.06.2026 21:30", city:"Ankara",   price:1550.0, img:"/img/events/scorpions.jpg"},
    {id:3,  name:"Flo Rida", type:"Konser", date:"15.07.2026 19:00", city:"İzmir",     price:990.0,  img:"/img/events/florida.jpg"},
    {id:4,  name:"Jason Mraz", type:"Konser", date:"22.07.2026 20:30", city:"Bursa",   price:1200.0, img:"/img/events/jasonmraz.jpeg"},
    {id:5,  name:"Teoman", type:"Konser", date:"01.08.2026 21:00", city:"İstanbul",   price:1300.0, img:"/img/events/teoman.jpg"},
    {id:6,  name:"Adamlar An Epic Senfoni", type:"Konser", date:"10.08.2026 19:30", city:"Ankara", price:1100.0, img:"/img/events/adamlar.jpg"},
    {id:7,  name:"Duman", type:"Konser", date:"15.09.2026 20:00", city:"İzmir",       price:1450.0, img:"/img/events/duman.jpg"},
    {id:8,  name:"Ben Fero", type:"Konser", date:"20.09.2026 21:00", city:"Bursa",    price:1150.0, img:"/img/events/benfero.jpg"},
    {id:9,  name:"Outlandish", type:"Konser", date:"25.09.2026 19:00", city:"İstanbul", price:1250.0, img:"/img/events/outlandish.jpg"},
    {id:10, name:"Adele", type:"Konser", date:"05.10.2026 20:00", city:"Ankara",      price:1600.0, img:"/img/events/adele.jpg"},
    {id:11, name:"Dire Straits", type:"Konser", date:"15.10.2026 20:30", city:"İzmir", price:1450.0, img:"/img/events/Dire Straits.jpg"},
    {id:12, name:"Daft Punk", type:"Konser", date:"01.11.2026 21:00", city:"Bursa",   price:1350.0, img:"/img/events/Daftpunk.jpg"},

    {id:13, name:"TolgShow", type:"Tiyatro", date:"10.05.2026 20:00", city:"İstanbul", price:800.0, img:"/img/events/TolgShow.jpg"},
    {id:14, name:"Aydınlıkevler", type:"Tiyatro", date:"12.06.2026 19:30", city:"Ankara", price:750.0, img:"/img/events/Aydınlıkevler.jpg"},
    {id:15, name:"Drakula", type:"Tiyatro", date:"05.07.2026 20:00", city:"İzmir", price:900.0, img:"/img/events/drakula.jpeg"},
    {id:16, name:"Saatleri Ayarlama Enstitüsü", type:"Tiyatro", date:"20.08.2026 20:00", city:"Bursa", price:850.0, img:"/img/events/Saatleri Ayarlama Enstitüsü.jpg"},

    {id:17, name:"Avengers Endgame", type:"Sinema", date:"18.05.2026 21:00", city:"İstanbul", price:3000.0, img:"/img/events/Avengers Endgae.jpg"},
    {id:18, name:"Thor Ragnarok", type:"Sinema", date:"20.06.2026 21:00", city:"Ankara", price:2900.0, img:"/img/events/Thor Ragnarok.jpg"},
    {id:19, name:"Angels And Demons", type:"Sinema", date:"15.07.2026 22:00", city:"İzmir", price:3500.0, img:"/img/events/Angels And Demons.jpg"},
    {id:20, name:"Inferno", type:"Sinema", date:"22.07.2026 21:30", city:"Bursa", price:3200.0, img:"/img/events/Inferno.jpeg"},
    {id:21, name:"Lucy", type:"Sinema", date:"05.08.2026 20:00", city:"İstanbul", price:2400.0, img:"/img/events/lucy.jpeg"},
    {id:22, name:"Ford vs Ferrari", type:"Sinema", date:"12.08.2026 21:00", city:"Ankara", price:2600.0, img:"/img/events/Ford vs Ferrari.jpeg"},
    {id:23, name:"Sixth Sense", type:"Sinema", date:"20.09.2026 20:30", city:"İzmir", price:1800.0, img:"/img/events/Sixth Sense.jpeg"},
    {id:24, name:"Multiverse Of Madness", type:"Sinema", date:"01.10.2026 21:00", city:"Bursa", price:2100.0, img:"/img/events/Multiverse Of Madness.jpeg"}
  ];

  const grid = document.getElementById("eventsGrid");
  const searchEl = document.getElementById("eventSearch");
  const typeEl = document.getElementById("eventTypeFilter");
  const clearBtn = document.getElementById("clearFilters");
  const goToCartBtn = document.getElementById("goToCart");

  if (!grid) return; 

  function getCart() {
    try { return JSON.parse(localStorage.getItem("cart")) || []; }
    catch { return []; }
  }

  function saveCart(cart) {
    localStorage.setItem("cart", JSON.stringify(cart));
  }

  function addToCart(eventId) {
    const event = events.find(e => e.id === eventId);
    if (!event) return;

    const cart = getCart();
    const existing = cart.find(i => i.id === eventId);

    if (existing) existing.quantity += 1;
    else cart.push({ ...event, quantity: 1 });

    saveCart(cart);
    alert(`${event.name} sepete eklendi!`);
  }

  function render(list) {
    grid.innerHTML = "";
    list.forEach(ev => {
      const card = document.createElement("div");
      card.className = "event-card";
      card.innerHTML = `
        <img src="${ev.img}" alt="${ev.name}">
        <div class="event-info">
          <h3>${ev.name}</h3>
          <p class="event-meta">${ev.type} • ${ev.city} • ${ev.date}</p>
          <p class="event-price">₺${ev.price}</p>
          <button class="btn-add" data-add="${ev.id}" type="button">Sepete Ekle</button>
        </div>
      `;
      grid.appendChild(card);
    });
  }

  function applyFilters() {
    const q = (searchEl?.value || "").trim().toLowerCase();
    const type = (typeEl?.value || "").trim();

    const filtered = events.filter(e => {
      const matchesType = !type || e.type === type;
      const hay = `${e.name} ${e.type} ${e.city} ${e.date}`.toLowerCase();
      const matchesQuery = !q || hay.includes(q);
      return matchesType && matchesQuery;
    });

    render(filtered);
  }

  render(events);

  grid.addEventListener("click", (e) => {
    const btn = e.target.closest("[data-add]");
    if (!btn) return;
    addToCart(Number(btn.getAttribute("data-add")));
  });

  searchEl?.addEventListener("input", applyFilters);
  typeEl?.addEventListener("change", applyFilters);

  clearBtn?.addEventListener("click", () => {
    if (searchEl) searchEl.value = "";
    if (typeEl) typeEl.value = "";
    render(events);
  });

  goToCartBtn?.addEventListener("click", () => {
    window.location.href = "/event/cart";
  });
})();



(function () {
  // ---------- CART STORAGE HELPERS ----------
  function getCart() {
    try {
      return JSON.parse(localStorage.getItem("cart")) || [];
    } catch (e) {
      return [];
    }
  }

  function saveCart(cart) {
    localStorage.setItem("cart", JSON.stringify(cart));
  }

  function clearCart() {
    localStorage.removeItem("cart");
  }

  function removeFromCart(eventId) {
    const cart = getCart().filter(item => Number(item.id) !== Number(eventId));
    saveCart(cart);
  }

  // ---------- CART RENDER ----------
  function renderCart() {
    const cart = getCart();
    const tbody = document.getElementById("cartBody");
    const totalEl = document.getElementById("totalPrice");

    if (!tbody || !totalEl) return;

    tbody.innerHTML = "";
    let total = 0;

    if (cart.length === 0) {
      tbody.innerHTML = `<tr><td colspan="6">Sepetiniz boş.</td></tr>`;
      totalEl.textContent = "";
      return;
    }

    cart.forEach((item) => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${item.name}</td>
        <td>${item.date}</td>
        <td>${item.city}</td>
        <td>${item.price} ₺</td>
        <td>${item.quantity}</td>
        <td>
          <button class="btn-remove" data-remove="${item.id}" type="button">Kaldır</button>
        </td>
      `;
      tbody.appendChild(tr);

      const price = Number(item.price) || 0;
      const qty = Number(item.quantity) || 0;
      total += price * qty;
    });

    totalEl.textContent = `Sepetinizin Toplam Tutarı: ${total} ₺`;
  }

  // ---------- CART COUNTDOWN ----------
  function startCountdown() {
    let timeLeft = 15;
    const timerElement = document.getElementById("timer");
    const countdownBox = document.getElementById("countdown");

    if (!timerElement || !countdownBox) return null;

    timerElement.textContent = String(timeLeft);

    const interval = setInterval(() => {
      timeLeft--;
      timerElement.textContent = String(timeLeft);

      if (timeLeft <= 0) {
        clearInterval(interval);
        clearCart();
        renderCart();
        countdownBox.innerHTML =
          '<span style="color: #ef6c00; font-weight:700;">Sepet süresi dolduğu için sepetiniz sıfırlandı!</span>';

        const confirmBtn = document.getElementById("confirmBtn");
        if (confirmBtn) confirmBtn.style.display = "none";
      }
    }, 1000);

    return interval;
  }

  // ---------- CART INIT ----------
  function initCartPage() {
    renderCart();

    const countdownBox = document.getElementById("countdown");
    const confirmBtn = document.getElementById("confirmBtn");
    const tbody = document.getElementById("cartBody");

    if (!countdownBox || !confirmBtn || !tbody) return;

    tbody.addEventListener("click", (e) => {
      const btn = e.target.closest("[data-remove]");
      if (!btn) return;

      const id = Number(btn.getAttribute("data-remove"));
      removeFromCart(id);
      renderCart();

      const cart = getCart();
      if (cart.length === 0) {
        countdownBox.style.display = "none";
        confirmBtn.style.display = "none";
      }
    });

    const cart = getCart();

    if (cart.length === 0) {
      countdownBox.style.display = "none";
      confirmBtn.style.display = "none";
      return;
    }

    let countdownInterval = startCountdown();

    confirmBtn.addEventListener("click", () => {
      if (countdownInterval) clearInterval(countdownInterval);
      countdownBox.innerHTML =
        '<span style="color:green; font-weight:700;">Sepetiniz onaylandı!</span>';
    });

    const backLink = document.getElementById("backToEvents");
    if (backLink) {
      backLink.addEventListener("click", () => {
        if (countdownInterval) clearInterval(countdownInterval);
      });
    }
  }

  // ---------- BOOTSTRAP ----------
  document.addEventListener("DOMContentLoaded", () => {
    if (window.__PAGE__ === "cart") {
      initCartPage();
    }
  });
})();