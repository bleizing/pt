package jullendgatc.punyatemenv4;

/**
 * Created by Bleizing on 12/30/2017.
 */

public class Barang {
    private int id;
    private String nama = "";
    private String deskripsi = "";
    private String tgl_mulai = "";
    private String tgl_berakhir = "";
    private String foto_barang = "";
    private String lat = "";
    private String lng = "";
    private int user_penyewa_id;

    public Barang() {

    }

    public Barang(int id, String nama, String deskripsi, String tgl_mulai, String tgl_berakhir, String foto_barang, String lat, String lng, int user_penyewa_id) {
        this.id = id;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.tgl_mulai = tgl_mulai;
        this.tgl_berakhir = tgl_berakhir;
        this.foto_barang = foto_barang;
        this.lat = lat;
        this.lng = lng;
        this.user_penyewa_id = user_penyewa_id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setFoto_barang(String foto_barang) {
        this.foto_barang = foto_barang;
    }

    public String getFoto_barang() {
        return foto_barang;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLat() {
        return lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLng() {
        return lng;
    }

    public void setTgl_berakhir(String tgl_berakhir) {
        this.tgl_berakhir = tgl_berakhir;
    }

    public String getTgl_berakhir() {
        return tgl_berakhir;
    }

    public void setTgl_mulai(String tgl_mulai) {
        this.tgl_mulai = tgl_mulai;
    }

    public String getTgl_mulai() {
        return tgl_mulai;
    }

    public void setUser_penyewa_id(int user_penyewa_id) {
        this.user_penyewa_id = user_penyewa_id;
    }

    public int getUser_penyewa_id() {
        return user_penyewa_id;
    }
}
