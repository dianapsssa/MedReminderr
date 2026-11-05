package com.example.medreminder;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {

    PrefsManager prefsManager;

    EditText etNama, etObat, etDosis;
    TextView tvJamDipilih;
    Switch swDarkMode;
    Button btnPilihJam, btnSimpan, btnKembali;

    int jamDipilih = 8, menitDipilih = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefsManager = new PrefsManager(this);

        etNama       = findViewById(R.id.et_nama);
        etObat       = findViewById(R.id.et_obat);
        etDosis      = findViewById(R.id.et_dosis);
        tvJamDipilih = findViewById(R.id.tv_jam_dipilih);
        swDarkMode   = findViewById(R.id.sw_dark_mode);
        btnPilihJam  = findViewById(R.id.btn_pilih_jam);
        btnSimpan    = findViewById(R.id.btn_simpan);
        btnKembali   = findViewById(R.id.btn_kembali);

        isiForm();

        btnPilihJam.setOnClickListener(v -> {
            new TimePickerDialog(this, (view, jam, menit) -> {
                jamDipilih   = jam;
                menitDipilih = menit;
                tvJamDipilih.setText(String.format("%02d:%02d", jam, menit));
            }, jamDipilih, menitDipilih, true).show();
        });

        swDarkMode.setOnCheckedChangeListener((btn, isChecked) -> {
            prefsManager.simpanDarkMode(isChecked);
            // terapkan dark mode & refresh tampilan sekarang juga
            AppCompatDelegate.setDefaultNightMode(
                isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
            recreate(); // restart activity agar tema langsung berubah
        });

        btnSimpan.setOnClickListener(v -> simpanData());
        btnKembali.setOnClickListener(v -> finish());
    }

    void isiForm() {
        etNama.setText(prefsManager.getNama());
        etObat.setText(prefsManager.getNamaObat().equals("Belum diatur") ? "" : prefsManager.getNamaObat());
        etDosis.setText(prefsManager.getDosis().equals("-") ? "" : prefsManager.getDosis());
        jamDipilih   = prefsManager.getJam();
        menitDipilih = prefsManager.getMenit();
        tvJamDipilih.setText(String.format("%02d:%02d", jamDipilih, menitDipilih));
        swDarkMode.setChecked(prefsManager.isDarkMode());
    }

    void simpanData() {
        String nama  = etNama.getText().toString().trim();
        String obat  = etObat.getText().toString().trim();
        String dosis = etDosis.getText().toString().trim();

        if (nama.isEmpty() || obat.isEmpty()) {
            Toast.makeText(this, "Nama dan nama obat tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        prefsManager.simpanNama(nama);
        prefsManager.simpanNamaObat(obat);
        prefsManager.simpanDosis(dosis.isEmpty() ? "1 tablet" : dosis);
        prefsManager.simpanJam(jamDipilih, menitDipilih);

        Toast.makeText(this, "Pengaturan berhasil disimpan!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
