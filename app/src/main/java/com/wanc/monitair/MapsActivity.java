package com.wanc.monitair;

import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dialog = ProgressDialog.show(this, "Please Wait", "Loading ways to save the world");
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(19.2183, 72.9781);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Thane"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        float zoom = mMap.getCameraPosition().zoom;

        //loadAQIdata(mMap);
        loadAQIdataLocal(mMap);
        darkCloud(0.01f,19.2183,72.9781,100000f/zoom,100000f/zoom);
    }

    public void darkCloud(float t, double lat, double lng, float width, float height) {
        LatLng NEWARK = new LatLng(lat, lng);

        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .transparency(t)
                .image(BitmapDescriptorFactory.fromResource(R.drawable.cloudwithoutprivelages))
                .position(NEWARK, width, height);

        // Add an overlay to the map, retaining a handle to the GroundOverlay object.
        GroundOverlay imageOverlay = mMap.addGroundOverlay(newarkMap);

    }

    public void lightCloud(float t, double lat, double lng, float width, float height) {
        LatLng NEWARK = new LatLng(lat, lng);

        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .transparency(t)
                .image(BitmapDescriptorFactory.fromResource(R.drawable.cloudwithprivelages))
                .position(NEWARK, width, height);

// Add an overlay to the map, retaining a handle to the GroundOverlay object.
        GroundOverlay imageOverlay = mMap.addGroundOverlay(newarkMap);

    }

    private void loadAQIdata(GoogleMap loadedMap)
    {
        //final ArrayList<AQImodel> dataList=new ArrayList<AQImodel>();
        Log.d("Mainapp","Response not error1");
        // Initialize a new JsonArrayRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                ChordEnds.AQIrequest,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with response
                        //mTextView.setText(response.toString());
                        Log.d("Mainapp","Response not error2");
                        // Process the JSON
                        try{
                            // Loop through the array elements
                            if(!response.getBoolean("error"))
                            {
                                Log.d("testDAta","Test"+response.toString());
                                dialog.dismiss();
                                JSONArray results=response.getJSONArray("resultset");
                                JSONArray tempres;
                                Log.d("Mainapp","Response not error3");
                                for(int i=1;i<results.length()-1;i++) {
                                    // Get current json object
                                    tempres = results.getJSONArray(i);

                                    // Get the current student (json object) data
                                    //dataList.add(new AQImodel(tempres.getString(1),tempres.getString(2),tempres.getString(3)));
                                    darkCloud(Float.parseFloat(tempres.getString(3)),
                                            Double.parseDouble(tempres.getString(1)),
                                            Double.parseDouble(tempres.getString(2)),
                                            100000f, 100000f);
                                }

                            }
                            else
                            {
                                Log.d("Mainapp","Response not error4");
                                Toast.makeText(getApplicationContext(),"Server Error, try again later",Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        VolleyUni.getInstance(this.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
    private void loadAQIdataLocal(GoogleMap loadedMap)
    {
        //final ArrayList<AQImodel> dataList=new ArrayList<AQImodel>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ChordEnds.AQILocalrequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String Response) {
                    //Creating JsonObject from response String
                    Log.d("Mainapp","Response not error2");
                    // Process the JSON

                    try{
                        // Loop through the array elements
                        JSONObject response=new JSONObject(Response.toString());
                        if(!response.getBoolean("error"))
                        {
                            Log.d("testDAta","Test"+response.toString());
                            dialog.dismiss();
                            JSONArray results=response.getJSONArray("resultset");
                            JSONArray tempres;
                            Log.d("Mainapp","Response not error3");
                            for(int i=1;i<results.length()-1;i++){
                                // Get current json object
                                tempres = results.getJSONArray(i);

                                // Get the current student (json object) data
                                //dataList.add(new AQImodel(tempres.getString(1),tempres.getString(2),tempres.getString(3)));
                                darkCloud(Float.parseFloat(tempres.getString(3)),
                                        Double.parseDouble(tempres.getString(1)),
                                        Double.parseDouble(tempres.getString(2)),
                                        100000f,100000f);
                            }

                        }
                        else
                        {
                            Log.d("Mainapp","Response not error4");
                            Toast.makeText(getApplicationContext(),"Server Error, try again later",Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String,String>();
                parameters.put("Lat","34");
                parameters.put("Long","-117");

                return parameters;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        VolleyUni.getInstance(this.getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
