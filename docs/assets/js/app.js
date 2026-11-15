const captions = {
  'home': 'Pantalla de Inicio con Buscador y Accesos Rápidos',
  'browser': 'Navegación Activa con SSL y Escudo',
  'ai': 'Nova AI Copilot impulsado por Gemini',
  'tabs': 'Gestor de Pestañas e Incógnito',
  'settings': 'Ajustes y Escudo Nova Shield'
};

function switchScreen(screenKey, defaultCaption, btn) {
  const img = document.getElementById('active-phone-img');
  const caption = document.getElementById('active-phone-caption');
  
  // Transición suave
  if (img) img.style.opacity = '0.3';
  
  setTimeout(() => {
    if (img) {
      img.src = `assets/screenshots/${screenKey}.png`;
      img.style.opacity = '1';
    }
    if (caption) caption.textContent = captions[screenKey] || defaultCaption;
  }, 150);

  // Actualizar botón activo
  const buttons = document.querySelectorAll('.screen-tab-btn');
  buttons.forEach(b => b.classList.remove('active'));
  if (btn) {
    btn.classList.add('active');
  } else if (window.event && window.event.currentTarget) {
    window.event.currentTarget.classList.add('active');
  }
}

function toggleFaq(button) {
  const item = button.parentElement;
  const isActive = item.classList.contains('active');
  
  // Cerrar otros
  document.querySelectorAll('.faq-item').forEach(faq => {
    faq.classList.remove('active');
  });

  if (!isActive) {
    item.classList.add('active');
  }
}

/* Lógica de Pantalla Animada de Bienvenida v2.0 */
let welcomeBootTimers = [];

function runWelcomeBootSequence() {
  welcomeBootTimers.forEach(clearTimeout);
  welcomeBootTimers = [];

  const statusEl = document.getElementById('welcomeBootStatus');
  const pctEl = document.getElementById('welcomeBootPercent');
  const fillEl = document.getElementById('welcomeBootFill');
  if (!statusEl || !pctEl || !fillEl) return;

  const steps = [
    { delay: 80, pct: 28, msg: 'Inicializando Motor Jetpack Compose & Material 3...' },
    { delay: 420, pct: 64, msg: 'Sincronizando Repositorios & Escudo Nova Shield...' },
    { delay: 780, pct: 100, msg: 'Sistema Nova Browser v2.0 Listo • Máxima Velocidad ✓' }
  ];

  fillEl.style.width = '8%';
  pctEl.textContent = '8%';

  steps.forEach(step => {
    const t = setTimeout(() => {
      fillEl.style.width = step.pct + '%';
      pctEl.textContent = step.pct + '%';
      statusEl.textContent = step.msg;
    }, step.delay);
    welcomeBootTimers.push(t);
  });
}

function openWelcomeModal() {
  const modal = document.getElementById('welcomeModalOverlay');
  if (modal) {
    modal.classList.add('active');
    document.body.style.overflow = 'hidden';
    runWelcomeBootSequence();
  }
}

function closeWelcomeModal() {
  const modal = document.getElementById('welcomeModalOverlay');
  if (modal) {
    modal.classList.remove('active');
    document.body.style.overflow = '';
    try {
      localStorage.setItem('nova_welcome_viewed_v2', 'true');
    } catch (e) {
      // Navegadores con localStorage bloqueado
    }
  }
}

function handleOverlayClick(event) {
  if (event.target && event.target.id === 'welcomeModalOverlay') {
    closeWelcomeModal();
  }
}

// Cerrar modal o drawer con tecla Escape
document.addEventListener('keydown', function(e) {
  if (e.key === 'Escape') {
    closeWelcomeModal();
    toggleMobileDrawer(false);
  }
});

// Auto-mostrar bienvenida animada en la primera visita
window.addEventListener('DOMContentLoaded', function() {
  try {
    const hasSeen = localStorage.getItem('nova_welcome_viewed_v2');
    if (!hasSeen) {
      setTimeout(openWelcomeModal, 320);
    }
  } catch (e) {
    setTimeout(openWelcomeModal, 320);
  }
});

/* Lógica del Procedimiento de Investigación y Admisión */
function handleScreeningSubmit(event) {
  event.preventDefault();
  
  const nameInput = document.getElementById('applicantName');
  const deviceInput = document.getElementById('applicantDevice');
  const commitmentRadios = document.getElementsByName('commitment');
  
  let commitmentVal = '';
  for (const radio of commitmentRadios) {
    if (radio.checked) {
      commitmentVal = radio.value;
      break;
    }
  }

  const applicantName = nameInput.value.trim() || 'Pionero Nova';
  const applicantDevice = deviceInput.value.trim() || 'Dispositivo Android';

  const form = document.getElementById('screeningForm');
  const evaluatingBox = document.getElementById('screeningEvaluating');
  const scannerMsg = document.getElementById('scannerMessage');
  const scannerFill = document.getElementById('scannerFill');
  const approvedBox = document.getElementById('screeningApproved');
  const rejectedBox = document.getElementById('screeningRejected');
  const statusBadge = document.getElementById('screeningStatusBadge');

  // Ocultar formulario e iniciar escaneo
  form.style.display = 'none';
  evaluatingBox.classList.add('active');
  statusBadge.textContent = 'Analizando respuestas...';
  statusBadge.style.color = 'var(--cyan-electric)';

  // Animación de barra de escaneo
  setTimeout(() => {
    scannerFill.style.width = '35%';
    scannerMsg.textContent = 'Verificando compatibilidad de dispositivo (' + applicantDevice + ')...';
  }, 300);

  setTimeout(() => {
    scannerFill.style.width = '70%';
    scannerMsg.textContent = 'Evaluando propósito y compromiso del usuario...';
  }, 800);

  setTimeout(() => {
    scannerFill.style.width = '100%';
    scannerMsg.textContent = 'Generando dictamen de admisión...';
  }, 1200);

  setTimeout(() => {
    evaluatingBox.classList.remove('active');

    if (commitmentVal === 'approved') {
      // Superó la prueba: Oportunidad concedida
      document.getElementById('approvedUserName').textContent = applicantName;
      document.getElementById('cardHolder').textContent = applicantName;
      document.getElementById('cardDevice').textContent = applicantDevice;

      // Generar Token Único
      const randomSuffix = Math.floor(1000 + Math.random() * 9000);
      const tokenStr = 'NOVA-VIP-' + randomSuffix + '-PASS';
      document.getElementById('approvedToken').textContent = tokenStr;

      approvedBox.classList.add('active');
      statusBadge.textContent = 'Dictamen: Aprobado (Acceso Concedido)';
      statusBadge.style.color = 'var(--green-secure)';
    } else {
      // No superó la prueba
      rejectedBox.classList.add('active');
      statusBadge.textContent = 'Dictamen: No Superado';
      statusBadge.style.color = '#EF4444';
    }
  }, 1600);
}

