package com.example.repoexplorer

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repoexplorer.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repoAdapter: RepoAdapter
    private val repoList: MutableList<RepoItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflar el diseño de la actividad con View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initRecyclerView()

        // Configurar el listener del SearchView
        binding.searchRepos.setOnQueryTextListener(this)
    }

    // Inicializa el RecyclerView y su adaptador
    private fun initRecyclerView() {
        repoAdapter = RepoAdapter(repoList)
        binding.listRepos.layoutManager = LinearLayoutManager(this)
        binding.listRepos.adapter = repoAdapter
    }

    // Realiza la petición a la API REST de GitHub para buscar repositorios por nombre de usuario
    private fun searchByUsername(username: String) {
        val trimmedUser = username.trim()
        if (trimmedUser.isEmpty()) {
            Toast.makeText(this, "Por favor, escribe un nombre de usuario", Toast.LENGTH_SHORT).show()
            return
        }

        // Mostrar indicador de carga y ocultar mensajes anteriores
        binding.progressBar.visibility = View.VISIBLE
        binding.tvStatusMessage.visibility = View.GONE
        repoList.clear()
        repoAdapter.updateList(repoList)

        // Ocultar teclado
        hideKeyboard()

        // Llamada asíncrona a través de Retrofit
        val call: Call<List<RepoItem>> = RetrofitClient.instance.getReposByUsername(trimmedUser)
        call.enqueue(object : Callback<List<RepoItem>> {
            override fun onResponse(call: Call<List<RepoItem>>, response: Response<List<RepoItem>>) {
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val repos = response.body()
                    if (repos.isNullOrEmpty()) {
                        binding.tvStatusMessage.text = "El usuario \"$trimmedUser\" no tiene repositorios públicos."
                        binding.tvStatusMessage.visibility = View.VISIBLE
                        Toast.makeText(this@MainActivity, "Sin repositorios públicos", Toast.LENGTH_SHORT).show()
                    } else {
                        binding.tvStatusMessage.visibility = View.GONE
                        repoList.clear()
                        repoList.addAll(repos)
                        repoAdapter.updateList(repoList)
                        Toast.makeText(
                            this@MainActivity,
                            "Se encontraron ${repos.size} repositorios",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    when (response.code()) {
                        404 -> {
                            binding.tvStatusMessage.text = "No se encontró el usuario \"$trimmedUser\" en GitHub."
                            binding.tvStatusMessage.visibility = View.VISIBLE
                            Toast.makeText(this@MainActivity, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                        }
                        403 -> {
                            binding.tvStatusMessage.text = "Límite de peticiones de GitHub alcanzado. Intenta de nuevo más tarde."
                            binding.tvStatusMessage.visibility = View.VISIBLE
                            Toast.makeText(this@MainActivity, "Límite de API alcanzado", Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            binding.tvStatusMessage.text = "Error del servidor: ${response.code()}"
                            binding.tvStatusMessage.visibility = View.VISIBLE
                            Toast.makeText(this@MainActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<RepoItem>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                binding.tvStatusMessage.text = "Error de conexión: ${t.localizedMessage ?: "Fallo de red"}"
                binding.tvStatusMessage.visibility = View.VISIBLE
                Toast.makeText(this@MainActivity, "Fallo al conectar con GitHub", Toast.LENGTH_LONG).show()
            }
        })
    }

    // Oculta el teclado virtual de la pantalla
    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        currentFocus?.let { view ->
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    // Se ejecuta al presionar el botón de búsqueda / enter en el SearchView
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrBlank()) {
            searchByUsername(query)
            binding.searchRepos.clearFocus()
        }
        return true
    }

    // Se ejecuta cada vez que el texto cambia en el SearchView
    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText.isNullOrBlank()) {
            repoList.clear()
            repoAdapter.updateList(repoList)
            binding.tvStatusMessage.text = "Ingresa un nombre de usuario de GitHub y presiona buscar para consultar sus repositorios públicos."
            binding.tvStatusMessage.visibility = View.VISIBLE
        }
        return false
    }
}