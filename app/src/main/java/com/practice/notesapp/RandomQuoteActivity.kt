package com.practice.notesapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class RandomQuoteActivity : AppCompatActivity() {
    private lateinit var quoteTextView: TextView
    private lateinit var authorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_random_quote)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        quoteTextView = findViewById(R.id.quoteTextView)
        authorTextView = findViewById(R.id.authorTextView)

        quoteTextView.text = ""
        authorTextView.text = ""

        val generateQuoteBtn = findViewById<Button>(R.id.generateQuoteButton)
        generateQuoteBtn.setOnClickListener {
            fetchQuote()
        }

        fetchQuote()
    }

    // Function to fetch quote using Kotlin Coroutines
    private fun fetchQuote() {
        // Launch a coroutine on the main thread
        lifecycleScope.launch {
            val result = fetchDataFromApi("https://zenquotes.io/api/random") // TODO you should probably use an API key here lol
            result?.let {
                val jsonArray = JSONArray(it)
                val jsonObject = jsonArray.getJSONObject(0)
                val quote = jsonObject.getString("q")
                val author = jsonObject.getString("a")

                // Update the TextView with the quote
                quoteTextView.text = quote
                authorTextView.text = author
            }
        }
    }

    // Suspend function to fetch data from the API
    private suspend fun fetchDataFromApi(urlString: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(urlString)
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                val reader = InputStreamReader(urlConnection.inputStream)
                val result = StringBuilder()
                var data = reader.read()
                while (data != -1) {
                    result.append(data.toChar())
                    data = reader.read()
                }
                reader.close()
                result.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

}