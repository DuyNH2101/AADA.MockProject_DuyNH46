package com.example.aadamockproject_duynh46.presentation.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.aadamockproject_duynh46.R;
import com.example.aadamockproject_duynh46.databinding.FragmentAboutBinding;
import com.example.aadamockproject_duynh46.presentation.main.ToolbarChangeViewModel;


public class AboutFragment extends Fragment {

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentAboutBinding binding = FragmentAboutBinding.inflate(inflater, container, false);


        ToolbarChangeViewModel toolbarChangeViewModel = new ViewModelProvider(requireActivity()).get(ToolbarChangeViewModel.class);
        toolbarChangeViewModel.updateCurrentFragment("about");

        WebSettings webSettings = binding.fragmentAboutWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(false);
        binding.fragmentAboutWebView.setInitialScale(100);

        binding.fragmentAboutWebView.setWebViewClient(new WebViewClient());

        binding.fragmentAboutWebView.loadUrl("https://www.themoviedb.org/about/our-history");
        return binding.getRoot();
    }

}