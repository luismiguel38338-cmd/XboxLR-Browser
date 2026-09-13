# 📦 Guía Oficial de Publicación y Distribución - Nova Browser

> **Política de Distribución**: **Nova Browser** está diseñado para ser publicado y distribuido **exclusivamente en plataformas y tiendas 100% GRATUITAS** y de libre acceso para los usuarios, **SIN costo alguno para el desarrollador ni para los usuarios**.
>
> ⚠️ **Restricción Específica**:
> - **NO se publicará en PlayStation** (ni en PlayStation Network, PlayStation Store, ni en ecosistemas de consolas de Sony).
> - **NO se publicará en plataformas que requieran el pago de tarifas de desarrollador** (como las suscripciones anuales o cuotas de registro obligatorias).
> - La distribución se centrará en plataformas abiertas, libres y sin barreras de entrada económicas.

---

## 📋 Canales Oficiales de Distribución Gratuita (100% Gratis)

| Plataforma | Costo de Registro | Tipo de Distribución | Enlace |
| :--- | :---: | :--- | :--- |
| **GitHub Releases** | **$0 (Gratis)** | APK directo automático mediante CI/CD | [GitHub Releases](https://github.com/luismiguel38338-cmd/Nova-Browser/releases) |
| **IzzyOnDroid (F-Droid Repo)** | **$0 (Gratis)** | Repositorio APK para Android / F-Droid client | [IzzyOnDroid Submission](https://gitlab.com/IzzyOnDroid/repo) |
| **F-Droid Oficial** | **$0 (Gratis)** | Catálogo de software libre para Android | [F-Droid Inclusion](https://f-droid.org/docs/Inclusion_Policy/) |
| **Uptodown Developers** | **$0 (Gratis)** | Portal mundial de distribución de APKs | [Uptodown Zone](https://developer.uptodown.com/) |
| **APKMirror** | **$0 (Gratis)** | Repositorio público de APKs verificados | [APKMirror Upload](https://www.apkmirror.com/apk-submissions/) |
| **itch.io** | **$0 (Gratis)** | Plataforma abierta de distribución de software | [itch.io](https://itch.io/developers) |
| **Amazon Appstore** | **$0 (Gratis)** | Tienda oficial para dispositivos Android y Fire OS | [Amazon Developer](https://developer.amazon.com/) |
| **Descarga Directa (Web / GitHub Pages)** | **$0 (Gratis)** | Descarga directa desde tu propio sitio web | [GitHub Pages](https://pages.github.com/) |

---

## 🚀 Paso 1: Publicación Automática en GitHub Releases (Recomendado)

El repositorio cuenta con un flujo de integración y entrega continua configurado en `.github/workflows/build.yml`.

### 1.1 Comandos para subir todo el código a GitHub

Ejecuta los siguientes comandos en tu terminal local para inicializar Git y subir todos los archivos y novedades del proyecto:

```bash
# 1. Iniciar repositorio Git local (si no está iniciado)
git init

# 2. Agregar todos los archivos y novedades del proyecto
git add .

# 3. Crear el commit inicial con la versión renovada
git commit -m "feat: Lanzamiento oficial de Nova Browser v1.0.0 - Navegador con IA y privacidad"

# 4. Establecer la rama principal 'main'
git branch -M main

# 5. Conectar con tu repositorio de GitHub (reemplaza con tu URL si es diferente)
git remote add origin https://github.com/luismiguel38338-cmd/Nova-Browser.git

# 6. Subir los cambios a GitHub
git push -u origin main
```

### 1.2 Cómo funciona el Pipeline Automático

En cuanto ejecutas `git push`:
1. **GitHub Actions** detecta el cambio e inicia el flujo de trabajo (`Build & Release Nova Browser`).
2. Descarga las dependencias y compila el APK de la aplicación (`./gradlew assembleDebug`).
3. Empaqueta el archivo con el nombre **`Nova-Browser-v1.0-debug.apk`**.
4. Sube el artefacto listo para descargar en la sección **Actions > Artifacts**.
5. Crea automáticamente una **GitHub Release (v1.0.0)** con las notas de lanzamiento completas y el archivo `.apk` adjunto para descarga pública inmediata.

### 1.3 Cómo publicar una nueva versión / actualización futura

Cada vez que agregues mejoras o nuevas características:

```bash
# Guarda tus cambios
git add .
git commit -m "feat: Mejoras en rendimiento y nuevas funciones"
git push origin main

# Para crear una versión formal con etiqueta:
git tag -a v1.0.1 -m "Nova Browser v1.0.1"
git push origin v1.0.1
```

---

## 📱 Paso 2: Publicación en F-Droid y Repositorios Abiertos (Gratis)

### 2.1 IzzyOnDroid (F-Droid compatible)
**IzzyOnDroid** permite que los usuarios de la aplicación cliente de F-Droid instalen y actualicen tu APK directamente desde GitHub Releases sin coste alguno:
1. Asegúrate de tener tu APK publicado en GitHub Releases (completado con el Paso 1).
2. Abre una solicitud de inclusión en el repositorio de GitLab: [Solicitar inclusión en IzzyOnDroid](https://gitlab.com/IzzyOnDroid/repo/-/issues).
3. Proporciona la URL de tu repositorio de GitHub: `https://github.com/luismiguel38338-cmd/Nova-Browser`.
4. Una vez aceptado, cada nueva release que publiques en GitHub se actualizará automáticamente para todos los usuarios.

---

## 🌐 Paso 3: Publicación en Uptodown (Gratis)

**Uptodown** es una de las mayores plataformas de distribución de aplicaciones Android en el mundo, sin cuotas de registro:
1. Crea una cuenta gratuita en la [Consola de Desarrolladores de Uptodown](https://developer.uptodown.com/).
2. Haz clic en **"Añadir nueva aplicación"**.
3. Sube el archivo **`Nova-Browser-v1.0-debug.apk`** generado por GitHub.
4. Rellena los datos de la app:
   - **Nombre**: Nova Browser
   - **Descripción**: Navegador web moderno, privado y con Inteligencia Artificial integrada.
   - **Categoría**: Comunicación / Navegadores web.
   - **Capturas de pantalla**: Utiliza las capturas ubicadas en la carpeta `public/screenshots/`.
5. Envía la aplicación a revisión; en 24-48 horas estará disponible públicamente para millones de usuarios.

---

## 🛒 Paso 4: Publicación en itch.io (Gratis)

**itch.io** permite distribuir aplicaciones para Android sin costes:
1. Regístrate gratis en [itch.io](https://itch.io/).
2. En el panel de control, haz clic en **"Create new project"**.
3. Configura:
   - **Title**: Nova Browser
   - **Classification**: App / Tool
   - **Kind of project**: Downloadable
   - **Pricing**: $0 / Free
4. En la sección **Uploads**, sube el archivo **`Nova-Browser-v1.0-debug.apk`** y marca la casilla **"Android"**.
5. Agrega las capturas de pantalla de `public/screenshots/` y publica el proyecto.

---

## 📦 Paso 5: Publicación en Amazon Appstore (Gratis)

A diferencia de otras tiendas comerciales que cobran cuotas anuales, la cuenta de desarrollador de Amazon es **100% gratuita**:
1. Regístrate en el [Amazon Developer Portal](https://developer.amazon.com/).
2. Crea una cuenta de desarrollador sin costo.
3. Ve a **App List > Add New App > Android**.
4. Sube tu APK y completa la ficha de la tienda con la información y capturas provistas en este repositorio.

---

## ⛔ Plataformas Excluidas y Prohibidas

Para cumplir estrictamente con los lineamientos del proyecto:
1. **PlayStation (Sony)**: Queda terminantemente descartada y prohibida cualquier gestión o publicación en PlayStation Store o consolas Sony.
2. **Cualquier tienda de pago**: Queda prohibido el uso de pasarelas que exijan el abono de cuotas de registro, membresías de desarrollador o tarifas por publicación.
