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


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ItemOneFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = "ItemOneFragment";

    GoogleMap mMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;

    private RequestQueue requestQueue;

    private FloatingActionButton fab;

    private Penyewa penyewa;

    private PermintaanBarang permintaanBarang;
    private Barang barang;

    private ArrayList<Barang> barangArrayList;
    private ArrayList<PermintaanBarang> permintaanBarangArrayList;

    private Double lat, lng;

    private LocationManager locationManager;

    public static ItemOneFragment newInstance() {
        ItemOneFragment fragment = new ItemOneFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        penyewa = Model.getPenyewa();

        // Request Queue Volley Network Connection
        requestQueue = Volley.newRequestQueue(getActivity());
        barangArrayList = new ArrayList<>();
        permintaanBarangArrayList = new ArrayList<>();

        lat = -6.175206;
        lng = 106.827131;

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        getCurrentLocation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_one, container, false);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.add(R.id.mapView, fragment);
        transaction.commit();
        fragment.getMapAsync(this);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fbClicked();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        permintaanBarang = null;
        barang = null;
        fab.setImageResource(android.R.drawable.ic_input_add);
        if (mMap != null) {
            mMap.clear();
            getCurrentLocation();
            setCenterPoint();
            if (barangArrayList != null) {
                if (barangArrayList.size() != 0) {
                    barangArrayList.clear();
                }
            } else {
                barangArrayList = new ArrayList<>();
            }

            if (permintaanBarangArrayList != null) {
                if (permintaanBarangArrayList.size() != 0) {
                    permintaanBarangArrayList.clear();
                }
            } else {
                permintaanBarangArrayList = new ArrayList<>();
            }
            getBarangSewa();
            getPermintaanBarang();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        setCenterPoint();

        if (barangArrayList != null) {
            if (barangArrayList.size() != 0) {
                barangArrayList.clear();
            }
        } else {
            barangArrayList = new ArrayList<>();
        }

        if (permintaanBarangArrayList != null) {
            if (permintaanBarangArrayList.size() != 0) {
                permintaanBarangArrayList.clear();
            }
        } else {
            permintaanBarangArrayList = new ArrayList<>();
        }
        getBarangSewa();
        getPermintaanBarang();

//        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker);

//        // Add a marker in Sydney, Australia, and move the camera.
//        LatLng b1 = new LatLng(-6.179158, 106.876802);
//        mMap.addMarker(new MarkerOptions().position(b1).title("Jual Baju Second").icon(icon));
//
//        LatLng b2 = new LatLng(-6.180150, 106.875021);
//        mMap.addMarker(new MarkerOptions().position(b2).title("Mobil").icon(icon));
//
//        LatLng b3 = new LatLng(-6.176712, 106.873415);
//        mMap.addMarker(new MarkerOptions().position(b3).title("Motor").icon(icon));
//
//        LatLng b4 = new LatLng(-6.175871, 106.876963);
//        mMap.addMarker(new MarkerOptions().position(b4).title("Kerete Bayi").icon(icon));

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(b1));
//        updateBarangLokasi();

        mMap.setMinZoomPreference(10.0f);
        mMap.setMaxZoomPreference(20.0f);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
//        lat = location.getLatitude();
//        lng = location.getLongitude();
    }

    private void getCurrentLocation() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();

//                getAddress();
            }
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
    }

    private android.location.LocationListener locationListener = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            lat = location.getLatitude();
            lng = location.getLongitude();

