package jullendgatc.punyatemenv4;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DetailItemInput extends AppCompatActivity {
    private static final String TAG = "DetailItemInput";
    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    private Penyewa penyewa;
    private Barang barang;

    private RequestQueue requestQueue;

    final Calendar cal = Calendar.getInstance();

    private int year_x, month_x, day_x;   // untuk tgl mulai
    private int year_y, month_y, day_y;   // untuk tgl berakhir

    private EditText editNama, editDeskripsi, editLokasi, editHarga;
    private TextView tvTglMulai, tvTglBerakhir;
    private ImageView img_foto_barang;
    private String tgl_mulai, tgl_berakhir;

    private String kategori;

    private String type;

    private LinearLayout linearAdd, linearModif, linearLokasi, linearFoto;

    private double lat,lng;

//    private LocationManager locationManager;

    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item_input);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        if (getIntent().getExtras() !=  null) {
            int barangId = getIntent().getExtras().getInt("barang_id");

            if (Model.getBarangArrayList() != null || Model.getBarangArrayList().size() != 0) {
                for (Barang b : Model.getBarangArrayList()) {
                    if (b.getId() == barangId) {
                        barang = b;
                        break;
                    }
                }
            }
        }

        lat = Model.getLat();
        lng = Model.getLng();

        type = "";

        kategori = "";

        penyewa = Model.getPenyewa();

        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(year_x, month_x, day_x);

        year_y = cal.get(Calendar.YEAR);
        month_y = cal.get(Calendar.MONTH);
        day_y = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(year_y, month_y, day_y);

        // Request Queue Volley Network Connection
        requestQueue = Volley.newRequestQueue(this);

        editNama = (EditText) findViewById(R.id.edNamaBarang);
        editDeskripsi = (EditText) findViewById(R.id.edDeskripsi);
        editHarga = (EditText) findViewById(R.id.edHarga);
        tvTglMulai = (TextView) findViewById(R.id.tvTglMulai);
        tvTglBerakhir = (TextView) findViewById(R.id.tvTglBerakhir);
        editLokasi = (EditText) findViewById(R.id.editLokasi);
        img_foto_barang = (ImageView) findViewById(R.id.img_foto_barang);

        linearLokasi = (LinearLayout) findViewById(R.id.linear_lokasi);
        linearAdd = (LinearLayout) findViewById(R.id.linear_add);
        linearModif = (LinearLayout) findViewById(R.id.linear_modif);
        linearFoto = (LinearLayout) findViewById(R.id.linear_foto);

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        getCurrentLocation();

        List<String> categories = new ArrayList<String>();
        categories.add("Kendaraan");
        categories.add("Elektronik");
        categories.add("Hobi");
        categories.add("Kebutuhan anak dan bayi");
        categories.add("Travel");
        categories.add("Perhiasan");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spin_kategori = (Spinner) findViewById(R.id.spin_kategori);
        spin_kategori.setAdapter(dataAdapter);

        spin_kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kategori = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spin_kategori.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ((TextView) spin_kategori.getSelectedView()).setTextColor(Color.WHITE);
            }
        });

        if (barang != null) {
            linearLokasi.setVisibility(View.GONE);
            linearAdd.setVisibility(View.GONE);
            linearModif.setVisibility(View.VISIBLE);
            linearFoto.setVisibility(View.GONE);
            editNama.setText(barang.getNama());

            String tglMulai = barang.getTgl_mulai();
            String tglMulaiArr[] = tglMulai.split("-");
            tglMulai = tglMulaiArr[0];

            String tglBerakhir = barang.getTgl_berakhir();
            String tglBerakhirArr[] = tglBerakhir.split("-");
            tglBerakhir = tglBerakhirArr[0];

            editDeskripsi.setText(barang.getDeskripsi());
            editHarga.setText(barang.getHarga().toString());
            tvTglMulai.setText(tglMulai);
            tvTglBerakhir.setText(tglBerakhir);

            int spinnerPosition = dataAdapter.getPosition(barang.getKategori());
            spin_kategori.setSelection(spinnerPosition);
        } else {
            linearLokasi.setVisibility(View.VISIBLE);
            linearAdd.setVisibility(View.VISIBLE);
            linearModif.setVisibility(View.GONE);
            linearFoto.setVisibility(View.VISIBLE);
            type = "insertBarang";
        }

        getAddress();

        Button btn_ambil_gambar = (Button) findViewById(R.id.btn_ambil_gambar);
        btn_ambil_gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        tvTglMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewD) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                                cal.set(year, monthOfYear, dayOfMonth);
                                day_x = dayOfMonth;
                                month_x = monthOfYear;
                                year_x = year;
                                Locale.setDefault(new Locale("in", "ID"));
                                tgl_mulai = DateFormat.format("dd/MM/yyyy", cal).toString();
                                String str = DateFormat.format("dd MMMM yyyy", cal).toString();
                                tvTglMulai.setText(str);
                            }
                        })
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setPreselectedDate(year_x, month_x, day_x)
                        .setDoneText("Submit")
                        .setCancelText("Cancel");
                cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
            }
        });

        tvTglBerakhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewD) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                                cal.set(year, monthOfYear, dayOfMonth);
                                day_y = dayOfMonth;
                                month_y = monthOfYear;
                                year_y = year;
                                Locale.setDefault(new Locale("in", "ID"));
                                tgl_berakhir = DateFormat.format("dd/MM/yyyy", cal).toString();
                                String str = DateFormat.format("dd MMMM yyyy", cal).toString();
                                tvTglBerakhir.setText(str);
                            }
                        })
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setPreselectedDate(year_y, month_y, day_y)
                        .setDoneText("Submit")
                        .setCancelText("Cancel");
                cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
            }
        });

        Button btn_verifikasi = (Button) findViewById(R.id.btn_post);
        btn_verifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnClickHandle();
            }
        });

        Button btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "editBarang";
                btnClickHandle();
            }
        });

        Button btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "deleteBarang";
                btnClickHandle();
            }
        });

