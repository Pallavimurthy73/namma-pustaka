package com.nammapustaka.app.database

import java.time.Duration

object SeedData {
    val users = listOf(
        UserEntity(name = "Priya Kulkarni", role = "student", roll = "12", pin = "1234"),
        UserEntity(name = "Arjun Bhat", role = "student", roll = "04", pin = "2222"),
        UserEntity(name = "Meena Sharma", role = "student", roll = "22", pin = "3333"),
        UserEntity(name = "Raju Murthy", role = "student", roll = "33", pin = "4444"),
    )

    val books = listOf(
        BookEntity(
            title = "Malgudi Days",
            author = "R. K. Narayan",
            category = "Story",
            pages = 248,
            summary = "ಮಾಲ್ಗುಡಿಯ ಸಣ್ಣ ಪಟ್ಟಣದಲ್ಲಿ ಜನರ ಬದುಕು, ಸ್ನೇಹ ಮತ್ತು ಮಮತೆಯ ಸೊಗಸಾದ ಕಥೆಗಳು.",
            available = true,
            qrData = "BOOK_1700000000000001",
        ),
        BookEntity(
            title = "A Brief History of Time",
            author = "Stephen Hawking",
            category = "Science",
            pages = 212,
            summary = "ಅಂತರಿಕ್ಷ, ಕಾಲ ಮತ್ತು ಬ್ರಹ್ಮಾಂಡದ ರಹಸ್ಯಗಳನ್ನು ಸರಳವಾಗಿ ಪರಿಚಯಿಸುವ ವಿಜ್ಞಾನ ಕೃತಿ.",
            available = false,
            qrData = "BOOK_1700000000000002",
        ),
        BookEntity(
            title = "The Jungle Book",
            author = "Rudyard Kipling",
            category = "Story",
            pages = 198,
            summary = "ಕಾಡಿನಲ್ಲೇ ಬೆಳೆದ ಮಗುವಿನ ಸಾಹಸ, ಸ್ನೇಹ ಮತ್ತು ಬದುಕಿನ ಪಾಠಗಳ ಕತೆ.",
            available = true,
            qrData = "BOOK_1700000000000003",
        ),
        BookEntity(
            title = "Science All Around",
            author = "NCERT",
            category = "Science",
            pages = 164,
            summary = "ನಮ್ಮ ಸುತ್ತಮುತ್ತಲಿನ ಪ್ರಕೃತಿ ಮತ್ತು ದಿನನಿತ್ಯದ ಅನುಭವಗಳಲ್ಲಿ ಇರುವ ವಿಜ್ಞಾನವನ್ನು ತೋರಿಸುವ ಪುಸ್ತಕ.",
            available = true,
            qrData = "BOOK_1700000000000004",
        ),
        BookEntity(
            title = "Tipu Sultan",
            author = "B. Sheik Ali",
            category = "History",
            pages = 320,
            summary = "ಟಿಪ್ಪು ಸುಲ್ತಾನನ ಸಾಹಸ, ನಾಯಕತ್ವ ಮತ್ತು ಮೈಸೂರು ಇತಿಹಾಸದ ಸಂಕ್ಷಿಪ್ತ ಪರಿಚಯ.",
            available = false,
            qrData = "BOOK_1700000000000005",
        ),
        BookEntity(
            title = "Wild India",
            author = "Salim Ali",
            category = "Nature",
            pages = 190,
            summary = "ಭಾರತದ ಅರಣ್ಯಗಳು, ಪಕ್ಷಿಗಳು ಮತ್ತು ವನ್ಯಜೀವಿಗಳ ಅದ್ಭುತ ಲೋಕವನ್ನು ಪರಿಚಯಿಸುವ ಪುಸ್ತಕ.",
            available = false,
            qrData = "BOOK_1700000000000006",
        ),
        BookEntity(
            title = "Chandamama Kathegalu",
            author = "Kannada Classics",
            category = "Story",
            pages = 126,
            summary = "ಮಕ್ಕಳಿಗೆ ಹಾಸ್ಯ, ಬುದ್ಧಿವಂತಿಕೆ ಮತ್ತು ನೈತಿಕತೆಯನ್ನು ಕೊಡುವ ಕನ್ನಡ ಕಥೆಗಳ ಸಂಕಲನ.",
            available = true,
            qrData = "BOOK_1700000000000007",
        ),
        BookEntity(
            title = "Water Wisdom",
            author = "Rural Futures",
            category = "Nature",
            pages = 144,
            summary = "ನೀರು ಸಂರಕ್ಷಣೆ, ಹಳ್ಳಿಯ ಪರಿಸರ ಮತ್ತು ಸುಸ್ಥಿರ ಬದುಕಿನ ಮಹತ್ವವನ್ನು ತಿಳಿಸುವ ಪುಸ್ತಕ.",
            available = true,
            qrData = "BOOK_1700000000000008",
        ),
    )

