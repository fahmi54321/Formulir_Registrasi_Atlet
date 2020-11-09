package com.mobile.formulirregistrasiatlet.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mobile.formulirregistrasiatlet.Constants.Constants
import com.mobile.formulirregistrasiatlet.R
import com.mobile.formulirregistrasiatlet.model.ResultItemPeserta
import kotlinx.android.synthetic.main.item_peserta.view.*

class PesertaAdapter(
    val data: List<ResultItemPeserta?>?,
    val itemClick: onClickListener
) : RecyclerView.Adapter<PesertaAdapter.MainViewHolder>() {
    class MainViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val idPeserta = itemView.txtIdPribadi
        val namaLengkap = itemView.txtNamaLengkap
        val perguruan = itemView.txtPerguruan
        val kategori = itemView.txtKategori
        val kelas = itemView.txtKelas
        val btnHapus = itemView.btnHapus
        val btnEdit = itemView.btnEdit
        val btnView = itemView.btnView
        val imgProfilePeserta = itemView.imgProfilePeserta
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_peserta, parent, false)

        return MainViewHolder(view)
    }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = data?.get(position)
        holder.namaLengkap.text = item?.nama_lengkap
        holder.perguruan.text = item?.perguruan
        holder.kategori.text = item?.kategori
        holder.kelas.text = item?.kelas
        holder.idPeserta.text = item?.id_peserta
        var constants: Constants? = null
        constants = Constants()
        Glide.with(holder.itemView.context)
            .load(constants?.URL_Image_Peserta + data?.get(position)?.photo_peserta)
            .apply(RequestOptions().error(R.drawable.icon_nopic))
            .into(holder.imgProfilePeserta)

        holder.view.setOnClickListener {
            itemClick.detail(item)
        }
        holder.btnHapus.setOnClickListener {
            itemClick.hapus(item)
        }
        holder.btnEdit.setOnClickListener {
            itemClick.edit(item)
        }
        holder.btnView.setOnClickListener {
            itemClick.view(item)
        }
    }

    interface onClickListener {
        fun detail(item: ResultItemPeserta?)
        fun hapus(item: ResultItemPeserta?)
        fun edit(item: ResultItemPeserta?)
        fun view(item: ResultItemPeserta?)
    }
}