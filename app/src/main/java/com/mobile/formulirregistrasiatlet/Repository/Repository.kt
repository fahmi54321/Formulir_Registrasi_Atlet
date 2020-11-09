package com.mobile.formulirregistrasiatlet.Repository

import com.mobile.formulirregistrasiatlet.model.ResponsePembayaran
import com.mobile.formulirregistrasiatlet.model.ResponsePeserta
import com.mobile.formulirregistrasiatlet.model.ResponseUser
import com.mobile.formulirregistrasiatlet.network.kotlin.ConfigNetwork
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Repository {

    fun addUser(
        namaKontingen: String,
        namaPelatih: String,
        noHp: String,
        emailAddress: String,
        password: String,
        responseHandler: (ResponseUser) -> Unit,
        errorHandler: (Throwable) -> Unit
    ) {
        ConfigNetwork.getRetrofit().daftar(namaKontingen, namaPelatih, noHp, emailAddress, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                responseHandler(it)
            }, {
                errorHandler(it)
            })
    }

    fun login(
        namaKontingen: String,
        password: String,
        responseHandler: (ResponseUser) -> Unit,
        errorHandler: (Throwable) -> Unit
    ) {
        ConfigNetwork.getRetrofit().login(namaKontingen, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                responseHandler(it)
            }, {
                errorHandler(it)
            })
    }

    fun tambahPeserta(
        idUser: RequestBody,
        namaLengkap: RequestBody,
        tempatLahir: RequestBody,
        tglLahir: RequestBody,
        jk: RequestBody,
        perguruan: RequestBody,
        kategori: RequestBody,
        kelas: RequestBody,
        kelas_dua: RequestBody,
        link1: RequestBody,
        link2: RequestBody,
        link3: RequestBody,
        link4: RequestBody,
        fotoPeserta: MultipartBody.Part,
        fotoAkte: MultipartBody.Part,
        responseHandler: (ResponsePeserta) -> Unit,
        errorHandler: (Throwable) -> Unit
    ) {
        ConfigNetwork.getRetrofit().tambahPeserta(
            idUser,
            namaLengkap,
            tempatLahir,
            tglLahir,
            jk,
            perguruan,
            kategori,
            kelas,
            kelas_dua,
            link1,
            link2,
            link3,
            link4,
            fotoPeserta,
            fotoAkte
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                responseHandler(it)
            }, {
                errorHandler(it)
            })
    }

    fun getPeserta(
        idUser: String,
        responseHandler: (ResponsePeserta) -> Unit,
        errorHandler: (Throwable) -> Unit
    ) {
        ConfigNetwork.getRetrofit().selectUser(idUser)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                responseHandler(it)
            }, {
                errorHandler(it)
            })
    }

    fun deletePeserta(
        id_peserta: String?,
        responseHandler: (ResponsePeserta) -> Unit,
        errorHandler: (Throwable) -> Unit
    ) {
        ConfigNetwork.getRetrofit().deletePeserta(id_peserta)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                responseHandler(it)
            }, {
                errorHandler(it)
            })
    }

    fun editPeserta(
        idPeserta: RequestBody,
        namaLengkap: RequestBody,
        tempatLahir: RequestBody,
        tglLahir: RequestBody,
        jk: RequestBody,
        perguruan: RequestBody,
        kategori: RequestBody,
        kelas: RequestBody,
        kelas_dua: RequestBody,
        link1: RequestBody,
        link2: RequestBody,
        link3: RequestBody,
        link4: RequestBody,
        fotoPeserta: MultipartBody.Part,
        fotoAkte: MultipartBody.Part,
        responseHandler: (ResponsePeserta) -> Unit,
        errorHandler: (Throwable) -> Unit
    ) {
        ConfigNetwork.getRetrofit().editPeserta(
            idPeserta,
            namaLengkap,
            tempatLahir,
            tglLahir,
            jk,
            perguruan,
            kategori,
            kelas,
            kelas_dua,
            link1,
            link2,
            link3,
            link4,
            fotoPeserta,
            fotoAkte
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                responseHandler(it)
            }, {
                errorHandler(it)
            })
    }

    fun selectSemuaPeserta(
        responseHandler: (ResponsePeserta) -> Unit,
        errorHandler: (Throwable) -> Unit
    ) {
        ConfigNetwork.getRetrofit().selectSemuaPeserta()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
              responseHandler(it)
            },{
                errorHandler(it)
            })
    }

    fun tambahPembayaran(
        idUser:RequestBody,
        photo:MultipartBody.Part,
        responseHandler: (ResponsePembayaran) -> Unit,
        errorHandler: (Throwable) -> Unit
    ){
        ConfigNetwork.getRetrofit().tambahVerifikasiPembayaran(idUser,photo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                responseHandler(it)
            },{
                errorHandler(it)
            })
    }

    fun selectPembayaran(
        idUser: String,
        responseHandler: (ResponsePembayaran) -> Unit,
        errorHandler: (Throwable) -> Unit
    ) {
        ConfigNetwork.getRetrofit().selectPembayaran(idUser)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                responseHandler(it)
            }, {
                errorHandler(it)
            })
    }
    fun searchingPeserta(
        key:String,
        responseHandler: (ResponsePeserta) -> Unit,
        errorHandler: (Throwable) -> Unit
    ){
        ConfigNetwork.getRetrofit().selectDataPeserta(key)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                responseHandler(it)
            },{
                errorHandler(it)
            })
    }

    fun banyakPeserta(
        responseHandler: (ResponsePeserta) -> Unit,
        errorHandler: (Throwable) -> Unit
    ){
        ConfigNetwork.getRetrofit().banyakPeserta()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                responseHandler(it)
            },{
                errorHandler(it)
            })
    }

    fun selectBanyakPeserta(
        idUser:String,
        responseHandler: (ResponsePeserta) -> Unit,
        errorHandler: (Throwable) -> Unit
    ){
        ConfigNetwork.getRetrofit().selectBanyakPeserta(idUser)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                responseHandler(it)
            },{
                errorHandler(it)
            })
    }

    fun editPembayaran(
        idUser:RequestBody,
        photo:MultipartBody.Part,
        responseHandler: (ResponsePembayaran) -> Unit,
        errorHandler: (Throwable) -> Unit
    ){
        ConfigNetwork.getRetrofit().editVerifikasiPembayaran(idUser,photo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                responseHandler(it)
            },{
                errorHandler(it)
            })
    }
}