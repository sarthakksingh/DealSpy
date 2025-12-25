# 🚀 DealSpy – Smart Price Tracking App (Demo)

DealSpy is an Android application that helps users **track products, monitor prices, and manage watchlists** across multiple e-commerce platforms.  
The app focuses on **clean architecture, modern UI, and real-world Android development practices**.

> ⚠️ This is a **demo / pre-release build** created for portfolio and learning purposes.

---

## 📱 App Overview

DealSpy allows users to:
- Search products by category
- Add products to a watchlist
- Save items for later
- View price drops and discounts
- Customize app themes
- Manage profile and preferences

The UI is built with **Jetpack Compose** and follows a modern, dark-themed design with multiple theme options.

---

## ✨ Key Features

### 🔍 Product Search
- Search products by category (Phones, Apparel, Electronics, etc.)
- Popular search categories displayed visually
- Clean and responsive search UI

### ⭐ Watchlist
- Add products to a watchlist
- View current prices, discounts, and platforms
- Quick “Buy Now” redirection
- Remove items easily

### 💾 Saved Items
- Save products for later
- Manage saved items from the profile screen

### 🎨 Theme Customization
- Multiple custom themes (e.g. Obsidian Whisper, Neon Dusk, Midnight Bloom)
- Seamless theme switching from profile screen

### 👤 Profile Management
- View saved items
- Enable/disable notifications
- Change theme
- Log out / delete profile

---

## 🛠 Tech Stack

### Android
- **Kotlin**
- **Jetpack Compose**
- **MVVM Architecture**
- **Material 3**
- **Navigation Compose**
- **Retrofit**
- **OkHttp**
- **DataStore**

### Firebase
- Firebase Authentication
- Firebase Analytics
- Firebase Cloud Messaging (planned)

### Backend
- **Spring Boot REST APIs**
- Price and product data served from backend
- Deployed backend (demo environment)

---

## 🧱 Architecture

- MVVM (Model–View–ViewModel)
- Repository pattern
- Clean separation of UI, domain, and data layers
- API-driven data flow
- State handling using Compose best practices

---

## 📸 Screenshots

| Search | Watchlist |
|------|----------|
| ![](screenshots/search.jpg) | ![](screenshots/watchlist.jpg) |

| Profile | Theme Selector |
|--------|----------------|
| ![](screenshots/profile.jpg) | ![](screenshots/theme.jpg) |

> *(Add the screenshots inside a `/screenshots` folder and rename accordingly)*

---

## 📦 Demo APK

You can download the demo APK from **GitHub Releases**:

👉 **[Download DealSpy Demo APK](https://github.com/<your-username>/<repo-name>/releases)**

---

## 🚧 Project Status

- ✅ Core features implemented
- ✅ Release build generated
- 🚧 Notification automation (planned)
- 🚧 Price-drop alerts (planned)
- ❌ Not production-ready yet

This project is intended for **learning, demos, and portfolio showcase**.

---

## 👥 Contributors

- **Sunnik Chatterjee**
- **Sarthak**

Both contributors worked equally on design, development, and implementation.

---

## 📜 License

This project is licensed under the **MIT License**.  
See the [LICENSE](LICENSE) file for details.

---

## 🙌 Acknowledgements

- Android Jetpack & Compose team
- Firebase
- Open-source libraries used in the project
