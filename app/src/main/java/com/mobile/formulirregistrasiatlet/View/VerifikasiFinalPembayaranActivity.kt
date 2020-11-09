package com.mobile.formulirregistrasiatlet.View

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mobile.formulirregistrasiatlet.Constants.Constants
import com.mobile.formulirregistrasiatlet.R
import com.mobile.formulirregistrasiatlet.Utils.ImageAttachmentListener
import com.mobile.formulirregistrasiatlet.Utils.ImageUtils
import com.mobile.formulirregistrasiatlet.ViewModel.ViewModel
import com.mobile.formulirregistrasiatlet.model.ResponsePembayaran
import kotlinx.android.synthetic.main.activity_verifikasi_final_pembayaran.*
import kotlinx.android.synthetic.main.activity_verifikasi_final_pembayaran.bttnTambah
import kotlinx.android.synthetic.main.activity_verifikasi_final_pembayaran.imagePembayaran
import kotlinx.android.synthetic.main.activity_verifikasi_final_pembayaran.txtIdUserPembayaran
import java.io.File

class VerifikasiFinalPembayaranActivity : AppCompatActivity(), ImageAttachmentListener {

    //todo 1 deklarasi view model
    lateinit var viewModel: ViewModel

    var photoPembayaran: File? = null
    var imageUtils: ImageUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifikasi_final_pembayaran)

        val bundle = intent.extras
        txtIdUserPembayaran.text = bundle?.getString("idUser")

        //todo 2 inisialisasi view model
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)

        // todo 3 pengamatan
        pengamatan()

        // todo 4 eksekusi
        viewModel.selectPembayaran(txtIdUserPembayaran.text.toString())

        bttnTambah.setOnClickListener {
            val intent = Intent(this,VerifikasiPembayaranActivity::class.java)
            intent.putExtra("idUser",txtIdUserPembayaran.text.toString())
            startActivity(intent)
        }
        imageUtils = ImageUtils(this)

        bttnFinish.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Konfirmasi")
                setMessage("Silahkan Ditunggu sampai proses verifikasi selesai")
                setPositiveButton("Ya") { dialog, which ->
                    val intent = Intent(applicationContext,HomeActivity::class.java)
                    intent.putExtra("idUser",txtIdUserPembayaran.text.toString())
                    startActivity(intent)
                }
                setNegativeButton("Batal") { dialog, which ->
                    dialog.dismiss()
                }.show()
            }

        }

        bttnEdit.setOnClickListener {
            val intent = Intent(this,VerifikasiPembayaranActivity::class.java)
            intent.putExtra("idUser",txtIdUserPembayaran.text.toString())
            intent.putExtra("edit","Edit")
            startActivity(intent)
        }

//        refresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
//
//            // todo 4 eksekusi
//            viewModel.selectPembayaran(txtIdUserPembayaran.text.toString())
//
//            val handler = Handler()
//            handler.postDelayed({
//                refresh.isRefreshing = false
//            }, 8000)
//
//        })
    }

    private fun pengamatan() {
        viewModel.responGetPembayaran.observe(this, Observer { responGetPembayaran(it) })
        viewModel.errorGetPembayaran.observe(this, Observer { errorGetPembayaran(it) })
        viewModel.showLoading.observe(this, Observer { showLoading(it) })
    }

    private fun showLoading(it: Boolean?) {

    }

    private fun errorGetPembayaran(it: Throwable?) {
        toast(it?.message)
    }

    private fun responGetPembayaran(it: ResponsePembayaran?) {
        if (it?.message.equals("Berhasil")){
            var data = it?.result
            for (i: Int in data?.indices ?: 0..1) {
                var constants: Constants? = null
                constants = Constants()
                Glide.with(this)
                    .load(constants?.URL_Image_Pembayaran + data?.get(i)?.photo)
                    .apply(RequestOptions().error(R.drawable.icon_nopic))
                    .into(imagePembayaran)

                var id = data?.get(i)?.verifikasi_id
                if (id=="1" || id=="2"){
                    bttnEdit.visibility = View.GONE
                }
                if (id=="0"){
                    bttnEdit.visibility = View.VISIBLE
                }
            }
        }
    }

    fun toast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun image_attachment(from: Int, filename: String?, file: Bitmap?, uri: Uri?) {
        imagePembayaran.setImageBitmap(file)

        val path: String? = imageUtils?.getPath(uri)
        photoPembayaran = File(path)
    }
}