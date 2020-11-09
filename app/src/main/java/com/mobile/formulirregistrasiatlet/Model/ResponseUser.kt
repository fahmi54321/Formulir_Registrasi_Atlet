package com.mobile.formulirregistrasiatlet.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ResponseUser(

	@field:SerializedName("result")
	val result: List<ResultItemUser?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

@Parcelize
data class ResultItemUser(

	@field:SerializedName("nama_kontingen")
	val nama_kontingen: String? = null,

	@field:SerializedName("id_user")
	val id_user: String? = null,

	@field:SerializedName("verifikasi_id")
	val verifikasi_id: String? = null,

	@field:SerializedName("nama_pelatih")
	val nama_pelatih: String? = null,

	@field:SerializedName("no_hp")
	val no_hp: String? = null,

	@field:SerializedName("email_address")
	val email_address: String? = null,

	@field:SerializedName("password")
	val password: String? = null
):Parcelable
