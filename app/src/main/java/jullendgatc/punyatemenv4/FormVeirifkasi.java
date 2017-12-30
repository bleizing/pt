package jullendgatc.punyatemenv4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FormVeirifkasi extends AppCompatActivity {
    private static final String TAG = "FormVerifikasi";

    private Penyewa penyewa;

    private RequestQueue requestQueue;

    private EditText editNama, editNoId, editNoTelp;
    private ImageView img_foto_id;

    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_veirifkasi);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        penyewa = Model.getPenyewa();

        // Request Queue Volley Network Connection
        requestQueue = Volley.newRequestQueue(this);

        editNama = (EditText) findViewById(R.id.edNama);
        editNoId = (EditText) findViewById(R.id.edNoId);
        editNoTelp = (EditText) findViewById(R.id.edTelp);
        img_foto_id = (ImageView) findViewById(R.id.img_foto_id);

        Button btn_ambil_gambar = (Button) findViewById(R.id.btn_ambil_gambar);
        btn_ambil_gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        Button btn_verifikasi = (Button) findViewById(R.id.btn_verifikasi);
        btn_verifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = editNama.getText().toString().trim();
                String noId = editNoId.getText().toString().trim();
                String noTelp = editNoTelp.getText().toString().trim();
                String image = "";

                if (bitmap != null) {
                    image = getStringImage(bitmap);
                }


                if (TextUtils.isEmpty(nama)) {
                    Toast.makeText(FormVeirifkasi.this, "Nama Harus Diisi!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(noId)) {
                    Toast.makeText(FormVeirifkasi.this, "No ID Harus Diisi!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(noTelp)) {
                    Toast.makeText(FormVeirifkasi.this, "No Telepon Harus Diisi!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(image) || image.equals("")) {
                    Toast.makeText(FormVeirifkasi.this, "Foto Harus Diisi!", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendData(nama, noId, noTelp, image);
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
                img_foto_id.setVisibility(View.VISIBLE);
                img_foto_id.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendData(String nama, String noId, String noTelp, String image) {
        // Create a JSON Object
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "formVerifikasi");
            jsonObject.put("nama", nama);
            jsonObject.put("no_ktp", noId);
            jsonObject.put("no_hp", noTelp);
            jsonObject.put("id", penyewa.getId());
            jsonObject.put("foto_ktp", image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "jsonObject = " + jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NetAPI.URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "updateProfileResponse : " + response);

                try {
                    String type = response.getString("type");

                    if (type.equals("success")) {
                        String nama = response.getString("nama");
                        String no_hp = response.getString("no_hp");
                        String no_ktp = response.getString("no_ktp");
                        String foto_ktp = response.getString("foto_ktp");
                        int status = Integer.parseInt(response.getString("status_user"));

                        penyewa.setNama(nama);
                        penyewa.setNo_hp(no_hp);
                        penyewa.setNo_ktp(no_ktp);
                        penyewa.setFoto_ktp(foto_ktp);
                        penyewa.setStatus(status);

                        Model.setPenyewa(penyewa);

                        Toast.makeText(FormVeirifkasi.this, "Verifikasi Berhasil", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error formVerifikasi : " + error);
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
}
