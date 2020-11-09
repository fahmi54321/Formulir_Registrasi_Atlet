package com.mobile.formulirregistrasiatlet.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ResponsePembayaran(

	@field:SerializedName("result")
	val result: List<ResultItemPembayaran?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

@Parcelize
data class ResultItemPembayaran(

	@field:SerializedName("photo")
	val photo: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("verifikasi_id")
	val verifikasi_id: String? = null,

	@field:SerializedName("id_user")
	val id_user: String? = null
):Parcelable
