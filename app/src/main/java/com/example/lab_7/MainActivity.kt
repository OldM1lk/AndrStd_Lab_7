package com.example.lab_7

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Timber.plant(Timber.DebugTree())

        val adapter = ContactAdapter()
        var allContacts: List<Contact> = emptyList()
        val etSearch: EditText = findViewById(R.id.et_search)
        val rView: RecyclerView = findViewById(R.id.rView)
        rView.layoutManager = LinearLayoutManager(this)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val contacts = parseContacts()
                allContacts = contacts
                withContext(Dispatchers.Main) {
                    adapter.submitList(contacts)
                    rView.adapter = adapter
                }
            }
            catch (e: IOException) {
                Timber.e("Ошибка запроса: ${e.message}")
            }
        }

        etSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filteredContacts = filterContacts(allContacts, etSearch.text.toString())
                adapter.submitList(filteredContacts)
                adapter.notifyDataSetChanged()
            }
        })
    }

    private suspend fun parseContacts(): List<Contact> {
        val client = OkHttpClient()
        val url = "https://drive.google.com/u/0/uc?id=1-KO-9GA3NzSgIc1dkAsNm8Dqw0fuPxcR&export=download"
        val request = Request.Builder().url(url).build()

        return withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()
            val responseBody = response.body
            val json = responseBody?.string()
            Gson().fromJson(json, Array<Contact>::class.java).toList()
        }
    }

    private fun filterContacts(allContacts: List<Contact>, query: String): List<Contact> {
        return if (query.isEmpty()) {
            allContacts
        }
        else {
            allContacts.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.phone.contains(query) ||
                        it.type.contains(query, ignoreCase = true)
            }
        }
    }
}