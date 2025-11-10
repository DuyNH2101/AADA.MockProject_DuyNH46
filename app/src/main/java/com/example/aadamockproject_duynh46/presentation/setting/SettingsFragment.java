package com.example.aadamockproject_duynh46.presentation.setting;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;

import com.example.aadamockproject_duynh46.R;
import com.example.aadamockproject_duynh46.presentation.main.MovieListViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsFragment extends PreferenceFragmentCompat {
    private MovieListViewModel viewModel;

    public SettingsFragment(){}
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        viewModel = new ViewModelProvider(requireActivity()).get(MovieListViewModel.class);

        ListPreference loadType = findPreference("category");
        if(loadType != null){
            loadType.setOnPreferenceChangeListener((preference, newValue) -> {
                viewModel.getMovieLoadType().setValue((String) newValue);
                return true;
            });
        }

        SeekBarPreference fromRating = findPreference("fromRating");
        if (fromRating != null) {
            int value = fromRating.getSharedPreferences().getInt("fromRating", 0);
            fromRating.setSummary("Selected: " + value/10.0f);

            fromRating.setOnPreferenceChangeListener((preference, newValue) -> {
                preference.setSummary("Selected: " + (Integer)newValue / 10.0f);
                viewModel.getFromRating().setValue((Integer)newValue / 10.0f);
                return true;
            });
        }

        EditTextPreference fromYear = findPreference("fromYear");
        if(fromYear != null){
            fromYear.setOnPreferenceChangeListener(((preference, newValue) -> {
                try{
                    int newYear = Integer.parseInt((String)newValue);
                    viewModel.getFromYear().setValue(newYear);
                } catch (NumberFormatException e) {

                }
                return true;
            }));
        }
        ListPreference sortBy = findPreference("sortBy");
        if(sortBy != null){
            sortBy.setOnPreferenceChangeListener((preference, newValue) -> {
                viewModel.getSortBy().setValue((String) newValue);
                return true;
            });
        }
    }
}