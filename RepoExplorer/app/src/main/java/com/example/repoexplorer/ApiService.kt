package com.example.repoexplorer

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiService {
    /*
    Obtiene el listado de repositorios públicos de un usuario de GitHub.
    @GET indica que es una solicitud HTTP GET.
    @Path("username") reemplaza {username} en la URL con el valor del parámetro username.
    */
    @Headers("User-Agent: RepoExplorer-App")
    @GET("users/{username}/repos")
    fun getReposByUsername(@Path("username") username: String): Call<List<RepoItem>>
}
