package dev.fathony.currencyexchange.android

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember

val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState provided.")
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SnackbarContainer(content: @Composable () -> Unit) {
    val snackBarState = remember { SnackbarHostState() }

    CompositionLocalProvider(LocalSnackbarHostState provides snackBarState) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackBarState) }
        ) { content() }
    }
}
