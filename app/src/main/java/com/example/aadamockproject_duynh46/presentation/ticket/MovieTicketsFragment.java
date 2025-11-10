package com.example.aadamockproject_duynh46.presentation.ticket;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aadamockproject_duynh46.R;
import com.example.aadamockproject_duynh46.presentation.Model.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MovieTicketsFragment extends Fragment implements TicketAdapter.OnTicketClickListener {

    private RecyclerView rvTickets;
    private TextView tvTotalTickets;
    private ImageView ivQRCode;
    private Button btnGenerateQR;
    private List<Ticket> ticketList;
    private TicketAdapter adapter;
    private FirebaseFirestore db;

    private static final int PRICE_PER_TICKET = 50000; // 50,000 VND mỗi vé
    private static final String ACCOUNT_NO = "5121008105"; // tài khoản MB Bank
    private static final String ACCOUNT_NAME = "Nguyen Thanh Trung"; // tên tài khoản
    private static final String BANK_ID = "bidv"; // bank code VietQR

    private int totalAmount = 0; // giữ tổng tiền để tạo QR khi nhấn nút

    private TicketDetailsViewModel ticketDetailsViewModel;

    public MovieTicketsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieTicketsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieTicketsFragment newInstance(String param1, String param2) {
        MovieTicketsFragment fragment = new MovieTicketsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ticketDetailsViewModel = new ViewModelProvider(requireActivity()).get(TicketDetailsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_tickets, container, false);

        rvTickets = view.findViewById(R.id.rvTickets);
        tvTotalTickets = view.findViewById(R.id.tvTotalTickets);
        ivQRCode = view.findViewById(R.id.ivQRCode);
        btnGenerateQR = view.findViewById(R.id.btnGenerateQR);

        db = FirebaseFirestore.getInstance();
        ticketList = new ArrayList<>();
        adapter = new TicketAdapter(ticketList, this);
        rvTickets.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvTickets.setAdapter(adapter);
        LinearLayout qrContainer = view.findViewById(R.id.qrContainer);

        loadTickets();

        // 👇 Khi bấm nút mới hiển thị QR
        btnGenerateQR.setOnClickListener(v -> {
            if (totalAmount > 0) {
                loadVietQR(totalAmount);
                qrContainer.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(requireContext(), "Không có vé để thanh toán!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void loadTickets() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("Ticket")
                .whereEqualTo("UserId", userId)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        Toast.makeText(requireContext(), "Lỗi khi tải vé: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (querySnapshot == null) return;

                    ticketList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Ticket ticket = doc.toObject(Ticket.class);
                        ticket.setTicketId(doc.getId());
                        ticketList.add(ticket);
                    }

                    adapter.notifyDataSetChanged();
                    int totalTickets = ticketList.size();
                    totalAmount = totalTickets * PRICE_PER_TICKET;
                    tvTotalTickets.setText("Tổng vé đã đặt: " + totalTickets + " (Tổng tiền: " + totalAmount + " VND)");
                });
    }


    private void loadVietQR(int amount) {
        try {
            String addInfo = "Thanh toan ve xem phim";
            String qrUrl = "https://img.vietqr.io/image/"
                    + BANK_ID + "-" + ACCOUNT_NO + "-compact2.jpg"
                    + "?amount=" + amount
                    + "&addInfo=" + URLEncoder.encode(addInfo, "UTF-8")
                    + "&accountName=" + URLEncoder.encode(ACCOUNT_NAME, "UTF-8");

            Glide.with(this)
                    .load(qrUrl)
                    .into(ivQRCode);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Lỗi tạo QR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTicketClick(
            Ticket ticket
    ) {
        ticketDetailsViewModel.getTicketDetailsMutableLiveData().setValue(ticket);
    }
}