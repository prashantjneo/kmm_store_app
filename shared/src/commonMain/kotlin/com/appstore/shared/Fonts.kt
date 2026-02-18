package com.nutrisport.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import kmm_product_listing.shared.generated.resources.Res
import kmm_product_listing.shared.generated.resources.bebas_neue_regular
import kmm_product_listing.shared.generated.resources.roboto_condensed_medium


@Composable
fun BebasNeueFont() = FontFamily(
    org.jetbrains.compose.resources.Font(Res.font.bebas_neue_regular)
)

@Composable
fun RobotoCondensedFont() = FontFamily(
    org.jetbrains.compose.resources.Font(Res.font.roboto_condensed_medium)
)

object FontSize {
    val SMALL = 12.sp
    val REGULAR = 14.sp
    val MEDIUM = 18.sp
    val LARGE = 30.sp
}