package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.favourite.presentation.FavoritesViewModel
import ru.practicum.android.diploma.filter.area.presentation.ChoosingRegionViewModel
import ru.practicum.android.diploma.search.presentation.SearchViewModel
import ru.practicum.android.diploma.vacancy.presentation.VacancyViewModel

val viewModelModule = module {

    viewModel {
        SearchViewModel(get())
    }

    viewModel<FavoritesViewModel> {
        FavoritesViewModel(get())
    }

    viewModel {
        VacancyViewModel(get(), get())
    }

    viewModel{
        ChoosingRegionViewModel(get())
    }
}
