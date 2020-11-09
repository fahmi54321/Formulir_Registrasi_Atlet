package com.mobile.formulirregistrasiatlet.network.kotlin

import com.mobile.formulirregistrasiatlet.model.ResponsePembayaran
import com.mobile.formulirregistrasiatlet.model.ResponsePeserta
import com.mobile.formulirregistrasiatlet.model.ResponseUser
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


//todo 2.1 Config API
interface ConfigApi {

    @GET("selectDataPeserta.php")
    fun selectDataPeserta(
        @Query("key") key:String
    ): Flowable<ResponsePeserta>

    @GET("selectPeserta.php")
    fun selectUser(
        @Query("id_user") id_user:String
    ): Flowable<ResponsePeserta>

    @GET("selectBanyakPeserta.php")
    fun selectBanyakPeserta(
        @Query("id_user") id_user:String
    ): Flowable<ResponsePeserta>

    @GET("selectPembayaran.php")
    fun selectPembayaran(
        @Query("id_user") id_user:String
    ): Flowable<ResponsePembayaran>

    @GET("selectSemuaPeserta.php")
    fun selectSemuaPeserta(): Flowable<ResponsePeserta>

    @GET("banyakPeserta.php")
    fun banyakPeserta(): Observable<ResponsePeserta>

    @GET("deletePeserta.php")
    fun deletePeserta(
        @Query("id_peserta") id_peserta: String?
    ): Single<ResponsePeserta>

    @FormUrlEncoded
    @POST("daftar.php")
    fun daftar(
        @Field("nama_kontingen") nama_kontingen:String,
        @Field("nama_pelatih") nama_pelatih:String,
        @Field("no_hp") no_hp:String,
        @Field("email_address") email_address:String,
        @Field("password") password:String
    ): Single<ResponseUser>
    //todo Single jika response nya cuma satu yaitu berhasil atau gagal
    //todo Observable jika response nya banyak
    //todo Flowable jika response nya banyak

    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("nama_kontingen") nama_kontingen:String,
        @Field("password") password:String
    ): Single<ResponseUser>
    //todo Single jika response nya cuma satu yaitu berhasil atau gagal
    //todo Observable jika response nya banyak
    //todo Flowable jika response nya banyak

    @Multipart
    @POST("tambahPeserta.php")
    fun tambahPeserta(
        @Part("id_user") id_user:RequestBody,
        @Part("nama_lengkap") nama_lengkap:RequestBody,
        @Part("tempat_lahir") tempat_lahir:RequestBody,
        @Part("tgl_lahir") tgl_lahir:RequestBody,
        @Part("jk") jk:RequestBody,
        @Part("perguruan") perguruan:RequestBody,
        @Part("kategori") kategori:RequestBody,
        @Part("kelas") kelas:RequestBody,
        @Part("kelas_dua") kelas_dua:RequestBody,
        @Part("link_video_satu") link_video_satu:RequestBody,
        @Part("link_video_dua") link_video_dua:RequestBody,
        @Part("link_video_tiga") link_video_tiga:RequestBody,
        @Part("link_video_empat") link_video_empat:RequestBody,
        @Part photo_peserta:MultipartBody.Part,
        @Part photo_akte:MultipartBody.Part
    ): Single<ResponsePeserta>
    //todo Single jika response nya cuma satu yaitu berhasil atau gagal
    //todo Observable jika response nya banyak
    //todo Flowable jika response nya banyak

    @Multipart
    @POST("editPeserta.php")
    fun editPeserta(
        @Part("id_peserta") id_peserta:RequestBody,
        @Part("nama_lengkap") nama_lengkap:RequestBody,
        @Part("tempat_lahir") tempat_lahir:RequestBody,
        @Part("tgl_lahir") tgl_lahir:RequestBody,
        @Part("jk") jk:RequestBody,
        @Part("perguruan") perguruan:RequestBody,
        @Part("kategori") kategori:RequestBody,
        @Part("kelas") kelas:RequestBody,
        @Part("kelas_dua") kelas_dua:RequestBody,
        @Part("link_video_satu") link_video_satu:RequestBody,
        @Part("link_video_dua") link_video_dua:RequestBody,
        @Part("link_video_tiga") link_video_tiga:RequestBody,
        @Part("link_video_empat") link_video_empat:RequestBody,
        @Part photo_peserta:MultipartBody.Part,
        @Part photo_akte:MultipartBody.Part
    ): Single<ResponsePeserta>
    //todo Single jika response nya cuma satu yaitu berhasil atau gagal
    //todo Observable jika response nya banyak
    //todo Flowable jika response nya banyak

    @Multipart
    @POST("tambahVerifikasiPembayaran.php")
    fun tambahVerifikasiPembayaran(
        @Part("id_user") id_user:RequestBody,
        @Part photo:MultipartBody.Part
    ): Single<ResponsePembayaran>
    //todo Single jika response nya cuma satu yaitu berhasil atau gagal
    //todo Observable jika response nya banyak
    //todo Flowable jika response nya banyak

    @Multipart
    @POST("editVerifikasiPembayaran.php")
    fun editVerifikasiPembayaran(
        @Part("id_user") id_user:RequestBody,
        @Part photo:MultipartBody.Part
    ): Single<ResponsePembayaran>
    //todo Single jika response nya cuma satu yaitu berhasil atau gagal
    //todo Observable jika response nya banyak
    //todo Flowable jika response nya banyak
}