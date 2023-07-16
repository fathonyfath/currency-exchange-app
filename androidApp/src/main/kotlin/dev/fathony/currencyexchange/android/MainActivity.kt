package dev.fathony.currencyexchange.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.github.michaelbull.result.fold
import dev.fathony.currencyexchange.api.CurrencyExchangeApi
import dev.fathony.currencyexchange.sqldelight.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App((application as MyApplication).getDatabase())
        }
    }
}

@Composable
fun App(database: Database) {
    val api = remember { CurrencyExchangeApi() }

    LaunchedEffect(Unit) {
        api.getCurrencies().fold(
            success = { currencies ->
                Log.d("MainActivity", "currencies: ${currencies.values}")
            },
            failure = { exception ->
                Log.d("MainActivity", "currencies: exception: $exception")
            }
        )
    }

    LaunchedEffect(Unit) {
        database.playerQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .collectLatest { hockeyPlayer ->
                Log.d("MainActivity", "hockeyPlayers: $hockeyPlayer")
            }
    }

    MyApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            GreetingView("Hello")
        }
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
