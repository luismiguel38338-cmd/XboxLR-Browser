/* =========================================================================
   9. MOTOR CÓSMICO: CREADOR DE GALAXIA DE AMOR (#galaxia-de-amor)
   ========================================================================= */
const GALAXY_PALETTES = {
  'rose': {
    primary: '#FF2E93',
    secondary: '#FF758C',
    accent: '#FFC0D3',
    nebula: 'rgba(255, 46, 147, 0.18)',
    core: '#FFF0F5'
  },
  'purple': {
    primary: '#A855F7',
    secondary: '#D946EF',
    accent: '#E879F9',
    nebula: 'rgba(168, 85, 247, 0.18)',
    core: '#FAF5FF'
  },
  'cyan': {
    primary: '#00E5FF',
    secondary: '#38BDF8',
    accent: '#818CF8',
    nebula: 'rgba(0, 229, 255, 0.16)',
    core: '#F0FDFA'
  },
  'gold': {
    primary: '#FBBF24',
    secondary: '#F59E0B',
    accent: '#FDE68A',
    nebula: 'rgba(251, 191, 36, 0.18)',
    core: '#FFFBEB'
  },
  'ruby': {
    primary: '#EF4444',
    secondary: '#F43F5E',
    accent: '#FDA4AF',
    nebula: 'rgba(239, 68, 68, 0.2)',
    core: '#FFF1F2'
  }
};

const GALAXY_POEM_PRESETS = {
  1: "En un universo de miles de millones de estrellas, tú eres mi constelación favorita. ✨",
  2: "Te amo desde la primera estrella que encendió el cosmos hasta el último suspiro del infinito. 💖",
  3: "Nuestras almas orbitan en una danza eterna que ninguna gravedad ni distancia podrá separar. 🌌"
};

let galaxyCurrentPalette = 'rose';
let galaxyCoreStyle = 'heart';
let galaxyStarCount = 450;
let galaxyStars = [];
let galaxyBursts = [];
let galaxyRotation = { x: 0.45, y: 0, targetX: 0.45, targetY: 0 };
let galaxyOrbitSpeed = 0.002;
let isGalaxyDragging = false;
let galaxyLastMouse = { x: 0, y: 0 };
let galaxyAnimFrameId = null;
let cosmicAudioCtx = null;
let cosmicAudioPlaying = false;
let cosmicAudioTimer = null;

class GalaxyStar {
  constructor(canvasWidth, canvasHeight, palette) {
    this.reset(canvasWidth, canvasHeight, palette, true);
  }

  reset(w, h, palette, initial = false) {
    const radiusDist = Math.pow(Math.random(), 1.5) * (Math.min(w, h) * 0.42);
    this.radius = radiusDist;
    this.angle = initial ? Math.random() * Math.PI * 2 : 0;
    this.speed = (0.0018 + (1 - this.radius / (Math.min(w, h) * 0.42)) * 0.0035) * (Math.random() > 0.5 ? 1 : 1);
    this.size = Math.random() * 2.2 + 0.6;
    this.twinklePhase = Math.random() * Math.PI * 2;
    this.twinkleSpeed = 0.03 + Math.random() * 0.05;
    this.z = (Math.random() - 0.5) * (this.radius * 0.35);

    const colors = [palette.primary, palette.secondary, palette.accent, palette.core, '#FFFFFF'];
    this.color = colors[Math.floor(Math.random() * colors.length)];
  }

  update(time) {
    this.angle += this.speed;
    this.twinklePhase += this.twinkleSpeed;
  }
}

class GalaxyHeartBurst {
  constructor(x, y, color) {
    this.x = x;
    this.y = y;
    this.vx = (Math.random() - 0.5) * 4.5;
    this.vy = (Math.random() - 0.5) * 4.5 - 1.5;
    this.life = 1.0;
    this.decay = 0.012 + Math.random() * 0.01;
    this.size = 10 + Math.random() * 12;
    this.color = color;
    this.rotation = Math.random() * Math.PI * 2;
    this.rotSpeed = (Math.random() - 0.5) * 0.08;
  }

  update() {
    this.x += this.vx;
    this.y += this.vy;
    this.rotation += this.rotSpeed;
    this.life -= this.decay;
  }

