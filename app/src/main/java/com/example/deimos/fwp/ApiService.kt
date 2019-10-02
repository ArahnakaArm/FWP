package com.example.deimos.fwp

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/users")
fun fecthAllUsers(): Call<List<Video>>
}