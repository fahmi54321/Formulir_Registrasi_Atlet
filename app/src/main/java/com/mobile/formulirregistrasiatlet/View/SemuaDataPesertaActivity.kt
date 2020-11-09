package com.mobile.formulirregistrasiatlet.View

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mobile.formulirregistrasiatlet.Adapter.SemuaPesertaAdapter
import com.mobile.formulirregistrasiatlet.Constants.Constants
import com.mobile.formulirregistrasiatlet.R
import com.mobile.formulirregistrasiatlet.ViewModel.ViewModel
import com.mobile.formulirregistrasiatlet.model.ResponsePeserta
import com.mobile.formulirregistrasiatlet.model.ResultItemPeserta
import kotlinx.android.synthetic.main.activity_semua_data_peserta.*
import kotlinx.android.synthetic.main.activity_semua_data_peserta.refresh
import java.util.*

class SemuaDataPesertaActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    //todo 1 deklarasi view model
    lateinit var viewModel: ViewModel
    lateinit var viewModelSearching: ViewModel
    lateinit var viewModelBanyak: ViewModel

    var cari2=""
    var hasilCari=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_semua_data_peserta)

        refresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

            // todo 4 eksekusi view model
            viewModel.selectSemuaPeserta()

            // todo 4 eksekusi view model banyak
            viewModelBanyak.banyakPeserta()

            val handler = Handler()
            handler.postDelayed({
                refresh.isRefreshing = false
            }, 8000)

        })

        spinnerCari.setOnItemSelectedListener(this)

        //todo 2 inisialisasi view model
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
        viewModelSearching = ViewModelProviders.of(this).get(ViewModel::class.java)
        viewModelBanyak = ViewModelProviders.of(this).get(ViewModel::class.java)

        // todo 3 pengamatan
        pengamatan()

        // todo 4 eksekusi view model
        viewModel.selectSemuaPeserta()

        // todo 4 eksekusi view model banyak
        viewModelBanyak.banyakPeserta()

        bttnCari.setOnClickListener {
            when(spinnerCari2.visibility){
                View.VISIBLE->{
                    hasilCari = spinnerCari2.selectedItem.toString()
                }
            }
            when(edtCari.visibility){
                View.VISIBLE->{
                    hasilCari = edtCari.text.toString()
                }
            }
            // todo 4 eksekusi view model edit
            viewModelSearching.searchingPeserta(hasilCari)


        }
    }

    private fun pengamatan() {
        viewModel.responSelectSemuaPeserta.observe(this, Observer { responseBerhasil(it) })
        viewModel.errorSelectSemuaPeserta.observe(this, Observer { responseGagal(it) })
        viewModel.showLoading.observe(this, Observer { showLoading(it) })
        viewModelSearching.responSearchingPeserta.observe(this, Observer { responSearchingPeserta(it) })
        viewModelSearching.errorSearchingPeserta.observe(this, Observer { errorSearchingPeserta(it) })
        viewModelSearching.showLoading.observe(this, Observer { showLoading(it) })
        viewModelBanyak.responBanyakPeserta.observe(this, Observer { responBanyakPeserta(it) })
        viewModelBanyak.errorBanyakPeserta.observe(this, Observer { errorBanyakPeserta(it) })
        viewModelBanyak.showLoading.observe(this, Observer { showLoading(it) })
    }

    private fun errorBanyakPeserta(it: Throwable?) {
        Toast.makeText(this, it?.message, Toast.LENGTH_LONG).show()
    }

    private fun responBanyakPeserta(it: ResponsePeserta?) {
        Toast.makeText(this, it?.message, Toast.LENGTH_LONG).show()
        banyakPeserta.text = it?.total
    }

    private fun errorSearchingPeserta(it: Throwable?) {
        Toast.makeText(this, it?.message, Toast.LENGTH_SHORT).show()
    }

    private fun responSearchingPeserta(it: ResponsePeserta?) {
        if (it?.message.equals("Berhasil")){
            Toast.makeText(this, it?.message, Toast.LENGTH_SHORT).show()
            val adapter = SemuaPesertaAdapter(it?.result,object :SemuaPesertaAdapter.onClickListener{
                override fun view(item: ResultItemPeserta?) {
                    AlertDialog.Builder(this@SemuaDataPesertaActivity).apply {
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

            rvDataPeserta.adapter = adapter
        }
        if (it?.message.equals("Data Kosong")){
            Toast.makeText(this, it?.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(it: Boolean?) {
        if (it==true){
            progresBar.visibility = View.VISIBLE
        }
        if (it==false){
            progresBar.visibility = View.GONE
        }
    }

    private fun responseGagal(it: Throwable?) {
        Toast.makeText(this, it?.localizedMessage, Toast.LENGTH_SHORT).show()
    }

    private fun responseBerhasil(it: ResponsePeserta?) {
        if (it?.message.equals("Berhasil")){
            val adapter = SemuaPesertaAdapter(it?.result,object :SemuaPesertaAdapter.onClickListener{
                override fun view(item: ResultItemPeserta?) {
                    AlertDialog.Builder(this@SemuaDataPesertaActivity).apply {
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

            rvDataPeserta.adapter = adapter
        }
        if (it?.message.equals("Data Kosong")){
            Toast.makeText(this, it?.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDialog(item: ResultItemPeserta?) {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_semua_item_details)


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

        dialog.show()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        cari2 = spinnerCari.selectedItem.toString()
        if (cari2.equals("Nama")) {
            edtCari.visibility = View.VISIBLE
            spinnerCari2.visibility = View.GONE
        } else if (cari2.equals("Perguruan")) {
            edtCari.visibility = View.VISIBLE
            spinnerCari2.visibility = View.GONE
        }else if (cari2.equals("Kategori")) {
            edtCari.visibility = View.GONE
            spinnerCari2.visibility = View.VISIBLE
            val Kategori =
                resources.getStringArray(R.array.kategori)

            //todo 7
            val arrayKategori: ArrayAdapter<*> =
                ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, Kategori)
            arrayKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCari2.adapter = arrayKategori
        } else if (cari2.equals("Kelas")) {
            edtCari.visibility = View.GONE
            spinnerCari2.visibility = View.VISIBLE
            val Kelas =
                resources.getStringArray(R.array.kelas)

            //todo 7
            val arrayKelas: ArrayAdapter<*> =
                ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, Kelas)
            arrayKelas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCari2.adapter = arrayKelas
        }
    }
}