  draw(ctx) {
    if (this.life <= 0) return;
    ctx.save();
    ctx.translate(this.x, this.y);
    ctx.rotate(this.rotation);
    ctx.globalAlpha = Math.max(0, this.life);
    ctx.fillStyle = this.color;
    ctx.shadowColor = this.color;
    ctx.shadowBlur = 10;

    // Dibujar corazón
    const s = this.size * 0.05;
    ctx.beginPath();
    for (let t = 0; t <= Math.PI * 2; t += 0.1) {
      const hx = 16 * Math.pow(Math.sin(t), 3) * s;
      const hy = -(13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t)) * s;
      if (t === 0) ctx.moveTo(hx, hy);
      else ctx.lineTo(hx, hy);
    }
    ctx.closePath();
    ctx.fill();
    ctx.restore();
  }
}

let isGalaxyVisible = true;
let galaxyObserverInitialized = false;

function initGalaxyCanvas() {
  const canvas = document.getElementById('galaxyCanvas');
  if (!canvas) return;

  const container = document.getElementById('galaxyCanvasWrapper');
  if (!container) return;

  const rect = container.getBoundingClientRect();
  const w = rect.width || 560;
  const h = rect.height || 480;
  const dpr = Math.min(window.devicePixelRatio || 1, 2);

  canvas.width = w * dpr;
  canvas.height = h * dpr;

  const ctx = canvas.getContext('2d');
  ctx.setTransform(dpr, 0, 0, dpr, 0, 0);

  // Crear estrellas
  rebuildGalaxyStars(w, h);

  // Eventos de interacción
  setupGalaxyInteraction(canvas, container);

  // Pausar animación automáticamente fuera del viewport para máxima velocidad
  if ('IntersectionObserver' in window && !galaxyObserverInitialized) {
    galaxyObserverInitialized = true;
    const obs = new IntersectionObserver((entries) => {
      entries.forEach(entry => {
        isGalaxyVisible = entry.isIntersecting;
        if (isGalaxyVisible && !galaxyAnimFrameId) {
          renderGalaxyLoop();
        } else if (!isGalaxyVisible && galaxyAnimFrameId) {
          cancelAnimationFrame(galaxyAnimFrameId);
          galaxyAnimFrameId = null;
        }
      });
    }, { rootMargin: '120px' });
    obs.observe(container);
  }

  // Loop de renderizado
  if (galaxyAnimFrameId) cancelAnimationFrame(galaxyAnimFrameId);
  renderGalaxyLoop();

  // Cargar dedicatoria desde URL si viene con parámetros
  checkUrlGalaxyParams();
  loadVaultGalaxies();
}

function rebuildGalaxyStars(width, height) {
  const pal = GALAXY_PALETTES[galaxyCurrentPalette];
  galaxyStars = [];
  for (let i = 0; i < galaxyStarCount; i++) {
    galaxyStars.push(new GalaxyStar(width, height, pal));
  }
}

function setupGalaxyInteraction(canvas, container) {
  // Arrastre con ratón
  container.addEventListener('mousedown', (e) => {
    isGalaxyDragging = true;
    galaxyLastMouse = { x: e.clientX, y: e.clientY };
  });

  window.addEventListener('mousemove', (e) => {
    if (!isGalaxyDragging) return;
    const dx = e.clientX - galaxyLastMouse.x;
    const dy = e.clientY - galaxyLastMouse.y;
    galaxyRotation.targetY += dx * 0.005;
    galaxyRotation.targetX += dy * 0.005;
    // Limitar inclinación
    galaxyRotation.targetX = Math.max(-1.1, Math.min(1.1, galaxyRotation.targetX));
    galaxyLastMouse = { x: e.clientX, y: e.clientY };
  });

  window.addEventListener('mouseup', () => {
    isGalaxyDragging = false;
  });

  // Clic para corazones
  container.addEventListener('click', (e) => {
    const rect = container.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const y = e.clientY - rect.top;
    spawnHeartBurstAt(x, y);
  });

  // Toque móvil
  container.addEventListener('touchstart', (e) => {
    if (e.touches.length === 1) {
      isGalaxyDragging = true;
      galaxyLastMouse = { x: e.touches[0].clientX, y: e.touches[0].clientY };
    }
  }, { passive: true });

  window.addEventListener('touchmove', (e) => {
    if (!isGalaxyDragging || e.touches.length !== 1) return;
    const dx = e.touches[0].clientX - galaxyLastMouse.x;
    const dy = e.touches[0].clientY - galaxyLastMouse.y;
    galaxyRotation.targetY += dx * 0.007;
    galaxyRotation.targetX += dy * 0.007;
    galaxyRotation.targetX = Math.max(-1.1, Math.min(1.1, galaxyRotation.targetX));
    galaxyLastMouse = { x: e.touches[0].clientX, y: e.touches[0].clientY };
  }, { passive: true });

  window.addEventListener('touchend', () => {
    isGalaxyDragging = false;
  });

  window.addEventListener('resize', () => {
    const container = document.getElementById('galaxyCanvasWrapper');
    const canvas = document.getElementById('galaxyCanvas');
    if (!container || !canvas) return;
    const rect = container.getBoundingClientRect();
    if (!rect.width || !rect.height) return;
    const dpr = Math.min(window.devicePixelRatio || 1, 2);
    canvas.width = rect.width * dpr;
    canvas.height = rect.height * dpr;
    const ctx = canvas.getContext('2d');
    ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
    rebuildGalaxyStars(rect.width, rect.height);
  });
}

