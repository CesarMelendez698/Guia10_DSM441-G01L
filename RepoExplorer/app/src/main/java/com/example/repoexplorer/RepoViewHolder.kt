package com.example.repoexplorer

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.repoexplorer.databinding.ItemRepoBinding
import com.squareup.picasso.Picasso

class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Declaración e inicialización del binding generado para item_repo.xml
    private val binding: ItemRepoBinding = ItemRepoBinding.bind(view)

    // Vincula los datos del repositorio a las vistas
    fun bind(repo: RepoItem) {
        binding.tvRepoName.text = repo.name ?: "Sin nombre"
        binding.tvOwnerName.text = repo.owner?.login ?: ""

        // Configuración de descripción
        if (!repo.description.isNullOrBlank()) {
            binding.tvRepoDescription.visibility = View.VISIBLE
            binding.tvRepoDescription.text = repo.description
        } else {
            binding.tvRepoDescription.visibility = View.GONE
        }

        // Configuración de lenguaje principal
        if (!repo.language.isNullOrBlank()) {
            binding.tvLanguage.visibility = View.VISIBLE
            binding.tvLanguage.text = "● ${repo.language}"
        } else {
            binding.tvLanguage.visibility = View.GONE
        }

        // Estrellas y forks
        binding.tvStars.text = "★ ${repo.stargazersCount ?: 0}"
        binding.tvForks.text = "⑂ ${repo.forksCount ?: 0}"

        // Avatar del propietario mediante Picasso
        val avatarUrl = repo.owner?.avatarUrl
        if (!avatarUrl.isNullOrBlank()) {
            Picasso.get()
                .load(avatarUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(binding.ivOwnerAvatar)
        } else {
            binding.ivOwnerAvatar.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        // Al presionar el elemento se abre el repositorio en el navegador
        itemView.setOnClickListener {
            repo.htmlUrl?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                itemView.context.startActivity(intent)
            }
        }
    }
}
