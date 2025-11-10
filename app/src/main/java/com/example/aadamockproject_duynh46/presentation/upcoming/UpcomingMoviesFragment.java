package com.example.aadamockproject_duynh46.presentation.upcoming;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.aadamockproject_duynh46.R;
import com.example.aadamockproject_duynh46.presentation.Model.Moviedat;
import com.example.aadamockproject_duynh46.presentation.adapter.MovieAdapter;
import com.example.aadamockproject_duynh46.presentation.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UpcomingMoviesFragment extends Fragment implements MovieAdapter.OnMovieClickListener {

    private static final String TAG = "MovieListActivity";
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private List<Moviedat> movieList;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private String movieId;
    private ImageView menuButton;
    private UpcomingMovieDetailsViewModel upcomingMovieDetailsViewModel;

    public UpcomingMoviesFragment() {
        // Required empty public constructor
    }

    public static UpcomingMoviesFragment newInstance(String param1, String param2) {
        UpcomingMoviesFragment fragment = new UpcomingMoviesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        upcomingMovieDetailsViewModel = new ViewModelProvider(requireActivity()).get(UpcomingMovieDetailsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming_movies, container, false);

        db = FirebaseFirestore.getInstance();
        progressBar = view.findViewById(R.id.progress_bar);
        recyclerView = view.findViewById(R.id.movie_recycler_view);
        menuButton = view.findViewById(R.id.header_menu_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(requireContext(), movieList, this);
        recyclerView.setAdapter(movieAdapter);

        loadMoviesFromFirestore();

        // 👉 Sự kiện click menu icon
        menuButton.setOnClickListener(this::showPopupMenu);
        return view;
    }

    private void showPopupMenu(View anchor) {
        PopupMenu popup = new PopupMenu(requireContext(), anchor);
        popup.getMenuInflater().inflate(R.menu.menu_popup, popup.getMenu());

        // xử lý khi chọn menu item
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_my_tickets) {
                upcomingMovieDetailsViewModel.getGoToTicketMutableLiveData().setValue("Go to ticket");
                return true;
            } else if (id == R.id.menu_home) {
                return true;
            } else if (id == R.id.menu_logout) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(requireContext(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                requireActivity().finish();
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void loadMoviesFromFirestore() {
        progressBar.setVisibility(View.VISIBLE);

        db.collection("Movies")
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        movieList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Moviedat movie = document.toObject(Moviedat.class);
                            movie.setMovieId(document.getId());
                            movieId = document.getId();
                            movieList.add(movie);
                        }
                        movieAdapter.notifyDataSetChanged();
                        Log.d(TAG, "🎬 Total movies loaded: " + movieList.size());
                        if (movieList.isEmpty()) {
                            Toast.makeText(requireContext(), "Không có phim nào.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.w(TAG, "❌ Error getting documents.", task.getException());
                    }
                });
    }

    @Override
    public void onMovieClick(Moviedat movie) {
        upcomingMovieDetailsViewModel.getUpcomingMovieDetailsMutableLiveData().setValue(movie);
    }
}