function spawnHeartBurstAt(x, y, count = 12) {
  const pal = GALAXY_PALETTES[galaxyCurrentPalette];
  for (let i = 0; i < count; i++) {
    const color = Math.random() > 0.4 ? pal.primary : (Math.random() > 0.5 ? pal.secondary : '#FFFFFF');
    galaxyBursts.push(new GalaxyHeartBurst(x, y, color));
  }
}

function triggerHeartBurst() {
  const container = document.getElementById('galaxyCanvasWrapper');
  if (!container) return;
  const rect = container.getBoundingClientRect();
  spawnHeartBurstAt(rect.width / 2, rect.height / 2, 28);
  showToast('💖 ¡Lluvia cósmica de amor enviada al universo!', 'love');
}

function renderGalaxyLoop() {
  if (!isGalaxyVisible) {
    galaxyAnimFrameId = null;
    return;
  }

  const canvas = document.getElementById('galaxyCanvas');
  const container = document.getElementById('galaxyCanvasWrapper');
  if (!canvas || !container) {
    galaxyAnimFrameId = null;
    return;
  }

  const rect = container.getBoundingClientRect();
  if (rect.width === 0 || rect.height === 0) {
    galaxyAnimFrameId = requestAnimationFrame(renderGalaxyLoop);
    return;
  }

  const ctx = canvas.getContext('2d');
  const w = rect.width;
  const h = rect.height;
  const cx = w / 2;
  const cy = h / 2;
  const pal = GALAXY_PALETTES[galaxyCurrentPalette];

  // Interpolación suave de rotación
  galaxyRotation.x += (galaxyRotation.targetX - galaxyRotation.x) * 0.08;
  galaxyRotation.y += (galaxyRotation.targetY - galaxyRotation.y) * 0.08;
  galaxyRotation.targetY += galaxyOrbitSpeed; // Rotación orbital dinámica

  // Fondo cósmico
  ctx.fillStyle = '#02030A';
  ctx.fillRect(0, 0, w, h);

  // Resplandor de nebulosa
  const nebGrad = ctx.createRadialGradient(cx, cy, 10, cx, cy, Math.min(w, h) * 0.48);
  nebGrad.addColorStop(0, pal.nebula);
  nebGrad.addColorStop(0.5, pal.nebula.replace('0.18', '0.06').replace('0.16', '0.05').replace('0.2', '0.07'));
  nebGrad.addColorStop(1, 'transparent');
  ctx.fillStyle = nebGrad;
  ctx.fillRect(0, 0, w, h);

  // Dibujar estrellas en proyección 3D
  const cosX = Math.cos(galaxyRotation.x);
  const sinX = Math.sin(galaxyRotation.x);
  const cosY = Math.cos(galaxyRotation.y);
  const sinY = Math.sin(galaxyRotation.y);

  // Ordenar estrellas por profundidad Z para renderizado realista
  const projectedStars = [];

  for (let i = 0; i < galaxyStars.length; i++) {
    const star = galaxyStars[i];
    star.update();

    // Coordenadas en el disco galáctico
    const sx = Math.cos(star.angle) * star.radius;
    const sz = Math.sin(star.angle) * star.radius;
    const sy = star.z;

    // Rotación alrededor de Y y de X
    const rotY_x = sx * cosY - sz * sinY;
    const rotY_z = sx * sinY + sz * cosY;

    const rotX_y = sy * cosX - rotY_z * sinX;
    const rotX_z = sy * sinX + rotY_z * cosX;

    // Proyección de perspectiva
    const fov = 400;
    const scale = fov / (fov + rotX_z);
    const screenX = cx + rotY_x * scale;
    const screenY = cy + rotX_y * scale;

    projectedStars.push({
      x: screenX,
      y: screenY,
      scale: scale,
      z: rotX_z,
      size: star.size,
      color: star.color,
      twinkle: (Math.sin(star.twinklePhase) + 1) * 0.5
    });
  }

  // Ordenar de atrás hacia adelante
  projectedStars.sort((a, b) => b.z - a.z);

  for (let i = 0; i < projectedStars.length; i++) {
    const s = projectedStars[i];
    if (s.x < -20 || s.x > w + 20 || s.y < -20 || s.y > h + 20) continue;

    const finalSize = Math.max(0.5, s.size * s.scale);
    const alpha = Math.min(1, Math.max(0.15, (0.4 + s.twinkle * 0.6) * s.scale));

    ctx.globalAlpha = alpha;
    ctx.fillStyle = s.color;
    ctx.beginPath();
    ctx.arc(s.x, s.y, finalSize, 0, Math.PI * 2);
    ctx.fill();

    if (s.size > 2 && s.scale > 0.8) {
      ctx.globalAlpha = alpha * 0.28;
      ctx.beginPath();
      ctx.arc(s.x, s.y, finalSize * 2.2, 0, Math.PI * 2);
      ctx.fill();
    }
  }
  ctx.globalAlpha = 1;

  // Dibujar Núcleo Galáctico
  drawGalaxyCore(ctx, cx, cy, pal);

  // Actualizar y dibujar partículas de explosión de amor
  for (let i = galaxyBursts.length - 1; i >= 0; i--) {
    const burst = galaxyBursts[i];
    burst.update();
    burst.draw(ctx);
    if (burst.life <= 0) {
      galaxyBursts.splice(i, 1);
    }
  }

  galaxyAnimFrameId = requestAnimationFrame(renderGalaxyLoop);
}

