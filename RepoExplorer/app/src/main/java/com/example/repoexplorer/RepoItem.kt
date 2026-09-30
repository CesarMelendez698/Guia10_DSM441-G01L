package com.example.repoexplorer

import com.google.gson.annotations.SerializedName

data class RepoItem(
    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("node_id")
    val nodeId: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("full_name")
    val fullName: String? = null,

    @SerializedName("owner")
    val owner: Owner? = null,

    @SerializedName("private")
    val isPrivate: Boolean? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("html_url")
    val htmlUrl: String? = null,

    @SerializedName("language")
    val language: String? = null,

    @SerializedName("stargazers_count")
    val stargazersCount: Int? = null,

    @SerializedName("forks_count")
    val forksCount: Int? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
)
