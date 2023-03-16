package com.levi.tellmeajokeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.levi.tellmeajokeapp.ui.main.JokeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, JokeFragment.newInstance())
                .commitNow()
        }
    }
}