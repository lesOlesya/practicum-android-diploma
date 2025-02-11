package ru.practicum.android.diploma.filter.area.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ChoosingWithRvFragmentBinding
import ru.practicum.android.diploma.filter.area.domain.model.Area
import ru.practicum.android.diploma.filter.area.presentation.ChoosingRegionViewModel
import ru.practicum.android.diploma.filter.area.ui.adapter.AreaAdapter
import ru.practicum.android.diploma.filter.place.ui.PlaceOfWorkFragment

class ChoosingRegionFragment : Fragment(), AreaAdapter.AreaClickListener {
    private var _binding: ChoosingWithRvFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<ChoosingRegionViewModel>()
    private val adapter = AreaAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChoosingWithRvFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setBackPressedDispatcher()

        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter

            toolbar.title = getString(R.string.area_headline)
            editTextFilter.hint = getString(R.string.enter_region_hint)
            ivIconClear.setOnClickListener {
                editTextFilter.setText("")
            }

            toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            setOnTextChanged()
        }

        viewModel.observeRegionState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is RegionFragmentState.AreasData -> {
                    showData()
                    adapter.setAreas(state.areas)
                }

                RegionFragmentState.Empty -> showEmpty()
                RegionFragmentState.Error -> showError()
            }
        }

        if (arguments == null) {
            requestAreas(null)
        } else {
            requestAreas(arguments?.getString(COUNTRY_ID))
        }
    }

    private fun changeIcons(isTextEmpty: Boolean) {
        binding.ivIconSearch.isVisible = isTextEmpty
        binding.ivIconClear.isVisible = !isTextEmpty
    }

    private fun requestAreas(countryID: String?) {
        showProgressBar()
        viewModel.getAreas(countryID)
    }

    private fun showProgressBar() {
        with(binding) {
            recyclerView.isVisible = false
            tvFailedRequestPlaceholder.isVisible = false
            tvNotFoundPlaceholder.isVisible = false
            progressBar.isVisible = true
        }
    }

    private fun showEmpty() {
        with(binding) {
            recyclerView.isVisible = false
            tvFailedRequestPlaceholder.isVisible = false
            tvNotFoundPlaceholder.isVisible = true
            progressBar.isVisible = false
        }
    }

    private fun showError() {
        with(binding) {
            recyclerView.isVisible = false
            tvFailedRequestPlaceholder.isVisible = true
            tvNotFoundPlaceholder.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun showData() {
        with(binding) {
            recyclerView.isVisible = true
            tvFailedRequestPlaceholder.isVisible = false
            tvNotFoundPlaceholder.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun setOnTextChanged() {
        binding.editTextFilter.doOnTextChanged { text, _, _, _ ->
            val filterText = text.toString().trim()
            if (filterText.isEmpty()) {
                changeIcons(true)
            } else {
                changeIcons(false)
            }
            viewModel.filterRegions(filterText)
        }
    }

    private fun setBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            }
        )
    }

    override fun onAreaClick(area: Area) {
        getParentFragmentManager().setFragmentResult(
            "placeOfWorkKey",
            PlaceOfWorkFragment.createArgs(region = area)
        )
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    companion object {
        private const val COUNTRY_ID = "CountryID"

        fun setArguments(countryID: String?): Bundle = bundleOf(COUNTRY_ID to countryID)
    }
}