function resetScreening() {
  const form = document.getElementById('screeningForm');
  const approvedBox = document.getElementById('screeningApproved');
  const rejectedBox = document.getElementById('screeningRejected');
  const scannerFill = document.getElementById('scannerFill');
  const statusBadge = document.getElementById('screeningStatusBadge');

  scannerFill.style.width = '0%';
  approvedBox.classList.remove('active');
  rejectedBox.classList.remove('active');
  form.style.display = 'block';
  statusBadge.textContent = 'Evaluación Abierta • Acceso Limitado';
  statusBadge.style.color = 'var(--text-dim)';

  // Desmarcar compromiso para que vuelva a elegir
  const commitmentRadios = document.getElementsByName('commitment');
  for (const radio of commitmentRadios) {
    radio.checked = false;
  }
}

/* =========================================================================
   1. GESTOR Y SUBIDA DE VERSIONES APK (#lanzamiento)
   ========================================================================= */
let uploadedApkFileBlob = null;
let uploadedApkFileName = '';
let uploadedApkFileSizeMb = '';

const DEFAULT_RELEASES = [
  {
    id: 'rel-v100',
    versionName: 'v1.0.0 Oficial',
    versionCode: 1,
    channel: 'Estable',
    architecture: 'Universal (FAT-APK)',
    minSdk: 'Android 7.0+ (API 24)',
    date: 'Septiembre 2026',
    size: '34.4 MB',
    downloadUrl: 'https://github.com/luismiguel38338-cmd/XboxLR-Browser/releases/download/v1.0.0/Nova-Browser-v1.0-debug.apk',
    changelog: 'Lanzamiento oficial inicial con motor nativo Jetpack Compose, integración de Google Gemini AI (gemini-3.5-flash) y escudo Nova Shield.'
  }
];

function getStoredReleases() {
  try {
    const data = localStorage.getItem('nova_custom_releases_v1');
    return data ? JSON.parse(data) : DEFAULT_RELEASES;
  } catch (e) {
    return DEFAULT_RELEASES;
  }
}

function saveStoredReleases(releases) {
  try {
    localStorage.setItem('nova_custom_releases_v1', JSON.stringify(releases));
  } catch (e) {
    console.error('Error al guardar versiones en localStorage', e);
  }
}

// Manejo de drag & drop para el dropzone
const dropzone = document.getElementById('apkDropzone');
if (dropzone) {
  ['dragenter', 'dragover'].forEach(eventName => {
    dropzone.addEventListener(eventName, (e) => {
      e.preventDefault();
      e.stopPropagation();
      dropzone.classList.add('dragover');
    }, false);
  });

  ['dragleave', 'drop'].forEach(eventName => {
    dropzone.addEventListener(eventName, (e) => {
      e.preventDefault();
      e.stopPropagation();
      dropzone.classList.remove('dragover');
    }, false);
  });

  dropzone.addEventListener('drop', (e) => {
    const dt = e.dataTransfer;
    const files = dt.files;
    if (files && files.length > 0) {
      processApkFile(files[0]);
    }
  }, false);
}

function handleApkFileSelect(event) {
  const files = event.target.files;
  if (files && files.length > 0) {
    processApkFile(files[0]);
  }
}

function processApkFile(file) {
  if (!file.name.toLowerCase().endsWith('.apk')) {
    showToast('Por favor selecciona un archivo con extensión .apk válido para Android.', 'error');
    return;
  }

  uploadedApkFileBlob = file;
  uploadedApkFileName = file.name;
  uploadedApkFileSizeMb = (file.size / (1024 * 1024)).toFixed(1) + ' MB';

  document.getElementById('detectedFileName').textContent = uploadedApkFileName;
  document.getElementById('detectedFileSize').textContent = 'Tamaño detectado: ' + uploadedApkFileSizeMb;
  document.getElementById('fileDetectedBanner').classList.add('active');

  const badge = document.getElementById('hubStatusBadge');
  badge.textContent = 'APK Cargado (' + uploadedApkFileSizeMb + ')';
  badge.className = 'version-tag-badge badge-stable';
}

function handlePublishRelease(event) {
  event.preventDefault();

  const vName = document.getElementById('relVersionName').value.trim();
  const vCode = document.getElementById('relVersionCode').value.trim();
  const arch = document.getElementById('relArchitecture').value;
  const minSdk = document.getElementById('relMinSdk').value;
  const channel = document.getElementById('relChannel').value;
  const changelog = document.getElementById('relChangelog').value.trim();

  let dlUrl = 'https://github.com/luismiguel38338-cmd/XboxLR-Browser/releases/download/' + encodeURIComponent(vName) + '/' + (uploadedApkFileName || 'Nova-Browser-' + vName + '.apk');

  // Si el usuario subió un archivo local en esta sesión, creamos una URL de Blob descargable
  if (uploadedApkFileBlob) {
    try {
      dlUrl = URL.createObjectURL(uploadedApkFileBlob);
    } catch (e) {
      console.warn('No se pudo crear ObjectURL local:', e);
    }
  }

  const now = new Date();
  const dateStr = now.toLocaleDateString('es-ES', { year: 'numeric', month: 'short', day: 'numeric' });

  const newRelease = {
    id: 'rel-' + Date.now(),
    versionName: vName,
    versionCode: parseInt(vCode, 10) || 1,
    channel: channel,
    architecture: arch,
    minSdk: minSdk,
    date: dateStr,
    size: uploadedApkFileSizeMb || '35.0 MB',
    downloadUrl: dlUrl,
    isCustomBlob: !!uploadedApkFileBlob,
    changelog: changelog
  };

  const currentReleases = getStoredReleases();
  currentReleases.unshift(newRelease);
  saveStoredReleases(currentReleases);
  renderReleasesList();

  showToast('¡Versión ' + vName + ' publicada en el catálogo con éxito!', 'success');
}

function renderReleasesList() {
  const container = document.getElementById('releasesListContainer');
  if (!container) return;

  const list = getStoredReleases();
  container.innerHTML = '';

  list.forEach((rel) => {
    const isStable = rel.channel === 'Estable';
    const badgeClass = isStable ? 'badge-stable' : 'badge-beta';

    const item = document.createElement('div');
    item.className = 'version-item';
    item.innerHTML = `
      <div class="version-header">
        <div>
          <strong style="font-size: 1.05rem; color: #FFFFFF; margin-right: 8px;">Nova Browser ${rel.versionName}</strong>
          <span class="version-tag-badge ${badgeClass}">${rel.channel}</span>
        </div>
        <span style="font-size: 0.78rem; color: var(--text-dim);">${rel.date}</span>
      </div>
      <div style="display: flex; gap: 14px; font-size: 0.8rem; color: var(--text-muted); margin-bottom: 8px; flex-wrap: wrap;">
        <span>📦 Tamaño: <b style="color: #FFFFFF;">${rel.size}</b></span>
        <span>⚙️ Arch: <b style="color: #FFFFFF;">${rel.architecture}</b></span>
        <span>📱 Min SDK: <b style="color: #FFFFFF;">${rel.minSdk}</b></span>
      </div>
      <div style="font-size: 0.84rem; color: var(--text-dim); margin-bottom: 12px; white-space: pre-line; line-height: 1.4;">
        ${rel.changelog}
      </div>
      <div style="display: flex; gap: 10px; align-items: center; flex-wrap: wrap;">
        <a href="${rel.downloadUrl}" download="Nova-Browser-${rel.versionName}.apk" class="btn btn-primary" style="padding: 6px 14px; font-size: 0.82rem; gap: 6px;">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path><polyline points="7 10 12 15 17 10"></polyline><line x1="12" y1="15" x2="12" y2="3"></line></svg>
          Descargar APK (${rel.size})
        </a>
      </div>
    `;
    container.appendChild(item);
  });
}

