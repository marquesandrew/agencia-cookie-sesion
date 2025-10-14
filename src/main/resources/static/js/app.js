/* ===== Helpers REST (com sessão/cookie) ===== */
async function parseOrText(r) {
    try { return await r.json(); } catch { return await r.text(); }
}

export async function getJSON(url) {
    const r = await fetch(url, { credentials: 'include' });
    if (!r.ok) throw new Error(`GET ${url} -> ${r.status} ${await parseOrText(r)}`);
    return r.json();
}

export async function postJSON(url, body) {
    const r = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(body)
    });
    if (!r.ok) throw new Error(await parseOrText(r));
    return r.json();
}

export async function putJSON(url, body) {
    const r = await fetch(url, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(body)
    });
    if (!r.ok) throw new Error(await parseOrText(r));
    return r.json();
}

export async function del(url) {
    const r = await fetch(url, { method: 'DELETE', credentials: 'include' });
    if (!r.ok) throw new Error(await parseOrText(r));
}

/* ===== Sessão ===== */
export async function getUserSession() {
    const r = await fetch('/api/auth/me', { credentials: 'include' });
    if (r.status === 401) return null;
    return r.json();
}

export async function requireAuth() {
    const user = await getUserSession();
    if (!user && location.pathname !== '/login.html') {
        location.href = '/login.html';
    }
    return user;
}

/* ===== Navbar parcial =====
   - injeta /partials/navbar.html em <div id="navbar"></div>
   - marca link ativo
   - mostra nome do usuário da sessão
   - faz logout via /api/logout
*/
export async function loadNavbar(user) {
    const host = document.getElementById('navbar');
    if (!host) return;

    const html = await fetch('/partials/navbar.html', { credentials: 'include' }).then(r => r.text());
    host.innerHTML = html;

    // rota ativa
    const file = location.pathname.split('/').pop() || 'index.html';
    const route = file.replace('.html', '');
    const active = host.querySelector(`a.nav-link[data-route="${route}"]`);
    if (active) active.classList.add('active');

    // usuário
    if (!user) user = await getUserSession();
    const userInfo = host.querySelector('#userInfo');
    if (user && userInfo) userInfo.textContent = user.nome;

    // logout
    const btnLogout = host.querySelector('#btnLogout');
    if (btnLogout) {
        btnLogout.addEventListener('click', async () => {
            if (confirm('Deseja realmente sair?')) {
                await fetch('/api/logout', { method: 'POST', credentials: 'include' });
                location.href = '/login.html';
            }
        });
    }
}

/* ===== Boot padrão páginas internas ===== */
export async function bootPage() {
    const user = await requireAuth(); // garante sessão
    await loadNavbar(user);           // carrega navbar com nome
    return user;                      // devolve o usuário, se precisar usar na página
}
