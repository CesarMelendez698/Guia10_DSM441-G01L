package com.example.repoexplorer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RepoAdapter(private var repos: List<RepoItem>) : RecyclerView.Adapter<RepoViewHolder>() {

    // Se llama cuando RecyclerView necesita crear un nuevo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repo, parent, false)
        return RepoViewHolder(view)
    }

    // Se llama para mostrar los datos en una posición específica
    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(repos[position])
    }

    // Devuelve la cantidad de elementos en la lista
    override fun getItemCount(): Int {
        return repos.size
    }

    // Actualiza la lista de repositorios y notifica al adaptador
    fun updateList(newList: List<RepoItem>) {
        repos = newList
        notifyDataSetChanged()
    }
}
