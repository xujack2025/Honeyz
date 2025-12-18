# Honeyz 🍯

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)
[![Language](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org/)
[![Framework](https://img.shields.io/badge/UI-Jetpack%20Compose-brightgreen.svg)](https://developer.android.com/jetpack/compose)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-28-orange.svg)](https://developer.android.com/about/versions/pie)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-34-orange.svg)](https://developer.android.com/about/versions/14)

A production-ready native Android e-commerce application for honey products, featuring separate admin and customer workflows with real-time data synchronization.

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Screenshots](#screenshots)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Firebase Setup](#firebase-setup)
- [Build & Run](#build--run)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## ✨ Features

### 👤 Customer Features
- **User Authentication**: Secure login and registration with Firebase Auth
- **Product Browsing**: Browse honey products with images and details
- **Shopping Cart**: Add/remove items with quantity management
- **Checkout Process**: Streamlined order placement and payment confirmation
- **Order History**: Track order status and history
- **Promotions**: View featured deals and news

### 🔐 Admin Features
- **Dashboard Analytics**: Real-time statistics for products, orders, and users
- **Product Management**: CRUD operations with stock control
- **Order Management**: Update order status (Pending → Shipped → Delivered/Cancelled)
- **Inventory Alerts**: Low stock notifications
- **News Management**: Manage promotional content
- **User Management**: Role-based access control

## 🛠️ Tech Stack

### Core Technologies
- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Manual DI with ViewModel sharing

### Android Jetpack Components
- **Navigation**: Navigation Compose
- **Lifecycle**: ViewModel, LiveData, StateFlow
- **Material Design**: Material3 Components
- **Activity**: Compose Activity Integration

### Backend & Database
- **Authentication**: [Firebase Authentication](https://firebase.google.com/products/auth)
- **Database**: [Cloud Firestore](https://firebase.google.com/products/firestore)
- **Analytics**: Firebase Analytics
- **BOM Version**: 33.3.0

### Additional Libraries
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/) (v2.2.2)
- **Async Operations**: Kotlin Coroutines
- **State Management**: StateFlow, MutableStateFlow

### Development Tools
- **Build System**: Gradle (Kotlin DSL)
- **Min SDK**: 28 (Android 9.0 Pie)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34

## 🏗️ Architecture

The app follows **MVVM (Model-View-ViewModel)** architecture pattern with clear separation of concerns:

```
┌─────────────────┐
│   UI Layer      │  → Jetpack Compose (Screens & Components)
│  (Composables)  │
└────────┬────────┘
         │
┌────────▼────────┐
│  ViewModel      │  → Business Logic & State Management
│    Layer        │
└────────┬────────┘
         │
┌────────▼────────┐
│  Data Layer     │  → Firebase Firestore & Authentication
│  (Repository)   │
└─────────────────┘
```

### Key Architectural Components

#### ViewModels
- `LoginViewModel`: Handles authentication and user role management
- `StockViewModel`: Manages product inventory
- `OrderViewModel`: Handles order lifecycle
- `CartViewModel`: Shopping cart state management
- `ProductViewModel`: Product selection and details

#### Data Models
- `Product`: Product entity with Firestore mapping
- `Order`: Order entity with status enum
- `CartItem`: Shopping cart item representation
- `News` & `Promotion`: Content management models
- `User`: User profile with role-based access

#### Navigation
- Separate navigation graphs for Admin and Customer flows
- Deep linking support
- Bottom navigation for quick access

## 📱 Screenshots

> _Add screenshots of your app here_

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 8 or higher
- Android SDK with API 34
- Firebase account (for backend services)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/honeyz.git
   cd honeyz
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory

3. **Sync Gradle**
   - Android Studio should automatically sync Gradle
   - If not, click `File > Sync Project with Gradle Files`

## 🔥 Firebase Setup

1. **Create Firebase Project**
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Create a new project or use an existing one

2. **Add Android App**
   - Register your app with package name: `com.example.honeyz`
   - Download `google-services.json`
   - Place it in the `app/` directory

3. **Enable Firebase Services**
   - **Authentication**: Enable Email/Password provider
   - **Firestore Database**: Create database in production mode
   - **Firebase Analytics**: Auto-enabled with SDK

4. **Firestore Collections Structure**
   ```
   /Products
      /{productId}
         - id: String
         - name: String
         - description: String
         - price: String
         - stock: Int
         - photoUrl: String
         - isDisabled: Boolean
   
   /Orders
      /{orderId}
         - orderId: String
         - customerName: String
         - productName: String
         - quantity: Int
         - status: String
         - paymentMethod: String
         - totalPrice: Double
   
   /Promotions
      /{promotionId}
         - id: String
         - description: String
         - imageUrl: String
   
   /users
      /{userId}
         - uid: String
         - email: String
         - role: String (admin/customer)
         /cart
            /{cartItemId}
               - productId: String
               - quantity: Int
   ```

5. **Set up Admin User**
   - Create a user in Firebase Authentication
   - Add a document in `/users/{userId}` with `role: "admin"`

## 🏃 Build & Run

### Debug Build

```bash
./gradlew assembleDebug
```

### Install on Device/Emulator

```bash
./gradlew installDebug
```

### Run from Android Studio

1. Connect Android device or start emulator
2. Click the **Run** button (or press `Shift + F10`)
3. Select target device

### Build Release APK

```bash
./gradlew assembleRelease
```

> **Note**: Configure signing keys in `local.properties` or `build.gradle.kts` for release builds

## 📂 Project Structure

```
Honeyz/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/honeyz/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── model/           # Data models
│   │   │   │   │   ├── Product.kt
│   │   │   │   │   ├── Order.kt
│   │   │   │   │   ├── CartItem.kt
│   │   │   │   │   └── ...
│   │   │   │   ├── ui/              # UI layer
│   │   │   │   │   ├── admin/       # Admin screens
│   │   │   │   │   ├── customer/    # Customer screens
│   │   │   │   │   ├── login/       # Auth screens
│   │   │   │   │   ├── navigation/  # Navigation logic
│   │   │   │   │   └── component/   # Reusable UI components
│   │   │   │   ├── viewmodel/       # ViewModels
│   │   │   │   └── data/            # Data layer
│   │   │   ├── res/                 # Resources
│   │   │   └── AndroidManifest.xml
│   │   ├── androidTest/             # Instrumented tests
│   │   └── test/                    # Unit tests
│   ├── build.gradle.kts
│   └── google-services.json         # (Add your own)
├── gradle/
│   └── libs.versions.toml           # Version catalog
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
└── README.md
```

## 🧪 Testing

### Run Unit Tests

```bash
./gradlew test
```

### Run Instrumented Tests

```bash
./gradlew connectedAndroidTest
```

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Coding Guidelines

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Write meaningful commit messages
- Add unit tests for new features
- Update documentation as needed

## 📄 License

No license. All rights reserved.

## 👨‍💻 Author

**Jack**

- GitHub: [@xujack2025](https://github.com/xujack2025)
- LinkedIn: [Xu Jack Chong](https://linkedin.com/in/xu-jack-chong-6b432b398)

## 🙏 Acknowledgments

- [Firebase](https://firebase.google.com/) for backend services
- [Jetpack Compose](https://developer.android.com/jetpack/compose) for modern UI toolkit
- [Coil](https://coil-kt.github.io/coil/) for efficient image loading
- [Material Design 3](https://m3.material.io/) for design guidelines

## 📞 Support

For support, email xujack2025@gmail.com or create an issue in the repository.

---

Made with ❤️ and ☕ by Jack
