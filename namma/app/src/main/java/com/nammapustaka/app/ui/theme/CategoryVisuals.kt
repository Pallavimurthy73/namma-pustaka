package com.nammapustaka.app.ui.theme

import androidx.compose.ui.graphics.Color

fun categoryColor(category: String): Color = when (category.lowercase()) {
    "story" -> Gold
    "science" -> Sky
    "history" -> Danger
    "nature" -> Leaf
    else -> Purple
}

fun categorySoftColor(category: String): Color = when (category.lowercase()) {
    "story" -> GoldLight
    "science" -> SkyLight
    "history" -> DangerLight
    "nature" -> LeafLight
    else -> PurpleLight
}

fun categoryEmoji(category: String): String = when (category.lowercase()) {
    "story" -> "\uD83C\uDF3E"
    "science" -> "\uD83D\uDE80"
    "history" -> "\uD83C\uDFF0"
    "nature" -> "\uD83C\uDF3F"
    else -> "\uD83D\uDCD8"
}
