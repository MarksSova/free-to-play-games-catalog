package com.github.markssova.gamescatalog.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.markssova.gamescatalog.R
import com.github.markssova.gamescatalog.ui.catalog.CatalogAdapter
import com.github.markssova.gamescatalog.ui.catalog.CatalogViewModel

class FavoritesFragment : Fragment() {

    private lateinit var viewModel: CatalogViewModel
    private lateinit var adapter: CatalogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.favoritesRecyclerView)
        adapter = CatalogAdapter { }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(requireActivity())[CatalogViewModel::class.java]

        viewModel.favoriteGames.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return view
    }
}
