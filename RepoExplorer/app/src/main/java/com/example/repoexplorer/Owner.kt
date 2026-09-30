package com.example.repoexplorer

import com.google.gson.annotations.SerializedName

data class Owner(
    @SerializedName("login")
    val login: String? = null,

    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("avatar_url")
    val avatarUrl: String? = null,

    @SerializedName("html_url")
    val htmlUrl: String? = null
)
