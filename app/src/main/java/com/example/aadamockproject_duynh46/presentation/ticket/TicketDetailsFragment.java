package com.example.aadamockproject_duynh46.presentation.ticket;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.aadamockproject_duynh46.R;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class TicketDetailsFragment extends Fragment {

    private static final String TICKET_ID = "TICKET_ID";
    private static final String MOVIE_NAME = "MOVIE_NAME";
    private static final String CREATED_AT = "CREATED_AT";
    private static final String SHOW_TIME = "SHOW_TIME";
    private static final String PRICE = "PRICE";
    private String ticketId;
    private String movieName;
    private String createAt;
    private String showTime;
    private int price;

    private TextView tvMovieName, tvTicketId, tvBookTime, tvShowTime, tvPrice;

    private Button btnBack;

    private TicketDetailsViewModel ticketDetailsViewModel;

    public TicketDetailsFragment() {
    }

    public static TicketDetailsFragment newInstance(
            String ticketId,
            String movieName,
            String createAt,
            String showTime,
            int price
    ) {
        TicketDetailsFragment fragment = new TicketDetailsFragment();
        Bundle args = new Bundle();
        args.putString(TICKET_ID, ticketId);
        args.putString(MOVIE_NAME, movieName);
        args.putString(CREATED_AT, createAt);
        args.putString(SHOW_TIME, showTime);
        args.putInt(PRICE, price);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ticketId = getArguments().getString(TICKET_ID);
            movieName = getArguments().getString(MOVIE_NAME);
            createAt = getArguments().getString(CREATED_AT);
            showTime = getArguments().getString(SHOW_TIME);
            price = getArguments().getInt(PRICE);
        }
        ticketDetailsViewModel = new ViewModelProvider(requireActivity()).get(TicketDetailsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket_details, container, false);
        tvMovieName = view.findViewById(R.id.tvMovieName);
        tvBookTime = view.findViewById(R.id.tvBookTime);
        tvShowTime = view.findViewById(R.id.tvShowTime);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvTicketId = view.findViewById(R.id.tvTicketId);
        btnBack = view.findViewById(R.id.btn_back);

        tvTicketId.setText("Mã vé: " + ticketId);
        tvMovieName.setText("🎬 Phim: " + movieName);
        tvBookTime.setText("💺 Đặt lúc: " + createAt);
        tvShowTime.setText("🕒 Suất chiếu: " + showTime);
        tvPrice.setText("💰 Giá vé: " + price + " VND");


        btnBack.setOnClickListener(v -> {
            ticketDetailsViewModel.getTicketDetailsMutableLiveData().setValue(null);
        });

        return view;
    }
}