package com.aristotele.sleeptracker2022

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * هیچ تغییری اینجا نداریم و فقط لایه اصلی رو میخونه که داخلش
 * یک NavHostFragment هست
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}