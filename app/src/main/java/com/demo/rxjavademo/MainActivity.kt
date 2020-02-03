package com.demo.rxjavademo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    private lateinit var textListener: TextWatcher
    private var textChangedJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inputText.setText("")
        loadList("")
        testCarotene()
    }

    private fun testCarotene() {
        textListener = object : TextWatcher {
            private var searchFor = "" // Or view.editText.text.toString()
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                if (searchText != searchFor) {
                    searchFor = searchText
                    textChangedJob?.cancel()
                    textChangedJob = GlobalScope.launch(Dispatchers.Main) {
                        delay(500L)
                        if (searchText == searchFor) {
                            loadList(searchText)
                        }
                    }
                }
            }
        }
    }

    private fun loadList(searchText: String) {
        textView.text = searchText
    }


    override fun onResume() {
        super.onResume()
        inputText.addTextChangedListener(textListener)
    }

    override fun onPause() {
        inputText.removeTextChangedListener(textListener)
        super.onPause()
    }


    override fun onDestroy() {
        textChangedJob?.cancel()
        super.onDestroy()
    }
}
