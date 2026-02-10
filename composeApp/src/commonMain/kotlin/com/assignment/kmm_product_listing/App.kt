package com.assignment.kmm_product_listing

import Screen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.appstore.navigation.SetNavGraph

@Composable
@Preview
fun App() {
    MaterialTheme {
        val startDestination = remember {
            Screen.Auth
        }
        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = true
        ) {
            SetNavGraph(
                startDestination = startDestination
            )

        }
    }
}