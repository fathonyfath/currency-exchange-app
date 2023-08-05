package dev.fathony.currencyexchange.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.fold
import dev.fathony.currencyexchange.Currencies
import dev.fathony.currencyexchange.CurrencyExchange
import dev.fathony.currencyexchange.PlatformDependencies
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    val appContext = LocalContext.current.applicationContext

    val currencyExchange = remember {
        CurrencyExchange(PlatformDependencies(appContext))
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        launch {
            val currencies = currencyExchange.getCurrencies().filterIsInstance<Ok<Currencies>>().first().value
            val firstCurrency = currencies.random()
            val secondCurrency = currencies.random()

            currencyExchange.getRate(firstCurrency, secondCurrency).collect()
        }
    }

    MyApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                GreetingView("Hello")
                Button(
                    onClick = {
                        scope.launch {
                            currencyExchange.refreshCurrencies().fold(
                                success = {
                                    Log.d("MainActivity", "refreshCurrencies(): success")
                                },
                                failure = {
                                    Log.d("MainActivity", "refreshCurrencies(): failure: $it")
                                }
                            )
                        }
                    }
                ) {
                    Text(text = "Click Me!")
                }
            }
        }
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}
