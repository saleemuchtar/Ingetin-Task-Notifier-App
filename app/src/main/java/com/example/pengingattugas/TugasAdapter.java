package com.example.pengingattugas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TugasAdapter extends ArrayAdapter<Tugas> {

    private Context context;
    private List<Tugas> daftarTugas;

    public interface OnDeleteClickListener {
        void onDeleteClick(Tugas tugas);
    }
    private OnDeleteClickListener deleteClickListener;

    public TugasAdapter(@NonNull Context context, List<Tugas> list, OnDeleteClickListener listener) {
        super(context, 0, list);
        this.context = context;
        this.daftarTugas = list;
        this.deleteClickListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.list_item_tugas, parent, false);
        }

        Tugas tugasSekarang = daftarTugas.get(position);

        TextView tvNamaTugas = listItem.findViewById(R.id.tv_item_nama_tugas);
        tvNamaTugas.setText(tugasSekarang.getNamaTugas());

        TextView tvMatkul = listItem.findViewById(R.id.tv_item_matkul);
        tvMatkul.setText(tugasSekarang.getMataKuliah());

        TextView tvDeadline = listItem.findViewById(R.id.tv_item_deadline);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String deadlineStr = formatter.format(new Date(tugasSekarang.getDeadlineTimestamp()));
        tvDeadline.setText("Deadline: " + deadlineStr);

        ImageButton btnHapus = listItem.findViewById(R.id.btn_hapus_item);
        btnHapus.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(tugasSekarang);
            }
        });

        return listItem;
    }
}