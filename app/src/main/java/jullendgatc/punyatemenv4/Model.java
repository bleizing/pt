package jullendgatc.punyatemenv4;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Bleizing on 12/30/2017.
 */

public class Model {
    private static Penyewa penyewa;
    private static ArrayList<Barang> barangArrayList;
    private static ArrayList<PermintaanBarang> permintaanBarangArrayList;

    private static double lat = -6.175206;
    private static double lng = 106.827131;

    public static void setPenyewa(Penyewa penyewa) {
        Model.penyewa = penyewa;
    }

    public static Penyewa getPenyewa() {
        return penyewa;
    }

    public static void setBarangArrayList(ArrayList<Barang> barangArrayList) {
        Model.barangArrayList = barangArrayList;
    }

    public static ArrayList<Barang> getBarangArrayList() {
        return barangArrayList;
    }

    public static void setPermintaanBarangArrayList(ArrayList<PermintaanBarang> permintaanBarangArrayList) {
        Model.permintaanBarangArrayList = permintaanBarangArrayList;
    }

    public static ArrayList<PermintaanBarang> getPermintaanBarangArrayList() {
        return permintaanBarangArrayList;
    }

    public static void setLat(double lat) {
        Model.lat = lat;
    }

    public static double getLat() {
        return lat;
    }

    public static void setLng(double lng) {
        Model.lng = lng;
    }

    public static double getLng() {
        return lng;
    }
}
