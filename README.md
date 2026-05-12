# MyShoppingApp 🛒

![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-039BE5?style=for-the-badge&logo=Firebase&logoColor=white)

A high-performance, industry-grade E-commerce Android application built with **Modern Android Development (MAD)** tools. This project demonstrates the implementation of **Clean Architecture**, **MVVM**, and a robust payment ecosystem using **Razorpay**.

---

## ✨ Key Features

- **🛍️ Complete Shopping Loop**: From product discovery to secure checkout.
- **🔐 Secure Authentication**: Firebase-powered Email/Password login with token-based session management.
- **💎 Premium UI/UX**: 100% Jetpack Compose for smooth, declarative UI with Material 3 components.
- **💳 Payment Integration**: Production-ready **Razorpay** integration with secure key handling.
- **📦 Advanced Order Management**: Industry-standard order grouping (multiple items per order) and real-time status tracking.
- **📍 Address System**: Dynamic shipping address management with "Select Default" functionality.
- **🛒 Smart Cart**: Real-time stock validation and quantity syncing.
- **🔔 Real-time Notifications**: Order updates and promotional alerts via Firebase Cloud Messaging (FCM).

---

## 🛠 Tech Stack & Architecture

### **Architecture: Clean Architecture + MVVM**
The project is divided into three distinct layers to ensure scalability and testability:
- **`Domain Layer`**: Pure Kotlin layer containing Business Logic, Use Cases, and Repository Interfaces.
- **`Data Layer`**: Implementation of repositories, Firebase Firestore interactions, and Retrofit API services.
- **`UI Layer`**: Jetpack Compose screens, M3 components, and State-aware ViewModels using `StateFlow`.

### **Libraries & Tools**
- **DI**: Hilt (Dagger)
- **Networking**: Retrofit & OkHttp
- **Database**: Firebase Firestore
- **Async**: Kotlin Coroutines & Flow
- **Image Loading**: Coil
- **Serialization**: KotlinX Serialization

---

## 📸 Screenshots

| Home Page | Product Detail | Checkout | Order History | Tracking |
| :---: | :---: |:-------------------------------------------------------------------:| :---: | :---: |
| <img src="C:\Users\asus\Desktop\Png Logo\New folder\Screenshot_2026-05-13-02-08-35-60_9f051452c5acd4e8150126883bb94e1a.jpg" width="200"/> | <img src="C:\Users\asus\Desktop\Png Logo\New folder\Screenshot_2026-05-13-02-14-24-08.jpg" width="200"/> | <img src="C:\Users\asus\Desktop\Png Logo\New folder\IMG_20260513_025404.png" width="200"/> | <img src="C:\Users\asus\Desktop\Png Logo\New folder\Screenshot_2026-05-13-02-12-51-93_9f051452c5acd4e8150126883bb94e1a.jpg" width="200"/> | <img src="C:\Users\asus\Desktop\Png Logo\New folder\IMG_20260513_022129.jpg" width="200"/> |

---

## 🚀 Getting Started

### **Secure Configuration**
To run this project, you need to set up your local environment:
1. Add your `google-services.json` to the `app/` directory.
2. In your `local.properties`, add your Razorpay credentials:
   ```properties
   RAZORPAY_KEY_ID=your_actual_key_here
   ```

### **Installation**
```bash
git clone https://github.com/your-username/MyShoppingApp.git
cd MyShoppingApp
# Open in Android Studio and Sync Gradle
```

---

## 📈 Future Roadmaps
- [ ] Add Room Database for offline support.
- [ ] Implement Dark Mode support.
- [ ] Add Unit Tests for UseCases and ViewModels.
- [ ] Multi-language support (Localization).

---

## 🤝 Contributing
Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
*Developed with ❤️ by [Aditya Kumar]*
