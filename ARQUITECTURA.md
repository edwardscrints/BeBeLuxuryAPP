# 📐 Arquitectura de la App - BeBeLuxury

## 🏛️ Diagrama de Capas

```
┌─────────────────────────────────────────────────────────┐
│                    PRESENTACIÓN (UI)                    │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │  MainActivity│  │ProductDetail │  │AddEditProduct│   │
│  │  (Lista)     │  │  Activity    │  │  Activity    │   │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘   │
│         │                  │                  │         │
│         └──────────────────┴──────────────────┘         │
│                           │                             │
│                  ┌────────▼────────┐                    │
│                  │ ProductAdapter  │                    │
│                  │  (RecyclerView) │                    │
│                  └────────┬────────┘                    │
└───────────────────────────┼─────────────────────────────┘
                            │
┌───────────────────────────┼──────────────────────────────┐
│                      LÓGICA DE NEGOCIO                   │
│                  ┌────────▼────────┐                     │
│                  │ ProductRepository│                    │
│                  │  (Abstracción)  │                     │
│                  └────────┬────────┘                     │
└───────────────────────────┼──────────────────────────────┘
                            │
┌───────────────────────────┼──────────────────────────────┐
│                      ACCESO A DATOS                      │
│                  ┌────────▼────────┐                     │
│                  │   ProductDao    │                     │
│                  │  (Operaciones)  │                     │
│                  └────────┬────────┘                     │
│                           │                              │
│                  ┌────────▼────────┐                     │
│                  │   AppDatabase   │                     │
│                  │  (Room/SQLite)  │                     │
│                  └────────┬────────┘                     │
└───────────────────────────┼──────────────────────────────┘
                            │
                   ┌────────▼────────┐
                   │  SQLite Local   │
                   │   (bebeluxury_  │
                   │    database)    │
                   └─────────────────┘
```

## 📊 Flujo de Datos (CRUD)

### CREATE (Crear Producto)
```
Usuario → AddEditProductActivity → Repository.insert() 
    → ProductDao.insertProduct() → Room Database → SQLite
```

### READ (Leer Productos)
```
MainActivity → Repository.allProducts (Flow) 
    → ProductDao.getAllProducts() → Room Database → RecyclerView
```

### UPDATE (Actualizar Producto)
```
Usuario → ProductDetailActivity → AddEditProductActivity 
    → Repository.update() → ProductDao.updateProduct() → Room Database
```

### DELETE (Eliminar Producto)
```
Usuario → ProductDetailActivity → Confirmación 
    → Repository.delete() → ProductDao.deleteProduct() → Room Database
```

## 🗂️ Estructura de Paquetes

```
com.example.bebeluxury
│
├── 📱 MainActivity.kt                 (Activity principal)
│
├── 📁 activities/
│   ├── ProductDetailActivity.kt      (Detalles del producto)
│   └── AddEditProductActivity.kt     (Formulario CRUD)
│
├── 📁 adapter/
│   └── ProductAdapter.kt             (Adapter para lista)
│
├── 📁 model/
│   └── Product.kt                    (Entidad @Entity)
│
├── 📁 data/
│   ├── ProductDao.kt                 (Interface @Dao)
│   └── AppDatabase.kt                (@Database)
│
└── 📁 repository/
    └── ProductRepository.kt          (Capa intermedia)
```

## 🎨 Layouts y su Tipo

| Archivo XML | Layout Principal | Elementos Clave |
|-------------|------------------|-----------------|
| `activity_main.xml` | **ConstraintLayout** | RecyclerView, FAB, TextView vacío |
| `item_product.xml` | **MaterialCardView** + **ConstraintLayout** | ImageView, 4 TextViews |
| `activity_product_detail.xml` | **RelativeLayout** | ImageView, TextViews, 2 Buttons |
| `activity_add_edit_product.xml` | **LinearLayout** (vertical) | TextInputLayouts, AutoComplete, Button |

## 🔄 Ciclo de Vida - MainActivity

```
onCreate() 
    ↓
Inicializar Database y Repository
    ↓
Configurar RecyclerView + Adapter
    ↓
Configurar FAB (Floating Action Button)
    ↓
observeProducts() → Escucha cambios en Flow
    ↓
onResume() → Actualiza lista
```

## 🛠️ Componentes Técnicos

### 1. Room Database
```kotlin
@Entity(tableName = "products")
data class Product(...)

@Dao
interface ProductDao { 
    @Query, @Insert, @Update, @Delete 
}

@Database
abstract class AppDatabase : RoomDatabase()
```

### 2. RecyclerView Pattern
```kotlin
ProductAdapter extends ListAdapter
    ↓
ProductViewHolder (ViewHolder)
    ↓
item_product.xml (Layout del item)
```

### 3. Coroutines (Asincronía)
```kotlin
lifecycleScope.launch {
    // Operaciones en segundo plano
    repository.insert(product)
}
```

### 4. Flow (Observador Reactivo)
```kotlin
repository.allProducts.collect { products ->
    adapter.submitList(products)
}
```

## 🎯 Navegación entre Activities

```
MainActivity (Lista)
    ├─→ Click en producto → ProductDetailActivity
    │                           ├─→ Editar → AddEditProductActivity
    │                           └─→ Eliminar → Confirmación → Volver
    │
    └─→ FAB (+) → AddEditProductActivity → Guardar → Volver
```

## 📦 Dependencias Principales

| Librería | Propósito |
|----------|-----------|
| `room-runtime` | Base de datos local |
| `room-ktx` | Extensiones Kotlin (Flow, Coroutines) |
| `recyclerview` | Lista de productos |
| `material` | Material Design Components |
| `constraintlayout` | Layouts flexibles |
| `kotlinx-coroutines` | Programación asíncrona |

## 🔐 Permisos Necesarios

**Ninguno** - La app no requiere permisos especiales porque:
- ✅ Room Database es local (no requiere INTERNET)
- ✅ No accede a cámara, ubicación, ni contactos
- ✅ Solo escribe en almacenamiento privado de la app

## 🚀 Próximos Pasos para MySQL

Para migrar a MySQL remoto:

1. **Backend** (Laravel, Node.js, PHP)
   ```
   API REST → Endpoints CRUD
   GET    /products
   GET    /products/{id}
   POST   /products
   PUT    /products/{id}
   DELETE /products/{id}
   ```

2. **Android** (Retrofit)
   ```kotlin
   interface ApiService {
       @GET("products")
       suspend fun getProducts(): List<Product>
       
       @POST("products")
       suspend fun createProduct(@Body product: Product)
   }
   ```

3. **Reemplazar Repository**
   ```kotlin
   // Antes: Room
   repository.allProducts.collect { ... }
   
   // Después: Retrofit
   val products = apiService.getProducts()
   ```

---

## 📝 Notas Importantes

- **Room vs MySQL**: Room es local y offline. MySQL requiere servidor y conexión a internet.
- **Thread Safety**: Room ejecuta operaciones en background automáticamente con coroutines.
- **LiveData vs Flow**: Usamos Flow (moderno) en lugar de LiveData.
- **Material Design**: Seguimos las guías de diseño de Google para Android.

---

Esta arquitectura es escalable, mantenible y sigue las mejores prácticas de Android moderno. 🏆
