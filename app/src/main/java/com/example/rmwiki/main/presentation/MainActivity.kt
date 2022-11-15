package com.example.rmwiki.main.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rmwiki.R
import com.example.rmwiki.characters.presentation.CharactersFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CharactersFragment())
            .commit()
    }
}