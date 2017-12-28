package jullendgatc.punyatemenv4;

/**
 * Created by Bleizing on 12/28/2017.
 */

public class Penyewa {
    private int id;
    private String nama;
    private String tempat_lahir;
    private String tanggal_lahir;
    private String jekel;
    private String alamat;
    private String no_hp;
    private int status;
    private String no_ktp;
    private String email;
    private String foto_ktp;

    public Penyewa() {
        // Default Constructor
    }

    public Penyewa(int id, String email) {
        this.id = id;
        this.email = email;
        this.nama = "";
        this.tempat_lahir = "";
        this.tanggal_lahir = "";
        this.jekel = "";
        this.alamat = "";
        this.no_ktp = "";
        this.status = 0;    // Auto 0 because it must be Unverified
        this.no_ktp = "";
        this.foto_ktp = "";
    }

    // Constructor for Login
    public Penyewa(int id, String nama, String tempat_lahir, String tanggal_lahir, String jekel, String alamat, String no_hp, int status, String no_ktp, String email, String foto_ktp) {
        this.id = id;
        this.nama = nama;
        this.tempat_lahir = tempat_lahir;
        this.tanggal_lahir = tanggal_lahir;
        this.jekel = jekel;
        this.alamat = alamat;
        this.no_hp = no_hp;
        this.status = status;
        this.no_ktp = no_ktp;
        this.email = email;
        this.foto_ktp = foto_ktp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public void setTempat_lahir(String tempat_lahir) {
        this.tempat_lahir = tempat_lahir;
    }

    public String getTempat_lahir() {
        return tempat_lahir;
    }

    public void setTanggal_lahir(String tanggal_lahir) {
        this.tanggal_lahir = tanggal_lahir;
    }

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setFoto_ktp(String foto_ktp) {
        this.foto_ktp = foto_ktp;
    }

    public String getFoto_ktp() {
        return foto_ktp;
    }

    public void setJekel(String jekel) {
        this.jekel = jekel;
    }

    public String getJekel() {
        return jekel;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_ktp(String no_ktp) {
        this.no_ktp = no_ktp;
    }

    public String getNo_ktp() {
        return no_ktp;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
