/*
 * Copyright (c) 2017. Truiton (http://www.truiton.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Mohit Gupt (https://github.com/mohitgupt)
 *
 */

package jullendgatc.punyatemenv4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemThreeFragment extends Fragment {
    private static final String TAG = "UpdateProfile";

    TextView logout;
    FirebaseAuth auth;

    private Penyewa penyewa;

    private EditText editNama, editNoId, editNoTelp;

    private RequestQueue requestQueue;

    public static ItemThreeFragment newInstance() {
        ItemThreeFragment fragment = new ItemThreeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_three,container,false);

        auth = FirebaseAuth.getInstance();
        // Request Queue Volley Network Connection
        requestQueue = Volley.newRequestQueue(getActivity());

        penyewa = Model.getPenyewa();

        logout = (TextView) view.findViewById(R.id.tvLogout);
        editNama = (EditText) view.findViewById(R.id.edNama);
        editNoId = (EditText) view.findViewById(R.id.edNoId);
        editNoTelp = (EditText) view.findViewById(R.id.edTelp);

        setAttributeEditText(penyewa.getNama(), penyewa.getNo_ktp(), penyewa.getNo_hp());

        Button btn_simpan = (Button) view.findViewById(R.id.btn_simpan);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = editNama.getText().toString().trim();
                String noId = editNoId.getText().toString().trim();
                String noTelp = editNoTelp.getText().toString().trim();

                if (TextUtils.isEmpty(nama)) {
                    Toast.makeText(getActivity(), "Nama Harus Diisi!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(noId)) {
                    Toast.makeText(getActivity(), "No ID Harus Diisi!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(noTelp)) {
                    Toast.makeText(getActivity(), "No Telepon Harus Diisi!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Create a JSON Object
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("nama", nama);
                    jsonObject.put("no_ktp", noId);
                    jsonObject.put("no_hp", noTelp);
                    jsonObject.put("id", penyewa.getId());
                    jsonObject.put("type", "updatePenyewa");
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

                                penyewa.setNama(nama);
                                penyewa.setNo_hp(no_hp);
                                penyewa.setNo_ktp(no_ktp);

                                Model.setPenyewa(penyewa);

//                                setAttributeEditText(nama, no_ktp, no_hp);
                                Toast.makeText(getActivity(), "Data Berhasil Diubah!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error updateProfilePenyewa : " + error);
                    }
                });
                jsonObjectRequest.setTag(TAG);
                requestQueue.add(jsonObjectRequest);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getActivity(), HomeStart.class));
                getActivity().finish();
            }
        });
        return view;
    }

    private void setAttributeEditText(String nama, String noId, String noTelp) {
        editNama.setText(nama);
        editNoId.setText(noId);
        editNoTelp.setText(noTelp);
    }
}
