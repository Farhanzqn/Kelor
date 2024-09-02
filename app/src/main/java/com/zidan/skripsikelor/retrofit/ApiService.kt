package com.zidan.skripsikelor.retrofit

import com.zidan.skripsikelor.data.HistoryResponse
import com.zidan.skripsikelor.data.response.DomisiliUsahaRequest
import com.zidan.skripsikelor.data.response.KartuKeluargaRequest
import com.zidan.skripsikelor.data.response.LoginRequest
import com.zidan.skripsikelor.data.response.LoginResponse
import com.zidan.skripsikelor.data.response.PernyataanSKURequest
import com.zidan.skripsikelor.data.response.ProfileResponse
import com.zidan.skripsikelor.data.response.ResidentResponse
import com.zidan.skripsikelor.data.response.SkkmRumahSakitRequest
import com.zidan.skripsikelor.data.response.SkkmSekolahRequest
import com.zidan.skripsikelor.data.response.suratResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    
    @POST("authentication/login")
     fun postLogin(
        @Header("client-platform") clientPlatform: String,
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>

     @GET("residents/{nik}")
     fun getProfile(
         @Path("nik") nik: String,
         @Header("Authorization") token: String,
         @Header("client-platform") clientPlatform: String = "android"
     ):Call<ProfileResponse>

    @GET("documents/user/{nik}")
    fun getDocumentHistory(
        @Path("nik") nik: String,
        @Header("Authorization") token: String,
        @Header("client-platform") clientPlatform: String = "android"
    ): Call<HistoryResponse>

    @POST("documents/eb9ddbbf-d970-4c8f-9c87-800a3761a1d6")
    fun postPernyataanSKU(
        @Header("Authorization") token: String,
        @Header("client-platform") clientPlatform: String,
        @Body request: PernyataanSKURequest
    ): Call<suratResponse>

    @POST("documents/2b76a6b5-6c9e-4dea-90f8-6eec0183530a")
    fun postSuratSDKU(
        @Header("Authorization") token: String,
        @Header("client-platform") clientPlatform: String,
        @Body request: DomisiliUsahaRequest
    ): Call<suratResponse>

    @POST("documents/bde3a863-a632-4d63-b155-3c2896d3e989")
    fun postSkkmSekolah(
        @Header("Authorization") token: String,
        @Header("client-platform") clientPlatform: String,
        @Body request: SkkmSekolahRequest
    ): Call<suratResponse>

    @POST("documents/8b963d84-b2a2-43cb-9e9a-e7892905d7f3")
    fun postSkkmRumahSakit(
        @Header("Authorization") token: String,
        @Header("client-platform") clientPlatform: String,
        @Body request: SkkmRumahSakitRequest
    ): Call<suratResponse>

    @POST("documents/cde25fb3-ad23-463c-863a-a6f068e3bf6b")
    fun postKartuKeluarga(
        @Header("Authorization") token: String,
        @Header("client-platform") clientPlatform: String,
        @Body request: KartuKeluargaRequest
    ): Call<suratResponse>

    @GET("residents/{nik}")
    fun getResident(
        @Header("Authorization") token: String,
        @Header("client-platform") clientPlatform: String,
        @Path("nik") nik: String
    ): Call<ResidentResponse>
}