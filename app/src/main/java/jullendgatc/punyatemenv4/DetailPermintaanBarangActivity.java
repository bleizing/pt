package jullendgatc.punyatemenv4;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DetailPermintaanBarangActivity extends AppCompatActivity {
    private static final String TAG = "DetailPermintaanBarang";

    private PermintaanBarang permintaanBarang;
    private EditText editNama, editDeskripsi, editLokasi;

    private String addressLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_permintaan_barang);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        if (getIntent().getExtras() !=  null) {
            int barang_sewa_id = getIntent().getExtras().getInt("permintaan_barang_id");

            if (Model.getPermintaanBarangArrayList() != null || Model.getPermintaanBarangArrayList().size() != 0) {
                for (PermintaanBarang pb : Model.getPermintaanBarangArrayList()) {
                    if (barang_sewa_id == pb.getId()) {
                        permintaanBarang = pb;
                        break;
                    }
                }
            }
        }

        addressLocation = "";

        editNama = (EditText) findViewById(R.id.edNama);
        editDeskripsi = (EditText) findViewById(R.id.edDeskripsi);
        editLokasi = (EditText) findViewById(R.id.editLokasi);

        Button btn_close = (Button) findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getAddress();

        setData();
    }

    private void getAddress() {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(permintaanBarang.getLat()), Double.parseDouble(permintaanBarang.getLng()), 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getLocality()).append("\n");
                result.append(address.getCountryName()).append("\n");
                result.append(address.getAddressLine(0)).append("\n");
                result.append(address.getPostalCode()).append("\n");
                result.append(address.getSubAdminArea()).append("\n");
                result.append(address.getAdminArea()).append("\n");
                result.append(address.getLatitude()).append("\n");
                result.append(address.getLongitude()).append("\n");
                result.append(address.getPhone()).append("\n");
                result.append(address.getPremises()).append("\n");
                result.append(address.getSubLocality()).append("\n");
                result.append(address.getThoroughfare()).append("\n");
                result.append(address.getSubThoroughfare()).append("\n");
                result.append(address.getUrl()).append("\n");
                result.append(address.getMaxAddressLineIndex()).append("\n");

                Log.w(TAG, result.toString());
//                    tv_address.setText(address.getAddressLine(0));
                addressLocation = address.getAddressLine(0);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void setData() {
        editNama.setText(permintaanBarang.getNama());
        editDeskripsi.setText(permintaanBarang.getDeskripsi());
        editLokasi.setText(addressLocation);
    }
}
