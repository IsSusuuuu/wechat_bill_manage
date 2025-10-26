async function fetchJson(url, opts) {
    const res = await fetch(url, Object.assign({ headers: { 'Content-Type': 'application/json' } }, opts));
    if (!res.ok) {
        const text = await res.text().catch(() => '');
        throw new Error(text || ('HTTP ' + res.status));
    }
    const ct = res.headers.get('content-type') || '';
    if (ct.includes('application/json')) return res.json();
    return null;
}

function toCurrency(num) {
    try { return new Intl.NumberFormat('zh-CN', { style: 'currency', currency: 'CNY' }).format(Number(num || 0)); } catch (e) { return String(num); }
}

function qs(sel) { return document.querySelector(sel); }
function qsa(sel) { return Array.from(document.querySelectorAll(sel)); }

function openDialog(id) { const el = document.getElementById(id); if (el) el.classList.add('open'); }
function closeDialog(id) { const el = document.getElementById(id); if (el) el.classList.remove('open'); }

function setFormValues(formEl, values) {
    Object.entries(values || {}).forEach(([k, v]) => {
        const input = formEl.querySelector(`[name="${k}"]`);
        if (input) input.value = v == null ? '' : v;
    });
}

function getFormValues(formEl) {
    const data = {};
    new FormData(formEl).forEach((v, k) => { data[k] = v; });
    return data;
}

function toast(msg) {
    console.log('[toast]', msg);
    alert(msg);
}

