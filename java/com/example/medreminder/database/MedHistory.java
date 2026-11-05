package com.example.medreminder.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// tabel riwayat minum obat
// pake Room Database karena datanya berulang setiap hari
// tidak cocok disimpen di SharedPreferences (itu untuk data tunggal/sederhana)
@Entity(tableName = "riwayat_minum")
public class MedHistory {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String namaObat;
    public String tanggal; // format: yyyy-MM-dd
    public String jam;     // format: HH:mm
    public boolean sudahMinum;

    public MedHistory(String namaObat, String tanggal, String jam, boolean sudahMinum) {
        this.namaObat    = namaObat;
        this.tanggal     = tanggal;
        this.jam         = jam;
        this.sudahMinum  = sudahMinum;
    }
}
