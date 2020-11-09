package com.mobile.formulirregistrasiatlet.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mobile.formulirregistrasiatlet.R
import com.mobile.formulirregistrasiatlet.ViewModel.ViewModel
import com.mobile.formulirregistrasiatlet.model.ResponseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    // deklarasi animasi
    lateinit var top_to_bottom : Animation
    lateinit var bottom_to_top : Animation
    lateinit var left_to_right : Animation
    lateinit var right_to_left : Animation

    //todo 2 deklarasi view model
    lateinit var viewModel: ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //todo 3 inisialisasi view model
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)

        // todo 4 pengamatan
        pengamatan()

        //animasi
        animasi()

        bttnLogin.setOnClickListener {

            //todo 1 ambil view
            var namaKontingen = edtNamaKontingen.text.toString()
            var password = edtPassword.text.toString()

            //todo 5 eksekusi view model
            viewModel.login(namaKontingen, password)

        }
        bttnDaftar.setOnClickListener {
            val intent = Intent(
                this,
                RegisterActivity::class.java
            )
            startActivity(intent)
        }
    }

    private fun animasi() {
        top_to_bottom = AnimationUtils.loadAnimation(this,R.anim.top_to_bottom)
        bottom_to_top = AnimationUtils.loadAnimation(this,R.anim.bottom_to_top)
        left_to_right = AnimationUtils.loadAnimation(this,R.anim.left_to_right)
        right_to_left = AnimationUtils.loadAnimation(this,R.anim.right_to_left)

        txtNamaKontingen.startAnimation(left_to_right)
        edtNamaKontingen.startAnimation(left_to_right)
        txtPassword.startAnimation(left_to_right)
        edtPassword.startAnimation(left_to_right)
    }

    private fun pengamatan() {
        viewModel.responLogin.observe(this, Observer { responLogin(it) })
        viewModel.errorLogin.observe(this, Observer { errorLogin(it) })
        viewModel.showLoading.observe(this, Observer { showLoading(it) })
        viewModel.namaKontingenKosong.observe(this, Observer { namaKontingenKosong(it) })
        viewModel.passwordKosong.observe(this, Observer { passwordKosong(it) })

    }

    private fun passwordKosong(it: Boolean?) {
        edtPassword.requestFocus()
        edtPassword.error = "Password kosong"
    }

    private fun namaKontingenKosong(it: Boolean?) {
        edtNamaKontingen.requestFocus()
        edtNamaKontingen.error = "Nama kontingen kosong"
    }

    private fun showLoading(it: Boolean?) {
        if (it == true) {
            progresBar.visibility = View.VISIBLE
            bttnLogin.text = "Loading..."
            bttnLogin.isEnabled = false
            bttnDaftar.isEnabled = false
        }
        if (it == false) {
            progresBar.visibility = View.GONE
            bttnLogin.text = "LOGIN"
            bttnLogin.isEnabled = true
            bttnDaftar.isEnabled = true
        }
    }

    private fun errorLogin(it: Throwable?) {
        toast(it?.message)
    }

    private fun responLogin(it: ResponseUser?) {
        if (it?.message.equals("Berhasil Login")) {
            toast(it?.message)
            val intent = Intent(this, HomeActivity::class.java)
            var data = it?.result
            for (i: Int in data?.indices ?: 0..1) {
                intent.putExtra("idUser", data?.get(i)?.id_user)
            }
            startActivity(intent)
            finishAffinity()
        } else {
            toast(it?.message)
        }
    }

    fun toast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}