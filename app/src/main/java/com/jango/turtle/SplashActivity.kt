package com.jango.turtle

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import com.jango.turtle.ui.search.SearchActivity
/**
 * Created by Ashwani on 11/06/18.
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        Handler().postDelayed(Runnable {
            var intent = Intent(this@SplashActivity, SearchActivity::class.java)
            startActivity(intent)
            finish()
        },2000)
    }
}
