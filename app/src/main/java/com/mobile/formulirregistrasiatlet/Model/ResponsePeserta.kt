package com.mobile.formulirregistrasiatlet.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ResponsePeserta(

    @field:SerializedName("result")
    val result: List<ResultItemPeserta?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("total")
    val total: String? = null
)

@Parcelize
data class ResultItemPeserta(

    @field:SerializedName("nama_lengkap")
    val nama_lengkap: String? = null,

    @field:SerializedName("id_peserta")
    val id_peserta: String? = null,

    @field:SerializedName("id_user")
    val id_user: String? = null,

    @field:SerializedName("tempat_lahir")
    val tempat_lahir: String? = null,

    @field:SerializedName("tgl_lahir")
    val tgl_lahir: String? = null,

    @field:SerializedName("jk")
    val jk: String? = null,

    @field:SerializedName("perguruan")
    val perguruan: String? = null,

    @field:SerializedName("kategori")
    val kategori: String? = null,

    @field:SerializedName("kelas")
    val kelas: String? = null,

    @field:SerializedName("kelas_dua")
    val kelas_dua: String? = null,

    @field:SerializedName("link_video_satu")
    val link_video_satu: String? = null,

    @field:SerializedName("link_video_dua")
    val link_video_dua: String? = null,

    @field:SerializedName("link_video_tiga")
    val link_video_tiga: String? = null,

    @field:SerializedName("link_video_empat")
    val link_video_empat: String? = null,

    @field:SerializedName("photo_peserta")
    val photo_peserta: String? = null,

    @field:SerializedName("photo_akte")
    val photo_akte: String? = null
) : Parcelable
