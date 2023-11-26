package com.betrybe.currencyview.ui.views.activities

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.betrybe.currencyview.common.ApiIdlingResource
import com.betrybe.currencyview.data.SymbolsService
import com.betrye.currencyview.R
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val mCurrencySelectLayout: AutoCompleteTextView by lazy {
        findViewById(R.id.currency_selection_input_layout)
    }
    private val mSelectCurrencyState: MaterialTextView by lazy {
        findViewById(R.id.select_currency_state)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        val context = this

        CoroutineScope(Dispatchers.IO).launch {
            try {
                ApiIdlingResource.increment()

                val responseSymbols = SymbolsService.instanceSymbol.getSymbol()

                val symbolsMap = responseSymbols.body()?.symbols!!

                val symbolsList = symbolsMap.keys.toTypedArray()

                withContext(Dispatchers.Main) {
                    val adapter = ArrayAdapter(context, R.layout.item_symbol, symbolsList)
                    mCurrencySelectLayout.setAdapter(adapter)

                    mSelectCurrencyState.visibility = View.VISIBLE

                    mCurrencySelectLayout.setOnItemClickListener { _, _, position, _ ->
                    }
                }
                ApiIdlingResource.decrement()
            } catch (e: HttpException) {
                ApiIdlingResource.decrement()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Algum erro ocorreu com o site: ${e.message}",
                        Toast.LENGTH_SHORT
                    )
                }
            } catch (e: IOException) {
                ApiIdlingResource.decrement()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Erro desconhecido: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