function drawGalaxyCore(ctx, cx, cy, pal) {
  const now = Date.now() * 0.003;
  const pulse = 1 + Math.sin(now) * 0.08;

  ctx.save();
  ctx.translate(cx, cy);

  if (galaxyCoreStyle === 'heart') {
    // Corazón Cósmico Radiante
    const s = 1.6 * pulse;
    ctx.shadowColor = pal.primary;
    ctx.shadowBlur = 24;
    ctx.fillStyle = pal.primary;

    ctx.beginPath();
    for (let t = 0; t <= Math.PI * 2; t += 0.1) {
      const x = 16 * Math.pow(Math.sin(t), 3) * s;
      const y = -(13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t)) * s;
      if (t === 0) ctx.moveTo(x, y);
      else ctx.lineTo(x, y);
    }
    ctx.closePath();
    ctx.fill();

    // Resplandor interior blanco
    ctx.fillStyle = pal.core;
    ctx.shadowBlur = 10;
    ctx.beginPath();
    for (let t = 0; t <= Math.PI * 2; t += 0.1) {
      const x = 16 * Math.pow(Math.sin(t), 3) * (s * 0.5);
      const y = -(13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t)) * (s * 0.5);
      if (t === 0) ctx.moveTo(x, y);
      else ctx.lineTo(x, y);
    }
    ctx.closePath();
    ctx.fill();

  } else if (galaxyCoreStyle === 'twinstars') {
    // Almas gemelas orbitando
    const orbitR = 24 * pulse;
    const a1 = now * 1.5;
    const a2 = a1 + Math.PI;

    const x1 = Math.cos(a1) * orbitR;
    const y1 = Math.sin(a1) * (orbitR * 0.5);
    const x2 = Math.cos(a2) * orbitR;
    const y2 = Math.sin(a2) * (orbitR * 0.5);

    // Puente de luz
    ctx.strokeStyle = pal.secondary;
    ctx.lineWidth = 2;
    ctx.globalAlpha = 0.6;
    ctx.beginPath();
    ctx.moveTo(x1, y1);
    ctx.lineTo(x2, y2);
    ctx.stroke();

    // Estrella 1
    ctx.shadowColor = pal.primary;
    ctx.shadowBlur = 20;
    ctx.fillStyle = pal.primary;
    ctx.beginPath();
    ctx.arc(x1, y1, 8 * pulse, 0, Math.PI * 2);
    ctx.fill();

    // Estrella 2
    ctx.shadowColor = pal.accent;
    ctx.shadowBlur = 20;
    ctx.fillStyle = pal.accent;
    ctx.beginPath();
    ctx.arc(x2, y2, 8 * pulse, 0, Math.PI * 2);
    ctx.fill();

  } else if (galaxyCoreStyle === 'spiral') {
    // Espiral Cósmica
    ctx.strokeStyle = pal.primary;
    ctx.shadowColor = pal.primary;
    ctx.shadowBlur = 18;
    ctx.lineWidth = 3;

    for (let arm = 0; arm < 2; arm++) {
      ctx.beginPath();
      const armOffset = arm * Math.PI;
      for (let a = 0; a < Math.PI * 3; a += 0.1) {
        const r = a * 10 * pulse;
        const x = Math.cos(a + armOffset + now) * r;
        const y = Math.sin(a + armOffset + now) * (r * 0.5);
        if (a === 0) ctx.moveTo(x, y);
        else ctx.lineTo(x, y);
      }
      ctx.stroke();
    }

    ctx.fillStyle = '#FFFFFF';
    ctx.beginPath();
    ctx.arc(0, 0, 9 * pulse, 0, Math.PI * 2);
    ctx.fill();

  } else {
    // Agujero de Amor Cósmico
    ctx.fillStyle = '#000000';
    ctx.shadowColor = pal.primary;
    ctx.shadowBlur = 30;
    ctx.beginPath();
    ctx.arc(0, 0, 16 * pulse, 0, Math.PI * 2);
    ctx.fill();

    ctx.strokeStyle = pal.secondary;
    ctx.lineWidth = 3;
    ctx.beginPath();
    ctx.ellipse(0, 0, 32 * pulse, 12 * pulse, now, 0, Math.PI * 2);
    ctx.stroke();
  }

  ctx.restore();
}

