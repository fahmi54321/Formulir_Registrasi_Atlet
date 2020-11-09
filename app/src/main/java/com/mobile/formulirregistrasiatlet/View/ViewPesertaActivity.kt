package com.mobile.formulirregistrasiatlet.View

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mobile.formulirregistrasiatlet.Adapter.PesertaAdapter
import com.mobile.formulirregistrasiatlet.Constants.Constants
import com.mobile.formulirregistrasiatlet.R
import com.mobile.formulirregistrasiatlet.ViewModel.ViewModel
import com.mobile.formulirregistrasiatlet.model.ResponsePeserta
import com.mobile.formulirregistrasiatlet.model.ResultItemPeserta
import kotlinx.android.synthetic.main.activity_view_peserta.*
import kotlinx.android.synthetic.main.activity_view_peserta.txtIdUser
import java.util.*

class ViewPesertaActivity : AppCompatActivity() {

    //todo 1 deklarasi view model
    lateinit var viewModel: ViewModel
    lateinit var viewModelDelete: ViewModel

    // deklarasi animasi
    lateinit var top_to_bottom : Animation
    lateinit var bottom_to_top : Animation
    lateinit var left_to_right : Animation
    lateinit var right_to_left : Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_peserta)

        refresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

            // todo 4 eksekusi view model
            viewModel.getPeserta(txtIdUser.text.toString())

            val handler = Handler()
            handler.postDelayed({
                refresh.isRefreshing = false
            }, 3000)

        })

        //todo 2 inisialisasi view model
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
        viewModelDelete = ViewModelProviders.of(this).get(ViewModel::class.java)

        // todo 3 pengamatan
        pengamatan()

        //animasi
        animasi()

        val bundle = intent.extras
        txtIdUser.text = bundle?.getString("idUser")

        bttnTambah.setOnClickListener {
            val intent = Intent(this, PesertaActivity::class.java)
            intent.putExtra("id", txtIdUser.text.toString())
            startActivity(intent)
        }

        // todo 4 eksekusi view model
        viewModel.getPeserta(txtIdUser.text.toString())
    }

    private fun animasi() {
        top_to_bottom = AnimationUtils.loadAnimation(this,R.anim.top_to_bottom)
        bottom_to_top = AnimationUtils.loadAnimation(this,R.anim.bottom_to_top)
        left_to_right = AnimationUtils.loadAnimation(this,R.anim.left_to_right)
        right_to_left = AnimationUtils.loadAnimation(this,R.anim.right_to_left)

        rvPeserta.startAnimation(left_to_right)
    }

    // todo 3 pengamatan
    private fun pengamatan() {
        viewModel.responGetPeserta.observe(this, Observer { responGetPeserta(it) })
        viewModel.errorGetPeserta.observe(this, Observer { errorGetPeserta(it) })
        viewModelDelete.responDelete.observe(this, Observer { responseDelete(it) })
        viewModelDelete.errorDelete.observe(this, Observer { errorDelete(it) })
    }

    private fun errorDelete(it: Throwable?) {
        toast(it?.localizedMessage)
    }

    private fun responseDelete(it: ResponsePeserta?) {
        toast(it?.message)
        val intent = Intent(this, ViewPesertaActivity::class.java)
        intent.putExtra("idUser", txtIdUser.text.toString())
        startActivity(intent)
    }

    private fun errorGetPeserta(it: Throwable?) {
        toast(it?.message)
    }

    private fun responGetPeserta(it: ResponsePeserta?) {
        if (it?.message.equals("Berhasil")) {
            val adapter = PesertaAdapter(it?.result, object : PesertaAdapter.onClickListener {
                override fun detail(item: ResultItemPeserta?) {
                    toast("Details")
                }

                override fun hapus(item: ResultItemPeserta?) {
                    AlertDialog.Builder(this@ViewPesertaActivity).apply {
                        setTitle("Hapus Data")
                        setMessage("Yakin mau menghapus ?")
                        setPositiveButton("Hapus") { dialog, which ->

                            // todo 4 eksekusi view model
                            viewModelDelete.deletePeserta(item?.id_peserta)
                        }
                        setNegativeButton("Batal") { dialog, which ->
                            dialog.dismiss()
                        }.show()
                    }
                }

                override fun edit(item: ResultItemPeserta?) {
                    AlertDialog.Builder(this@ViewPesertaActivity).apply {
                        setTitle("Konfirmasi")
                        setMessage("Klik Ya untuk melanjutkan update")
                        setPositiveButton("Ya") { dialog, which ->

                            val intent = Intent(applicationContext, PesertaActivity::class.java)
                            intent.putExtra("data",item)
                            startActivity(intent)
                        }
                        setNegativeButton("Batal") { dialog, which ->
                            dialog.dismiss()
                        }.show()
                    }

                }

                override fun view(item: ResultItemPeserta?) {
                    AlertDialog.Builder(this@ViewPesertaActivity).apply {
                        setTitle("Konfirmasi")
                        setMessage("Klik Ya untuk melanjutkan")
                        setPositiveButton("Ya") { dialog, which ->

                            showDialog(item)
                        }
                        setNegativeButton("Batal") { dialog, which ->
                            dialog.dismiss()
                        }.show()
                    }

                }

            })

            rvPeserta.adapter = adapter
        }
    }

    private fun showDialog(item: ResultItemPeserta?) {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_item_details)


        Objects.requireNonNull(dialog.window)
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val namaLengkap = dialog.findViewById<TextView>(R.id.txtNamaLengkap)
        val tempatLahir = dialog.findViewById<TextView>(R.id.txtTempatLahir)
        val tglLahir = dialog.findViewById<TextView>(R.id.txtTglLahir)
        val jk = dialog.findViewById<TextView>(R.id.txtJk)
        val perguruan = dialog.findViewById<TextView>(R.id.txtPerguruan)
        val kategori = dialog.findViewById<TextView>(R.id.txtKategori)
        val kelas = dialog.findViewById<TextView>(R.id.txtKelas)
        val kelas2 = dialog.findViewById<TextView>(R.id.txtKelas2)
        val linkSatu = dialog.findViewById<TextView>(R.id.txtLinkSatu)
        val linkDua = dialog.findViewById<TextView>(R.id.txtLinkDua)
        val linkTiga = dialog.findViewById<TextView>(R.id.txtLinkTiga)
        val linkEmpat = dialog.findViewById<TextView>(R.id.txtLinkEmpat)
        val imgPeserta = dialog.findViewById<ImageView>(R.id.imgPeserta)
        val imgAkte = dialog.findViewById<ImageView>(R.id.imgAkte)
        val imgClose = dialog.findViewById<ImageView>(R.id.imgClose)

        imgClose.setOnClickListener {
            dialog.dismiss()
        }

        namaLengkap.text = item?.nama_lengkap
        tempatLahir.text = item?.tempat_lahir
        tglLahir.text = item?.tgl_lahir
        jk.text = item?.jk
        perguruan.text = item?.perguruan
        kategori.text = item?.kategori
        kelas.text = item?.kelas
        kelas2.text = item?.kelas_dua
        linkSatu.text = item?.link_video_satu
        linkDua.text = item?.link_video_dua
        linkTiga.text = item?.link_video_tiga
        linkEmpat.text = item?.link_video_empat

        var constants: Constants? = null
        constants = Constants()

        Glide.with(dialog.context)
            .load(constants?.URL_Image_Peserta + item?.photo_peserta)
            .apply(RequestOptions().error(R.drawable.icon_nopic))
            .into(imgPeserta)

        Glide.with(dialog.context)
            .load(constants?.URL_Image_Akte + item?.photo_akte)
            .apply(RequestOptions().error(R.drawable.icon_nopic))
            .into(imgAkte)

        dialog.show()
    }

    fun toast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPeserta(txtIdUser.text.toString())
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this@ViewPesertaActivity).apply {
            setTitle("Konfirmasi")
            setMessage("Kembali ke menu Home ?")
            setPositiveButton("Ya") { dialog, which ->
                val intent = Intent(applicationContext,HomeActivity::class.java)
                intent.putExtra("idUser",txtIdUser.text.toString())
                startActivity(intent)
                finishAffinity()
            }
            setNegativeButton("Batal") { dialog, which ->
                dialog.dismiss()
            }.show()
        }
    }
}