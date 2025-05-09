package com.github.markssova.gamescatalog.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.markssova.gamescatalog.databinding.FragmentCatalogBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CatalogFragment : Fragment() {

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!

    private lateinit var catalogRecyclerView: RecyclerView
    private lateinit var catalogAdapter: CatalogAdapter
    private lateinit var catalogViewModel: CatalogViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)
        val root: View = binding.root

        catalogViewModel =
            ViewModelProvider(this)[CatalogViewModel::class.java]

        catalogRecyclerView = binding.catalogRecyclerView
        catalogRecyclerView.layoutManager = LinearLayoutManager(context)
        catalogAdapter = CatalogAdapter()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

//class CatalogFragment : Fragment() {
//
//    private val viewModel: CatalogViewModel by viewModels()
//    private lateinit var adapter: CatalogAdapter
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        adapter = CatalogAdapter()
//        val recyclerView = view.findViewById<RecyclerView>(R.id.catalogRecyclerView)
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//
//        viewModel.games.observe(viewLifecycleOwner) { games ->
//            adapter.submitList(games)
//        }
//
//        viewModel.error.observe(viewLifecycleOwner) {
//            it?.let { msg -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
//        }
//    }
//}