    fun transactions(nowMillis: Long): List<BorrowTransactionEntity> {
        val day = Duration.ofDays(1).toMillis()
        return listOf(
            BorrowTransactionEntity(
                userId = 1,
                bookId = 5,
                issueDateMillis = nowMillis - (16 * day),
                dueDateMillis = nowMillis - (2 * day),
                returned = false,
            ),
            BorrowTransactionEntity(
                userId = 2,
                bookId = 2,
                issueDateMillis = nowMillis - (6 * day),
                dueDateMillis = nowMillis + (8 * day),
                returned = false,
            ),
            BorrowTransactionEntity(
                userId = 3,
                bookId = 6,
                issueDateMillis = nowMillis - (4 * day),
                dueDateMillis = nowMillis + (10 * day),
                returned = false,
            ),
            BorrowTransactionEntity(
                userId = 1,
                bookId = 1,
                issueDateMillis = nowMillis - (30 * day),
                dueDateMillis = nowMillis - (18 * day),
                returned = true,
            ),
            BorrowTransactionEntity(
                userId = 1,
                bookId = 7,
                issueDateMillis = nowMillis - (22 * day),
                dueDateMillis = nowMillis - (10 * day),
                returned = true,
            ),
            BorrowTransactionEntity(
                userId = 2,
                bookId = 4,
                issueDateMillis = nowMillis - (28 * day),
                dueDateMillis = nowMillis - (14 * day),
                returned = true,
            ),
            BorrowTransactionEntity(
                userId = 2,
                bookId = 3,
                issueDateMillis = nowMillis - (18 * day),
                dueDateMillis = nowMillis - (4 * day),
                returned = true,
            ),
            BorrowTransactionEntity(
                userId = 4,
                bookId = 8,
                issueDateMillis = nowMillis - (14 * day),
                dueDateMillis = nowMillis - day,
                returned = true,
            ),
        )
    }

    val reviews = listOf(
        ReviewEntity(userId = 1, bookId = 1, rating = 5, review = "ಮಾಲ್ಗುಡಿಯ ಕಥೆಗಳು ತುಂಬಾ ಮನರಂಜನೆ ನೀಡಿದವು."),
        ReviewEntity(userId = 1, bookId = 7, rating = 4, review = "ಮನೆದಲ್ಲೂ ಓದಲು ಇಷ್ಟವಾಗುವ ಚಂದದ ಕನ್ನಡ ಕಥೆಗಳು."),
        ReviewEntity(userId = 2, bookId = 4, rating = 4, review = "ವಿಜ್ಞಾನವನ್ನು ಸರಳವಾಗಿ ಹೇಳಿರುವುದು ಬಹಳ ಚೆನ್ನಾಗಿದೆ."),
        ReviewEntity(userId = 4, bookId = 8, rating = 3, review = "ಗ್ರಾಮದಲ್ಲಿ ನೀರಿನ ಮಹತ್ವವನ್ನು ಚೆನ್ನಾಗಿ ವಿವರಿಸಿದೆ."),
    )
}