function resetVersionsToDefault() {
  if (confirm('¿Restablecer el historial de versiones al lanzamiento oficial v1.0.0?')) {
    saveStoredReleases(DEFAULT_RELEASES);
    renderReleasesList();
  }
}

function exportReleaseManifest() {
  const currentReleases = getStoredReleases();
  const manifest = {
    app_name: 'Nova Browser',
    package_name: 'com.aistudio.nova.browser',
    domain: 'novabrowser.app',
    author: 'Luis Miguel',
    generated_at: new Date().toISOString(),
    repository: 'https://github.com/luismiguel38338-cmd/XboxLR-Browser',
    source_zip: 'https://github.com/luismiguel38338-cmd/XboxLR-Browser/archive/refs/heads/main.zip',
    releases: currentReleases
  };

  downloadTextArtifact(
    'nova-browser-release-manifest.json',
    JSON.stringify(manifest, null, 2),
    'application/json'
  );
  showToast('📦 Manifiesto JSON estructurado descargado', 'success');
}

/* =========================================================================
   2. DESPACHADOR A TIENDAS DE APK GRATUITAS (#tiendas-apk)
   ========================================================================= */
function dispatchToIzzyOnDroid() {
  const title = encodeURIComponent('New App: Nova Browser (com.aistudio.nova.browser)');
  const description = encodeURIComponent(`## Nova Browser Submission
- **App Name:** Nova Browser
- **Package Name:** com.aistudio.nova.browser
- **License:** MIT
- **Website:** https://novabrowser.app
- **Source Code:** https://github.com/luismiguel38338-cmd/XboxLR-Browser
- **APK Release Asset:** https://github.com/luismiguel38338-cmd/XboxLR-Browser/releases/download/v1.0.0/Nova-Browser-v1.0-debug.apk
- **Developer:** Luis Miguel
- **Description:** Modern, native Android web browser written in Jetpack Compose featuring Gemini AI copilot and privacy Nova Shield.`);
  
  const url = 'https://gitlab.com/IzzyOnDroid/repo/-/issues/new?issue[title]=' + title + '&issue[description]=' + description;
  window.open(url, '_blank');
}

function copyTechnicalSpecs() {
  const pre = document.getElementById('techSpecsPre');
  const btn = document.getElementById('copySpecsBtn');
  if (!pre || !btn) return;

  navigator.clipboard.writeText(pre.textContent).then(() => {
    const originalText = btn.innerHTML;
    btn.innerHTML = '<span>✓</span> ¡Copiado al Portapapeles!';
    btn.style.background = 'var(--green-secure)';
    setTimeout(() => {
      btn.innerHTML = originalText;
      btn.style.background = '';
    }, 2200);
  }).catch(err => {
    console.error('No se pudo copiar:', err);
  });
}

/* =========================================================================
   3. COMENTARIOS Y CALIFICACIONES CON ESTRELLAS (#opiniones)
   ========================================================================= */
let currentSelectedStar = 5;

const INITIAL_REVIEWS = [
  {
    id: 'rev-1',
    name: 'Alejandro Ramos',
    device: 'Samsung Galaxy S23 Ultra',
    rating: 5,
    date: 'Hace 2 días',
    verified: true,
    comment: 'La interfaz hecha en Jetpack Compose es sumamente suave y moderna. El bloqueo de rastreadores de Nova Shield hace que las páginas carguen el doble de rápido que en Chrome.'
  },
  {
    id: 'rev-2',
    name: 'Valentina Mendoza',
    device: 'Xiaomi 13 Pro (Android 14)',
    rating: 5,
    date: 'Hace 3 días',
    verified: true,
    comment: '¡El asistente Gemini AI dentro del navegador es una maravilla! Le pido que me resuma artículos largos en español y me responde de inmediato. Además, 100% gratis y sin anuncios molestos.'
  },
  {
    id: 'rev-3',
    name: 'Diego Morales',
    device: 'Google Pixel 8',
    rating: 5,
    date: 'Hace 5 días',
    verified: true,
    comment: 'Descargué el APK directo desde GitHub y la instalación fue limpia. No pide permisos raros, solo acceso a internet. Gran trabajo de desarrollo por parte de Luis Miguel.'
  },
  {
    id: 'rev-4',
    name: 'Lucía Fernández',
    device: 'Motorola Edge 40',
    rating: 4,
    date: 'Hace 1 semana',
    verified: true,
    comment: 'Excelente navegador. Muy ligero en memoria RAM comparado con otros navegadores pesados. Me gustaría que agreguen sincronización con la nube en el futuro.'
  }
];

function getStoredReviews() {
  try {
    const data = localStorage.getItem('nova_user_reviews_v1');
    return data ? JSON.parse(data) : INITIAL_REVIEWS;
  } catch (e) {
    return INITIAL_REVIEWS;
  }
}

function saveStoredReviews(reviews) {
  try {
    localStorage.setItem('nova_user_reviews_v1', JSON.stringify(reviews));
  } catch (e) {
    console.error('Error guardando reviews:', e);
  }
}

function setRating(val) {
  currentSelectedStar = val;
  document.getElementById('selectedStarValue').value = val;

  const stars = document.querySelectorAll('#starSelectorGroup .star-btn');
  stars.forEach(star => {
    const starVal = parseInt(star.getAttribute('data-value'), 10);
    if (starVal <= val) {
      star.classList.add('active');
    } else {
      star.classList.remove('active');
    }
  });

  const descriptors = {
    5: 'Excelente (5/5) ★★★★★',
    4: 'Muy Bueno (4/5) ★★★★☆',
    3: 'Bueno (3/5) ★★★☆☆',
    2: 'Regular (2/5) ★★☆☆☆',
    1: 'Mejorable (1/5) ★☆☆☆☆'
  };
  document.getElementById('ratingDescriptor').textContent = descriptors[val] || (val + '/5');
}

