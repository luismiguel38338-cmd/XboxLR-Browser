<p align="center">
  <img src="public/nova-browser-logo.png" alt="Nova Browser Logo" width="160" style="border-radius: 28px; box-shadow: 0 8px 24px rgba(0, 229, 255, 0.35);" />
</p>

<h1 align="center">Nova Browser</h1>

<p align="center">
  <b>Navegador web moderno, ultrarrápido y seguro para Android, diseñado con Jetpack Compose, Material Design 3 y copiloto de Inteligencia Artificial impulsado por Google Gemini.</b>
</p>

<p align="center">
  <a href="https://github.com/luismiguel38338-cmd/Nova-Browser/releases"><img src="https://img.shields.io/badge/Descargar%20APK-v1.0.0-00E5FF?style=for-the-badge&logo=android&logoColor=black" alt="Descargar APK" /></a>
  <a href="https://github.com/luismiguel38338-cmd/Nova-Browser/actions"><img src="https://img.shields.io/badge/CI%2FCD-Build%20Passing-success?style=for-the-badge&logo=githubactions&logoColor=white" alt="Build Status" /></a>
  <img src="https://img.shields.io/badge/Android-7.0%2B%20(API%2024%2B)-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android Version" />
  <img src="https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin" />
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose%20M3-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose" />
  <img src="https://img.shields.io/badge/AI-Google%20Gemini-EA4335?style=for-the-badge&logo=google&logoColor=white" alt="Google Gemini" />
  <a href="LICENSE"><img src="https://img.shields.io/badge/Licencia-MIT-00C853?style=for-the-badge" alt="MIT License" /></a>
</p>

---

## 📌 Tabla de Contenidos

