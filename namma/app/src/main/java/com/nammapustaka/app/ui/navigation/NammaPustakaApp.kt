package com.nammapustaka.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nammapustaka.app.ui.screens.AddBookScreen
import com.nammapustaka.app.ui.screens.BookDetailScreen
import com.nammapustaka.app.ui.screens.BookQrScreen
import com.nammapustaka.app.ui.screens.LeaderboardScreen
import com.nammapustaka.app.ui.screens.LibraryScreen
import com.nammapustaka.app.ui.screens.LoginScreen
import com.nammapustaka.app.ui.screens.QrScanScreen
import com.nammapustaka.app.ui.screens.TeacherDashboardScreen
import com.nammapustaka.app.viewmodel.LibraryViewModel

@Composable
fun NammaPustakaApp(
    viewModel: LibraryViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (!uiState.isLoggedIn) {
        LoginScreen(viewModel = viewModel)
        return
    }

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val currentRoute = currentDestination?.route
    val showBottomBar = topLevelDestinations.any { destination ->
        currentDestination?.hierarchy?.any { it.route == destination.route } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    topLevelDestinations.forEach { destination ->
                        val selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selected) destination.selectedIcon else destination.icon,
                                    contentDescription = destination.label,
                                )
                            },
                            label = { androidx.compose.material3.Text(destination.label) },
                        )
                    }
                }
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            NavHost(
                navController = navController,
                startDestination = TopLevelDestination.Library.route,
            ) {
                composable(TopLevelDestination.Library.route) {
                    LibraryScreen(
                        uiState = uiState,
                        onLogout = viewModel::logout,
                        onRefresh = { viewModel.refresh() },
                        onSearchChanged = viewModel::setSearchQuery,
                        onCategorySelected = viewModel::setCategory,
                        onOpenBook = { bookId -> navController.navigate(AppDestination.bookDetail(bookId)) },
                    )
                }

                composable(TopLevelDestination.Scanner.route) {
                    QrScanScreen(
                        uiState = uiState,
                        onBorrowFromQr = viewModel::borrowBookFromQr,
                    )
                }

                composable(TopLevelDestination.Leaderboard.route) {
                    LeaderboardScreen(
                        uiState = uiState,
                        onRefresh = { viewModel.refresh() },
                    )
                }

                composable(TopLevelDestination.TeacherDashboard.route) {
                    TeacherDashboardScreen(
                        uiState = uiState,
                        onLogout = viewModel::logout,
                        onRefresh = { viewModel.refresh() },
                        onReturnBook = { borrow -> viewModel.returnBorrowedBook(borrow) },
                        onOpenAddBook = { navController.navigate(AppDestination.ADD_BOOK) },
                        onOpenBookQr = { bookId -> navController.navigate(AppDestination.bookQr(bookId)) },
                    )
                }

                composable(
                    route = AppDestination.BOOK_DETAIL,
                    arguments = listOf(navArgument("bookId") { type = androidx.navigation.NavType.LongType }),
                ) { entry ->
                    val bookId = entry.arguments?.getLong("bookId") ?: 0L
                    BookDetailScreen(
                        uiState = uiState,
                        bookId = bookId,
                        onBack = navController::popBackStack,
                        onSaveReview = viewModel::submitReview,
                        onNavigateToScanner = {
                            navController.navigate(TopLevelDestination.Scanner.route) {
                                popUpTo(TopLevelDestination.Library.route) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        },
                    )
                }

                composable(AppDestination.ADD_BOOK) {
                    AddBookScreen(
                        uiState = uiState,
                        onBack = navController::popBackStack,
                        onAddBook = viewModel::addBook,
                        onBookSaved = { bookId ->
                            navController.navigate(AppDestination.bookQr(bookId, showSuccess = true)) {
                                popUpTo(AppDestination.ADD_BOOK) {
                                    inclusive = true
                                }
                            }
                        },
                    )
                }

                composable(
                    route = AppDestination.BOOK_QR,
                    arguments = listOf(
                        navArgument("bookId") { type = androidx.navigation.NavType.LongType },
                        navArgument("showSuccess") { type = androidx.navigation.NavType.BoolType },
                    ),
                ) { entry ->
                    BookQrScreen(
                        uiState = uiState,
                        bookId = entry.arguments?.getLong("bookId") ?: 0L,
                        showSuccessMessage = entry.arguments?.getBoolean("showSuccess") == true,
                        onBack = navController::popBackStack,
                    )
                }
            }
        }
    }
}
