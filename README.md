# OrderIt

App para tablets Android que usan los meseros en un restaurante para gestionar pedidos, consultar el menu, ver metricas y administrar ordenes en tiempo real.

## Arquitectura

**Clean Architecture + MVVM** con Jetpack Compose.

```
com.orderit/
├── core/           # DI (Hilt modules), networking (Retrofit), utilidades
├── data/           # DTOs (@Serializable), repository implementations, SessionStore
├── domain/         # Repository interfaces, modelos de dominio
└── ui/             # Compose screens, ViewModels (StateFlow), componentes, theme
```

| Capa | Responsabilidad |
|------|----------------|
| **UI** | Jetpack Compose screens + ViewModels con `StateFlow` |
| **Domain** | Interfaces de repositorio y modelos de dominio |
| **Data** | Implementaciones de repositorio, API service (Retrofit), almacenamiento local |

## Tech Stack

| Categoria | Tecnologia | Version |
|-----------|-----------|---------|
| UI | Jetpack Compose (Material3) | BOM 2025.12.01 |
| Navegacion | Navigation Compose (type-safe) | 2.9.6 |
| DI | Dagger Hilt | 2.57.2 |
| Networking | Retrofit + OkHttp | 3.0.0 / 5.3.2 |
| Serializacion | kotlinx-serialization-json | 1.9.0 |
| Imagenes | Coil Compose | 2.7.0 |
| Analytics | Firebase Crashlytics + Analytics | BOM 34.7.0 |
| Lenguaje | Kotlin | 2.2.0 |
| Min SDK | Android 11 (API 30) | - |
| Target SDK | Android 16 (API 36) | - |

## Pantallas

| Pantalla | Descripcion |
|----------|------------|
| **Login** | Autenticacion por username del mesero |
| **Dashboard** | Resumen del dia: total ordenes, ventas, conteo por status, productos destacados |
| **Ordenes** | Grid de ordenes con panel lateral de detalle (status, productos, pago) |
| **Menu** | Catalogo de productos por categoria con formulario para crear/editar productos |
| **Metricas** | Estadisticas: ingresos, promedio por orden, top productos, desglose por status |

## API

Backend REST deployado en Google Cloud Run.

```
Base URL: https://orderit-806794513763.us-east1.run.app/
```

Endpoints principales: `/waiters`, `/categories`, `/products`, `/tables`, `/orders`, `/orders/{id}/details`

## Setup

### Requisitos

- Android Studio Ladybug o superior
- JDK 11+
- Tablet Android o emulador (API 30+, landscape)

### Pasos

1. Clona el repositorio
   ```bash
   git clone <repo-url>
   cd OrderIt
   ```

2. Agrega tu archivo `google-services.json` de Firebase en `app/`
   > Este archivo no esta en el repo por seguridad. Obtenlo desde la consola de Firebase del proyecto.

3. Sincroniza y compila desde Android Studio o terminal:
   ```bash
   ./gradlew assembleDebug
   ```

4. Corre en un emulador de tablet en modo landscape.

## Estructura de archivos

```
app/src/main/java/com/orderit/
├── MainActivity.kt                  # Entry point (@AndroidEntryPoint)
├── OrderItApp.kt                    # Hilt Application
├── core/
│   ├── di/
│   │   ├── NetworkModule.kt         # Retrofit, OkHttp, Json providers
│   │   └── RepositoryModule.kt      # Repository bindings
│   ├── network/
│   │   └── OrderItApi.kt            # Retrofit interface (todos los endpoints)
│   ├── result/
│   │   └── AppResult.kt             # Sealed class Ok/Err + safeApiCall()
│   └── util/
│       └── TimeUtils.kt             # "Hace X min" helper, formateo de fechas
├── data/
│   ├── local/
│   │   └── SessionStore.kt          # SharedPreferences para sesion del mesero
│   ├── model/
│   │   ├── CategoryDto.kt
│   │   ├── DiningTableDto.kt
│   │   ├── OrderDetailDto.kt
│   │   ├── OrderDto.kt
│   │   ├── ProductDto.kt
│   │   └── WaiterDto.kt
│   └── repository/
│       ├── CategoryRepositoryImpl.kt
│       ├── OrderRepositoryImpl.kt
│       ├── ProductRepositoryImpl.kt
│       └── WaiterRepositoryImpl.kt
├── domain/
│   ├── model/
│   │   └── OrderWithDetails.kt      # Orden + detalles + productos resueltos
│   └── repository/
│       ├── CategoryRepository.kt
│       ├── OrderRepository.kt
│       ├── ProductRepository.kt
│       └── WaiterRepository.kt
└── ui/
    ├── components/
    │   ├── SideBar.kt                # Navegacion lateral permanente
    │   └── TopBar.kt                 # Barra de busqueda + carrito
    ├── dashboard/
    │   ├── DashboardScreen.kt
    │   └── DashboardViewModel.kt
    ├── login/
    │   ├── LoginScreen.kt
    │   └── LoginViewModel.kt
    ├── menu/
    │   ├── MenuScreen.kt
    │   ├── MenuViewModel.kt
    │   └── ProductFormPanel.kt       # Formulario crear/editar producto
    ├── metrics/
    │   ├── MetricsScreen.kt
    │   └── MetricsViewModel.kt
    ├── navigation/
    │   ├── MainScaffold.kt           # Layout principal (sidebar + content)
    │   ├── NavGraph.kt               # Login vs Main routing
    │   └── Routes.kt                 # Type-safe routes
    ├── orders/
    │   ├── OrdersScreen.kt           # Grid + panel de detalle
    │   └── OrdersViewModel.kt
    └── theme/
        ├── Color.kt
        ├── Theme.kt
        └── Type.kt
```
