package jullendgatc.punyatemenv4;

/**
 * Created by jullendgatc on 8/25/17.
 */

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "Welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private static final String ID = "id";
    private static final String NAMA = "nama";
    private static final String TEMPAT_LAHIR = "tempat_lahir";
    private static final String TANGGAL_LAHIR = "tanggal_lahir";
    private static final String JEKEL = "jekel";
    private static final String ALAMAT = "alamat";
    private static final String NO_HP = "no_hp";
    private static final String NO_KTP = "no_ktp";
    private static final String EMAIL = "email";
    private static final String FOTO_KTP = "foto_ktp";
    private static final String STATUS = "status";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public void setUser(Penyewa penyewa) {
        editor.putInt(ID, penyewa.getId());
        editor.putString(NAMA, penyewa.getNama());
        editor.putString(TEMPAT_LAHIR, penyewa.getTempat_lahir());
        editor.putString(TANGGAL_LAHIR, penyewa.getTanggal_lahir());
        editor.putString(JEKEL, penyewa.getJekel());
        editor.putString(ALAMAT, penyewa.getAlamat());
        editor.putString(NO_HP, penyewa.getNo_hp());
        editor.putString(NO_KTP, penyewa.getNo_ktp());
        editor.putString(EMAIL, penyewa.getEmail());
        editor.putString(FOTO_KTP, penyewa.getFoto_ktp());
        editor.putInt(STATUS, penyewa.getStatus());

        editor.commit();
    }

    public boolean getUser() {
        Penyewa penyewa = new Penyewa();
        penyewa.setId(pref.getInt(ID, 0));
        penyewa.setNama(pref.getString(NAMA, ""));
        penyewa.setTempat_lahir(pref.getString(TEMPAT_LAHIR, ""));
        penyewa.setTanggal_lahir(pref.getString(TANGGAL_LAHIR, ""));
        penyewa.setJekel(pref.getString(JEKEL, ""));
        penyewa.setAlamat(pref.getString(ALAMAT, ""));
        penyewa.setNo_hp(pref.getString(NO_HP, ""));
        penyewa.setNo_ktp(pref.getString(NO_KTP, ""));
        penyewa.setEmail(pref.getString(EMAIL, ""));
        penyewa.setFoto_ktp(pref.getString(FOTO_KTP, ""));
        penyewa.setStatus(pref.getInt(STATUS, 0));

        if (penyewa.getId() != 0) {
            Model.setPenyewa(penyewa);
            return true;
        } else {
            return false;
        }
    }

    public  void clearUser() {
        editor.putInt(ID, 0);
        editor.putString(NAMA, "");
        editor.putString(TEMPAT_LAHIR, "");
        editor.putString(TANGGAL_LAHIR, "");
        editor.putString(JEKEL, "");
        editor.putString(ALAMAT, "");
        editor.putString(NO_HP, "");
        editor.putString(NO_KTP, "");
        editor.putString(EMAIL, "");
        editor.putString(FOTO_KTP, "");

        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, false);
    }

}