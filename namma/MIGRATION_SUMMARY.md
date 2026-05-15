# Namma Pustaka Native Android Migration

## What Was Migrated

The Flutter app was rebuilt as a standalone native Android Studio project in `native-android/` using:

- Kotlin
- Jetpack Compose
- MVVM
- Room
- Coroutines + Flow
- CameraX + ML Kit
- Navigation Compose
- Android Print Framework

The source Flutter app was analyzed for:

- `lib/` screens, widgets, services, models, and database helpers
- `pubspec.yaml` package usage
- local authentication rules
- SQLite schema + seed data
- QR generation and QR scan flows
- OCR cover capture flow
- leaderboard logic
- teacher dashboard logic

## Analysis Findings

- Remote APIs: none
- Firebase: none
- Local DB: yes, `sqflite`
- State management: `provider` with `LibraryProvider`
- Navigation: `MaterialApp`, `IndexedStack`, `MaterialPageRoute`
- Authentication: local teacher credentials + local student table
- Assets folder: none declared in `pubspec.yaml`
- Printing/PDF: local QR PDF generation only

## Flutter to Kotlin Mapping

| Flutter / Dart | Native Android / Kotlin |
|---|---|
| `MaterialApp` + `AppGate` | `MainActivity` + `NammaPustakaApp` |
| `provider` / `ChangeNotifier` | `LibraryViewModel` + `StateFlow` |
| `sqflite` | `Room` |
| `DatabaseHelper` | `NammaPustakaDatabase` + DAO layer |
| `LibraryService` | `LibraryRepository` |
| `AuthService` | `AuthRepository` |
| `MobileScanner` | `CameraX` + ML Kit barcode scanning |
| `google_mlkit_text_recognition` | ML Kit Text Recognition |
| `qr_flutter` | ZXing QR bitmap generation |
| `printing` / `pdf` | Android Print Framework + `PdfDocument` |
| `ImagePicker.camera` | `ActivityResultContracts.TakePicture` + `FileProvider` |
| Flutter widgets (`BookCard`, `SectionCard`, etc.) | Compose components in `ui/components/` |
| `IndexedStack` bottom navigation | `NavigationBar` + Navigation Compose |

## Screen Mapping

- `login_screen.dart` -> `ui/screens/LoginScreen.kt`
- `home_shell.dart` -> `ui/navigation/NammaPustakaApp.kt`
- `library_screen.dart` -> `ui/screens/LibraryScreen.kt`
- `book_detail_screen.dart` -> `ui/screens/BookDetailScreen.kt`
- `leaderboard_screen.dart` -> `ui/screens/LeaderboardScreen.kt`
- `teacher_dashboard_screen.dart` -> `ui/screens/TeacherDashboardScreen.kt`
- `add_book_screen.dart` -> `ui/screens/AddBookScreen.kt`
- `qr_screen.dart` -> `ui/screens/BookQrScreen.kt`
- `qr_scan_screen.dart` -> `ui/screens/QrScanScreen.kt`

## Native Project Structure

- `activities/` host activity
- `viewmodel/` UI state + `LibraryViewModel`
- `repository/` auth and domain repositories
- `database/` Room entities, DAO interfaces, seed data, mappers
- `models/` domain models
- `utils/` OCR, QR generation, print, image storage, formatting
- `ui/components/` shared Compose components
- `ui/navigation/` navigation graph + routes
- `ui/screens/` screen-by-screen Compose implementations
- `api/`, `fragments/`, `adapters/` are present as reserved migration folders; the Flutter source app had no REST API layer and the native app uses Compose instead of Fragment/RecyclerView screen adapters

## SDK and Tooling

- JDK: 17
- Android Gradle Plugin: 8.5.2
- Kotlin: 2.0.21
- Compile SDK: 35
- Target SDK: 35
- Min SDK: 24

## Build and Run

1. Open `native-android/` in Android Studio.
2. Confirm `local.properties` points to your Android SDK path.
3. Sync Gradle.
4. Run the `app` configuration on an emulator or device.

Command line:

```bash
cd native-android
./gradlew.bat :app:assembleDebug
```

Generated debug APK:

- `app/build/outputs/apk/debug/app-debug.apk`

## Notes

- The native project is fully standalone and does not depend on Flutter.
- The original Flutter project was left intact outside `native-android/` as migration reference material.
- No Retrofit service was created because the source app has no remote API integrations to migrate.
