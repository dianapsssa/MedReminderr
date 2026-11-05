package com.example.medreminder;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsManager {

    private static final String NAMA_FILE = "pref_medreminder";

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public PrefsManager(Context context) {
        sp = context.getSharedPreferences(NAMA_FILE, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void simpanNama(String nama) { editor.putString("nama_user", nama); editor.apply(); }
    public String getNama() { return sp.getString("nama_user", "Pengguna"); }

    public void simpanNamaObat(String obat) { editor.putString("nama_obat", obat); editor.apply(); }
    public String getNamaObat() { return sp.getString("nama_obat", "Belum diatur"); }

    public void simpanDosis(String dosis) { editor.putString("dosis", dosis); editor.apply(); }
    public String getDosis() { return sp.getString("dosis", "-"); }

    public void simpanJam(int jam, int menit) {
        editor.putInt("jam_notif", jam);
        editor.putInt("menit_notif", menit);
        editor.apply();
    }
    public int getJam()   { return sp.getInt("jam_notif", 8); }
    public int getMenit() { return sp.getInt("menit_notif", 0); }

    public void simpanStatusNotif(boolean aktif) { editor.putBoolean("notif_aktif", aktif); editor.apply(); }
    public boolean isNotifAktif() { return sp.getBoolean("notif_aktif", true); }

    // dark mode — disimpan di SharedPreferences karena ini preferensi tampilan
    public void simpanDarkMode(boolean dark) { editor.putBoolean("dark_mode", dark); editor.apply(); }
    public boolean isDarkMode() { return sp.getBoolean("dark_mode", false); }

    public boolean isPertamaKali() { return sp.getBoolean("pertama_kali", true); }
    public void sudahPertamaKali() { editor.putBoolean("pertama_kali", false); editor.apply(); }

    public void hapusSemua() { editor.clear(); editor.apply(); }
}
