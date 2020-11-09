package com.mobile.formulirregistrasiatlet.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobile.formulirregistrasiatlet.R
import kotlinx.android.synthetic.main.activity_success.*

class SuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        val bundle = intent.extras
        txtIdUser.text = bundle?.getString("idUser")

        bttnKembali.setOnClickListener {
            val intent = Intent(this,ViewPesertaActivity::class.java)
            intent.putExtra("idUser",txtIdUser.text.toString())
            startActivity(intent)
        }
    }
}