package com.example.aadamockproject_duynh46.presentation.ticket;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.aadamockproject_duynh46.R;
import com.example.aadamockproject_duynh46.presentation.Model.Ticket;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private List<Ticket> ticketList;

    private OnTicketClickListener listener;

    public TicketAdapter(List<Ticket> ticketList, OnTicketClickListener onTicketClickListener) {
        this.ticketList = ticketList;
        this.listener = onTicketClickListener;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = ticketList.get(position);
        holder.tvTicketName.setText(ticket.getName());
        holder.tvStartTime.setText("Bắt đầu: " + ticket.getStartTime());
        holder.tvEndTime.setText("Kết thúc: " + ticket.getEndTime());
        holder.tvStatus.setText("Trạng thái: " + ticket.getStatus());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        holder.btnViewDetail.setOnClickListener(v -> {
             listener.onTicketClick(ticket);
        });
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Xác nhận xoá vé")
                    .setMessage("Bạn có chắc muốn xoá vé này không?")
                    .setPositiveButton("Xoá", (dialog, which) -> {
                        if (ticket.getTicketId() == null || ticket.getTicketId().isEmpty()) {
                            Toast.makeText(holder.itemView.getContext(), "Không thể xoá vé: Thiếu ID", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        db.collection("Ticket").document(ticket.getTicketId())
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    int currentPos = holder.getAdapterPosition();
                                    if (currentPos != RecyclerView.NO_POSITION && currentPos < ticketList.size()) {
                                        ticketList.remove(currentPos);
                                        notifyItemRemoved(currentPos);
                                    }
                                    Toast.makeText(holder.itemView.getContext(), "Đã xoá vé thành công", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e ->
                                    Toast.makeText(holder.itemView.getContext(), "Lỗi xoá vé: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                );
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView tvTicketName, tvStartTime, tvEndTime, tvStatus;
        Button btnViewDetail, btnDelete;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTicketName = itemView.findViewById(R.id.tvTicketName);
            tvStartTime = itemView.findViewById(R.id.tvStartTime);
            tvEndTime = itemView.findViewById(R.id.tvEndTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnViewDetail = itemView.findViewById(R.id.btnViewDetail);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    interface OnTicketClickListener {
        public void onTicketClick(Ticket ticket);
    }
}