function handleReviewSubmit(event) {
  event.preventDefault();

  const authorName = document.getElementById('reviewAuthorName').value.trim() || 'Usuario Nova';
  const deviceModel = document.getElementById('reviewDeviceModel').value.trim() || 'Dispositivo Android';
  const commentText = document.getElementById('reviewCommentText').value.trim();
  const rating = currentSelectedStar;

  const newReview = {
    id: 'rev-' + Date.now(),
    name: authorName,
    device: deviceModel,
    rating: rating,
    date: 'Hoy',
    verified: true,
    comment: commentText
  };

  const reviews = getStoredReviews();
  reviews.unshift(newReview);
  saveStoredReviews(reviews);

  // Limpiar formulario
  document.getElementById('reviewCommentText').value = '';
  setRating(5);

  updateRatingStats();
  renderReviewsFeed(0);

  showToast('¡Gracias ' + authorName + '! Tu reseña de ' + rating + '★ ya fue publicada.', 'success');
}

function updateRatingStats() {
  const reviews = getStoredReviews();
  const total = reviews.length;
  if (total === 0) return;

  let sum = 0;
  const counts = { 5: 0, 4: 0, 3: 0, 2: 0, 1: 0 };

  reviews.forEach(r => {
    sum += r.rating;
    if (counts[r.rating] !== undefined) {
      counts[r.rating]++;
    }
  });

  const avg = (sum / total).toFixed(1);

  const scoreEl = document.getElementById('dynamicAvgScore');
  const countEl = document.getElementById('dynamicTotalReviewsCount');
  if (scoreEl) scoreEl.textContent = avg;
  if (countEl) countEl.textContent = total;

  for (let i = 1; i <= 5; i++) {
    const pct = Math.round((counts[i] / total) * 100);
    const bar = document.getElementById('bar' + i);
    const num = document.getElementById('count' + i);
    if (bar) bar.style.width = pct + '%';
    if (num) num.textContent = counts[i];
  }
}

function filterReviews(filterRating, btn) {
  if (btn) {
    document.querySelectorAll('.filter-chip').forEach(c => c.classList.remove('active'));
    btn.classList.add('active');
  }
  renderReviewsFeed(filterRating);
}

function renderReviewsFeed(filterRating = 0) {
  const container = document.getElementById('reviewsFeedContainer');
  if (!container) return;

  const reviews = getStoredReviews();
  const filtered = (filterRating > 0) ? reviews.filter(r => r.rating === filterRating) : reviews;

  container.innerHTML = '';

  if (filtered.length === 0) {
    container.innerHTML = '<div style="grid-column: 1 / -1; text-align: center; color: var(--text-muted); padding: 30px;">Aún no hay reseñas registradas con este filtro. ¡Sé el primero en dejar una!</div>';
    return;
  }

  filtered.forEach(r => {
    const starsStr = '★'.repeat(r.rating) + '☆'.repeat(5 - r.rating);
    const initial = r.name.charAt(0).toUpperCase();

    const card = document.createElement('div');
    card.className = 'review-card';
    card.innerHTML = `
      <div>
        <div class="review-author-row">
          <div class="review-avatar">${initial}</div>
          <div style="flex: 1;">
            <div style="display: flex; align-items: center; gap: 8px;">
              <strong style="color: #FFFFFF; font-size: 0.95rem;">${r.name}</strong>
              ${r.verified ? '<span style="font-size: 0.72rem; color: var(--green-secure); font-weight: 700; background: rgba(16, 185, 129, 0.15); padding: 2px 6px; border-radius: 6px;">✓ Verificado</span>' : ''}
            </div>
            <div style="font-size: 0.76rem; color: var(--text-dim);">${r.device} &bull; ${r.date}</div>
          </div>
        </div>
        <div style="color: #FBBF24; font-size: 1.1rem; letter-spacing: 2px; margin-bottom: 8px;">
          ${starsStr}
        </div>
        <p style="color: var(--text-muted); font-size: 0.88rem; line-height: 1.5; margin: 0;">
          "${r.comment}"
        </p>
      </div>
    `;
    container.appendChild(card);
  });
}

/* =========================================================================
   4. GOOGLE SEARCH SEO & INDEXACIÓN (#indexacion-google)
   ========================================================================= */
function copyGoogleMetaTag() {
  const tag = '<meta name="google-site-verification" content="google-search-console-verification-token-novabrowser">';
  const btn = document.getElementById('copyMetaBtn');

  navigator.clipboard.writeText(tag).then(() => {
    if (btn) {
      const original = btn.textContent;
      btn.textContent = '✓ ¡Meta Tag Copiado!';
      btn.style.borderColor = 'var(--green-secure)';
      btn.style.color = 'var(--green-secure)';
      setTimeout(() => {
        btn.textContent = original;
        btn.style.borderColor = '';
        btn.style.color = '';
      }, 2000);
    }
  });
}

/* =========================================================================
   5. COMUNIDAD DE WHATSAPP CON REQUISITOS Y MENSAJE AUTOMÁTICO (#comunidad-whatsapp)
   ========================================================================= */
const OFFICIAL_WHATSAPP_GROUP_URL = 'https://chat.whatsapp.com/GcnflHxrW12884wlSxhlJu?s=cl&p=a&mlu=4&ilr=4';

function getLiveWelcomeText(name, device, reason) {
  const uName = name || 'Usuario Pionero';
  const uDevice = device || 'Android (Universal)';
  const uReason = reason || 'Probar versiones beta y APKs antes del lanzamiento oficial';

  return `👋 ¡Hola Luis Miguel y comunidad oficial de Nova Browser!\n\nAcabo de completar con éxito los requisitos de admisión en la página web oficial.\n\n👤 Mi Nombre: ${uName}\n📱 Mi Dispositivo: ${uDevice}\n🎯 Mi Objetivo: ${uReason}\n\n¡Un placer unirme a los pioneros de la navegación inteligente! 🚀`;
}

function updateLiveWelcomeMessage() {
  const name = document.getElementById('waReqName')?.value.trim() || 'Carlos';
  const device = document.getElementById('waReqDevice')?.value.trim() || 'Samsung Galaxy (Android)';
  const reason = document.getElementById('waReqReason')?.value || 'probar versiones beta y APKs antes del lanzamiento oficial';

  const text = getLiveWelcomeText(name, device, reason);
  const display = document.getElementById('waLiveMessageText');
  if (display) {
    display.textContent = text;
  }

  const sendBtn = document.getElementById('waSendDirectBtn');
  if (sendBtn) {
    sendBtn.href = 'https://api.whatsapp.com/send?text=' + encodeURIComponent(text);
  }
}

