package com.example.russiancompanies

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stat)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "results.db"
        ).build()

        val companiesLiveData = db.resultsDao().getAll("RESULT DESC")
        companiesLiveData.observe(this, Observer { companies ->
            GlobalScope.launch {
                displayStatistics(companies)
            }
        })
    }

    private suspend fun displayStatistics(companies: List<ResultEntity>) {
        withContext(Dispatchers.Main) {
            Log.i("mytag", companies.toString())
            val totalCapitalization = companies.sumOf { it.result ?: 0 }
            val averageCapitalization = if (companies.isNotEmpty()) {
                totalCapitalization / companies.size
            } else {
                0
            }

            val aboveAverageCount = companies.count { it.result ?: 0 > averageCapitalization }
            val englishNamesCount =
                companies.count { it.name?.any { c -> c.isLetter() && c !in 'а'..'я' } ?: false }
            val maxCapitalizationCompany = companies.maxByOrNull { it.result ?: 0 }
            val longestNameCompany = companies.maxByOrNull { it.name?.length ?: 0 }

            findViewById<TextView>(R.id.money).text = totalCapitalization.toString()
            findViewById<TextView>(R.id.good).text = aboveAverageCount.toString()
            findViewById<TextView>(R.id.english).text = englishNamesCount.toString()
            findViewById<TextView>(R.id.best).text = maxCapitalizationCompany?.name ?: "N/A"
            findViewById<TextView>(R.id.longest).text = longestNameCompany?.name ?: "N/A"
        }
    }
}