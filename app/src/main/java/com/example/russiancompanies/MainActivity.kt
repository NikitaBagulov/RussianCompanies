package com.example.russiancompanies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    val db by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java, "results.db"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = this
        GlobalScope.launch {
            for (company in TestData.russianCompanies2020) {
                db.resultsDao().insert(company)
            }
        }

        val companies_list = findViewById<RecyclerView>(R.id.companies_list)
        val statistics = findViewById<Button>(R.id.statistics)

        val deleteButton = findViewById<Button>(R.id.delete)
        val deleteEditText = findViewById<EditText>(R.id.toDelete)

        companies_list.layoutManager = LinearLayoutManager(this)
        db.resultsDao().getAll("RESULT DESC").observe(this
        ) { results -> companies_list.adapter = ResultAdapter(results) }

        statistics.setOnClickListener {
            startActivity(Intent(this, StatActivity::class.java))
        }

        deleteButton.setOnClickListener {
            val substring = deleteEditText.text.toString()
            GlobalScope.launch {
                db.resultsDao().deleteBySubstring(substring)
            }
        }

    }
}