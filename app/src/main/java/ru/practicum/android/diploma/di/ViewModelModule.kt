package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.favourite.presentation.FavoritesViewModel
import ru.practicum.android.diploma.filter.area.presentation.ChoosingRegionViewModel
import ru.practicum.android.diploma.filter.country.presentation.ChoosingCountryViewModel
import ru.practicum.android.diploma.filter.industry.presentation.ChoosingIndustryViewModel
import ru.practicum.android.diploma.filter.place.presentation.PlaceOfWorkViewModel
import ru.practicum.android.diploma.filter.settings.presentation.FilterSettingsViewModel
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

    viewModel {
        FilterSettingsViewModel(get())
    }

    viewModel {
        PlaceOfWorkViewModel(get())
    }

    viewModel {
        ChoosingIndustryViewModel(get())
    }

    viewModel {
        ChoosingCountryViewModel(get())
    }

    viewModel {
        ChoosingRegionViewModel(get())
    }
}
