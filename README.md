# BeBeLuxury - Tienda Virtual de Productos

Aplicación Android nativa que funciona como una vitrina virtual de productos de lujo con funcionalidad CRUD completa.

## 🌟 Características

- ✅ **Listar productos**: Visualiza todos los productos en un RecyclerView con cards
- ✅ **Ver detalles**: Accede a información completa de cada producto
- ✅ **Agregar productos**: Crea nuevos productos con formulario completo
- ✅ **Editar productos**: Modifica productos existentes
- ✅ **Eliminar productos**: Borra productos con confirmación

## 🏗️ Arquitectura

### Capas de la aplicación:
- **Model**: Entidades de datos (Product)
- **Data**: Room Database (DAO, Database)
- **Repository**: Capa de abstracción para acceso a datos
- **UI**: Activities y Adapters

### Base de datos:
- **Room Database** (SQLite local) - Alternativa a MySQL para Android
- Persistencia local en el dispositivo
- 5 productos de ejemplo precargados

## 📱 Pantallas (Activities)

### 1. MainActivity (Lista de Productos)
- Layout: **ConstraintLayout** + CoordinatorLayout
- RecyclerView con LinearLayoutManager
- FAB para agregar productos
- Click en producto → Ver detalles

### 2. ProductDetailActivity (Detalles)
- Layout: **RelativeLayout** + ScrollView
- Muestra toda la información del producto
- Botones: Editar y Eliminar
- Confirmación antes de eliminar

### 3. AddEditProductActivity (Agregar/Editar)
- Layout: **LinearLayout** + ScrollView
- Formulario completo con validación
- Campos: nombre, descripción, precio, categoría, stock, imagen
- Dropdown de categorías predefinidas

## 🛠️ Tecnologías Utilizadas

- **Kotlin**: Lenguaje de programación
- **Room Database**: Persistencia de datos (SQLite)
- **RecyclerView**: Lista de productos
- **Material Design Components**: UI moderna
- **Coroutines**: Operaciones asíncronas
- **ViewBinding**: Acceso a vistas XML

## 📦 Estructura de archivos creados

```
app/src/main/
├── java/com/example/bebeluxury/
│   ├── MainActivity.kt (Actividad principal - lista)
│   ├── activities/
│   │   ├── ProductDetailActivity.kt (Detalles del producto)
│   │   └── AddEditProductActivity.kt (Agregar/Editar)
│   ├── adapter/
│   │   └── ProductAdapter.kt (Adapter para RecyclerView)
│   ├── model/
│   │   └── Product.kt (Entidad de datos)
│   ├── data/
│   │   ├── ProductDao.kt (Acceso a datos)
│   │   └── AppDatabase.kt (Configuración BD)
│   └── repository/
│       └── ProductRepository.kt (Repositorio)
├── res/
│   ├── layout/
│   │   ├── activity_main.xml (Layout principal)
│   │   ├── item_product.xml (Item del RecyclerView)
│   │   ├── activity_product_detail.xml (Layout detalles)
│   │   └── activity_add_edit_product.xml (Layout formulario)
│   └── values/
│       └── strings.xml (Strings en español)
└── AndroidManifest.xml (Activities registradas)
```


## 📝 Modelo de Datos

```kotlin
Product(
    id: Long,              // ID autoincremental
    name: String,          // Nombre del producto
    description: String,   // Descripción detallada
    price: Double,         // Precio en USD
    category: String,      // Categoría (Bolsos, Relojes, etc.)
    stock: Int,           // Cantidad disponible
    imageUrl: String      // URL de la imagen (opcional)
)
```

## 🎨 Categorías disponibles

- Bolsos
- Relojes
- Perfumes
- Calzado
- Joyería
- Accesorios
- Ropa

## ⚙️ Operaciones CRUD

| Operación | Método Repository | Activity |
|-----------|------------------|----------|
| **Create** | `insert(product)` | AddEditProductActivity |
| **Read** | `getAllProducts()`, `getProductById()` | MainActivity, ProductDetailActivity |
| **Update** | `update(product)` | AddEditProductActivity (modo edición) |
| **Delete** | `delete(product)` | ProductDetailActivity |

## 📱 Layouts utilizados

- **ConstraintLayout**: activity_main.xml, item_product.xml
- **RelativeLayout**: activity_product_detail.xml
- **LinearLayout**: activity_add_edit_product.xml
- **CoordinatorLayout**: Todas las activities (para AppBar)

## 🔄 Migración a MySQL (futuro)

Para conectar con un servidor MySQL:

1. Crear API REST con Laravel/PHP/Node.js
2. Agregar Retrofit para llamadas HTTP
3. Reemplazar Room por llamadas a la API
4. Mantener la arquitectura Repository

Las dependencias de Retrofit ya están incluidas en `build.gradle.kts`.

## 💡 Próximas mejoras para v1.2

- [ ] Agregar búsqueda de productos
- [ ] Filtrar por categoría
- [ ] Ordenar por precio/nombre
- [ ] Capturar fotos con la cámara
- [ ] Implementar carrito de compras
- [ ] Conectar con API backend
- [ ] Agregar autenticación de usuarios

## 📄 Licencia

Proyecto educativo - BeBeLuxury 2024
