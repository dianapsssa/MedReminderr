package com.example.medreminder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.medreminder.database.AppDatabase;
import com.example.medreminder.database.MedHistory;
import com.example.medreminder.database.MedHistoryDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    PrefsManager prefsManager;
    AppDatabase db;
    MedHistoryDao dao;

    TextView tvNama, tvObat, tvDosis, tvJam, tvStatusNotif, tvRiwayat;
    Switch swNotif;
    Button btnSudahMinum, btnPengaturan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // terapkan dark mode dulu sebelum inflate layout
        prefsManager = new PrefsManager(this);
        AppCompatDelegate.setDefaultNightMode(
            prefsManager.isDarkMode()
                ? AppCompatDelegate.MODE_NIGHT_YES
                : AppCompatDelegate.MODE_NIGHT_NO
        );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db  = AppDatabase.getInstance(this);
        dao = db.medHistoryDao();

        tvNama        = findViewById(R.id.tv_nama);
        tvObat        = findViewById(R.id.tv_obat);
        tvDosis       = findViewById(R.id.tv_dosis);
        tvJam         = findViewById(R.id.tv_jam);
        tvStatusNotif = findViewById(R.id.tv_status_notif);
        tvRiwayat     = findViewById(R.id.tv_riwayat);
        swNotif       = findViewById(R.id.sw_notif);
        btnSudahMinum = findViewById(R.id.btn_sudah_minum);
        btnPengaturan = findViewById(R.id.btn_pengaturan);

        if (prefsManager.isPertamaKali()) {
            prefsManager.sudahPertamaKali();
            startActivity(new Intent(this, SettingsActivity.class));
        }

        btnPengaturan.setOnClickListener(v ->
            startActivity(new Intent(this, SettingsActivity.class))
        );

        swNotif.setOnCheckedChangeListener((btn, isChecked) -> {
            prefsManager.simpanStatusNotif(isChecked);
            updateStatusNotif(isChecked);
        });

        btnSudahMinum.setOnClickListener(v -> {
            String namaObat = prefsManager.getNamaObat();

            // pastikan obat sudah diatur dulu
            if (namaObat.equals("Belum diatur")) {
                Toast.makeText(this, "Atur nama obat di Pengaturan dulu ya!", Toast.LENGTH_SHORT).show();
                return;
            }

            String hari = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String jam  = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

            if (dao.hitungHariIni(hari) > 0) {
                Toast.makeText(this, "Kamu sudah mencatat minum obat hari ini!", Toast.LENGTH_SHORT).show();
                return;
            }

            // simpan ke Room Database
            dao.insert(new MedHistory(namaObat, hari, jam, true));
            Toast.makeText(this, "Berhasil dicatat jam " + jam + "!", Toast.LENGTH_SHORT).show();
            tampilRiwayat();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        muatData();
    }

    void muatData() {
        tvNama.setText("Halo, " + prefsManager.getNama() + "!");
        tvObat.setText(prefsManager.getNamaObat());   // tanpa prefix "Obat:"
        tvDosis.setText(prefsManager.getDosis());      // tanpa prefix "Dosis:"
        tvJam.setText(String.format("%02d:%02d", prefsManager.getJam(), prefsManager.getMenit()));

        boolean aktif = prefsManager.isNotifAktif();
        swNotif.setChecked(aktif);
        updateStatusNotif(aktif);
        tampilRiwayat();
    }

    void updateStatusNotif(boolean aktif) {
        tvStatusNotif.setText(aktif ? "Aktif" : "Nonaktif");
        tvStatusNotif.setTextColor(getResources().getColor(
            aktif ? android.R.color.holo_green_dark : android.R.color.holo_red_light, null));
    }

    void tampilRiwayat() {
        List<MedHistory> list = dao.semua();
        if (list == null || list.isEmpty()) {
            tvRiwayat.setText("Belum ada riwayat minum obat.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        int max = Math.min(list.size(), 5);
        for (int i = 0; i < max; i++) {
            MedHistory h = list.get(i);
            sb.append("-  ").append(h.tanggal)
              .append("  ").append(h.jam)
              .append(" - ").append(h.namaObat).append("\n");
        }
        tvRiwayat.setText(sb.toString().trim());
    }
}
