# OrderIt

Android tablet app used by waiters in a restaurant to manage orders, browse the menu, view metrics, and handle orders in real time.

## Architecture

**Clean Architecture + MVVM** with Jetpack Compose.

```
com.orderit/
├── core/           # DI (Hilt modules), networking (Retrofit), utilities
├── data/           # DTOs (@Serializable), repository implementations, SessionStore
├── domain/         # Repository interfaces, domain models
└── ui/             # Compose screens, ViewModels (StateFlow), components, theme
```

| Layer | Responsibility |
|-------|---------------|
| **UI** | Jetpack Compose screens + ViewModels with `StateFlow` |
| **Domain** | Repository interfaces and domain models |
| **Data** | Repository implementations, API service (Retrofit), local storage |

## Tech Stack

| Category | Technology | Version |
|----------|-----------|---------|
| UI | Jetpack Compose (Material3) | BOM 2025.12.01 |
| Navigation | Navigation Compose (type-safe) | 2.9.6 |
| DI | Dagger Hilt | 2.57.2 |
| Networking | Retrofit + OkHttp | 3.0.0 / 5.3.2 |
| Serialization | kotlinx-serialization-json | 1.9.0 |
| Images | Coil Compose | 2.7.0 |
| Analytics | Firebase Crashlytics + Analytics | BOM 34.7.0 |
| Language | Kotlin | 2.2.0 |
| Min SDK | Android 11 (API 30) | - |
| Target SDK | Android 16 (API 36) | - |

## Screens

| Screen | Description |
|--------|------------|
| **Login** | Username-based waiter authentication |
| **Dashboard** | Daily summary: total orders, sales, status counts, featured products |
| **Orders** | Order grid with a side detail panel (status, products, payment) |
| **Menu** | Product catalog by category with a form to create/edit products |
| **Metrics** | Statistics: revenue, average order value, top products, status breakdown |

## API

REST backend deployed on Google Cloud Run.

```
Base URL: https://orderit-806794513763.us-east1.run.app/
```

Main endpoints: `/waiters`, `/categories`, `/products`, `/tables`, `/orders`, `/orders/{id}/details`

## Setup

### Requirements

- Android Studio Ladybug or later
- JDK 11+
- Android tablet or emulator (API 30+, landscape)

### Steps

1. Clone the repository
   ```bash
   git clone <repo-url>
   cd OrderIt
   ```

2. Add your Firebase `google-services.json` file inside `app/`
   > This file is not included in the repo for security reasons. Get it from the Firebase console for this project.

3. Sync and build from Android Studio or the terminal:
   ```bash
   ./gradlew assembleDebug
   ```

4. Run on a tablet emulator in landscape mode.

## File Structure

```
app/src/main/java/com/orderit/
├── MainActivity.kt                  # Entry point (@AndroidEntryPoint)
├── OrderItApp.kt                    # Hilt Application
├── core/
│   ├── di/
│   │   ├── NetworkModule.kt         # Retrofit, OkHttp, Json providers
│   │   └── RepositoryModule.kt      # Repository bindings
│   ├── network/
│   │   └── OrderItApi.kt            # Retrofit interface (all endpoints)
│   ├── result/
│   │   └── AppResult.kt             # Sealed class Ok/Err + safeApiCall()
│   └── util/
│       └── TimeUtils.kt             # "X min ago" helper, date formatting
├── data/
│   ├── local/
│   │   └── SessionStore.kt          # SharedPreferences for waiter session
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
│   │   └── OrderWithDetails.kt      # Order + details + resolved products
│   └── repository/
│       ├── CategoryRepository.kt
│       ├── OrderRepository.kt
│       ├── ProductRepository.kt
│       └── WaiterRepository.kt
└── ui/
    ├── components/
    │   ├── SideBar.kt                # Permanent side navigation
    │   └── TopBar.kt                 # Search bar + cart icon
    ├── dashboard/
    │   ├── DashboardScreen.kt
    │   └── DashboardViewModel.kt
    ├── login/
    │   ├── LoginScreen.kt
    │   └── LoginViewModel.kt
    ├── menu/
    │   ├── MenuScreen.kt
    │   ├── MenuViewModel.kt
    │   └── ProductFormPanel.kt       # Create/edit product form
    ├── metrics/
    │   ├── MetricsScreen.kt
    │   └── MetricsViewModel.kt
    ├── navigation/
    │   ├── MainScaffold.kt           # Main layout (sidebar + content)
    │   ├── NavGraph.kt               # Login vs Main routing
    │   └── Routes.kt                 # Type-safe routes
    ├── orders/
    │   ├── OrdersScreen.kt           # Grid + detail panel
    │   └── OrdersViewModel.kt
    └── theme/
        ├── Color.kt
        ├── Theme.kt
        └── Type.kt
```