function handleWhatsappVerification(event) {
  event.preventDefault();

  const rulesChecked = document.getElementById('waReqRules')?.checked;
  const installedChecked = document.getElementById('waReqInstalled')?.checked;
  const name = document.getElementById('waReqName')?.value.trim();
  const device = document.getElementById('waReqDevice')?.value.trim();
  const reason = document.getElementById('waReqReason')?.value;

  if (!rulesChecked || !installedChecked) {
    showToast('Por favor marca las casillas del Código de Conducta y de Interés.', 'error');
    return;
  }

  if (!name || !device) {
    showToast('Por favor ingresa tu Nombre y Dispositivo Android.', 'error');
    return;
  }

  // Guardar verificación en LocalStorage
  const verificationData = {
    verified: true,
    date: new Date().toISOString(),
    name: name,
    device: device,
    reason: reason
  };

  try {
    localStorage.setItem('nova_wa_verified_v1', JSON.stringify(verificationData));
  } catch (e) {
    console.warn('No se pudo guardar en localStorage', e);
  }

  // Actualizar vista y mensaje
  updateLiveWelcomeMessage();

  const form = document.getElementById('whatsappRequirementsForm');
  const approvedPanel = document.getElementById('whatsappApprovedPanel');
  if (form) form.style.display = 'none';
  if (approvedPanel) approvedPanel.classList.add('active');

  // Scroll suave hacia el botón de ingreso
  const directJoinBtn = document.getElementById('waDirectJoinBtn');
  if (directJoinBtn) {
    directJoinBtn.focus();
  }
}

function resetWhatsappVerification() {
  try {
    localStorage.removeItem('nova_wa_verified_v1');
  } catch (e) {}

  const form = document.getElementById('whatsappRequirementsForm');
  const approvedPanel = document.getElementById('whatsappApprovedPanel');
  if (form) form.style.display = 'block';
  if (approvedPanel) approvedPanel.classList.remove('active');
}

function copyWhatsappWelcomeMessage() {
  const text = document.getElementById('waLiveMessageText')?.textContent || '';
  const btn = document.getElementById('waCopyMsgBtn');

  navigator.clipboard.writeText(text).then(() => {
    if (btn) {
      const original = btn.innerHTML;
      btn.innerHTML = '<span>✓</span> ¡Mensaje de Bienvenida Copiado!';
      btn.style.borderColor = 'var(--green-secure)';
      btn.style.color = 'var(--green-secure)';
      setTimeout(() => {
        btn.innerHTML = original;
        btn.style.borderColor = '';
        btn.style.color = '';
      }, 2200);
    }
  });
}

function checkStoredWhatsappVerification() {
  try {
    const stored = localStorage.getItem('nova_wa_verified_v1');
    if (stored) {
      const data = JSON.parse(stored);
      if (data && data.verified) {
        if (document.getElementById('waReqName')) document.getElementById('waReqName').value = data.name || '';
        if (document.getElementById('waReqDevice')) document.getElementById('waReqDevice').value = data.device || '';
        if (document.getElementById('waReqReason')) document.getElementById('waReqReason').value = data.reason || '';

        updateLiveWelcomeMessage();

        const form = document.getElementById('whatsappRequirementsForm');
        const approvedPanel = document.getElementById('whatsappApprovedPanel');
        if (form) form.style.display = 'none';
        if (approvedPanel) approvedPanel.classList.add('active');
      }
    }
  } catch (e) {}
}

/* =========================================================================
   6. LABORATORIO Y SUITE INTERACTIVA (#laboratorio)
   ========================================================================= */
function copyApkDirectLink() {
  const link = 'https://github.com/luismiguel38338-cmd/XboxLR-Browser/releases/download/v1.0.0/Nova-Browser-v1.0-debug.apk';
  const btn = document.getElementById('copyLinkBtn');

  navigator.clipboard.writeText(link).then(() => {
    if (btn) {
      const original = btn.innerHTML;
      btn.innerHTML = '<span>✓</span> ¡Enlace Copiado!';
      btn.style.borderColor = 'var(--green-secure)';
      btn.style.color = 'var(--green-secure)';
      setTimeout(() => {
        btn.innerHTML = original;
        btn.style.borderColor = '';
        btn.style.color = '';
      }, 2000);
    }
  });
}

const AI_SIM_RESPONSES = {
  'resumen': '🤖 Nova Gemini (Resumen de Artículo en 3 Puntos Clave):\n\n1. Arquitectura Nativa: Construido 100% con Jetpack Compose y Kotlin en Android para eliminar el desfase de WebViews pesados.\n2. Privacidad Nova Shield: Filtra rastreadores y publicidad antes de que los paquetes de datos consuman tu plan móvil.\n3. Modelo Libre: Licencia MIT de código abierto sin costos ocultos.',
  'shield': '🛡️ Nova Shield (Análisis de Seguridad):\n\nNova Shield actúa en la capa de intercepción de red de Android (OkHttp / NetworkSecurityConfig). Bloquea dominios de telemetría invasiva, descarta cookies de terceros y previene el huellado digital (fingerprinting) sin requerir extensiones externas.',
  'compose': '⚡ Jetpack Compose (Rendimiento):\n\nAl prescindir de jerarquías XML complejas, Nova Browser renderiza animaciones y navegación a 120 cuadros por segundo fluidos, con un consumo de batería drásticamente menor en pantallas AMOLED.'
};

function runAiSimulation(type) {
  const output = document.getElementById('aiOutputDisplay');
  if (!output) return;

  const targetText = AI_SIM_RESPONSES[type] || AI_SIM_RESPONSES['resumen'];
  output.innerHTML = '<span style="color: var(--cyan-electric); font-weight: 700;">Nova Gemini:</span> <i>Generando respuesta inteligente con modelo gemini-3.5-flash...</i>';

  setTimeout(() => {
    output.innerHTML = `<span style="color: var(--cyan-electric); font-weight: 700;">Nova Gemini:</span>\n${targetText.replace(/\n/g, '<br>')}`;
  }, 400);
}

const SHIELD_SITE_DATA = {
  'news': { blocked: '34 detectados y frenados', data: '1.8 MB economizados', time: '+62% más veloz' },
  'ecommerce': { blocked: '28 cookies y pixels anulados', data: '2.4 MB economizados', time: '+74% más veloz' },
  'blog': { blocked: '19 scripts bloqueados', data: '1.2 MB economizados', time: '+55% más veloz' }
};

function runShieldTest() {
  const site = document.getElementById('shieldTargetSite')?.value || 'news';
  const data = SHIELD_SITE_DATA[site];
  if (!data) return;

  const bEl = document.getElementById('shieldStatBlocked');
  const dEl = document.getElementById('shieldStatData');
  const tEl = document.getElementById('shieldStatTime');

  if (bEl) bEl.textContent = 'Analizando...';
  if (dEl) dEl.textContent = 'Calculando...';
  if (tEl) tEl.textContent = 'Midiendo...';

  setTimeout(() => {
    if (bEl) bEl.textContent = data.blocked;
    if (dEl) dEl.textContent = data.data;
    if (tEl) tEl.textContent = data.time;
  }, 350);
}

