package dev.fathony.currencyexchange.android

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow

private val randomState = listOf("Hello", "World", "Where", "Are", "We")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversionScreen() {
    SnackbarContainer {
        val fooBar = LocalSnackbarHostState.current

        LaunchedEffect(Unit) {
            fooBar.showSnackbar("Hello world!")
        }

        val currentState = rememberSaveable { mutableStateOf(randomState.random()) }

        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.app_name),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { padding ->
            Column(
                modifier = Modifier.padding(padding)
            ) {
                OutlineDropdownButton(currentState.value) {
                    currentState.value = randomState.random()
                }
                ButtonDefaults
            }
        }
    }
}
