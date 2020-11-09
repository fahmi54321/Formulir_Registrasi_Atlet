package com.mobile.formulirregistrasiatlet.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobile.formulirregistrasiatlet.Repository.Repository
import com.mobile.formulirregistrasiatlet.model.ResponsePembayaran
import com.mobile.formulirregistrasiatlet.model.ResponsePeserta
import com.mobile.formulirregistrasiatlet.model.ResponseUser
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ViewModel : ViewModel() {

    // todo 2 inisiaslisasi repository
    val repository = Repository()

    // todo 3 membuat variabel agar bisa ditampung oleh view
    //addUser
    var responUser = MutableLiveData<ResponseUser>()
    var errorUser = MutableLiveData<Throwable>()
    var namaKontingenKosong = MutableLiveData<Boolean>()
    var namaPelatihKosong = MutableLiveData<Boolean>()
    var noHpKosong = MutableLiveData<Boolean>()
    var emailKosong = MutableLiveData<Boolean>()
    var passwordKosong = MutableLiveData<Boolean>()
    var showLoading = MutableLiveData<Boolean>()
    var banyakPassword = MutableLiveData<Boolean>()

    //login
    var responLogin = MutableLiveData<ResponseUser>()
    var errorLogin = MutableLiveData<Throwable>()

    //tambah peserta
    var responTambahPeserta = MutableLiveData<ResponsePeserta>()
    var errorTambahPeserta = MutableLiveData<Throwable>()
    var ukuranBesar = MutableLiveData<ResponsePeserta>()

    //peserta
    var namaLengkapKosong = MutableLiveData<Boolean>()
    var tempatLahirKosong = MutableLiveData<Boolean>()
    var tglLahirKosong = MutableLiveData<Boolean>()
    var jkKosong = MutableLiveData<Boolean>()
    var perguruanKosong = MutableLiveData<Boolean>()

    //getPeserta
    var responGetPeserta = MutableLiveData<ResponsePeserta>()
    var errorGetPeserta = MutableLiveData<Throwable>()

    //delete
    var responDelete = MutableLiveData<ResponsePeserta>()
    var errorDelete = MutableLiveData<Throwable>()

    //edit peserta
    var responEditPeserta = MutableLiveData<ResponsePeserta>()
    var errorEditPeserta = MutableLiveData<Throwable>()
    var editNamaLengkapKosong = MutableLiveData<Boolean>()
    var editShowLoading = MutableLiveData<Boolean>()

    //select semua peserta
    var responSelectSemuaPeserta = MutableLiveData<ResponsePeserta>()
    var errorSelectSemuaPeserta = MutableLiveData<Throwable>()

    // tambah pembayaran
    var responPembayaran = MutableLiveData<ResponsePembayaran>()
    var errorPembayaran = MutableLiveData<Throwable>()

    // edit pembayaran
    var responEditPembayaran = MutableLiveData<ResponsePembayaran>()
    var errorEditPembayaran = MutableLiveData<Throwable>()
    var editShowLoadingPembayaran = MutableLiveData<Boolean>()

    // get pembayaran
    var responGetPembayaran = MutableLiveData<ResponsePembayaran>()
    var errorGetPembayaran = MutableLiveData<Throwable>()

    // searching peserta
    var responSearchingPeserta = MutableLiveData<ResponsePeserta>()
    var errorSearchingPeserta = MutableLiveData<Throwable>()

    // banyak peserta global
    var responBanyakPeserta = MutableLiveData<ResponsePeserta>()
    var errorBanyakPeserta = MutableLiveData<Throwable>()

    // banyak peserta kontingen
    var responBanyakPesertaKontingen = MutableLiveData<ResponsePeserta>()
    var errorBanyakPesertaKontingen = MutableLiveData<Throwable>()

    // todo 4 membuat fungsi yang diperlukan
    fun addUser(
        namaKontingen: String,
        namaPelatih: String,
        noHp: String,
        emailAddress: String,
        password: String
    ) {
        showLoading.value = true

        if (namaKontingen.isEmpty()) {
            namaKontingenKosong.value = true
            showLoading.value = false
        } else if (namaPelatih.isEmpty()) {
            namaPelatihKosong.value = true
            showLoading.value = false
        } else if (noHp.isEmpty()) {
            noHpKosong.value = true
            showLoading.value = false
        } else if (emailAddress.isEmpty()) {
            emailKosong.value = true
            showLoading.value = false
        } else if (password.isEmpty()) {
            passwordKosong.value = true
            showLoading.value = false
        } else if (password.length < 7) {
            banyakPassword.value = true
            showLoading.value = false
        } else {
            repository.addUser(namaKontingen, namaPelatih, noHp, emailAddress, password, {
                responUser.value = it
                showLoading.value = false
            }, {
                errorUser.value = it
                showLoading.value = false
            })
        }
    }

    fun login(
        namaKontingen: String,
        password: String
    ) {
        showLoading.value = true

        if (namaKontingen.isEmpty()) {
            namaKontingenKosong.value = true
            showLoading.value = false
        } else if (password.isEmpty()) {
            namaKontingenKosong.value = true
            showLoading.value = false
        } else {
            repository.login(namaKontingen, password, {
                responLogin.value = it
                showLoading.value = false
            }, {
                errorLogin.value = it
                showLoading.value = false
            })
        }
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
        fotoAkte: MultipartBody.Part
    ) {

        showLoading.value = true

        if (namaLengkap.contentLength() < 1) {
            namaLengkapKosong.value = true
            showLoading.value = false
        } else if (tempatLahir.contentLength() < 1) {
            tempatLahirKosong.value = true
            showLoading.value = false
        } else if (tglLahir.contentLength() < 1) {
            tglLahirKosong.value = true
            showLoading.value = false
        } else if (jk.contentLength() < 1) {
            jkKosong.value = true
            showLoading.value = false
        } else if (perguruan.contentLength() < 1) {
            perguruanKosong.value = true
            showLoading.value = false
        } else {

            repository.tambahPeserta(
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
                fotoAkte,
                {
                    responTambahPeserta.value = it
                    showLoading.value = false
                },
                {
                    errorTambahPeserta.value = it
                    showLoading.value = false
                })
        }
    }

    fun getPeserta(idUser: String) {
        repository.getPeserta(idUser, {
            responGetPeserta.value = it
        }, {
            errorGetPeserta.value = it
        })
    }

    fun deletePeserta(id_peserta: String?){
        repository.deletePeserta(id_peserta,{
            responDelete.value = it
        },{
            errorDelete.value = it
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
        fotoAkte: MultipartBody.Part
    ) {

        editShowLoading.value = true

        if (namaLengkap.contentLength() < 1) {
            namaLengkapKosong.value = true
            editShowLoading.value = false
        } else if (tempatLahir.contentLength() < 1) {
            tempatLahirKosong.value = true
            editShowLoading.value = false
        } else if (tglLahir.contentLength() < 1) {
            tglLahirKosong.value = true
            editShowLoading.value = false
        } else if (jk.contentLength() < 1) {
            jkKosong.value = true
            editShowLoading.value = false
        } else if (perguruan.contentLength() < 1) {
            perguruanKosong.value = true
            editShowLoading.value = false
        }else{
            repository.editPeserta(
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
                fotoAkte,
                {
                    responEditPeserta.value = it
                    editShowLoading.value = false
                },
                {
                    errorEditPeserta.value = it
                    editShowLoading.value = false
                })
        }
    }

    fun selectSemuaPeserta(){
        showLoading.value = true
        repository.selectSemuaPeserta({
            showLoading.value = false
            responSelectSemuaPeserta.value = it
        },{
            showLoading.value = false
            errorSelectSemuaPeserta.value = it
        })
    }

    fun tambahPembayaran(
        idUser:RequestBody,
        photo:MultipartBody.Part
    ){
        showLoading.value = true
        repository.tambahPembayaran(idUser,photo,{
            responPembayaran.value = it
            showLoading.value = false
        },{
            errorPembayaran.value = it
            showLoading.value = false
        })
    }

    fun selectPembayaran(
        idUser:String
    ){
        showLoading.value = true
        repository.selectPembayaran(idUser,{
            responGetPembayaran.value = it
            showLoading.value = false
        },{
            errorGetPembayaran.value = it
            showLoading.value = false
        })
    }

    fun searchingPeserta(
        key:String
    ){
        showLoading.value = true
        repository.searchingPeserta(key,{
            responSearchingPeserta.value =it
            showLoading.value = false
        },{
            errorSearchingPeserta.value = it
            showLoading.value = false
        })
    }

    fun banyakPeserta(){
        showLoading.value = true
        repository.banyakPeserta({
            responBanyakPeserta.value = it
            showLoading.value = false
        },{
            errorBanyakPeserta.value = it
            showLoading.value = false
        })
    }

    fun banyakPesertaKontingen(
        idUser:String
    ){
        showLoading.value = true
        repository.selectBanyakPeserta(idUser,{
            responBanyakPesertaKontingen.value = it
            showLoading.value = false
        },{
            errorBanyakPesertaKontingen.value = it
            showLoading.value = false
        })
    }

    fun editPembayaran(
        idUser:RequestBody,
        photo:MultipartBody.Part
    ){
        showLoading.value = true
        repository.editPembayaran(idUser,photo,{
            responEditPembayaran.value = it
            showLoading.value = false
        },{
            errorEditPembayaran.value = it
            showLoading.value = false
        })
    }
}