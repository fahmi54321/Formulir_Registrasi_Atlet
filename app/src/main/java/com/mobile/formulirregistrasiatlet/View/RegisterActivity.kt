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
import com.mobile.formulirregistrasiatlet.model.ResponsePeserta
import com.mobile.formulirregistrasiatlet.model.ResponseUser
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.txtIdUser
import kotlinx.android.synthetic.main.activity_view_peserta.*

class RegisterActivity : AppCompatActivity() {

    //todo 2 deklarasi view model
    lateinit var viewModel: ViewModel

    // deklarasi animasi
    lateinit var top_to_bottom : Animation
    lateinit var bottom_to_top : Animation
    lateinit var left_to_right : Animation
    lateinit var right_to_left : Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val bundle = intent.extras
        txtIdUser.text = bundle?.getString("id")

        //todo 3 inisialisasi view model
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)

        // todo 4 pengamatan
        pengamatan()

        //animasi
        animasi()

        bttnDaftar.setOnClickListener {

            //todo 1 ambil view
            var namaKontingen = edtNamaKontingen.text.toString()
            var namaPelatih = edtNamaPelatih.text.toString()
            var noHp = edtNoHp.text.toString()
            var emailAddress = edtEmailAddress.text.toString()
            var password = edtPassword.text.toString()

            // todo 5 eksekusi view model
            viewModel.addUser(namaKontingen, namaPelatih, noHp, emailAddress, password)
        }
    }

    private fun animasi() {
        top_to_bottom = AnimationUtils.loadAnimation(this,R.anim.top_to_bottom)
        bottom_to_top = AnimationUtils.loadAnimation(this,R.anim.bottom_to_top)
        left_to_right = AnimationUtils.loadAnimation(this,R.anim.left_to_right)
        right_to_left = AnimationUtils.loadAnimation(this,R.anim.right_to_left)

        txtNamaKontingen.startAnimation(left_to_right)
        edtNamaKontingen.startAnimation(left_to_right)
        txtNamaPelatih.startAnimation(left_to_right)
        edtNamaPelatih.startAnimation(left_to_right)
        txtNoHp.startAnimation(left_to_right)
        edtNoHp.startAnimation(left_to_right)
        txtEmailAddress.startAnimation(left_to_right)
        edtEmailAddress.startAnimation(left_to_right)
        txtPassword.startAnimation(left_to_right)
        edtPassword.startAnimation(left_to_right)
    }

    // todo 4 pengamatan
    private fun pengamatan() {
        viewModel.responUser.observe(this, Observer { responseDaftar(it) })
        viewModel.errorUser.observe(this, Observer { errorDaftar(it) })
        viewModel.showLoading.observe(this, Observer { showLoading(it) })
        viewModel.namaKontingenKosong.observe(this, Observer { namaKontingenKosong(it) })
        viewModel.namaPelatihKosong.observe(this, Observer { namaPelatihKosong(it) })
        viewModel.noHpKosong.observe(this, Observer { noHpKosong(it) })
        viewModel.emailKosong.observe(this, Observer { emailKosong(it) })
        viewModel.passwordKosong.observe(this, Observer { passwordKosong(it) })
        viewModel.banyakPassword.observe(this, Observer { banyakPassword(it) })
    }

    private fun banyakPassword(it: Boolean?) {
        if (it == true) {
            edtPassword.requestFocus()
            edtPassword.error = "Banyak password harus lebih dari 6"
        }
    }

    private fun passwordKosong(it: Boolean?) {
        edtPassword.requestFocus()
        edtPassword.error = "Password kosong"
    }

    private fun emailKosong(it: Boolean?) {
        edtEmailAddress.requestFocus()
        edtEmailAddress.error = "Email address kosong"
    }

    private fun noHpKosong(it: Boolean?) {
        edtNoHp.requestFocus()
        edtNoHp.error = "No hp kosong"
    }

    private fun namaPelatihKosong(it: Boolean?) {
        edtNamaPelatih.requestFocus()
        edtNamaPelatih.error = "Nama pelatih kosong"
    }

    private fun namaKontingenKosong(it: Boolean?) {
        edtNamaKontingen.requestFocus()
        edtNamaKontingen.error = "Nama kontingen kosong"
    }

    private fun showLoading(it: Boolean?) {
        if (it == true) {
            progresBar.visibility = View.VISIBLE
            bttnDaftar.text = "Loading"
            bttnDaftar.isEnabled = false
        }
        if (it == false) {
            progresBar.visibility = View.GONE
            bttnDaftar.text = "Daftar"
            bttnDaftar.isEnabled = true
        }
    }

    private fun errorDaftar(it: Throwable?) {
        toast(it?.localizedMessage)
    }

    private fun responseDaftar(it: ResponseUser?) {
        if (it?.message.equals("Berhasil Menyimpan")) {
            toast(it?.message)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        if (it?.message.equals("Nama Kontingen atau Email address sudah ada")) {
            toast(it?.message)
        }
    }

    fun toast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}