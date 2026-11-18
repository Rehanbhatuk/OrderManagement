function escapeHtml(value) { return String(value ?? "").replace(/[&<>"']/g, c => ({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;", "'":"&#39;"}[c])); }
function apiRequest(options) {
  const settings = $.extend({ dataType: "json", contentType: "application/json", beforeSend: function(){ $("#apiDot").addClass("busy"); } }, options);
  settings.url = API_BASE_URL + options.url;
  return $.ajax(settings).always(() => $("#apiDot").removeClass("busy"));
}
function showToast(message, type="success") {
  const id = "toast" + Date.now(); const icon = type === "success" ? "check-circle" : type === "warning" ? "exclamation-triangle" : "x-circle";
  const html = `<div id="${id}" class="toast align-items-center border-0 text-bg-${type === "error" ? "danger" : type}" role="alert"><div class="d-flex"><div class="toast-body"><i class="bi bi-${icon} me-2"></i>${escapeHtml(message)}</div><button type="button" class="btn-close ${type === "error" || type === "success" || type === "warning" ? "btn-close-white" : ""} me-2 m-auto" data-bs-dismiss="toast"></button></div></div>`;
  $("#toastContainer").append(html); const el = document.getElementById(id); const toast = new bootstrap.Toast(el, {delay: 4000}); toast.show(); el.addEventListener("hidden.bs.toast", () => el.remove());
}
function backendError(xhr) {
  if (xhr.status === 0) return "Cannot connect to Spring Boot. Check that the backend is running and CORS is configured if needed.";
  const body = xhr.responseJSON || xhr.responseText;
  if (Array.isArray(body)) return body.map(e => `${e.key || "Field"}: ${e.errorMessage || "Invalid value"}`).join(" · ");
  if (body && typeof body === "object") return Object.entries(body).map(([k,v]) => `${k}: ${v}`).join(" · ");
  if (typeof body === "string" && body.trim()) return body;
  return ({400:"The request contains invalid data.",404:"The requested record was not found.",500:"The server encountered an error. Check the backend logs."})[xhr.status] || `Request failed (${xhr.status}).`;
}
function handleError(xhr, target) { const message = backendError(xhr); if (target) $(target).html(`<div class="alert alert-danger py-2 small mb-0">${escapeHtml(message)}</div>`); showToast(message, "error"); }
function setButtonLoading($button, loading, label="Please wait…") { if (loading) { $button.data("old-text", $button.html()).prop("disabled", true).html(`<span class="spinner-border spinner-border-sm me-2"></span>${label}`); } else { $button.prop("disabled", false).html($button.data("old-text")); } }
function renderTable(headers, rows) { if (!rows || !rows.length) return `<div class="empty-state compact"><i class="bi bi-inbox"></i><b>No records found</b><span>The API returned an empty result.</span></div>`; return `<div class="table-responsive"><table class="table align-middle"><thead><tr>${headers.map(h=>`<th>${h}</th>`).join("")}</tr></thead><tbody>${rows.join("")}</tbody></table></div>`; }
function money(value) { const n = Number(value); return Number.isFinite(n) ? "₹" + n.toLocaleString("en-IN", {minimumFractionDigits:2, maximumFractionDigits:2}) : "—"; }
function dateOnly(value) { if (!value) return "—"; return String(value).slice(0,10); }
function checkApi() { $.ajax({url: API_BASE_URL + "/customer/get/active", method:"GET", timeout:5000}).done(()=>{$("#apiStatus").text("API reachable");$("#apiDot").addClass("online");}).fail(()=>{$("#apiStatus").text("API unavailable");$("#apiDot").removeClass("online");}); }