1. [Descripción General](#-descripción-general)
2. [Capturas de Pantalla Reales](#-capturas-de-pantalla-reales)
3. [Características Destacadas](#-características-destacadas)
4. [Módulos y Funcionalidades](#-módulos-y-funcionalidades)
5. [Nova AI Copilot (Google Gemini)](#-nova-ai-copilot-google-gemini)
6. [Escudo de Privacidad Nova Shield](#-escudo-de-privacidad-nova-shield)
7. [Tecnologías y Arquitectura](#-tecnologías-y-arquitectura)
8. [Requisitos del Sistema](#-requisitos-del-sistema)
9. [Instalación y Configuración](#-instalación-y-configuración)
10. [Cómo Compilar y Generar el APK](#-cómo-compilar-y-generar-el-apk)
11. [Política de Distribución (100% Gratis - Sin PlayStation)](#-política-de-distribución-100-gratis---sin-playstation)
12. [Estructura del Repositorio](#-estructura-del-repositorio)
13. [Cómo Contribuir](#-cómo-contribuir)
14. [Licencia](#-licencia)
15. [Desarrollador](#-desarrollador)

---

## 📖 Descripción General

**Nova Browser** es una aplicación de navegación web nativa para dispositivos Android desarrollada con **Jetpack Compose** y los principios de diseño de **Material Design 3**. Su propósito es ofrecer una experiencia de navegación ágil, estética y privada, combinando un tema visual cósmico (obsidiana oscura, azul zafiro y acentos en cian eléctrico `#00E5FF`) con un conjunto de herramientas avanzadas:

- **Nova AI Copilot**: Inteligencia Artificial integrada mediante la API de Google Gemini para resumir artículos, responder dudas contextuales y traducir contenido web al instante.
- **Nova Shield**: Sistema de filtrado y protección activa que bloquea anuncios invasivos, rastreadores de telemetría y scripts analíticos de terceros.
- **Modo Lectura & Búsqueda en Página**: Herramientas integradas para consumir contenido sin distracciones visuales y localizar términos con conteo en tiempo real.
- **Privacidad Local Garantizada**: Almacenamiento local mediante SQLite y Room. Los datos de navegación, marcadores e historial permanecen siempre en tu dispositivo.

---

## 📱 Capturas de Pantalla Reales

A continuación se muestran las interfaces reales y el diseño visual de **Nova Browser**:

<table align="center">
  <tr>
    <td align="center" width="33%">
      <b>Pantalla de Inicio</b><br/><br/>
      <img src="public/screenshots/home.png" alt="Pantalla de Inicio Nova Browser" width="240" style="border-radius: 16px; border: 1px solid #1E293B;" />
      <br/><br/><em>Buscador multiconfigurable y accesos rápidos</em>
    </td>
    <td align="center" width="33%">
      <b>Navegación Web Activa</b><br/><br/>
      <img src="public/screenshots/browser.png" alt="Navegador Web Nova Browser" width="240" style="border-radius: 16px; border: 1px solid #1E293B;" />
      <br/><br/><em>Barra con cifrado SSL, escudo y barra inferior</em>
    </td>
    <td align="center" width="33%">
      <b>Nova AI Copilot</b><br/><br/>
      <img src="public/screenshots/ai.png" alt="Copiloto de IA Nova Browser" width="240" style="border-radius: 16px; border: 1px solid #1E293B;" />
      <br/><br/><em>Resumen de lectura y asistencia con Gemini</em>
    </td>
  </tr>
  <tr>
    <td align="center" width="33%">
      <b>Gestor de Pestañas</b><br/><br/>
      <img src="public/screenshots/tabs.png" alt="Gestor de Pestañas Nova Browser" width="240" style="border-radius: 16px; border: 1px solid #1E293B;" />
      <br/><br/><em>Cuadrícula interactiva y modo incógnito</em>
    </td>
    <td align="center" width="33%">
      <b>Ajustes y Nova Shield</b><br/><br/>
      <img src="public/screenshots/settings.png" alt="Ajustes y Escudo Nova Browser" width="240" style="border-radius: 16px; border: 1px solid #1E293B;" />
      <br/><br/><em>Protección de rastreo y limpieza de datos</em>
    </td>
    <td align="center" width="33%">
      <b>Emblema Oficial</b><br/><br/>
      <img src="public/nova-browser-logo.png" alt="Logo Oficial Nova Browser" width="180" style="border-radius: 24px; box-shadow: 0 4px 16px rgba(0, 229, 255, 0.25);" />
      <br/><br/><em>Identidad visual de Nova Browser</em>
    </td>
  </tr>
</table>

---

## ⚡ Características Destacadas

- **🎨 Interfaz Inmersiva Nova**: Diseño de alto contraste optimizado para pantallas OLED/AMOLED con paleta de color zafiro y cian brillante.
- **🤖 Copiloto de IA (Nova AI)**: Asistente conversacional flotante para síntesis de artículos, traducciones inmediatas y aclaración de dudas.
- **🛡️ Nova Shield (Anti-Rastreo)**: Bloqueo de dominios de telemetría y anuncios mediante intercepción nativa en `WebViewClient`.
- **📖 Modo Lectura (Reader View)**: Extracción limpia del texto y las imágenes principales del artículo, eliminando scripts, barras flotantes y publicidad.
- **🔍 Búsqueda en Página (Find in Page)**: Búsqueda interactiva con resaltado en tiempo real y contador dinámico de ocurrencias (`1 de 12`).
- **🖥️ Vista de Escritorio (Desktop Mode)**: Conmutador de *User-Agent* para solicitar la versión para ordenadores de cualquier página web.
- **🔤 Ajuste de Tamaño de Fuente**: Personalización del tamaño tipográfico (80%, 100%, 125%, 150%) para máxima legibilidad.
- **📲 Compartir mediante Código QR**: Generador instantáneo de código QR para transferir la URL activa a otros dispositivos móviles sin cables.
- **📑 Pestañas Ilimitadas e Incógnito**: Administrador visual de tarjetas con pestañas normales y sesiones de incógnito efímeras.
- **💾 Almacenamiento Local Seguro**: Todos los marcadores, historial y preferencias se guardan de forma local en SQLite con **Room**.

---

## 🌐 Funciones de Navegación

### Barra de Navegación Superior Inteligente (Omnibox)
- Indicador visual de seguridad y cifrado SSL (`https://`).
- Botón directo de acceso al **Escudo de Protección Nova**.
- Acceso con un toque para añadir o eliminar la página actual de **Marcadores**.
- Recarga rápida y selector visual de pestañas activas.

### Pantalla de Inicio (Home Hub)
- Selector rápido del motor de búsqueda preferido: **Google**, **Bing**, **DuckDuckGo**, **Yahoo** o **Ecosia**.
- Mosaico de accesos directos personalizables a plataformas y servicios populares:
  - *Google*
  - *YouTube*
  - *Wikipedia*
  - *GitHub*
  - *Reddit*
  - *The Verge*
  - Posibilidad de agregar cualquier URL favorita como acceso directo personalizado.

### Gestor de Pestañas y Modo Incógnito
- Vista en cuadrícula de miniaturas para cambiar de pestaña rápidamente.
- **Pestañas de Incógnito**: Navegación privada que no guarda historial, cookies ni fragmentos temporales en el almacenamiento del dispositivo.

---

## 🧠 Inteligencia Artificial (Nova AI Copilot)

Nova Browser incorpora un panel deslizable inferior de Inteligencia Artificial que se comunica con los modelos **Gemini 3.5 Flash** de Google mediante el SDK oficial:

| Acción de IA | Descripción |
| :--- | :--- |
| 📄 **Resumir Página** | Extrae el contenido clave de la página web actual y genera un resumen estructurado en segundos. |
| 💡 **Explicar Concepto** | Analiza términos complejos o temas especializados y los explica con lenguaje claro y accesible. |
| 🌐 **Traducir Página** | Traduce fluidamente secciones y contenidos web al idioma deseado. |
| 🔎 **Búsqueda Inteligente** | Respuestas sintetizadas y recomendaciones directas sin necesidad de navegar por múltiples enlaces. |
| 💬 **Chat Conversacional** | Chat libre con memoria contextual para hacer preguntas adicionales sobre la navegación. |

> **Nota sobre la clave API:** Para utilizar las funciones de IA, puedes configurar tu propia clave gratuita de Google Gemini en la pantalla de **Ajustes** de la app o a través de variables de entorno `.env`.

---

## 🔒 Privacidad y Seguridad

- **Intercepción de Rastreadores**: El `WebViewClient` filtra y bloquea automáticamente solicitudes a dominios publicitarios y telemetría conocida (DoubleClick, Criteo, Taboola, Outbrain, Google Analytics, etc.).
- **Almacenamiento Local 100% Seguro**: Todos los marcadores, pestañas, descargas e historial se gestionan localmente en el dispositivo utilizando **Room (SQLite)**. No se transmiten datos a servidores externos sin autorización del usuario.
- **Borrado Selectivo de Datos**: Diálogo para vaciar selectivamente:
  - Historial de navegación.
  - Cookies y sesiones web activas.
  - Archivos temporales de caché del navegador.

---

## 🛠️ Tecnologías Utilizadas

| Componente | Tecnología | Propósito |
| :--- | :--- | :--- |
| **Lenguaje** | Kotlin 2.0.21 | Código limpio, seguro y conciso |
| **Interfaz de Usuario** | Jetpack Compose (BOM 2024.10.01) | UI reactiva moderna y declarativa |
| **Diseño** | Material Design 3 (M3) | Componentes visuales, paleta dinámica y animaciones |
| **Arquitectura** | MVVM + Clean Architecture | Separación de lógica, ViewModel y StateFlow |
| **Base de Datos** | Room 2.6.1 + KSP | Persistencia local offline-first con SQLite |
| **Inteligencia Artificial** | Google Gemini API (gemini-3.5-flash) | Asistente de IA Copilot integrado |
| **Motor Web** | Android WebKit WebView | Renderizado web acelerado por GPU |
| **Automatización / CI** | GitHub Actions | Compilación continua y generación de APKs |
| **Construcción** | Gradle 8.11.1 (Kotlin DSL) | Gestión de dependencias y Version Catalog |

---

## 📋 Requisitos del Sistema

- **Dispositivo Android**: Android 7.0 (Nougat, API 24) o superior.
- **Android Studio**: Android Studio Koala Feature Drop / Ladybug (2024.1+) o posterior.
- **Java Development Kit (JDK)**: JDK 17 o superior.
- **Gradle**: Versión 8.11.1 (incluida en el repositorio mediante el Gradle Wrapper).
- **Conexión a Internet**: Necesaria para la navegación web y las funciones de Google Gemini.

---

## ⚙️ Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/luismiguel38338-cmd/Nova-Browser.git
cd Nova-Browser
```

### 2. Configurar la clave de Google Gemini (Opcional)

Si deseas preconfigurar tu API Key de Gemini para el asistente de IA:

1. Crea un archivo `.env` en la raíz del proyecto (puedes tomar como base `.env.example`):

```bash
cp .env.example .env
```

2. Agrega tu clave obtenida gratuitamente en [Google AI Studio](https://aistudio.google.com/):

```properties
GEMINI_API_KEY=tu_clave_api_aqui
```

*(También puedes ingresar la clave directamente dentro de la app desde la pantalla de **Ajustes**).*

---

## 🚀 Cómo Ejecutar el Proyecto

### Desde Android Studio
1. Abre Android Studio y selecciona **Open**.
2. Dirígete a la carpeta del proyecto clonado y haz clic en **OK**.
3. Espera a que Gradle sincronice las dependencias del proyecto.
4. Conecta tu dispositivo Android con la depuración USB activada o inicia un Emulador.
5. Presiona el botón verde **Run (Shift + F10)**.

### Desde la Terminal

Para compilar e instalar directamente en un dispositivo conectado:

```bash
./gradlew installDebug
```

Para ejecutar las pruebas unitarias:

```bash
./gradlew testDebugUnitTest
```

---

## 📦 Cómo Generar el APK para Android

### Generar APK de Depuración (Debug)

Ejecuta el siguiente comando en la raíz del proyecto:

```bash
./gradlew assembleDebug
```

El archivo APK compilado se generará en la ruta:
```text
app/build/outputs/apk/debug/app-debug.apk
```

### Generar APK de Producción (Release)

```bash
./gradlew assembleRelease
```

El archivo se encontrará en:
```text
app/build/outputs/apk/release/app-release-unsigned.apk
```

### Compilación Automatizada en GitHub Actions
El proyecto incluye un flujo de trabajo listo en `.github/workflows/build.yml`. Cada vez que subas cambios (`git push`) a tu repositorio de GitHub:
1. GitHub Actions compilará el proyecto con JDK 21.
2. Ejecutará las pruebas unitarias.
3. Generará el archivo APK listo para descargar directamente desde la pestaña **Actions > Artifacts**.
4. Publicará una **Release automática** con el instalador `Nova-Browser-v1.0-debug.apk` adjunto.

---

## 🌐 Publicación y Canales de Distribución Gratuitos

> 📌 **Política Oficial de Distribución**:
> **Nova Browser** se publica y distribuye **exclusivamente en plataformas 100% GRATUITAS** sin costos para los usuarios ni para el desarrollador.
> 
> ⛔ **Restricciones Claras**:
> - **Prohibida la publicación en PlayStation** (ni PlayStation Store ni consolas Sony).
> - **Prohibida la publicación en plataformas con cuotas o licencias de pago** (como tarifas de desarrollador comerciales).
>
> 📖 Para una guía detallada paso a paso sobre cómo subir el proyecto a cada canal sin costo, consulta la **[Guía Oficial de Distribución (DISTRIBUTION.md)](DISTRIBUTION.md)**.

### Canales Gratuitos Soportados:
- 🚀 **[GitHub Releases](https://github.com/luismiguel38338-cmd/Nova-Browser/releases)**: Descarga directa del APK generado automáticamente por CI/CD.
- 📱 **[IzzyOnDroid / F-Droid](https://gitlab.com/IzzyOnDroid/repo)**: Repositorio libre para Android sin costes.
- 🌐 **[Uptodown](https://developer.uptodown.com/)**: Plataforma global gratuita para subir APKs.
- 📂 **[APKMirror](https://www.apkmirror.com/apk-submissions/)**: Portal verificado y gratuito de instaladores Android.
- 🎮 **[itch.io](https://itch.io/developers)**: Distribución libre de software y APKs.
- 📦 **[Amazon Appstore](https://developer.amazon.com/)**: Cuenta de desarrollador sin costo.

---

## 📂 Estructura del Proyecto

```text
Nova-Browser/
├── .github/
│   └── workflows/
│       └── build.yml               # Pipeline de CI/CD para GitHub Actions
├── app/
│   ├── build.gradle.kts            # Configuración de dependencias de la app
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml # Manifiesto y permisos del sistema
│       │   ├── java/com/example/
│       │   │   ├── MainActivity.kt # Actividad principal y orquestación de UI
│       │   │   ├── ai/             # Servicios de IA y cliente de Google Gemini
│       │   │   │   ├── AiAction.kt
│       │   │   │   ├── AiMessage.kt
│       │   │   │   ├── AiService.kt
│       │   │   │   └── GeminiAiService.kt
│       │   │   ├── data/           # Modelos y base de datos local Room
│       │   │   │   ├── local/      # Entidades y DAOs (Room SQLite)
│       │   │   │   ├── model/      # Clases de datos del dominio
│       │   │   │   └── repository/ # Repositorios de datos
│       │   │   ├── ui/             # Interfaz de usuario con Jetpack Compose
│       │   │   │   ├── components/ # Componentes (NovaTopBar, NovaBottomBar, Sheets, Modales)
│       │   │   │   ├── screens/    # Pantallas (Home, Browser, Tabs, Settings, etc.)
│       │   │   │   └── theme/      # Paleta Nova, tipografías y formas M3
│       │   │   └── viewmodel/      # ViewModel y gestión de estado reactivo
│       │   └── res/                # Recursos gráficos, iconos y cadenas XML
│       └── test/                   # Pruebas unitarias con JUnit y Robolectric
├── gradle/
│   ├── libs.versions.toml          # Catálogo de versiones centralizado
│   └── wrapper/                    # Gradle Wrapper 8.11.1
├── public/                         # Recursos visuales y capturas para el repositorio
│   ├── screenshots/                # Capturas de pantalla de la interfaz
│   │   ├── home.png
│   │   ├── browser.png
│   │   ├── ai.png
│   │   ├── tabs.png
│   │   └── settings.png
│   └── nova-browser-logo.png       # Logo oficial
├── .gitignore                      # Reglas de exclusión de Git
├── build.gradle.kts                # Configuración Gradle raíz
├── gradlew                         # Script ejecutable de Gradle (Linux/macOS)
├── gradlew.bat                     # Script ejecutable de Gradle (Windows)
├── metadata.json                   # Metadatos de la plataforma AI Studio
├── README.md                       # Documentación principal del proyecto
└── settings.gradle.kts             # Configuración de repositorios y módulos
```

---

## 🤝 Cómo Contribuir

¡Las contribuciones son bienvenidas para seguir mejorando Nova Browser!

1. **Haz un Fork** del proyecto en GitHub.
2. **Crea una rama** para tu funcionalidad o corrección:
   ```bash
   git checkout -b feature/nueva-funcionalidad
   ```
3. **Realiza tus cambios** y haz commits descriptivos:
   ```bash
   git commit -m "feat: agregar soporte para marcadores anidados"
   ```
4. **Sube tu rama**:
   ```bash
   git push origin feature/nueva-funcionalidad
   ```
5. Abre un **Pull Request** detallando los cambios propuestos.

---

## 📄 Licencia

Este proyecto está bajo la Licencia **MIT**. Consulta el archivo `LICENSE` para más información.

---

## 👨‍💻 Desarrollador y Contacto

Desarrollado con dedicación y pasión por la tecnología por:

- **Desarrollador**: Luis Miguel
- **Perfil de GitHub**: [@luismiguel38338-cmd](https://github.com/luismiguel38338-cmd)
- **Repositorio Oficial**: [https://github.com/luismiguel38338-cmd/Nova-Browser](https://github.com/luismiguel38338-cmd)

---

<p align="center">
  <sub>Construido con Jetpack Compose, Kotlin y la tecnología de Google Gemini.</sub><br/>
  <b>Nova Browser &copy; 2026</b>
</p>