//        Button btn_close = (Button) findViewById(R.id.btn_close);
//        btn_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        TextView tv_close = (TextView) findViewById(R.id.tv_close);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // membuka gallery
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                img_foto_barang.setVisibility(View.VISIBLE);
                img_foto_barang.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void btnClickHandle() {
        String nama = editNama.getText().toString().trim();
        String deskripsi = editDeskripsi.getText().toString().trim();
        String harga = editHarga.getText().toString().trim();
        String tgl_mulai = tvTglMulai.getText().toString().trim();
        String tgl_berakhir = tvTglBerakhir.getText().toString().trim();
        String image = "";
        String latlng = "";
        String lat = String.valueOf(Model.getLat());
        String lng = String.valueOf(Model.getLng());
        if (barang == null) {
            latlng = editLokasi.getText().toString().trim();

            if (!latlng.equals("")) {
                String arrLatLng[] = latlng.split(",");
                lat = arrLatLng[0];
                lng = arrLatLng[1];
            }
        } else {
            lat = "";
            lng = "";
        }

        if (barang == null) {
            if (bitmap != null) {
                image = getStringImage(bitmap);
            }
        }

        if (TextUtils.isEmpty(nama)) {
            Toast.makeText(DetailItemInput.this, "Nama Barang Harus Diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(deskripsi)) {
            Toast.makeText(DetailItemInput.this, "Deskripsi Harus Diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(harga)) {
            Toast.makeText(DetailItemInput.this, "Harga Harus Diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(tgl_mulai)) {
            Toast.makeText(DetailItemInput.this, "Tanggal Mulai Harus Diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(tgl_berakhir)) {
            Toast.makeText(DetailItemInput.this, "Tangga Berakhir Harus Diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (barang == null) {
            if (TextUtils.isEmpty(image) || image.equals("")) {
                Toast.makeText(DetailItemInput.this, "Foto Harus Diisi!", Toast.LENGTH_SHORT).show();
                return;
            }


            if (TextUtils.isEmpty(latlng)) {
                Toast.makeText(DetailItemInput.this, "Menunggu Titik Lokasi!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        sendData(nama, deskripsi, harga, tgl_mulai, tgl_berakhir, image, lat, lng, kategori);
    }

    private void sendData(String nama, String deskripsi, String harga, String tgl_mulai, String tgl_berakhir, String image, String lat, String lng, String kategori) {
        // Create a JSON Object
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", type);
            if (!type.equals("deleteBarang")) {
                jsonObject.put("nama", nama);
                jsonObject.put("deskripsi", deskripsi);
                jsonObject.put("harga", harga);
                jsonObject.put("tanggal_mulai", tgl_mulai);
                jsonObject.put("tanggal_berakhir", tgl_berakhir);
                jsonObject.put("lat", lat);
                jsonObject.put("lng", lng);
                jsonObject.put("kategori", kategori);
                jsonObject.put("user_penyewa_id", penyewa.getId());
                jsonObject.put("foto", image);
            }
            if (barang != null) {
                jsonObject.put("barang_sewa_id", barang.getId());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "jsonObject = " + jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NetAPI.URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "insertBarangResponse : " + response);

                try {
                    String type = response.getString("type");
                    String message = response.getString("message");

                    if (type.equals("success")) {
                        Toast.makeText(DetailItemInput.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error DetailItemInput : " + error);
            }
        });
        jsonObjectRequest.setTag(TAG);
        requestQueue.add(jsonObjectRequest);
    }

    //convert image bitmap to string
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 15, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

//    private LocationListener locationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//            lat = location.getLatitude();
//            lng = location.getLongitude();
//
//            getAddress();
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(intent);
//        }
//    };

//    private void getCurrentLocation() {
//        if (locationManager != null) {
//            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if (location != null) {
//                lat = location.getLatitude();
//                lng = location.getLongitude();
//
//                getAddress();
//            }
//        }
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
//        }
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
//    }

    private void getAddress() {
        editLokasi.setText("" + lat + ", " + lng + "");
        Log.d(TAG, "lat : " + lat + ", lng : " + lng);
//        if (lat != 0 && lng != 0) {
//            StringBuilder result = new StringBuilder();
//            try {
//                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
//                if (addresses.size() > 0) {
//                    Address address = addresses.get(0);
//                    result.append(address.getLocality()).append("\n");
//                    result.append(address.getCountryName()).append("\n");
//                    result.append(address.getAddressLine(0)).append("\n");
//                    result.append(address.getPostalCode()).append("\n");
//                    result.append(address.getSubAdminArea()).append("\n");
//                    result.append(address.getAdminArea()).append("\n");
//                    result.append(address.getLatitude()).append("\n");
//                    result.append(address.getLongitude()).append("\n");
//                    result.append(address.getPhone()).append("\n");
//                    result.append(address.getPremises()).append("\n");
//                    result.append(address.getSubLocality()).append("\n");
//                    result.append(address.getThoroughfare()).append("\n");
//                    result.append(address.getSubThoroughfare()).append("\n");
//                    result.append(address.getUrl()).append("\n");
//                    result.append(address.getMaxAddressLineIndex()).append("\n");
//
//                    Log.w(TAG, result.toString());
////                    tv_address.setText(address.getAddressLine(0));
////                    addressLocation = address.getAddressLine(0);
//                }
//            } catch (IOException e) {
//                Log.e(TAG, e.getMessage());
//            }
//        } else {
////            tv_address.setText("Titik Lokasi Tidak Dapat Ditemukan");
//        }
    }
}
