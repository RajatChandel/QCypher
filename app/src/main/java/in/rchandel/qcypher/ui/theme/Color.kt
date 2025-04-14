package `in`.rchandel.qcypher.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val PayneGray = Color(0xFF262626)
val PersianOrange = Color(0xFFC18C5D)
val OldRose = Color(0xFFCE796B)
val Melon = Color(0xFFE7AD99)
val DesertSand = Color(0xFFECC8AF)
val Black = Color(0xFF090809)
val Isabelline = Color(0xFFF2EFE9)
val Night = Color(0xFF080F0F)

val NewBlue = Color(0xFF222831)
val NewBlueLight = Color(0xFF393E46)
val NewBlueGray = Color(0xFFF39F5A)
val SomeKindOfOrange = Color(0xFFEEEEEE)

val BackgroundCharcoalBlack = Color(0xFF121212)
val PrimaryTextLightGray = Color(0xFFE0E0E0)
val SecondaryTextMediumGray = Color(0xFFB0B0B0)
val BordersDividersDarkGray = Color(0xFF444444)
val AccentSoftGray = Color(0xFF888888)

val surfaceVariant = lightenColor(BackgroundCharcoalBlack, 0.05f)

fun lightenColor(color: Color, factor: Float): Color {
    return Color(
        red = color.red + (1 - color.red) * factor,
        green = color.green + (1 - color.green) * factor,
        blue = color.blue + (1 - color.blue) * factor,
        alpha = color.alpha
    )
}