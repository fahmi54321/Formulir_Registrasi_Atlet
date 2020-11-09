package com.mobile.formulirregistrasiatlet.View

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.mobile.formulirregistrasiatlet.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {

    // deklarasi animasi
    lateinit var top_to_bottom : Animation
    lateinit var bottom_to_top : Animation
    lateinit var left_to_right : Animation
    lateinit var right_to_left : Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //animasi
        animasi()

        val handler = Handler()
        handler.postDelayed({
            val gotogetstarted = Intent(this, LoginActivity::class.java)
            startActivity(gotogetstarted)
            finish()
        }, 10000)
    }

    private fun animasi() {
        top_to_bottom = AnimationUtils.loadAnimation(this,R.anim.top_to_bottom)
        bottom_to_top = AnimationUtils.loadAnimation(this,R.anim.bottom_to_top)
        left_to_right = AnimationUtils.loadAnimation(this,R.anim.left_to_right)
        right_to_left = AnimationUtils.loadAnimation(this,R.anim.right_to_left)

        imgLogo.startAnimation(top_to_bottom)
        txtNamaKejuaraan.startAnimation(bottom_to_top)
        progresBar.startAnimation(bottom_to_top)
    }
}