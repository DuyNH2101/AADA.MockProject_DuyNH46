package com.example.aadamockproject_duynh46.presentation.main;

import static com.example.aadamockproject_duynh46.data.utils.Constants.NOW_PLAYING_LOAD_TYPE;
import static com.example.aadamockproject_duynh46.data.utils.Constants.POPULAR_LOAD_TYPE;
import static com.example.aadamockproject_duynh46.data.utils.Constants.TOP_RATED_LOAD_TYPE;
import static com.example.aadamockproject_duynh46.data.utils.Constants.UPCOMING_LOAD_TYPE;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.aadamockproject_duynh46.R;

import com.example.aadamockproject_duynh46.databinding.ActivityMainBinding;
import com.example.aadamockproject_duynh46.domain.model.MovieModel;
import com.example.aadamockproject_duynh46.framework.utils.ImageUtils;
import com.example.aadamockproject_duynh46.presentation.about.AboutFragment;
import com.example.aadamockproject_duynh46.presentation.favoritelist.FavoriteMovieListFragment;
import com.example.aadamockproject_duynh46.presentation.favoritelist.FavoriteMovieListViewModel;
import com.example.aadamockproject_duynh46.presentation.moviedetail.MovieDetailViewModel;
import com.example.aadamockproject_duynh46.presentation.moviedetail.MovieInformationFragment;
import com.example.aadamockproject_duynh46.presentation.movielist.MovieListFragment;
import com.example.aadamockproject_duynh46.presentation.profile.ProfileViewModel;
import com.example.aadamockproject_duynh46.presentation.profile.UserInformationActivity;
import com.example.aadamockproject_duynh46.presentation.reminderlist.ReminderListFragment;
import com.example.aadamockproject_duynh46.presentation.reminderlist.ReminderListViewModel;
import com.example.aadamockproject_duynh46.presentation.setting.SettingsFragment;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private static final ArrayList<Integer> iconList = new ArrayList<>(
            Arrays.asList(R.drawable.ic_home, R.drawable.ic_favorite, R.drawable.ic_setting, R.drawable.ic_about)
    );
    private ActivityMainBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ViewPagerStateAdapter adapter;


    private ToolbarChangeViewModel toolbarChangeViewModel;

    private MovieListViewModel movieListViewModel;
    private MovieDetailViewModel movieDetailViewModel;
    private ReminderListViewModel reminderListViewModel;

    private ProfileViewModel profileViewModel;

    private FavoriteMovieListViewModel favoriteMovieListViewModel;

    private ArrayList<Fragment> fragmentArrayList;
    private MovieListFragment movieListFragment;
    private FavoriteMovieListFragment favoriteMovieListFragment;
    private MovieInformationFragment movieInformationFragment;
    private SettingsFragment settingsFragment;

    private AboutFragment aboutFragment;

    private boolean isFiltering;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        movieListFragment = new MovieListFragment();
        favoriteMovieListFragment = new FavoriteMovieListFragment();
        settingsFragment = new SettingsFragment();
        aboutFragment = new AboutFragment();

        setupViewPager2();


        toolbarChangeViewModel = new ViewModelProvider(this).get(ToolbarChangeViewModel.class);
        movieDetailViewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);
        reminderListViewModel = new ViewModelProvider(this).get(ReminderListViewModel.class);
        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        favoriteMovieListViewModel = new ViewModelProvider(this).get(FavoriteMovieListViewModel.class);


        toolbarChangeViewModel.updateCurrentFragment("movie_list");
        setupNavViewAndToolbar();

        binding.changeListViewTypeBtn.setOnClickListener(v->{
            if(movieListFragment.changeListViewType()){
                binding.changeListViewTypeBtn.setImageResource(R.drawable.ic_grid_list);
            } else {
                binding.changeListViewTypeBtn.setImageResource(R.drawable.ic_linear_list);
            }
        });
        setupMovieDetailViewModelListener();

        profileViewModel.getMutableLiveDataUserModel().observe(this, userModel -> {
            if(userModel != null){
                binding.userImageView.setImageBitmap(ImageUtils.base64ToBitmap(userModel.getImage()));
                binding.userBirthdayView.setText(userModel.getBirthday());
                binding.userEmailView.setText(userModel.getEmail());
                binding.userGenderView.setText(userModel.getGender());
                binding.userNameView.setText(userModel.getFullName());
            }
        });
        profileViewModel.getUser();

        binding.editUserBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserInformationActivity.class);
            startActivity(intent);
            finish();
        });
        MovieModel movieFromNotification = getIntent().getParcelableExtra("MOVIE");
        if(movieFromNotification != null){
            movieDetailViewModel.getMutableLiveDataMovieDetail().setValue(movieFromNotification);
        }
    }

    private void setupMovieDetailViewModelListener(){
        movieDetailViewModel.getMutableLiveDataMovieDetail().observe(this, movieModel -> {
            if(movieModel == null){
                movieInformationFragment = null;
                fragmentArrayList.set(0, movieListFragment);
                adapter.notifyItemChanged(0);
            } else{
                movieInformationFragment = new MovieInformationFragment();
                fragmentArrayList.set(0, movieInformationFragment);
                adapter.notifyItemChanged(0);
                binding.fragmentContainer.setCurrentItem(0, true);
            }
        });
    }

    private void setupNavViewAndToolbar() {
        setSupportActionBar(binding.toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.nav_open, R.string.nav_close);
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);



        ReminderListInNavAdapter reminderListInNavAdapter = new ReminderListInNavAdapter();
        binding.reminderRecyclerViewNavView.setAdapter(reminderListInNavAdapter);
        binding.reminderRecyclerViewNavView.setLayoutManager(new LinearLayoutManager(this));


        if(reminderListViewModel.getMutableLiveDataFirstTwoReminderList().getValue() != null){
            reminderListInNavAdapter.submitList(reminderListViewModel.getMutableLiveDataFirstTwoReminderList().getValue());
        }

        reminderListViewModel.getMutableLiveDataFirstTwoReminderList().observe(this, reminderModels -> {
            if(reminderModels != null){
                reminderListInNavAdapter.submitList(reminderModels);
            }
        });

        binding.movieInfoBackBtn.setOnClickListener(v -> {
            movieDetailViewModel.getMutableLiveDataMovieDetail().setValue(null);
            movieDetailViewModel.getMutableLiveDataListCredits().setValue(null);
        });
        binding.reminderListBackBtn.setOnClickListener(v -> {
            fragmentArrayList.set(2, settingsFragment);
            adapter.notifyItemChanged(2);
            binding.fragmentContainer.setCurrentItem(0, true);
        });
        binding.showAllReminderBtn.setOnClickListener(v -> {
            fragmentArrayList.set(2, new ReminderListFragment());
            adapter.notifyItemChanged(2);
            binding.fragmentContainer.setCurrentItem(2, true);
        });
        setupToolbarChangeListener();

        binding.searchFavoriteMovieBtn.setOnClickListener(v -> {
            isFiltering = !isFiltering;
            if(isFiltering){
                binding.searchFavoriteMovieBtn.setImageResource(R.drawable.ic_close);
                binding.fragmentTitle.setVisibility(View.GONE);
                binding.favoriteSearchBox.setVisibility(View.VISIBLE);
            } else {
                binding.searchFavoriteMovieBtn.setImageResource(R.drawable.ic_search);
                binding.fragmentTitle.setVisibility(View.VISIBLE);
                binding.favoriteSearchBox.setVisibility(View.GONE);
                binding.favoriteSearchBox.setText("");
            }
        });

        binding.favoriteSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                favoriteMovieListViewModel.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupToolbarChangeListener(){
        toolbarChangeViewModel.getMutableLiveDataCurrentFragment().observe(this, s -> {
            switch (s){
                case "movie_list":{
                    binding.fragmentTitle.setText(String.format("%s", "Movies"));
                    binding.fragmentTitle.setVisibility(View.VISIBLE);
                    binding.changeListViewTypeBtn.setVisibility(View.VISIBLE);
                    binding.movieInfoBackBtn.setVisibility(View.GONE);
                    binding.reminderListBackBtn.setVisibility(View.GONE);
                    binding.searchFavoriteMovieBtn.setVisibility(View.GONE);
                    binding.favoriteSearchBox.setVisibility(View.GONE);
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                    actionBarDrawerToggle.syncState();
                    break;
                }
                case "movie_detail":{
                    binding.fragmentTitle.setText(String.format("%s", "Detail"));
                    binding.fragmentTitle.setVisibility(View.VISIBLE);
                    binding.changeListViewTypeBtn.setVisibility(View.GONE);
                    binding.movieInfoBackBtn.setVisibility(View.VISIBLE);
                    binding.reminderListBackBtn.setVisibility(View.GONE);
                    binding.searchFavoriteMovieBtn.setVisibility(View.GONE);
                    binding.favoriteSearchBox.setVisibility(View.GONE);
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
                    break;
                }
                case "favorite_list":{
                    binding.fragmentTitle.setText(String.format("%s", "Favorites"));
                    binding.changeListViewTypeBtn.setVisibility(View.GONE);
                    binding.movieInfoBackBtn.setVisibility(View.GONE);
                    binding.reminderListBackBtn.setVisibility(View.GONE);
                    binding.searchFavoriteMovieBtn.setVisibility(View.VISIBLE);
                    if(binding.favoriteSearchBox.getText().toString().isEmpty()){
                        binding.fragmentTitle.setVisibility(View.VISIBLE);
                        binding.favoriteSearchBox.setVisibility(View.GONE);
                        isFiltering = false;
                        binding.searchFavoriteMovieBtn.setImageResource(R.drawable.ic_search);
                    } else {
                        binding.fragmentTitle.setVisibility(View.GONE);
                        binding.favoriteSearchBox.setVisibility(View.VISIBLE);
                        isFiltering = true;
                        binding.searchFavoriteMovieBtn.setImageResource(R.drawable.ic_close);
                    }
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                    actionBarDrawerToggle.syncState();
                    break;
                }
                case "settings":{
                    binding.fragmentTitle.setText(String.format("%s", "Settings"));
                    binding.fragmentTitle.setVisibility(View.VISIBLE);
                    binding.changeListViewTypeBtn.setVisibility(View.GONE);
                    binding.movieInfoBackBtn.setVisibility(View.GONE);
                    binding.reminderListBackBtn.setVisibility(View.GONE);
                    binding.searchFavoriteMovieBtn.setVisibility(View.GONE);
                    binding.favoriteSearchBox.setVisibility(View.GONE);
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                    actionBarDrawerToggle.syncState();
                    break;
                }
                case "about":{
                    binding.fragmentTitle.setText(String.format("%s", "About"));
                    binding.fragmentTitle.setVisibility(View.VISIBLE);
                    binding.changeListViewTypeBtn.setVisibility(View.GONE);
                    binding.movieInfoBackBtn.setVisibility(View.GONE);
                    binding.reminderListBackBtn.setVisibility(View.GONE);
                    binding.searchFavoriteMovieBtn.setVisibility(View.GONE);
                    binding.favoriteSearchBox.setVisibility(View.GONE);
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                    actionBarDrawerToggle.syncState();
                    break;
                }
                case "reminder_list":{
                    binding.fragmentTitle.setText(String.format("%s", "Reminders"));
                    binding.fragmentTitle.setVisibility(View.VISIBLE);
                    binding.changeListViewTypeBtn.setVisibility(View.GONE);
                    binding.movieInfoBackBtn.setVisibility(View.GONE);
                    binding.reminderListBackBtn.setVisibility(View.VISIBLE);
                    binding.searchFavoriteMovieBtn.setVisibility(View.GONE);
                    binding.favoriteSearchBox.setVisibility(View.GONE);
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
                    break;
                }
            }
        });
    }




    private void setupViewPager2() {
        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(movieListFragment);
        fragmentArrayList.add(favoriteMovieListFragment);
        fragmentArrayList.add(settingsFragment);
        fragmentArrayList.add(aboutFragment);

        adapter = new ViewPagerStateAdapter(this, fragmentArrayList);

        binding.fragmentContainer.setAdapter(adapter);
        ArrayList<String> titleArrayList = new ArrayList<>();

        titleArrayList.add("Movie");
        titleArrayList.add("Favorite");
        titleArrayList.add("Settings");
        titleArrayList.add("About");
        new TabLayoutMediator(binding.tabLayout, binding.fragmentContainer,
                (tab, position) -> {
                    tab.setText(titleArrayList.get(position));
                    tab.setIcon(iconList.get(position));
                })
                .attach();
        binding.fragmentContainer.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == 0) {
                    if(fragmentArrayList.get(0) instanceof MovieInformationFragment){
                        toolbarChangeViewModel.updateCurrentFragment("movie_detail");
                    } else if(fragmentArrayList.get(0) instanceof MovieListFragment){
                        toolbarChangeViewModel.updateCurrentFragment("movie_list");
                    }
                } else if (position == 1) {
                    toolbarChangeViewModel.updateCurrentFragment("favorite_list");
                } else if (position == 2) {
                    if(fragmentArrayList.get(2) instanceof ReminderListFragment){
                        toolbarChangeViewModel.updateCurrentFragment("reminder_list");
                    } else {
                        toolbarChangeViewModel.updateCurrentFragment("settings");
                    }
                } else {
                    toolbarChangeViewModel.updateCurrentFragment("about");
                }
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.popular_movie_choice) {
            movieListViewModel.getMovieLoadType().setValue(POPULAR_LOAD_TYPE);
            return true;
        } else if (itemId == R.id.top_rated_movie_choice) {
            movieListViewModel.getMovieLoadType().setValue(TOP_RATED_LOAD_TYPE);
            return true;
        } else if (itemId == R.id.upcoming_movie_choice) {
            movieListViewModel.getMovieLoadType().setValue(UPCOMING_LOAD_TYPE);
            return true;
        } else if (itemId == R.id.now_playing_movie_choice){
            movieListViewModel.getMovieLoadType().setValue(NOW_PLAYING_LOAD_TYPE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        MovieModel movieFromNotification = intent.getParcelableExtra("MOVIE");
        if(movieFromNotification != null){
            movieDetailViewModel.getMutableLiveDataMovieDetail().setValue(movieFromNotification);
        }
    }
}