Namma-Pustaka (“Our Book” in Kannada) is an Android application designed to digitize and modernize library management in rural schools. The app helps teachers manage books efficiently while encouraging students to build reading habits through reviews, ratings, and a gamified leaderboard.

Built with an offline-first architecture, the application works reliably even in schools with limited or unstable internet connectivity.

✨ Key Features
📖 Digital Book Catalog
Browse books in a clean grid layout
Search by title or author
Filter by categories like Story, Science, and History
View Kannada summaries and availability status
📷 Camera-Based Book Entry
Capture book cover using CameraX
Add title, author, category, pages, and language
Auto-generate QR codes for each book
Optional ISBN scanning with Google ML Kit
🔍 QR Borrow & Return System
Scan QR codes to issue and return books
Track due dates automatically
Highlight overdue books in red
Maintain complete borrowing history
⭐ Review Corner
Students can rate books (1–5 stars)
Submit short reviews in Kannada or English
Display average ratings in the catalog
🏆 Reading Leaderboard
Track pages read per month
Show top readers in the school
Encourage healthy reading competition
👩‍🏫 Teacher & Student Profiles
Teacher PIN authentication
Student borrowing history
Pages-read statistics
Role-based access control

🛠️ Tech Stack

Layer	Technology
Language	Kotlin
Architecture	MVVM + Repository Pattern
Database	Room DB (SQLite)
UI	Jetpack Compose
QR Scanning	Google ML Kit
QR Generation	ZXing
Camera	CameraX
Dependency Injection	Hilt
Background Tasks	WorkManager
Navigation	Jetpack Navigation
Image Loading	Coil

Platform Support
Android API Level 21+
Optimized for low and mid-range Android devices
Offline-first functionality

app/
├── data/
│   ├── local/
│   ├── repository/
│   └── model/
├── domain/
├── ui/
│   ├── catalog/
│   ├── issue/
│   ├── return/
│   ├── leaderboard/
│   ├── reviews/
│   └── profiles/
├── worker/
├── utils/
└── di/
Kannada (kn-IN) + English language support

🔐 Security
Teacher role protected by PIN
PIN hashing using SHA-256
Student data stored locally only
No external server dependency in MVP

🌐 Offline-First Design

All major features work without internet:
Book catalog
QR issue/return
Reviews
Leaderboard
Student profiles
Room DB acts as the single source of truth.

Screens Included
             Book Catalog
             Book Detail
             Add Book
             Issue Book
             Return Book
             Overdue Dashboard
             Review Corner
             Reading Leaderboard
             Student Profiles
             Settings

🎯 MVP Goals
Digitize rural school libraries
Reduce manual paperwork
Improve book discoverability
Encourage reading culture
Simplify book tracking

🧪 Future Enhancements
Firebase cloud backup
Multi-school support
AI-generated Kannada summaries
Parent notifications
Tablet optimization
ISBN-only return support
