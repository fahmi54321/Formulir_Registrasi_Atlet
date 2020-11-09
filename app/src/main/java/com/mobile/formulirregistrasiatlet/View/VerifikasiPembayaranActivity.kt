package com.mobile.formulirregistrasiatlet.View

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mobile.formulirregistrasiatlet.R
import com.mobile.formulirregistrasiatlet.Utils.ImageAttachmentListener
import com.mobile.formulirregistrasiatlet.Utils.ImageUtils
import com.mobile.formulirregistrasiatlet.ViewModel.ViewModel
import com.mobile.formulirregistrasiatlet.model.ResponsePembayaran
import kotlinx.android.synthetic.main.activity_verifikasi_pembayaran.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class VerifikasiPembayaranActivity : AppCompatActivity(), ImageAttachmentListener {

    //foto
    var photoPembayaran: File? = null;
    var imageUtils: ImageUtils? = null

    //todo 2 deklarasi view model
    lateinit var viewModel: ViewModel
    lateinit var viewModelEdit: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifikasi_pembayaran)

        val bundle = intent.extras
        txtIdUserPembayaran.text = bundle?.getString("idUser")
        var id = txtIdUserPembayaran.text.toString()

        val getEdit = intent.extras
        var data = getEdit?.getString("edit")
        if (data != null) {
            bttnSimpan.text = "Update"
            txtIdUserPembayaran.text = id
        }

        imageUtils = ImageUtils(this)

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    0
                )
            }
        }

        bttnPhotoPembayaran.setOnClickListener {
            imageUtils?.imagepicker(1)
        }

        //todo 3 inisialisasi view model
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
        viewModelEdit = ViewModelProviders.of(this).get(ViewModel::class.java)

        // todo 4 pengamatan
        pengamatan()

        bttnSimpan.setOnClickListener {

            //todo ambil view

            if (photoPembayaran != null) {
                val requestBody = RequestBody.create(MediaType.parse("image/*"), photoPembayaran)
                //id
                val requestBodyIdUser: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    txtIdUserPembayaran.text.toString()
                )

                //photo pembayaran
                val multipartBodyPhotoPembayaran =
                    MultipartBody.Part.createFormData("photo", photoPembayaran?.name, requestBody)


                when (bttnSimpan.text) {
                    "Update" -> {
                        // todo 5 eksekusi view model
                        viewModel.editPembayaran(requestBodyIdUser, multipartBodyPhotoPembayaran)
                    }
                    else -> {
                        // todo 5 eksekusi view model
                        viewModel.tambahPembayaran(requestBodyIdUser, multipartBodyPhotoPembayaran)
                    }
                }
            } else {
                toast("Photo kosong")
            }
        }
    }

    private fun pengamatan() {
        viewModel.responPembayaran.observe(this, Observer { responPembayaran(it) })
        viewModel.errorPembayaran.observe(this, Observer { errorPembayaran(it) })
        viewModel.showLoading.observe(this, Observer { showLoading(it) })

        viewModelEdit.responEditPembayaran.observe(this, Observer { responEditPembayaran(it) })
        viewModelEdit.errorEditPembayaran.observe(this, Observer { errorEditPembayaran(it) })
        viewModelEdit.editShowLoadingPembayaran.observe(
            this,
            Observer { editShowLoadingPembayaran(it) })
    }

    private fun editShowLoadingPembayaran(it: Boolean?) {
        if (it == true) {
            progresBar.visibility = View.VISIBLE
            bttnSimpan.text = "Loading..."
            bttnSimpan.isEnabled = false
        }
        if (it == false) {
            progresBar.visibility = View.GONE
            bttnSimpan.text = "Update"
            bttnSimpan.isEnabled = true
        }
    }

    private fun errorEditPembayaran(it: Throwable?) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("idUser", txtIdUserPembayaran.text.toString())
        startActivity(intent)
    }

    private fun responEditPembayaran(it: ResponsePembayaran?) {
        if (it?.message.equals("Data Belum Ada")) {
            toast(it?.message)
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("idUser", txtIdUserPembayaran.text.toString())
            startActivity(intent)
        }
        if (it?.message.equals("Tidak Bisa Edit")) {
            toast(it?.message)
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("idUser", txtIdUserPembayaran.text.toString())
            startActivity(intent)
        }
        if (it?.message.equals("Berhasil Mengubah")) {
            toast(it?.message)
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("idUser", txtIdUserPembayaran.text.toString())
            startActivity(intent)
        }
        if (it?.message.equals("Tidak Mengubah")) {
            toast(it?.message)
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("idUser", txtIdUserPembayaran.text.toString())
            startActivity(intent)
        }
        toast(it?.message)
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("idUser", txtIdUserPembayaran.text.toString())
        startActivity(intent)
    }

    private fun showLoading(it: Boolean?) {
        if (it == true) {
            progresBar.visibility = View.VISIBLE
            bttnSimpan.text = "Loading..."
            bttnSimpan.isEnabled = false
        }
        if (it == false) {
            progresBar.visibility = View.GONE
            bttnSimpan.text = "Lanjut Verifikasi"
            bttnSimpan.isEnabled = true
        }
    }

    private fun errorPembayaran(it: Throwable?) {
        toast(it?.message)
    }

    private fun responPembayaran(it: ResponsePembayaran?) {
        if (it?.message.equals("Ukuran Besar")){
            toast("Ukuran Max 2 Mb")
        }
        else if (it?.message.equals("Berhasil Menyimpan")) {
            toast(it?.message)
            val intent = Intent(this, VerifikasiFinalPembayaranActivity::class.java)
            intent.putExtra("idUser", txtIdUserPembayaran.text.toString())
            startActivity(intent)
        } else {
            toast(it?.message)
            AlertDialog.Builder(this).apply {
                setTitle(it?.message)
                setMessage("Silahkan Klik ya untuk finalisasi ?")
                setPositiveButton("Ya") { dialog, which ->
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    intent.putExtra("idUser", txtIdUserPembayaran.text.toString())
                    startActivity(intent)

                }
                setNegativeButton("Batal") { dialog, which ->
                    dialog.dismiss()
                }.show()
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        imageUtils?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        imageUtils?.request_permission_result(requestCode, permissions, grantResults)
    }

    override fun image_attachment(from: Int, filename: String?, file: Bitmap?, uri: Uri?) {
        imagePembayaran.setImageBitmap(file)

        val path: String? = imageUtils?.getPath(uri)
        photoPembayaran = File(path)
    }

    fun toast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}