//            getAddress();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    };

    private void fbClicked() {
        Intent intent;

        if (barang != null) {
            intent = new Intent(getActivity(), DetailItemInput.class);
            intent.putExtra("barang_id", barang.getId());
        } else {
            intent = new Intent(getActivity(), DetailItemInput.class);
        }

        if (permintaanBarang != null) {
            intent = new Intent(getActivity(), DetailPermintaanBarangActivity.class);
            intent.putExtra("permintaan_barang_id", permintaanBarang.getId());
        }
        startActivity(intent);
    }

    private void getBarangSewa() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, NetAPI.GET_BARANG_SEWA_BY_USER_PENYEWA_ID + penyewa.getId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "getBarangSewaByUserPenyewaId response : " + response);

                try {
                    JSONArray data = response.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObject = data.getJSONObject(i);
                        int id = Integer.parseInt(jsonObject.getString("id"));
                        String nama = jsonObject.getString("nama");
                        String deskripsi = jsonObject.getString("deskripsi");
                        String harga = jsonObject.getString("harga");
                        String tgl_mulai = jsonObject.getString("tanggal_mulai");
                        String tgl_berakhir = jsonObject.getString("tanggal_berakhir");
                        String foto = jsonObject.getString("foto");
                        String lat = jsonObject.getString("lat");
                        String lng = jsonObject.getString("lng");
                        int user_penyewa_id = Integer.parseInt(jsonObject.getString("user_penyewa_id"));

                        Barang barang = new Barang(id, nama, deskripsi, harga, tgl_mulai, tgl_berakhir, foto, lat, lng, user_penyewa_id);
                        barangArrayList.add(barang);
                    }
                    Model.setBarangArrayList(barangArrayList);
                    updateBarangLokasi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error getBarangSewaByUserPenyewaId : " + error);
            }
        });
        jsonObjectRequest.setTag(TAG);
        requestQueue.add(jsonObjectRequest);
    }

    private void getPermintaanBarang() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, NetAPI.GET_PERMINTAAN_BARANG, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "getPermintaanBarang response : " + response);

                try {
                    JSONArray data = response.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObject = data.getJSONObject(i);
                        int id = Integer.parseInt(jsonObject.getString("id"));
                        String nama = jsonObject.getString("nama");
                        String deskripsi = jsonObject.getString("deskripsi");
                        String tgl_mulai = jsonObject.getString("tanggal_mulai");
                        String tgl_berakhir = jsonObject.getString("tanggal_berakhir");
                        String lat = jsonObject.getString("lat");
                        String lng = jsonObject.getString("lng");
                        int calon_penyewa_id = Integer.parseInt(jsonObject.getString("calon_penyewa_id"));
                        String calon_penyewa_nama = jsonObject.getString("calon_penyewa_nama");
                        String calon_penyewa_no_hp = jsonObject.getString("calon_penyewa_no_hp");

                        PermintaanBarang permintaanBarang = new PermintaanBarang(id, nama, deskripsi, tgl_mulai, tgl_berakhir, lat, lng, calon_penyewa_id, calon_penyewa_nama, calon_penyewa_no_hp);
                        permintaanBarangArrayList.add(permintaanBarang);
                    }
                    Model.setPermintaanBarangArrayList(permintaanBarangArrayList);
                    updatePermintaanBarangLokasi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error getPermintaanBarang : " + error);
            }
        });
        jsonObjectRequest.setTag(TAG);
        requestQueue.add(jsonObjectRequest);
    }

    private void updateBarangLokasi() {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker);
        if (barangArrayList.size() != 0) {
            if (mMap != null) {
                for (Barang barang : barangArrayList) {
                    LatLng lokasiBarang = new LatLng(Double.parseDouble(barang.getLat()), Double.parseDouble(barang.getLng()));
                    mMap.addMarker(new MarkerOptions().position(lokasiBarang).title(barang.getNama()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasiBarang));
                }
            }
        } else {
            // LatLng Monumen Nasional as Default
//            LatLng lokasiBarang = new LatLng(-6.175206, 106.827131);
//            mMap.addMarker(new MarkerOptions().position(lokasiBarang).title("Jual Baju Second").icon(icon));

//            mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasiBarang));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                fab.setImageResource(android.R.drawable.ic_input_add);
                permintaanBarang = null;
                barang = null;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                checkTitle(marker.getTitle());

                return false;
            }
        });
    }

    private void updatePermintaanBarangLokasi() {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker);
        if (permintaanBarangArrayList.size() != 0) {
            if (mMap != null) {
                for (PermintaanBarang permintaanBarang : permintaanBarangArrayList) {
                    LatLng lokasiBarang = new LatLng(Double.parseDouble(permintaanBarang.getLat()), Double.parseDouble(permintaanBarang.getLng()));
                    mMap.addMarker(new MarkerOptions().position(lokasiBarang).title(permintaanBarang.getNama()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasiBarang));
                }
            }
        } else {
            // LatLng Monumen Nasional as Default
//            LatLng lokasiBarang = new LatLng(-6.175206, 106.827131);
//            mMap.addMarker(new MarkerOptions().position(lokasiBarang).title("Jual Baju Second").icon(icon));

//            mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasiBarang));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                fab.setImageResource(android.R.drawable.ic_input_add);
                permintaanBarang = null;
                barang = null;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                checkTitle(marker.getTitle());

                return false;
            }
        });
    }

    private void checkTitle(String title) {
        for (PermintaanBarang pb : permintaanBarangArrayList) {
            if (title.equals(pb.getNama())) {
//                        title = permintaanBarang.getNama();

                permintaanBarang = pb;
                break;
            } else {
                permintaanBarang = null;
            }
        }

        for (Barang bs : barangArrayList) {
            if (title.equals(bs.getNama())) {
//                        title = permintaanBarang.getNama();

                barang = bs;
                break;
            } else {
                barang = null;
            }
        }

        if (barang != null) {
            fab.setImageResource(R.drawable.ic_action_edit);
        } else {
            if (permintaanBarang != null) {
                fab.setImageResource(R.drawable.ic_action_view);
            }
        }
    }

    private void setCenterPoint() {
        if (mMap != null) {
            if (lat != 0.0 && lng != 0.0) {
                LatLng latLng = new LatLng(lat, lng);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera( CameraUpdateFactory.zoomTo( 12.0f ) );
            }
        }
    }
}
