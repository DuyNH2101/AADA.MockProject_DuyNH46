package com.example.aadamockproject_duynh46.presentation.upcoming;

import android.app.AlertDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aadamockproject_duynh46.R;
import com.example.aadamockproject_duynh46.presentation.Model.Cast;
import com.example.aadamockproject_duynh46.presentation.Model.Moviedat;
import com.example.aadamockproject_duynh46.presentation.adapter.CastAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UpcomingMovieDetailsFragment extends Fragment {
    public static final String EXTRA_MOVIE = "EXTRA_MOVIE";
    private static final String TAG = "MovieDetailActivity";
    private FirebaseFirestore db;
    private int ticketQuantity = 1;

    private RecyclerView castRecyclerView;
    private CastAdapter castAdapter;
    private List<Cast> castList;
    private ProgressBar castProgressBar;

    private UpcomingMovieDetailsViewModel upcomingMovieDetailsViewModel;
    public UpcomingMovieDetailsFragment() {
    }

    public static UpcomingMovieDetailsFragment newInstance(Moviedat moviedat) {
        UpcomingMovieDetailsFragment fragment = new UpcomingMovieDetailsFragment();

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
        View view = inflater.inflate(R.layout.fragment_upcoming_movie_details, container, false);
        db = FirebaseFirestore.getInstance();

        ImageView backButton = view.findViewById(R.id.header_back_button);
        ImageView moviePoster = view.findViewById(R.id.movie_info_movie_poster);
        TextView releaseDate = view.findViewById(R.id.movie_info_release_date_value);
        TextView endTime = view.findViewById(R.id.movie_info_end_time_value);
        TextView rating = view.findViewById(R.id.movie_info_rating_value);
        TextView overview = view.findViewById(R.id.movie_info_overview_value);
        MaterialButton ticketButton = view.findViewById(R.id.movie_info_ticket_btn);
        MaterialButton minusButton = view.findViewById(R.id.minus_button);
        MaterialButton plusButton = view.findViewById(R.id.plus_button);
        TextView quantityTextView = view.findViewById(R.id.movie_info_ticket_count);
        castRecyclerView = view.findViewById(R.id.cast_and_crew_recycler_view);
        castProgressBar = view.findViewById(R.id.load_cast_and_crew_progressbar);

        backButton.setOnClickListener(v -> upcomingMovieDetailsViewModel.getUpcomingMovieDetailsMutableLiveData().setValue(null));

        minusButton.setOnClickListener(v -> {
            if (ticketQuantity > 1) {
                ticketQuantity--;
                quantityTextView.setText(String.valueOf(ticketQuantity));
            }
        });

        plusButton.setOnClickListener(v -> {
            ticketQuantity++;
            quantityTextView.setText(String.valueOf(ticketQuantity));
        });

        setupCastRecyclerView();

        Moviedat movie = upcomingMovieDetailsViewModel.getUpcomingMovieDetailsMutableLiveData().getValue();

        if (movie != null) {
            Glide.with(this).load(movie.getPosterUrl()).into(moviePoster);

            releaseDate.setText(movie.getTimeStart());
            endTime.setText(movie.getTimeEnd());

            rating.setText(movie.getRating() + "/10");
            overview.setText(movie.getOverview());

            ticketButton.setOnClickListener(v -> showConfirmationDialog(movie));

            if (movie.getMovieId() != null && !movie.getMovieId().isEmpty()) {
                loadCast(movie.getMovieId());
            } else {
                Log.w(TAG, "Movie ID is null, cannot load cast.");
                castProgressBar.setVisibility(View.GONE);
            }
        }
        return view;
    }
    private void showConfirmationDialog(Moviedat movie) {
        String message = "Phim: " + movie.getMovieName() + "\n"
                + "Số lượng: " + ticketQuantity + "\n"
                + "Ngày chiếu: " + movie.getTimeStart();

        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận đặt vé")
                .setMessage(message)
                .setPositiveButton("Xác nhận", (dialog, which) -> bookTicket(movie))
                .setNegativeButton("Hủy", null)
                .create()
                .show();
    }

    private void setupCastRecyclerView() {
        castList = new ArrayList<>();
        castAdapter = new CastAdapter(requireContext(), castList);
        castRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        castRecyclerView.setAdapter(castAdapter);
    }

    private void loadCast(String movieId) {
        castProgressBar.setVisibility(View.VISIBLE);
        db.collection("Movies").document(movieId).collection("cast")
                .get()
                .addOnCompleteListener(task -> {
                    castProgressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        castList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Cast castMember = document.toObject(Cast.class);
                            castList.add(castMember);
                        }
                        castAdapter.notifyDataSetChanged();
                    } else {
                        Log.w(TAG, "Error getting cast documents: ", task.getException());
                    }
                });
    }

    private void bookTicket(Moviedat movie) {
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> data = new HashMap<>();
        data.put("UserId", userId);
        data.put("MovieId", movie.getMovieId());
        data.put("Name", "Ticket for " + movie.getMovieName());
        data.put("StartTime", currentDate);
        data.put("EndTime", movie.getTimeEnd());
        data.put("Status", "Confirmed");
        data.put("MaxTicket", ticketQuantity);
        data.put("CreatedAt", FieldValue.serverTimestamp());
        db.collection("Ticket")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(requireContext(), ticketQuantity + " ticket(s) booked successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Error booking ticket.", Toast.LENGTH_SHORT).show();
                });
    }
}