# 🚀 Guía Rápida de Ejecución - BeBeLuxury

## Paso 1: Sincronizar el proyecto

Después de que se creen todos los archivos, Android Studio mostrará una barra amarilla en la parte superior que dice:
> "Gradle files have changed since last project sync..."

**Haz click en "Sync Now"** o ejecuta:
```
Archivo → Sync Project with Gradle Files
```

Espera a que termine la sincronización (puede tardar 1-2 minutos).

## Paso 2: Verificar dependencias

Si hay errores de compilación, verifica que el archivo `build.gradle.kts` (Module: app) tenga estas dependencias:

```kotlin
// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")

// UI Components
implementation("androidx.appcompat:appcompat:1.6.1")
implementation("com.google.android.material:material:1.11.0")
implementation("androidx.constraintlayout:constraintlayout:2.1.4")
implementation("androidx.recyclerview:recyclerview:1.3.2")
```

## Paso 3: Compilar el proyecto

Ejecuta desde el menú:
```
Build → Make Project (Ctrl+F9 en Windows)
```

O desde la terminal de Android Studio:
```bash
./gradlew assembleDebug
```

## Paso 4: Ejecutar la aplicación

### Opción A: Con Emulador
1. Abre AVD Manager: Tools → Device Manager
2. Crea/inicia un dispositivo virtual (recomendado: Pixel 5, API 33)
3. Click en el botón ▶️ Run o presiona `Shift+F10`

### Opción B: Con Dispositivo Físico
1. Habilita "Opciones de desarrollador" en tu Android
2. Activa "Depuración USB"
3. Conecta tu dispositivo con cable USB
4. Click en ▶️ Run

## 🎯 Funcionalidades para probar

### 1. Ver Lista de Productos ✅
- Al abrir la app verás 5 productos de ejemplo
- Cada producto muestra: nombre, categoría, precio y stock

### 2. Ver Detalles de un Producto 🔍
- Toca cualquier producto de la lista
- Verás la información completa
- Botones disponibles: Editar y Eliminar

### 3. Agregar Nuevo Producto ➕
- Toca el botón flotante (+) en la esquina inferior derecha
- Llena todos los campos del formulario:
  - Nombre del producto
  - Descripción
  - Precio (números con decimales, ej: 299.99)
  - Categoría (selecciona del dropdown)
  - Stock (número entero)
  - URL de imagen (opcional por ahora)
- Toca "GUARDAR"

### 4. Editar Producto ✏️
- Desde los detalles del producto, toca "EDITAR"
- Modifica los campos que desees
- Toca "GUARDAR"

### 5. Eliminar Producto 🗑️
- Desde los detalles del producto, toca "ELIMINAR"
- Confirma la acción en el diálogo
- El producto será eliminado

## ⚠️ Solución de Problemas Comunes

### Error: "Unresolved reference: R"
**Solución**: 
1. Build → Clean Project
2. Build → Rebuild Project

### Error: "Cannot resolve symbol 'kapt'"
**Solución**: Verifica que en `build.gradle.kts` (app) esté el plugin:
```kotlin
plugins {
    id("kotlin-kapt")
}
```

### Error: "SDK location not found"
**Solución**: Verifica que exista el archivo `local.properties` con:
```
sdk.dir=C\:\\Users\\TU_USUARIO\\AppData\\Local\\Android\\Sdk
```

### La app se cierra al abrirla
**Solución**: Revisa el Logcat en Android Studio:
1. View → Tool Windows → Logcat
2. Busca mensajes en rojo
3. Verifica que todas las Activities estén registradas en `AndroidManifest.xml`

### RecyclerView está vacío
**Solución**: La base de datos se puebla automáticamente la primera vez. Si no ves productos:
1. Desinstala la app del emulador/dispositivo
2. Vuelve a ejecutar desde Android Studio

## 📊 Base de Datos

La app usa **Room Database** que crea automáticamente un archivo SQLite en:
```
/data/data/com.example.bebeluxury/databases/bebeluxury_database
```

Para ver la base de datos en Android Studio:
1. View → Tool Windows → App Inspection
2. Selecciona la pestaña "Database Inspector"
3. Selecciona "bebeluxury_database"

## 🔧 Comandos Útiles de Gradle

```bash
# Limpiar build
./gradlew clean

# Compilar versión debug
./gradlew assembleDebug

# Compilar versión release
./gradlew assembleRelease

# Ver todas las tareas disponibles
./gradlew tasks
```

## 📱 Versiones Soportadas

- **Min SDK**: 24 (Android 7.0 - Nougat)
- **Target SDK**: 36 (Android 14+)
- **Compile SDK**: 36

La app funcionará en cualquier dispositivo con Android 7.0 o superior.

## 💾 Persistencia de Datos

Los datos se guardan localmente en el dispositivo y persisten entre ejecuciones de la app. Para borrar todos los datos:

1. Ve a Ajustes del dispositivo
2. Apps → BeBeLuxury
3. Almacenamiento → Borrar datos

O desde Android Studio:
```
Run → Edit Configurations → Always install with package manager
```

## 🎨 Personalización

### Cambiar colores:
Edita `app/src/main/res/values/colors.xml`

### Cambiar nombre de la app:
Edita `app/src/main/res/values/strings.xml`
```xml
<string name="app_name">Tu Nombre</string>
```

### Cambiar ícono:
Reemplaza los archivos en `app/src/main/res/mipmap-*/`

---

¡Listo! Tu app de vitrina virtual está completamente funcional. 🎉
