package com.github.markssova.gamescatalog.ui.catalog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.markssova.gamescatalog.databinding.FragmentCatalogBinding
import com.github.markssova.gamescatalog.ui.details.GameDetailActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CatalogFragment : Fragment() {

    private lateinit var binding: FragmentCatalogBinding
    private lateinit var catalogRecyclerView: RecyclerView
    private lateinit var catalogAdapter: CatalogAdapter
    private lateinit var catalogViewModel: CatalogViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatalogBinding.inflate(inflater, container, false)
        val root: View = binding.root

        catalogViewModel =
            ViewModelProvider(this)[CatalogViewModel::class.java]

        catalogRecyclerView = binding.catalogRecyclerView
        catalogRecyclerView.layoutManager = LinearLayoutManager(context)
        catalogAdapter = CatalogAdapter { game ->
            val intent = Intent(requireContext(), GameDetailActivity::class.java)
            intent.putExtra("gameId", game.id)
            startActivity(intent)
        }
        catalogRecyclerView.adapter = catalogAdapter

        catalogViewModel.games.observe(viewLifecycleOwner) { catalogItems ->
            catalogAdapter.submitList(catalogItems)
        }

        val fab: FloatingActionButton = binding.fabRefreshItem
        fab.setOnClickListener { refreshCatalog() }

        return root
    }

    private fun refreshCatalog() {
        catalogViewModel.fetchGames(true)
    }
}
