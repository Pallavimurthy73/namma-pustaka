# Namma Pustaka

Smart Library App for rural schools built with Flutter, Provider, SQLite, QR generation, and QR scanning.

## Features

- Single login screen for Student and Teacher
- Student registration with name, roll number, and PIN
- Teacher dashboard with default credentials: `admin / admin123`
- Book catalog with search, category filters, and grid layout
- Book details with Kannada summary and review system
- QR-based book issue flow using `mobile_scanner`
- Local SQLite storage using `sqflite`
- Leaderboard based on books read and pages read
- Teacher tools for add-book, QR generation, overdue tracking, and returns

## Run in VS Code

```bash
flutter pub get
flutter run
```

## Build APK

```bash
flutter build apk
```

The generated APK will be available under:

```text
build/app/outputs/flutter-apk/app-release.apk
```

## Demo Accounts

- Teacher: `admin / admin123`
- Student demo users:
  - `Priya Kulkarni / 12 / 1234`
  - `Arjun Bhat / 04 / 2222`
  - `Meena Sharma / 22 / 3333`
  - `Raju Murthy / 33 / 4444`

## Getting Started

This project is a starting point for a Flutter application.

A few resources to get you started if this is your first Flutter project:

- [Learn Flutter](https://docs.flutter.dev/get-started/learn-flutter)
- [Write your first Flutter app](https://docs.flutter.dev/get-started/codelab)
- [Flutter learning resources](https://docs.flutter.dev/reference/learning-resources)

For help getting started with Flutter development, view the
[online documentation](https://docs.flutter.dev/), which offers tutorials,
samples, guidance on mobile development, and a full API reference.
