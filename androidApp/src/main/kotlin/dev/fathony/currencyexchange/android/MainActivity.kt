package dev.fathony.currencyexchange.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.github.michaelbull.result.Ok
import dev.fathony.currencyexchange.Currencies
import dev.fathony.currencyexchange.CurrencyExchange
import dev.fathony.currencyexchange.PlatformDependencies
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
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

    LaunchedEffect(Unit) {
        launch {
            val currencies =
                currencyExchange.getCurrencies().filterIsInstance<Ok<Currencies>>().first().value
            val firstCurrency = currencies.random()
            val secondCurrency = currencies.random()

            currencyExchange.getRate(firstCurrency, secondCurrency).collect()
        }
    }

    CurrencyExchangeTheme {
        ConversionScreen()
    }
}