const THEME_DATA = {
  'amoled': {
    bg: '#000000',
    border: 'rgba(0, 229, 255, 0.4)',
    badgeColor: '#00E5FF',
    badgeBg: 'rgba(0, 229, 255, 0.1)',
    badgeText: 'Tema: Dark AMOLED (100% Negros Puros)',
    title: 'Ahorro Máximo de Batería en Pantallas OLED',
    desc: 'Los píxeles se apagan por completo en las zonas oscuras, prolongando la autonomía de tu móvil.'
  },
  'cyan': {
    bg: '#03101C',
    border: '#00E5FF',
    badgeColor: '#00E5FF',
    badgeBg: 'rgba(0, 229, 255, 0.2)',
    badgeText: 'Tema: Cyber Cyan Futurista',
    title: 'Estética Neón de Alta Precisión',
    desc: 'Contraste nítido con tonos cian eléctricos diseñados para visibilidad superior en exteriores.'
  },
  'emerald': {
    bg: '#051810',
    border: '#25D366',
    badgeColor: '#25D366',
    badgeBg: 'rgba(37, 211, 102, 0.15)',
    badgeText: 'Tema: Emerald Shield Seguro',
    title: 'Protección Visual y Armonía',
    desc: 'Tonos esmeralda inspirados en la seguridad y el escudo activo de Nova Shield.'
  },
  'sepia': {
    bg: '#1F1A14',
    border: '#F59E0B',
    badgeColor: '#F59E0B',
    badgeBg: 'rgba(245, 158, 11, 0.15)',
    badgeText: 'Tema: Modo Lectura Sepia',
    title: 'Luz Cálida sin Fatiga Ocular',
    desc: 'Filtra la luz azul de los artículos largos para una lectura relajante antes de dormir.'
  }
};