/* =========================================================================
   10. MÚSICA CÓSMICA CON WEB AUDIO API (432Hz Y TONOS ARMÓNICOS)
   ========================================================================= */
function toggleCosmicLoveAudio() {
  if (cosmicAudioPlaying) {
    stopCosmicLoveAudio();
  } else {
    startCosmicLoveAudio();
  }
}

function startCosmicLoveAudio() {
  try {
    const AudioCtxClass = window.AudioContext || window.webkitAudioContext;
    if (!AudioCtxClass) {
      showToast('Tu navegador no soporta sintetizador de audio web', 'info');
      return;
    }

    if (!cosmicAudioCtx) {
      cosmicAudioCtx = new AudioCtxClass();
    }

    if (cosmicAudioCtx.state === 'suspended') {
      cosmicAudioCtx.resume();
    }

    cosmicAudioPlaying = true;
    document.getElementById('audioIcon').textContent = '⏸️';
    document.getElementById('audioText').textContent = 'Pausar Música Cósmica';
    showToast('🎵 Música cósmica activada (Tonos de amor en 432 Hz)', 'love');

    // Notas pentatónicas celestiales relajantes
    const notes = [432, 540, 648, 720, 864];
    let noteIdx = 0;

    function playChime() {
      if (!cosmicAudioPlaying || !cosmicAudioCtx) return;

      const osc = cosmicAudioCtx.createOscillator();
      const gain = cosmicAudioCtx.createGain();

      osc.type = 'sine';
      osc.frequency.setValueAtTime(notes[noteIdx % notes.length], cosmicAudioCtx.currentTime);
      noteIdx++;

      // Filtro para suavizar
      const filter = cosmicAudioCtx.createBiquadFilter();
      filter.type = 'lowpass';
      filter.frequency.setValueAtTime(1200, cosmicAudioCtx.currentTime);

      gain.gain.setValueAtTime(0.001, cosmicAudioCtx.currentTime);
      gain.gain.exponentialRampToValueAtTime(0.12, cosmicAudioCtx.currentTime + 0.4);
      gain.gain.exponentialRampToValueAtTime(0.0001, cosmicAudioCtx.currentTime + 3.2);

      osc.connect(filter);
      filter.connect(gain);
      gain.connect(cosmicAudioCtx.destination);

      osc.start();
      osc.stop(cosmicAudioCtx.currentTime + 3.3);

      cosmicAudioTimer = setTimeout(playChime, 1800 + Math.random() * 1200);
    }

    playChime();

  } catch (err) {
    console.warn('Error al iniciar audio cósmico:', err);
  }
}

function stopCosmicLoveAudio() {
  cosmicAudioPlaying = false;
  if (cosmicAudioTimer) clearTimeout(cosmicAudioTimer);
  const icon = document.getElementById('audioIcon');
  const text = document.getElementById('audioText');
  if (icon) icon.textContent = '🎵';
  if (text) text.textContent = 'Activar Música Cósmica (432Hz)';
  showToast('Música cósmica pausada', 'info');
}

function toggleGalaxyRotation() {
  if (galaxyOrbitSpeed > 0) {
    galaxyOrbitSpeed = 0;
    const icon = document.getElementById('rotateIcon');
    const text = document.getElementById('rotateText');
    if (icon) icon.textContent = '▶️';
    if (text) text.textContent = 'Girar Cosmos';
    showToast('Rotación orbital pausada', 'info');
  } else {
    galaxyOrbitSpeed = 0.002;
    const icon = document.getElementById('rotateIcon');
    const text = document.getElementById('rotateText');
    if (icon) icon.textContent = '⏸️';
    if (text) text.textContent = 'Pausar Giro';
    showToast('Rotación orbital reanudada ✨', 'love');
  }
}

