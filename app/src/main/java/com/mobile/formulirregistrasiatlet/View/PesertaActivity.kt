package com.mobile.formulirregistrasiatlet.View

import android.Manifest
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mobile.formulirregistrasiatlet.Constants.Constants
import com.mobile.formulirregistrasiatlet.R
import com.mobile.formulirregistrasiatlet.Utils.ImageAttachmentListener
import com.mobile.formulirregistrasiatlet.Utils.ImageUtils
import com.mobile.formulirregistrasiatlet.ViewModel.ViewModel
import com.mobile.formulirregistrasiatlet.model.ResponsePeserta
import com.mobile.formulirregistrasiatlet.model.ResultItemPeserta
import kotlinx.android.synthetic.main.activity_peserta.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PesertaActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    ImageAttachmentListener {

    var kelas = ""

    //foto
    var photoPeserta: File? = null;
    var photoAkte: File? = null;
    var imageUtils: ImageUtils? = null

    // deklarasi animasi
    lateinit var top_to_bottom: Animation
    lateinit var bottom_to_top: Animation
    lateinit var left_to_right: Animation
    lateinit var right_to_left: Animation

    //date
    var datePickerDialog: DatePickerDialog? = null
    var dateFormatter: SimpleDateFormat? = null

    //todo 2 deklarasi view model
    lateinit var viewModel: ViewModel
    lateinit var viewModelEdit: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peserta)

        val bundle = intent.extras
        txtIdUser.text = bundle?.getString("id")

        val bundle1 = intent.extras
        txtIdr.text = bundle1?.getString("id")


        var id = txtIdUser.text.toString()

        //todo 3 inisialisasi view model
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
        viewModelEdit = ViewModelProviders.of(this).get(ViewModel::class.java)

        //animasi
        animasi()

        // todo 4 pengamatan
        pengamatan()

        val getData = intent.getParcelableExtra<ResultItemPeserta>("data")
        if (getData != null) {
            edtNamaLengkap.setText(getData.nama_lengkap)
            edtTempatLahir.setText(getData.tempat_lahir)
            edtDate.setText(getData.tgl_lahir)
            edtPerguruan.setText(getData.perguruan)
            edtLinkVideoSatu.setText(getData.link_video_satu)
            edtLinkVideoDua.setText(getData.link_video_dua)
            edtLinkVideoTiga.setText(getData.link_video_tiga)
            edtLinkVideoEmpat.setText(getData.link_video_empat)
            txtIdUser.setText(getData.id_peserta)
            txtIdr.setText((getData.id_user))
            spinnerKelas2.visibility = View.VISIBLE
            imgHapusKelas.visibility = View.VISIBLE
            imgTambahKelas.visibility = View.GONE

            var constants: Constants? = null
            constants = Constants()
            Glide.with(this)
                .load(constants?.URL_Image_Peserta + getData.photo_peserta)
                .apply(RequestOptions().error(R.drawable.icon_nopic))
                .into(imagePeserta)

            Glide.with(this)
                .load(constants?.URL_Image_Akte + getData.photo_akte)
                .apply(RequestOptions().error(R.drawable.icon_nopic))
                .into(imageKtp)
            bttnSimpan.text = "Update"
        }


        spinnerKategori.setOnItemSelectedListener(this)

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
        // inisialisasi datepicker
        dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)

        bttnPhotoPeserta.setOnClickListener {
            imageUtils?.imagepicker(1)
        }
        bttnPhotoKtp.setOnClickListener {
            imageUtils?.imagepicker(2)
        }

        imageDate.setOnClickListener {
            showTanggal()
        }

        imgTambahKelas.setOnClickListener {
            spinnerKelas2.visibility = View.VISIBLE
            imgTambahKelas.visibility = View.GONE
            imgHapusKelas.visibility = View.VISIBLE
        }
        imgHapusKelas.setOnClickListener {
            spinnerKelas2.visibility = View.GONE
            imgTambahKelas.visibility = View.VISIBLE
            imgHapusKelas.visibility = View.GONE
        }

        bttnSimpan.setOnClickListener {

            //todo 1 ambil view
            if (photoAkte != null && photoPeserta != null) {

                val requestBody = RequestBody.create(MediaType.parse("image/*"), photoPeserta)
                val requestBody1 = RequestBody.create(MediaType.parse("image/*"), photoAkte)

                //id
                val requestBodyIdUser: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    id
                )

                //id
                val requestBodyIdPribadi: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    txtIdUser.text.toString()
                )

                //nama
                val requestBodyNamaLengkap: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    edtNamaLengkap.text.toString()
                )

                //tempat lahir
                val requestBodyTempatLahir: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    edtTempatLahir.text.toString()
                )

                //tgl
                val requestBodyTglLahir: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    edtDate.text.toString()
                )

                //jk
                var jk = ""
                if (rbLaki.isChecked) {
                    jk = "L"
                }
                if (rbPerempuan.isChecked) {
                    jk = "P"
                }
                val requestBodyJk: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    jk
                )

                //perguruan
                val requestBodyPerguruan: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    edtPerguruan.text.toString()
                )

                //kategori
                val requestBodyKategori: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    spinnerKategori.selectedItem.toString()
                )


                //kelas
                val requestBodyKelas: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    spinnerKelas.selectedItem.toString()
                )

                //kelas2
                var requestBodyKelas2: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    ""
                )
                when (spinnerKelas2.visibility) {
                    View.GONE -> {
                        //kelas2
                        requestBodyKelas2 = RequestBody.create(
                            MediaType.parse("text/plain"),
                            ""
                        )
                    }
                    View.VISIBLE -> {
                        //kelas2
                        requestBodyKelas2 = RequestBody.create(
                            MediaType.parse("text/plain"),
                            spinnerKelas2.selectedItem.toString()
                        )
                    }
                }


                //link satu
                val requestBodyLinkSatu: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    edtLinkVideoSatu.text.toString()
                )

                //link dua
                val requestBodyLinkDua: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    edtLinkVideoDua.text.toString()
                )

                //link tiga
                val requestBodyLinkTiga: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    edtLinkVideoTiga.text.toString()
                )

                //link empat
                val requestBodyLinkEmpat: RequestBody = RequestBody.create(
                    MediaType.parse("text/plain"),
                    edtLinkVideoEmpat.text.toString()
                )

                //photo peserta
                val multipartBodyPhotoPeserta =
                    MultipartBody.Part.createFormData(
                        "photo_peserta",
                        photoPeserta?.name,
                        requestBody
                    )

                //photo akte
                val multipartBodyPhotoAkte =
                    MultipartBody.Part.createFormData("photo_akte", photoAkte?.name, requestBody1)



                when (bttnSimpan.text) {
                    "Update" -> {
                        AlertDialog.Builder(this).apply {
                            setTitle("Update Data")
                            setMessage("Yakin mau mengupdate ?")
                            setPositiveButton("Update") { dialog, which ->
                                // todo 5 ekseksi view model
                                viewModelEdit.editPeserta(
                                    requestBodyIdPribadi,
                                    requestBodyNamaLengkap,
                                    requestBodyTempatLahir,
                                    requestBodyTglLahir,
                                    requestBodyJk,
                                    requestBodyPerguruan,
                                    requestBodyKategori,
                                    requestBodyKelas,
                                    requestBodyKelas2,
                                    requestBodyLinkSatu,
                                    requestBodyLinkDua,
                                    requestBodyLinkTiga,
                                    requestBodyLinkEmpat,
                                    multipartBodyPhotoPeserta,
                                    multipartBodyPhotoAkte
                                )
                            }
                            setNegativeButton("Batal") { dialog, which ->
                                dialog.dismiss()
                            }.show()
                        }
                    }
                    else -> {
                        AlertDialog.Builder(this).apply {
                            setTitle("Simpan Data")
                            setMessage("Yakin mau menyimpan ?")
                            setPositiveButton("Simpan") { dialog, which ->
                                // todo 5 ekseksi view model
                                viewModel.tambahPeserta(
                                    requestBodyIdUser,
                                    requestBodyNamaLengkap,
                                    requestBodyTempatLahir,
                                    requestBodyTglLahir,
                                    requestBodyJk,
                                    requestBodyPerguruan,
                                    requestBodyKategori,
                                    requestBodyKelas,
                                    requestBodyKelas2,
                                    requestBodyLinkSatu,
                                    requestBodyLinkDua,
                                    requestBodyLinkTiga,
                                    requestBodyLinkEmpat,
                                    multipartBodyPhotoPeserta,
                                    multipartBodyPhotoAkte
                                )
                            }
                            setNegativeButton("Batal") { dialog, which ->
                                dialog.dismiss()
                            }.show()
                        }

                    }
                }


            } else {
                toast("Upload Photo kembali")
            }

        }

    }

    private fun animasi() {
        top_to_bottom = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom)
        bottom_to_top = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top)
        left_to_right = AnimationUtils.loadAnimation(this, R.anim.left_to_right)
        right_to_left = AnimationUtils.loadAnimation(this, R.anim.right_to_left)

        txtNamaLengkap.startAnimation(left_to_right)
        edtNamaLengkap.startAnimation(left_to_right)
        txtKetNamaLengkap.startAnimation(left_to_right)
        txtTempatLahir.startAnimation(left_to_right)
        edtTempatLahir.startAnimation(left_to_right)
        txtTglLahir.startAnimation(left_to_right)
        edtDate.startAnimation(left_to_right)
        imageDate.startAnimation(left_to_right)
        txtJk.startAnimation(left_to_right)
        rbLaki.startAnimation(left_to_right)
        rbPerempuan.startAnimation(left_to_right)
        txtPerguruan.startAnimation(left_to_right)
        edtPerguruan.startAnimation(left_to_right)
    }

    // todo 4 pengamatan
    private fun pengamatan() {
        viewModel.responTambahPeserta.observe(
            this,
            androidx.lifecycle.Observer { responTambahPeserta(it) })
        viewModel.errorTambahPeserta.observe(
            this,
            androidx.lifecycle.Observer { errorTambahPeserta(it) })
        viewModel.namaLengkapKosong.observe(
            this,
            androidx.lifecycle.Observer { namaLengkapKosong(it) })
        viewModel.tempatLahirKosong.observe(
            this,
            androidx.lifecycle.Observer { tempatLahirKosong(it) })
        viewModel.tglLahirKosong.observe(this, androidx.lifecycle.Observer { tglLahirKosong(it) })
        viewModel.jkKosong.observe(this, androidx.lifecycle.Observer { jkKosong(it) })
        viewModel.perguruanKosong.observe(this, androidx.lifecycle.Observer { perguruanKosong(it) })
        viewModel.showLoading.observe(this, androidx.lifecycle.Observer { showLoading(it) })
        viewModelEdit.responEditPeserta.observe(
            this,
            androidx.lifecycle.Observer { responEditPeserta(it) })
        viewModelEdit.errorEditPeserta.observe(
            this,
            androidx.lifecycle.Observer { errorEditPeserta(it) })

        viewModelEdit.editShowLoading.observe(
            this,
            androidx.lifecycle.Observer { editShowLoading(it) })
        viewModelEdit.namaLengkapKosong.observe(
            this,
            androidx.lifecycle.Observer { namaLengkapKosong(it) })
        viewModelEdit.tempatLahirKosong.observe(
            this,
            androidx.lifecycle.Observer { tempatLahirKosong(it) })
        viewModelEdit.tglLahirKosong.observe(
            this,
            androidx.lifecycle.Observer { tglLahirKosong(it) })
        viewModelEdit.jkKosong.observe(this, androidx.lifecycle.Observer { jkKosong(it) })
        viewModelEdit.perguruanKosong.observe(
            this,
            androidx.lifecycle.Observer { perguruanKosong(it) })
        viewModelEdit.showLoading.observe(this, androidx.lifecycle.Observer { showLoading(it) })

    }


    private fun editShowLoading(it: Boolean?) {
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


    private fun errorEditPeserta(it: Throwable?) {
        val intent = Intent(this, ViewPesertaActivity::class.java)
        intent.putExtra("idUser", txtIdr.text.toString())
        startActivity(intent)
        finishAffinity()
    }

    private fun responEditPeserta(it: ResponsePeserta?) {
        val intent = Intent(this, ViewPesertaActivity::class.java)
        intent.putExtra("idUser", txtIdr.text.toString())
        startActivity(intent)
        finishAffinity()

        if (it?.message.equals("Ukuran Besar")){
            toast("Ukuran File Max 2 Mb")
        }
    }

    private fun showLoading(it: Boolean?) {
        if (it == true) {
            progresBar.visibility = View.VISIBLE
            bttnSimpan.text = "Loading..."
            bttnSimpan.isEnabled = false
        }
        if (it == false) {
            progresBar.visibility = View.GONE
            bttnSimpan.text = "Simpan"
            bttnSimpan.isEnabled = true
        }
    }


    private fun perguruanKosong(it: Boolean?) {
        if (it == true) {
            edtPerguruan.requestFocus()
            edtPerguruan.error = "Perguruan kosong"
        }
    }

    private fun jkKosong(it: Boolean?) {
        if (it == true) {
            rbLaki.error = "Jenis Kelamin kosong"
            toast("Jenis Kelamin Kosong")
        }
    }

    private fun tglLahirKosong(it: Boolean?) {
        if (it == true) {
            edtDate.requestFocus()
            edtDate.error = "Tgl Lahir kosong"
        }
    }

    private fun tempatLahirKosong(it: Boolean?) {
        if (it == true) {
            edtTempatLahir.requestFocus()
            edtTempatLahir.error = "Tempat lahir kosong"
        }
    }

    private fun namaLengkapKosong(it: Boolean?) {
        if (it == true) {
            edtNamaLengkap.requestFocus()
            edtNamaLengkap.error = "Nama lengkap kosong"
        }
    }

    private fun errorTambahPeserta(it: Throwable?) {
        toast(it?.localizedMessage)
    }

    private fun responTambahPeserta(it: ResponsePeserta?) {
        if (it?.message.equals("Berhasil Menyimpan")) {
            toast(it?.message)
            val intent = Intent(this, ViewPesertaActivity::class.java)
            intent.putExtra("idUser", txtIdUser.text.toString())
            startActivity(intent)
            finishAffinity()
        } else {
            toast(it?.message)
        }
        if (it?.message.equals("Ukuran Besar")){
            toast("Ukuran File Max 2 Mb")
        }
    }

    private fun showTanggal() {
        val newCalendar = Calendar.getInstance()
        datePickerDialog = DatePickerDialog(
            this,
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val newDate = Calendar.getInstance()
                newDate[year, monthOfYear] = dayOfMonth
                edtDate.setText(dateFormatter!!.format(newDate.time))
            },
            newCalendar[Calendar.YEAR],
            newCalendar[Calendar.MONTH],
            newCalendar[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog!!.show()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        kelas = spinnerKategori.selectedItem.toString()
        if (kelas.equals("Pra Dini")) {
            val praDini =
                resources.getStringArray(R.array.pra_dini)

            //todo 7
            val arrayPraDini: ArrayAdapter<*> =
                ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, praDini)
            arrayPraDini.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerKelas.adapter = arrayPraDini
            spinnerKelas2.adapter = arrayPraDini
        } else if (kelas.equals("Dini")) {
            val Dini =
                resources.getStringArray(R.array.dini)

            //todo 7
            val arrayDini: ArrayAdapter<*> =
                ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, Dini)
            arrayDini.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerKelas.adapter = arrayDini
            spinnerKelas2.adapter = arrayDini
        } else if (kelas.equals("Pra Pemula")) {
            val praPemula =
                resources.getStringArray(R.array.pra_pemula)

            //todo 7
            val arrayPraPemula: ArrayAdapter<*> =
                ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, praPemula)
            arrayPraPemula.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerKelas.adapter = arrayPraPemula
            spinnerKelas2.adapter = arrayPraPemula
        } else if (kelas.equals("Pemula")) {
            val Pemula =
                resources.getStringArray(R.array.pemula)

            //todo 7
            val arrayPemula: ArrayAdapter<*> =
                ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, Pemula)
            arrayPemula.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerKelas.adapter = arrayPemula
            spinnerKelas2.adapter = arrayPemula
        } else if (kelas.equals("Kadet")) {
            val Kadet =
                resources.getStringArray(R.array.kadet)

            //todo 7
            val arrayKadet: ArrayAdapter<*> =
                ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, Kadet)
            arrayKadet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerKelas.adapter = arrayKadet
            spinnerKelas2.adapter = arrayKadet
        } else if (kelas.equals("Junior")) {
            val Junior =
                resources.getStringArray(R.array.junior)

            //todo 7
            val arrayJunior: ArrayAdapter<*> =
                ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, Junior)
            arrayJunior.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerKelas.adapter = arrayJunior
            spinnerKelas2.adapter = arrayJunior
        } else if (kelas.equals("Senior")) {
            val Senior =
                resources.getStringArray(R.array.senior)

            //todo 7
            val arraySenior: ArrayAdapter<*> =
                ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, Senior)
            arraySenior.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerKelas.adapter = arraySenior
            spinnerKelas2.adapter = arraySenior
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
        if (from == 1) {
            imagePeserta.setImageBitmap(file)

            val path: String? = imageUtils?.getPath(uri)
            photoPeserta = File(path)
        }
        if (from == 2) {
            imageKtp.setImageBitmap(file)

            val path: String? = imageUtils?.getPath(uri)
            photoAkte = File(path)
        }
    }

    fun toast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}