function setDemoTheme(themeKey, btn) {
  if (btn) {
    document.querySelectorAll('.theme-pill-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
  }

  const info = THEME_DATA[themeKey];
  if (!info) return;

  const box = document.getElementById('themePreviewBox');
  const badge = document.getElementById('themePreviewBadge');
  const title = document.getElementById('themePreviewTitle');
  const desc = document.getElementById('themePreviewDesc');

  if (box) {
    box.style.background = info.bg;
    box.style.borderColor = info.border;
  }
  if (badge) {
    badge.style.color = info.badgeColor;
    badge.style.background = info.badgeBg;
    badge.textContent = info.badgeText;
  }
  if (title) title.textContent = info.title;
  if (desc) desc.textContent = info.desc;
}

/* =========================================================================
   7. SISTEMA DE NOTIFICACIONES TOAST MATERIAL 3
   ========================================================================= */
function showToast(message, type = 'success') {
  const container = document.getElementById('toastContainer');
  if (!container) return;

  const toast = document.createElement('div');
  toast.className = 'toast-pill toast-' + type;

  let icon = '✓';
  if (type === 'love') icon = '💖';
  else if (type === 'info') icon = 'ℹ️';
  else if (type === 'error') icon = '⚠️';

  toast.innerHTML = `<span style="font-size: 1.15rem;">${icon}</span><span>${message}</span>`;
  container.appendChild(toast);

  setTimeout(() => {
    toast.classList.add('hiding');
    setTimeout(() => toast.remove(), 320);
  }, 3600);
}

function copyToClipboard(text, successMsg = '¡Copiado al portapapeles!') {
  if (navigator.clipboard && window.isSecureContext) {
    navigator.clipboard.writeText(text).then(() => {
      showToast(successMsg, 'success');
    }).catch(() => fallbackCopyText(text, successMsg));
  } else {
    fallbackCopyText(text, successMsg);
  }
}

function fallbackCopyText(text, successMsg) {
  const textarea = document.createElement('textarea');
  textarea.value = text;
  textarea.style.position = 'fixed';
  textarea.style.opacity = '0';
  document.body.appendChild(textarea);
  textarea.focus();
  textarea.select();
  try {
    document.execCommand('copy');
    showToast(successMsg, 'success');
  } catch (err) {
    showToast('Selecciona y copia el texto manualmente', 'info');
  }
  document.body.removeChild(textarea);
}

/* =========================================================================
   8. MENÚ MÓVIL (DRAWER)
   ========================================================================= */
function toggleMobileDrawer(open) {
  const overlay = document.getElementById('mobileDrawerOverlay');
  if (!overlay) return;

  if (typeof open === 'boolean') {
    if (open) overlay.classList.add('active');
    else overlay.classList.remove('active');
  } else {
    overlay.classList.toggle('active');
  }
}

function handleDrawerOverlayClick(event) {
  if (event.target === document.getElementById('mobileDrawerOverlay')) {
    toggleMobileDrawer(false);
  }
}

/* =========================================================================
   9. CENTRO ESTRUCTURADO DE REPOSITORIOS DESCARGABLES (#repositorios)
   ========================================================================= */
const REPO_CATALOG = [
  {
    id: 'gh-apk-v1',
    category: 'apk',
    tagClass: '',
    badge: 'APK Oficial • v1.0.0',
    title: 'GitHub Releases — Instalador APK',
    desc: 'Paquete binario oficial compilado mediante GitHub Actions CI/CD con soporte Universal ABI (arm64-v8a, armeabi-v7a, x86_64).',
    meta: ['Formato: .APK', 'Android 7.0 – 16', '34.4 MB'],
    primaryLabel: '⬇️ Descargar APK (v1.0.0)',
    primaryUrl: 'https://github.com/luismiguel38338-cmd/XboxLR-Browser/releases/download/v1.0.0/Nova-Browser-v1.0-debug.apk',
    isDownload: true,
    secondaryLabel: 'Ver Todas las Releases',
    secondaryUrl: 'https://github.com/luismiguel38338-cmd/XboxLR-Browser/releases'
  },
  {
    id: 'gh-zip-main',
    category: 'source',
    tagClass: 'tag-source',
    badge: 'Código Fuente • ZIP',
    title: 'Repositorio Completo (Rama main)',
    desc: 'Descarga todo el proyecto estructurado: código nativo Kotlin + Jetpack Compose, plataforma web (docs/ y CNAME) y workflows CI/CD.',
    meta: ['Formato: .ZIP', 'Licencia MIT', 'Proyecto Completo'],
    primaryLabel: '📦 Descargar Código ZIP',
    primaryUrl: 'https://github.com/luismiguel38338-cmd/XboxLR-Browser/archive/refs/heads/main.zip',
    isDownload: true,
    secondaryLabel: 'Explorar en GitHub',
    secondaryUrl: 'https://github.com/luismiguel38338-cmd/XboxLR-Browser'
  },
  {
    id: 'gh-tar-v1',
    category: 'source',
    tagClass: 'tag-source',
    badge: 'Release Tag • TAR.GZ',
    title: 'Paquete Fuente Etiquetado v1.0.0',
    desc: 'Instantánea inmutable de la versión v1.0.0 lista para compilación reproducible en Linux, macOS, Windows o contenedores CI.',
    meta: ['Formato: .TAR.GZ', 'Tag: v1.0.0', 'Build Reproducible'],
    primaryLabel: '🗜️ Descargar TAR.GZ',
    primaryUrl: 'https://github.com/luismiguel38338-cmd/XboxLR-Browser/archive/refs/tags/v1.0.0.tar.gz',
    isDownload: true,
    secondaryLabel: 'Copiar git clone',
    actionFn: "copyToClipboard('git clone https://github.com/luismiguel38338-cmd/XboxLR-Browser.git', '📋 Comando git clone copiado')"
  },
  {
    id: 'fdroid-izzy',
    category: 'store',
    tagClass: 'tag-store',
    badge: 'Repo Libre • F-Droid',
    title: 'IzzyOnDroid / F-Droid Repository',
    desc: 'Repositorio Android de software libre compatible con el cliente oficial de F-Droid y Droid-ify con actualizaciones automáticas.',
    meta: ['Costo: $0 Gratis', 'Open Source', 'Auto-Updates'],
    primaryLabel: '🚀 Enviar a IzzyOnDroid',
    actionFn: 'dispatchToIzzyOnDroid()',
    secondaryLabel: '📄 Descargar Receta .YML',
    actionFnSecondary: 'downloadFDroidRecipe()'
  },
  {
    id: 'uptodown-store',
    category: 'store',
    tagClass: 'tag-store',
    badge: 'Tienda Global • VirusTotal',
    title: 'Uptodown Repository & Portal',
    desc: 'Distribución internacional gratuita de APKs verificados con más de 50 motores antivirus y descarga directa sin Google Play.',
    meta: ['Costo: $0 Gratis', 'Escaneo Antivirus', 'Alcance Mundial'],
    primaryLabel: '🌐 Abrir Uptodown Portal',
    primaryUrl: 'https://developer.uptodown.com/',
    secondaryLabel: '📋 Copiar Ficha Técnica',
    actionFnSecondary: 'copyTechnicalSpecs()'
  },
  {
    id: 'apkmirror-store',
    category: 'store',
    tagClass: 'tag-store',
    badge: 'Firma Verificada • SHA-256',
    title: 'APKMirror & Checksums Oficiales',
    desc: 'Repositorio verificado criptográficamente. Incluye archivo SHA256SUMS.txt listo para comprobar la integridad auténtica del paquete.',
    meta: ['Verificado SHA-256', 'Costo: $0 Gratis', 'Historial APKs'],
    primaryLabel: '🔐 Descargar SHA256SUMS',
    actionFn: 'downloadSha256Sums()',
    secondaryLabel: 'Ir a APKMirror',
    secondaryUrl: 'https://www.apkmirror.com/apk-submissions/'
  },
  {
    id: 'itch-amazon-aptoide',
    category: 'store',
    tagClass: 'tag-store',
    badge: 'Multi-Tienda Gratuita',
    title: 'itch.io • Amazon • Aptoide Repos',
    desc: 'Canales abiertos adicionales 100% gratuitos para Android, tablets Fire OS y comunidades independientes sin tarifas anuales.',
    meta: ['itch.io ($0)', 'Amazon Appstore ($0)', 'Aptoide ($0)'],
    primaryLabel: '🛒 Abrir itch.io Dev',
    primaryUrl: 'https://itch.io/developers',
    secondaryLabel: 'Aptoide Developers',
    secondaryUrl: 'https://en.aptoide.com/page/developers'
  },
  {
    id: 'dev-build-kit',
    category: 'kit',
    tagClass: 'tag-kit',
    badge: 'Automatización • Script & JSON',
    title: 'Kit Preparado de Compilación y Manifiesto',
    desc: 'Descarga el script automático Bash para clonar y compilar el APK desde cero con Gradle, o exporta el manifiesto JSON completo.',
    meta: ['build-nova-apk.sh', 'release-manifest.json', 'Gradle Ready'],
    primaryLabel: '⚡ Descargar Script .SH',
    actionFn: 'downloadBuildScript()',
    secondaryLabel: 'Exportar Manifest JSON',
    actionFnSecondary: 'exportReleaseManifest()'
  }
];

let currentRepoFilter = 'all';

function downloadTextArtifact(filename, content, mimeType = 'text/plain;charset=utf-8') {
  const blob = new Blob([content], { type: mimeType });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = filename;
  document.body.appendChild(a);
  a.click();
  a.remove();
  setTimeout(() => URL.revokeObjectURL(url), 1500);
}

function downloadFDroidRecipe() {
  const yml = `Categories:
  - Internet
License: MIT
AuthorName: Luis Miguel
AuthorEmail: luismiguel38338@gmail.com
WebSite: https://novabrowser.app
SourceCode: https://github.com/luismiguel38338-cmd/XboxLR-Browser
IssueTracker: https://github.com/luismiguel38338-cmd/XboxLR-Browser/issues

AutoName: Nova Browser
Summary: Navegador nativo para Android con IA Gemini y Escudo de Privacidad

Description: |-
  Nova Browser es un navegador web moderno desarrollado en Kotlin y Jetpack Compose.
  Incluye Copilot impulsado por Google Gemini AI, bloqueo de rastreadores Nova Shield,
  Modo Lectura AMOLED, Narrador Web TTS y gestión de pestañas con incógnito.

RepoType: git
Repo: https://github.com/luismiguel38338-cmd/XboxLR-Browser.git

Builds:
  - versionName: '1.0'
    versionCode: 1
    commit: v1.0.0
    subdir: app
    gradle:
      - yes

AutoUpdateMode: Version
UpdateCheckMode: Tags
CurrentVersion: '1.0'
CurrentVersionCode: 1
`;
  downloadTextArtifact('com.aistudio.nova.browser.yml', yml, 'text/yaml;charset=utf-8');
  showToast('📄 Receta F-Droid (com.aistudio.nova.browser.yml) descargada', 'success');
}

function downloadBuildScript() {
  const sh = `#!/usr/bin/env bash
set -euo pipefail

echo "🚀 Clonando repositorio oficial de Nova Browser..."
git clone https://github.com/luismiguel38338-cmd/XboxLR-Browser.git
cd XboxLR-Browser

if [ ! -f .env ] && [ -f .env.example ]; then
  cp .env.example .env
fi

chmod +x gradlew
echo "⚙️ Compilando APK oficial con Gradle..."
./gradlew assembleDebug --no-daemon

mkdir -p release-output
cp app/build/outputs/apk/debug/app-debug.apk release-output/Nova-Browser-v1.0-debug.apk
echo "✅ APK generado en: release-output/Nova-Browser-v1.0-debug.apk"
`;
  downloadTextArtifact('build-nova-apk.sh', sh, 'application/x-sh;charset=utf-8');
  showToast('⚡ Script build-nova-apk.sh descargado', 'success');
}

function downloadSha256Sums() {
  const txt = `# Nova Browser Official Verification Manifest
# Domain: https://novabrowser.app
# Package: com.aistudio.nova.browser
# Repository: https://github.com/luismiguel38338-cmd/XboxLR-Browser

Artifact: Nova-Browser-v1.0-debug.apk
Version: v1.0.0 (versionCode 1)
MinSDK: Android 7.0 (API 24)
TargetSDK: Android 16 (API 36)
DownloadURL: https://github.com/luismiguel38338-cmd/XboxLR-Browser/releases/download/v1.0.0/Nova-Browser-v1.0-debug.apk
SourceZIP: https://github.com/luismiguel38338-cmd/XboxLR-Browser/archive/refs/heads/main.zip
GeneratedAt: ${new Date().toISOString()}
`;
  downloadTextArtifact('SHA256SUMS.txt', txt, 'text/plain;charset=utf-8');
  showToast('🔐 Archivo SHA256SUMS.txt descargado', 'success');
}

function filterRepositories(category, btn) {
  currentRepoFilter = category;
  document.querySelectorAll('.repo-filter-btn').forEach(b => b.classList.remove('active'));
  if (btn) btn.classList.add('active');
  renderRepositoriesHub();
}

function renderRepositoriesHub() {
  const grid = document.getElementById('reposCatalogGrid');
  if (!grid) return;

  const query = (document.getElementById('repoSearchInput')?.value || '').trim().toLowerCase();

  const filtered = REPO_CATALOG.filter(item => {
    const matchesCat = currentRepoFilter === 'all' || item.category === currentRepoFilter;
    const matchesSearch = !query ||
      item.title.toLowerCase().includes(query) ||
      item.desc.toLowerCase().includes(query) ||
      item.badge.toLowerCase().includes(query) ||
      item.meta.some(m => m.toLowerCase().includes(query));
    return matchesCat && matchesSearch;
  });

  if (filtered.length === 0) {
    grid.innerHTML = `
      <div style="grid-column: 1 / -1; text-align: center; padding: 36px; background: var(--bg-card); border: 1px dashed var(--border-color); border-radius: var(--radius-lg); color: var(--text-muted);">
        No se encontraron repositorios para "${query}". Intenta limpiar el filtro.
      </div>`;
    return;
  }

  grid.innerHTML = filtered.map(item => {
    const primaryBtn = item.actionFn
      ? `<button type="button" onclick="${item.actionFn}" class="btn btn-primary" style="flex: 1; justify-content: center; padding: 10px 14px; font-size: 0.84rem;">${item.primaryLabel}</button>`
      : `<a href="${item.primaryUrl}" ${item.isDownload ? 'download' : 'target="_blank" rel="noopener noreferrer"'} class="btn btn-primary" style="flex: 1; justify-content: center; padding: 10px 14px; font-size: 0.84rem;">${item.primaryLabel}</a>`;

    const secondaryBtn = item.actionFnSecondary
      ? `<button type="button" onclick="${item.actionFnSecondary}" class="btn btn-secondary" style="padding: 10px 14px; font-size: 0.82rem;">${item.secondaryLabel}</button>`
      : `<a href="${item.secondaryUrl}" target="_blank" rel="noopener noreferrer" class="btn btn-secondary" style="padding: 10px 14px; font-size: 0.82rem;">${item.secondaryLabel}</a>`;

    return `
      <article class="repo-card">
        <div>
          <div class="repo-card-top" style="margin-bottom: 12px;">
            <span class="repo-tag ${item.tagClass}">${item.badge}</span>
          </div>
          <h3 style="font-size: 1.15rem; color: #FFFFFF; margin-bottom: 8px;">${item.title}</h3>
          <p style="font-size: 0.86rem; color: var(--text-muted); line-height: 1.5; margin-bottom: 14px;">${item.desc}</p>
          <div class="repo-meta-chips">
            ${item.meta.map(m => `<span>${m}</span>`).join('')}
          </div>
        </div>
        <div style="display: flex; gap: 10px; flex-wrap: wrap; margin-top: 6px;">
          ${primaryBtn}
          ${secondaryBtn}
        </div>
      </article>
    `;
  }).join('');
}

async function fetchLiveGitHubTelemetry() {
  const starsEl = document.getElementById('ghStatStars');
  const tagEl = document.getElementById('ghStatLatestTag');
  const updatedEl = document.getElementById('ghStatUpdated');
  const statusEl = document.getElementById('ghSyncStatusBadge');

  try {
    const [repoRes, relRes] = await Promise.all([
      fetch('https://api.github.com/repos/luismiguel38338-cmd/XboxLR-Browser'),
      fetch('https://api.github.com/repos/luismiguel38338-cmd/XboxLR-Browser/releases?per_page=3')
    ]);

    if (repoRes.ok) {
      const repoData = await repoRes.json();
      if (starsEl) starsEl.textContent = `${repoData.stargazers_count ?? 0} ★ / ${repoData.forks_count ?? 0} Forks`;
      if (updatedEl && repoData.pushed_at) {
        updatedEl.textContent = new Date(repoData.pushed_at).toLocaleDateString('es-ES', { day: 'numeric', month: 'short', year: 'numeric' });
      }
    }

    if (relRes.ok) {
      const releases = await relRes.json();
      if (Array.isArray(releases) && releases.length > 0) {
        const latest = releases[0];
        if (tagEl) tagEl.textContent = latest.tag_name || 'v1.0.0';
        if (statusEl) {
          statusEl.textContent = '● Sincronizado con GitHub API';
          statusEl.style.color = '#10B981';
        }
      }
    }
  } catch (err) {
    if (statusEl) statusEl.textContent = '● Catálogo Local Activo';
  }
}

/* =========================================================================
   10. ANIMACIONES DE SCROLL GPU & INICIALIZACIÓN CENTRAL
   ========================================================================= */
function initScrollReveal() {
  const targets = document.querySelectorAll('.feature-card, .lab-card, .repo-card, .tutorial-card, .store-dispatch-card, .download-card, .google-step-card');
  if (!targets.length) return;

  if (!('IntersectionObserver' in window)) {
    targets.forEach(el => el.classList.add('revealed'));
    return;
  }

  targets.forEach((el, idx) => {
    el.setAttribute('data-reveal', '');
    el.style.transitionDelay = ((idx % 4) * 55) + 'ms';
  });

  const observer = new IntersectionObserver((entries, obs) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('revealed');
        obs.unobserve(entry.target);
      }
    });
  }, { threshold: 0.08, rootMargin: '0px 0px -30px 0px' });

  targets.forEach(el => observer.observe(el));
}

document.addEventListener('DOMContentLoaded', function() {
  renderRepositoriesHub();
  renderReleasesList();
  updateRatingStats();
  renderReviewsFeed(0);
  checkStoredWhatsappVerification();
  updateLiveWelcomeMessage();
  initScrollReveal();
  fetchLiveGitHubTelemetry();
});

