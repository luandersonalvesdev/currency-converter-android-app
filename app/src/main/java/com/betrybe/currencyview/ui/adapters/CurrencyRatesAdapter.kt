package com.betrybe.currencyview.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.betrye.currencyview.R

class CurrencyRatesAdapter(private val currencyRates: List<Pair<String, Double>>) :
    Adapter<CurrencyRatesAdapter.CurrencyRatesPlaceHolder>() {

    class CurrencyRatesPlaceHolder(view: View) : ViewHolder(view) {
        val itemCurrencyName: TextView = view.findViewById(R.id.item_currency_name)
        val itemCurrencyRate: TextView = view.findViewById(R.id.item_currency_rate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyRatesPlaceHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_currency_rate, parent, false)
        return CurrencyRatesPlaceHolder(view)
    }

    override fun getItemCount(): Int = currencyRates.size

    override fun onBindViewHolder(holder: CurrencyRatesPlaceHolder, position: Int) {
        var currencyRate = currencyRates[position]

        holder.itemCurrencyName.text = currencyRate.first
        holder.itemCurrencyRate.text = currencyRate.second.toString()
    }
}