function toggleGalaxyFullscreen() {
  const container = document.getElementById('galaxyCanvasWrapper');
  if (!container) return;

  if (!document.fullscreenElement) {
    if (container.requestFullscreen) {
      container.requestFullscreen().catch(() => {
        showToast('Modo pantalla completa no disponible en este navegador', 'info');
      });
    } else if (container.webkitRequestFullscreen) {
      container.webkitRequestFullscreen();
    }
  } else {
    if (document.exitFullscreen) {
      document.exitFullscreen();
    }
  }
}

/* =========================================================================
   11. CONTROLES Y PERSONALIZACIÓN DE LA GALAXIA
   ========================================================================= */
function setGalaxyPoemPreset(num) {
  const msg = GALAXY_POEM_PRESETS[num] || GALAXY_POEM_PRESETS[1];
  const textarea = document.getElementById('galaxyInputMsg');
  if (textarea) {
    textarea.value = msg;
    syncGalaxyOverlayText();
  }
  showToast('Poema cósmico aplicado', 'love');
}

function setGalaxyPalette(palKey, btn) {
  galaxyCurrentPalette = palKey;
  if (btn) {
    document.querySelectorAll('.galaxy-palette-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
  }

  const container = document.getElementById('galaxyCanvasWrapper');
  if (container) {
    const rect = container.getBoundingClientRect();
    rebuildGalaxyStars(rect.width, rect.height);
  }

  showToast('Paleta ' + palKey.toUpperCase() + ' aplicada al cosmos ✨', 'love');
}

function updateGalaxyPhysics() {
  const select = document.getElementById('galaxyCoreStyle');
  if (select) {
    galaxyCoreStyle = select.value;
  }
}

function handleStarCountChange(val) {
  galaxyStarCount = parseInt(val, 10) || 450;
  const label = document.getElementById('starCountLabel');
  if (label) label.textContent = galaxyStarCount;

  const container = document.getElementById('galaxyCanvasWrapper');
  if (container) {
    const rect = container.getBoundingClientRect();
    rebuildGalaxyStars(rect.width, rect.height);
  }
}

function syncGalaxyOverlayText() {
  const name = document.getElementById('galaxyInputName')?.value.trim() || 'Luis Miguel & Mi Amor Especial';
  const msg = document.getElementById('galaxyInputMsg')?.value.trim() || 'En un universo de miles de millones de estrellas, tú eres mi constelación favorita. ✨';

  const titleEl = document.getElementById('galaxyOverlayTitle');
  const msgEl = document.getElementById('galaxyOverlayMsg');

  if (titleEl) titleEl.textContent = name;
  if (msgEl) msgEl.textContent = '"' + msg + '"';
}

function handleApplyGalaxyCustomization(event) {
  event.preventDefault();
  syncGalaxyOverlayText();

  const container = document.getElementById('galaxyCanvasWrapper');
  if (container) {
    const rect = container.getBoundingClientRect();
    rebuildGalaxyStars(rect.width, rect.height);
    spawnHeartBurstAt(rect.width / 2, rect.height / 2, 20);
  }

  showToast('¡Galaxia de Amor regenerada con éxito! ✨', 'love');
}

/* =========================================================================
   12. COMPARTIR, DESCARGAR IMAGEN Y GUARDAR EN BÓVEDA
   ========================================================================= */
function downloadGalaxyImage() {
  const canvas = document.getElementById('galaxyCanvas');
  if (!canvas) return;

  const name = document.getElementById('galaxyInputName')?.value.trim() || 'Luis Miguel & Amor Cósmico';
  const msg = document.getElementById('galaxyInputMsg')?.value.trim() || 'Tú eres mi estrella favorita.';

  // Crear lienzo compuesto con dedicatoria enmarcada
  const exportCanvas = document.createElement('canvas');
  exportCanvas.width = 1200;
  exportCanvas.height = 900;
  const eCtx = exportCanvas.getContext('2d');

  // 1. Dibujar galaxia escalada
  eCtx.drawImage(canvas, 0, 0, exportCanvas.width, exportCanvas.height);

  // 2. Marco oscuro elegante inferior
  const grad = eCtx.createLinearGradient(0, exportCanvas.height - 240, 0, exportCanvas.height);
  grad.addColorStop(0, 'rgba(4, 6, 15, 0)');
  grad.addColorStop(0.3, 'rgba(4, 6, 15, 0.85)');
  grad.addColorStop(1, 'rgba(4, 6, 15, 0.98)');
  eCtx.fillStyle = grad;
  eCtx.fillRect(0, exportCanvas.height - 240, exportCanvas.width, 240);

  // 3. Título de la dedicatoria
  eCtx.font = 'bold 36px system-ui, -apple-system, sans-serif';
  eCtx.fillStyle = '#FFFFFF';
  eCtx.textAlign = 'center';
  eCtx.shadowColor = '#FF2E93';
  eCtx.shadowBlur = 15;
  eCtx.fillText('🌌 ' + name, exportCanvas.width / 2, exportCanvas.height - 130);

  // 4. Mensaje
  eCtx.font = 'italic 22px system-ui, -apple-system, sans-serif';
  eCtx.fillStyle = '#FFC0D3';
  eCtx.shadowBlur = 8;
  eCtx.fillText('"' + msg + '"', exportCanvas.width / 2, exportCanvas.height - 85);

  // 5. Firma de Nova Browser
  eCtx.font = '600 16px system-ui, -apple-system, sans-serif';
  eCtx.fillStyle = 'rgba(255, 255, 255, 0.5)';
  eCtx.shadowBlur = 0;
  eCtx.fillText('Creado con Nova Browser • Universo de Amor Infinito', exportCanvas.width / 2, exportCanvas.height - 40);

  // Descargar enlace
  const link = document.createElement('a');
  link.download = 'galaxia-de-amor-' + encodeURIComponent(name.toLowerCase().replace(/\s+/g, '-')) + '.png';
  link.href = exportCanvas.toDataURL('image/png');
  link.click();

  showToast('📸 ¡Imagen de tu Galaxia descargada en alta resolución!', 'success');
}

function shareGalaxyWhatsApp() {
  const name = document.getElementById('galaxyInputName')?.value.trim() || 'Persona Especial';
  const msg = document.getElementById('galaxyInputMsg')?.value.trim() || 'Eres mi estrella favorita en el universo.';

  const shareUrl = window.location.origin + window.location.pathname + '#galaxia-de-amor?to=' + encodeURIComponent(name) + '&msg=' + encodeURIComponent(msg) + '&pal=' + galaxyCurrentPalette;

  const waText = `✨🌌 ¡He creado una Galaxia de Amor en el universo para ti!*\n\n👑 *Dedicatoria:* ${name}\n💌 *Mensaje Cósmico:* "${msg}"\n\n🪐 Entra a ver tu constelación interactiva en 3D aquí:\n${shareUrl}`;

  const waLink = 'https://api.whatsapp.com/send?text=' + encodeURIComponent(waText);
  window.open(waLink, '_blank');
  showToast('📲 Abriendo WhatsApp con tu dedicatoria cósmica...', 'love');
}

function copyGalaxyDedicationLink() {
  const name = document.getElementById('galaxyInputName')?.value.trim() || 'Mi Amor';
  const msg = document.getElementById('galaxyInputMsg')?.value.trim() || 'Tú eres mi constelación favorita.';

  const shareUrl = window.location.origin + window.location.pathname + '#galaxia-de-amor?to=' + encodeURIComponent(name) + '&msg=' + encodeURIComponent(msg) + '&pal=' + galaxyCurrentPalette;

  copyToClipboard(shareUrl, '📋 ¡Enlace mágico de tu Galaxia copiado al portapapeles!');
}

function checkUrlGalaxyParams() {
  try {
    const hash = window.location.hash;
    if (hash.includes('galaxia-de-amor?')) {
      const queryString = hash.split('?')[1];
      const params = new URLSearchParams(queryString);

      const to = params.get('to');
      const msg = params.get('msg');
      const pal = params.get('pal');

      if (to && document.getElementById('galaxyInputName')) {
        document.getElementById('galaxyInputName').value = decodeURIComponent(to);
      }
      if (msg && document.getElementById('galaxyInputMsg')) {
        document.getElementById('galaxyInputMsg').value = decodeURIComponent(msg);
      }
      if (pal && GALAXY_PALETTES[pal]) {
        galaxyCurrentPalette = pal;
        document.querySelectorAll('.galaxy-palette-btn').forEach(b => {
          if (b.getAttribute('onclick').includes(pal)) b.classList.add('active');
          else b.classList.remove('active');
        });
      }

      syncGalaxyOverlayText();
      showToast('✨ ¡Dedicatoria especial recibida del cosmos!', 'love');
    }
  } catch (e) {
    console.warn('Error leyendo parámetros de galaxia:', e);
  }
}

/* Bóveda en LocalStorage */
function saveGalaxyToVault() {
  const name = document.getElementById('galaxyInputName')?.value.trim() || 'Amor Cósmico';
  const msg = document.getElementById('galaxyInputMsg')?.value.trim() || 'Constelación eterna';

  const item = {
    id: 'gal-' + Date.now(),
    name: name,
    msg: msg,
    palette: galaxyCurrentPalette,
    core: galaxyCoreStyle,
    date: new Date().toLocaleDateString('es-ES', { month: 'short', day: 'numeric', year: 'numeric' })
  };

  try {
    const current = JSON.parse(localStorage.getItem('nova_galaxy_vault') || '[]');
    current.unshift(item);
    localStorage.setItem('nova_galaxy_vault', JSON.stringify(current));
    loadVaultGalaxies();
    showToast('💾 ¡Galaxia guardada en tu Bóveda Cósmica!', 'love');
  } catch (e) {
    showToast('No se pudo guardar la galaxia', 'error');
  }
}

function loadVaultGalaxies() {
  const container = document.getElementById('galaxyVaultContainer');
  const countLabel = document.getElementById('vaultCountLabel');
  if (!container) return;

  try {
    const list = JSON.parse(localStorage.getItem('nova_galaxy_vault') || '[]');
    if (countLabel) countLabel.textContent = list.length + ' guardadas';

    if (list.length === 0) {
      container.innerHTML = '<div style="text-align: center; color: var(--text-dim); font-size: 0.82rem; padding: 14px 0;">Aún no has guardado ninguna galaxia. ¡Personaliza una y pulsa "Guardar en Bóveda"!</div>';
      return;
    }

    container.innerHTML = '';
    list.forEach(g => {
      const card = document.createElement('div');
      card.className = 'vault-item-card';
      card.innerHTML = `
        <div>
          <strong style="color: #FFFFFF; font-size: 0.88rem;">${g.name}</strong>
          <div style="font-size: 0.74rem; color: #FFC0D3; max-width: 220px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">"${g.msg}"</div>
          <span style="font-size: 0.7rem; color: var(--text-dim);">${g.date}</span>
        </div>
        <div style="display: flex; gap: 6px;">
          <button type="button" class="btn btn-secondary" style="padding: 4px 8px; font-size: 0.75rem;" onclick="applyVaultGalaxy('${g.id}')">Ver</button>
          <button type="button" class="btn btn-secondary" style="padding: 4px 8px; font-size: 0.75rem; color: #EF4444;" onclick="deleteVaultGalaxy('${g.id}')">&times;</button>
        </div>
      `;
      container.appendChild(card);
    });

  } catch (e) {}
}

function applyVaultGalaxy(id) {
  try {
    const list = JSON.parse(localStorage.getItem('nova_galaxy_vault') || '[]');
    const found = list.find(g => g.id === id);
    if (!found) return;

    if (document.getElementById('galaxyInputName')) document.getElementById('galaxyInputName').value = found.name;
    if (document.getElementById('galaxyInputMsg')) document.getElementById('galaxyInputMsg').value = found.msg;
    if (document.getElementById('galaxyCoreStyle')) document.getElementById('galaxyCoreStyle').value = found.core || 'heart';
    galaxyCoreStyle = found.core || 'heart';

    if (found.palette && GALAXY_PALETTES[found.palette]) {
      galaxyCurrentPalette = found.palette;
      document.querySelectorAll('.galaxy-palette-btn').forEach(b => {
        if (b.getAttribute('onclick').includes(found.palette)) b.classList.add('active');
        else b.classList.remove('active');
      });
    }

    syncGalaxyOverlayText();
    const container = document.getElementById('galaxyCanvasWrapper');
    if (container) {
      const rect = container.getBoundingClientRect();
      rebuildGalaxyStars(rect.width, rect.height);
      spawnHeartBurstAt(rect.width / 2, rect.height / 2, 24);
    }

    showToast('Galaxia "' + found.name + '" cargada desde tu bóveda ✨', 'love');
  } catch (e) {}
}

function deleteVaultGalaxy(id) {
  try {
    let list = JSON.parse(localStorage.getItem('nova_galaxy_vault') || '[]');
    list = list.filter(g => g.id !== id);
    localStorage.setItem('nova_galaxy_vault', JSON.stringify(list));
    loadVaultGalaxies();
    showToast('Galaxia eliminada de la bóveda', 'info');
  } catch (e) {}
}

// Inicialización del motor Galaxia de Amor al cargar el DOM
document.addEventListener('DOMContentLoaded', function() {
  initGalaxyCanvas();
});
