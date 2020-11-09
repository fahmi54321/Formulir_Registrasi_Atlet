package com.mobile.formulirregistrasiatlet.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mobile.formulirregistrasiatlet.R
import com.mobile.formulirregistrasiatlet.ViewModel.ViewModel
import com.mobile.formulirregistrasiatlet.model.ResponsePembayaran
import com.mobile.formulirregistrasiatlet.model.ResponsePeserta
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.txtIdUser

class HomeActivity : AppCompatActivity() {

    //todo 1 deklarasi view model
    lateinit var viewModel: ViewModel
    lateinit var viewModelBanyakPeserta: ViewModel
    lateinit var viewModelSemuaBanyakPeserta: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bundle = intent.extras
        txtIdUser.text = bundle?.getString("idUser")

        //todo 2 inisialisasi view model
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
        viewModelBanyakPeserta = ViewModelProviders.of(this).get(ViewModel::class.java)
        viewModelSemuaBanyakPeserta = ViewModelProviders.of(this).get(ViewModel::class.java)

        // todo 3 pengamatan
        pengamatan()

        refresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

            // todo 4 eksekusi view model
            viewModel.selectPembayaran(txtIdUser.text.toString())
            // todo 4 eksekusi view model banyak peserta
            viewModelBanyakPeserta.banyakPesertaKontingen(txtIdUser.text.toString())
            // todo 4 eksekusi view model banyak peserta
            viewModelSemuaBanyakPeserta.banyakPeserta()

            val handler = Handler()
            handler.postDelayed({
                refresh.isRefreshing = false
            }, 8000)

        })


        lnTambah.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Konfirmasi")
                setMessage("Klik ya untuk melanjutkan ?")
                setPositiveButton("Ya") { dialog, which ->

                    val intent = Intent(applicationContext, ViewPesertaActivity::class.java)
                    intent.putExtra("idUser", txtIdUser.text.toString())
                    startActivity(intent)
                }
                setNegativeButton("Batal") { dialog, which ->
                    dialog.dismiss()
                }.show()
            }

        }

        lnTampil.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Konfirmasi")
                setMessage("Klik ya untuk melanjutkan ?")
                setPositiveButton("Ya") { dialog, which ->

                    val intent = Intent(applicationContext, SemuaDataPesertaActivity::class.java)
                    startActivity(intent)
                }
                setNegativeButton("Batal") { dialog, which ->
                    dialog.dismiss()
                }.show()
            }

        }

        // todo 4 eksekusi view model
        viewModel.selectPembayaran(txtIdUser.text.toString())
        // todo 4 eksekusi view model banyak peserta
        viewModelBanyakPeserta.banyakPesertaKontingen(txtIdUser.text.toString())

        // todo 4 eksekusi view model banyak peserta
        viewModelSemuaBanyakPeserta.banyakPeserta()

        lnPembayaran.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Konfirmasi")
                setMessage("Klik ya untuk melanjutkan ?")
                setPositiveButton("Ya") { dialog, which ->

                    val intent = Intent(applicationContext, VerifikasiFinalPembayaranActivity::class.java)
                    intent.putExtra("idUser", txtIdUser.text.toString())
                    startActivity(intent)
                }
                setNegativeButton("Batal") { dialog, which ->
                    dialog.dismiss()
                }.show()
            }

        }

        lnKeluar.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Konfirmasi")
                setMessage("Klik ya untuk keluar ?")
                setPositiveButton("Ya") { dialog, which ->

                    finishAffinity()
                }
                setNegativeButton("Batal") { dialog, which ->
                    dialog.dismiss()
                }.show()
            }
        }
    }

    private fun pengamatan() {
        viewModel.responGetPembayaran.observe(this, Observer { responGetPembayaran(it) })
        viewModel.errorGetPembayaran.observe(this, Observer { errorGetPembayaran(it) })
        viewModel.showLoading.observe(this, Observer { showLoading(it) })
        viewModelBanyakPeserta.responBanyakPesertaKontingen.observe(this, Observer { responBanyakPesertaKontingen(it) })
        viewModelBanyakPeserta.errorBanyakPesertaKontingen.observe(this, Observer { errorBanyakPesertaKontingen(it) })
        viewModelBanyakPeserta.showLoading.observe(this, Observer { showLoading(it) })

        viewModelSemuaBanyakPeserta.responBanyakPeserta.observe(this, Observer { responSelectSemuaPeserta(it) })
        viewModelSemuaBanyakPeserta.errorBanyakPeserta.observe(this, Observer { errorSelectSemuaPeserta(it) })
        viewModelSemuaBanyakPeserta.showLoading.observe(this, Observer { showLoading(it) })
    }

    private fun errorSelectSemuaPeserta(it: Throwable?) {
        toast(it?.message)
    }

    private fun responSelectSemuaPeserta(it: ResponsePeserta?) {
        txtTotalSemuaPeserta.text = it?.total
    }

    private fun errorBanyakPesertaKontingen(it: Throwable?) {
        toast(it?.message)
    }

    private fun responBanyakPesertaKontingen(it: ResponsePeserta?) {
        txtTotalPeserta.text = it?.total
    }

    private fun showLoading(it: Boolean?) {
        if (it==true){
            progresBarHome.visibility = View.VISIBLE
        }
        if (it == false){
            progresBarHome.visibility = View.GONE
        }
    }

    private fun errorGetPembayaran(it: Throwable?) {
        toast(it?.message)
    }

    private fun responGetPembayaran(it: ResponsePembayaran?) {
        if (it?.message.equals("Berhasil")){
            var data = it?.result
            for (i: Int in data?.indices ?: 0..1) {
                var id = data?.get(i)?.verifikasi_id
                if (id=="0"){
                    iconPending.visibility = View.GONE
                    iconCheck.visibility = View.GONE
                    iconTolak.visibility = View.VISIBLE
                }
                else if (id=="1"){
                    iconCheck.visibility = View.VISIBLE
                    iconPending.visibility = View.GONE
                    iconTolak.visibility = View.GONE
                }
                else if (id=="2"){
                    iconTolak.visibility = View.GONE
                    iconPending.visibility = View.VISIBLE
                    iconCheck.visibility = View.GONE
                }
            }
        }else{
            toast(it?.message)
        }
    }

    fun toast(message: String?){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle("Konfirmasi")
            setMessage("Klik ya untuk keluar ?")
            setPositiveButton("Ya") { dialog, which ->

                finishAffinity()
            }
            setNegativeButton("Batal") { dialog, which ->
                dialog.dismiss()
            }.show()
        }
    }
}