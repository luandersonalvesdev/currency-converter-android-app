package com.betrybe.currencyview.ui.views.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.betrybe.currencyview.common.ApiIdlingResource
import com.betrybe.currencyview.data.SymbolsService
import com.betrybe.currencyview.ui.adapters.CurrencyRatesAdapter
import com.betrye.currencyview.R
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val mWaitResponseState: FrameLayout by lazy {
        findViewById(R.id.waiting_response_state)
    }

    private val mRecyclerCurrencyState: RecyclerView by lazy {
        findViewById(R.id.currency_rates_state)
    }

    private val mCurrencySelectLayout: AutoCompleteTextView by lazy {
        findViewById(R.id.currency_selection_input_layout)
    }
    private val mSelectCurrencyState: MaterialTextView by lazy {
        findViewById(R.id.select_currency_state)
    }

    private val mLoadCurrencyState: MaterialTextView by lazy {
        findViewById(R.id.load_currency_state)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mLoadCurrencyState.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()

        val context = this

        CoroutineScope(Dispatchers.IO).launch {
            try {
                ApiIdlingResource.increment()

                val responseSymbols = SymbolsService.instance.getSymbol()

                val symbolsMap = responseSymbols.body()?.symbols!!

                val symbolsList = symbolsMap.keys.toTypedArray()

                withContext(Dispatchers.Main) {
                    val adapter = ArrayAdapter(context, R.layout.item_symbol, symbolsList)
                    mCurrencySelectLayout.setAdapter(adapter)

                    mLoadCurrencyState.visibility = View.GONE

                    mSelectCurrencyState.visibility = View.VISIBLE

                    mCurrencySelectLayout.setOnItemClickListener { _, _, position, _ ->

                        mSelectCurrencyState.visibility = View.GONE

                        mWaitResponseState.visibility = View.VISIBLE

                        val base = symbolsList[position]

                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                ApiIdlingResource.increment()

                                val responseLatestRate =
                                    SymbolsService.instance.getLatestRates(base)

                                val latestRateMap = responseLatestRate.body()!!

                                withContext(Dispatchers.Main) {
                                    mRecyclerCurrencyState.layoutManager =
                                        LinearLayoutManager(baseContext)
                                    mRecyclerCurrencyState.adapter =
                                        CurrencyRatesAdapter(latestRateMap.rates.toList())

                                    mWaitResponseState.visibility = View.GONE
                                    mRecyclerCurrencyState.visibility = View.VISIBLE
                                }

                                ApiIdlingResource.decrement()
                            } catch (e: HttpException) {
                                ApiIdlingResource.decrement()
                                withContext(Dispatchers.Main) { showError(e) }
                            } catch (e: IOException) {
                                ApiIdlingResource.decrement()
                                withContext(Dispatchers.Main) { showError(e) }
                            }
                        }
                    }
                }

                ApiIdlingResource.decrement()
            } catch (e: HttpException) {
                ApiIdlingResource.decrement()
                withContext(Dispatchers.Main) { showError(e) }
            } catch (e: IOException) {
                ApiIdlingResource.decrement()
                withContext(Dispatchers.Main) { showError(e) }
            }
        }
    }

    fun showError(e: Exception) {
        Toast.makeText(this, "Erro: ${e.message}", Toast.LENGTH_SHORT)
            .show()
    }
}
