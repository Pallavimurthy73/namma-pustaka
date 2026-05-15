package com.nammapustaka.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammapustaka.app.ui.components.SectionCard
import com.nammapustaka.app.ui.theme.CardBorder
import com.nammapustaka.app.ui.theme.Gold
import com.nammapustaka.app.ui.theme.Ink
import com.nammapustaka.app.ui.theme.InkSoft
import com.nammapustaka.app.ui.theme.Saffron
import com.nammapustaka.app.ui.theme.SaffronDark
import com.nammapustaka.app.ui.theme.SaffronLight
import com.nammapustaka.app.viewmodel.AppUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    uiState: AppUiState,
    onRefresh: suspend () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val currentId = uiState.currentUser?.id
    val myIndex = uiState.leaderboard.indexOfFirst { it.user.id == currentId }
    val myRank = if (myIndex >= 0) myIndex + 1 else 0
    val myEntry = uiState.leaderboard.getOrNull(myIndex)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Leaderboard") },
                actions = {
                    IconButton(onClick = { scope.launch { onRefresh() } }) {
                        Icon(Icons.Rounded.Refresh, contentDescription = "Refresh")
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SaffronLight),
                    border = BorderStroke(1.dp, Saffron),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = if (myRank > 0) "#$myRank" else "\uD83C\uDFC6",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Black,
                            color = Saffron,
                        )
                        Column(
                            modifier = Modifier.padding(start = 14.dp),
                        ) {
                            Text(
                                text = myEntry?.user?.name ?: "School Reading Board",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = SaffronDark,
                            )
                            Text(
                                text = if (myEntry != null) {
                                    "${myEntry.pagesRead} pages read across ${myEntry.booksRead} books"
                                } else {
                                    "Pages read and books completed decide the ranking."
                                },
                                modifier = Modifier.padding(top = 4.dp),
                                color = InkSoft,
                            )
                        }
                    }
                }
            }

            if (uiState.leaderboard.isEmpty()) {
                item {
                    SectionCard {
                        Text(
                            text = "Leaderboard data will appear once students start returning books.",
                            color = InkSoft,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            } else {
                itemsIndexed(uiState.leaderboard) { index, item ->
                    val rank = index + 1
                    val isCurrentUser = item.user.id == currentId
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCurrentUser) SaffronLight else Color.White,
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (isCurrentUser) Saffron else CardBorder,
                        ),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(rankColor(rank), RoundedCornerShape(18.dp))
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                            ) {
                                Text(
                                    text = "$rank",
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 12.dp),
                            ) {
                                Text(
                                    text = if (isCurrentUser) "${item.user.name} (You)" else item.user.name,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                )
                                Text(
                                    text = "Roll No. ${item.user.roll ?: "--"}",
                                    color = InkSoft,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(top = 3.dp),
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${item.pagesRead} pg",
                                    color = Ink,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                )
                                Text(
                                    text = "${item.booksRead} books",
                                    color = InkSoft,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun rankColor(rank: Int): Color = when (rank) {
    1 -> Gold
    2 -> Color(0xFF9098A4)
    3 -> Color(0xFFB87A49)
    else -> InkSoft
}
