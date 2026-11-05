package com.example.medreminder.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MedHistoryDao {

    @Insert
    void insert(MedHistory data);

    // ambil semua riwayat, terbaru duluan
    @Query("SELECT * FROM riwayat_minum ORDER BY tanggal DESC, jam DESC")
    List<MedHistory> semua();

    // cek sudah minum belum hari ini
    @Query("SELECT COUNT(*) FROM riwayat_minum WHERE tanggal = :hari AND sudahMinum = 1")
    int hitungHariIni(String hari);

    @Query("DELETE FROM riwayat_minum")
    void hapusSemua();
}
