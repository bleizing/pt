package jullendgatc.punyatemenv4;

/**
 * Created by Bleizing on 12/30/2017.
 */

public class Model {
    private static Penyewa penyewa;

    public static void setPenyewa(Penyewa penyewa) {
        Model.penyewa = penyewa;
    }

    public static Penyewa getPenyewa() {
        return penyewa;
    }
}
