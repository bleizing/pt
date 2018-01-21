package jullendgatc.punyatemenv4;

import java.util.ArrayList;

/**
 * Created by Bleizing on 12/30/2017.
 */

public class Model {
    private static Penyewa penyewa;
    private static ArrayList<Barang> barangArrayList;
    private static ArrayList<PermintaanBarang> permintaanBarangArrayList;

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
}
