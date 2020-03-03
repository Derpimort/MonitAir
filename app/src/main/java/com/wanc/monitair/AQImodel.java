package com.wanc.monitair;

import com.google.android.gms.maps.model.LatLng;

public final class AQImodel {
    public LatLng Coords;
    public double AQI;
    public AQImodel(String Latitude, String Longitude, String AQI)
    {
        this.Coords=new LatLng(Double.parseDouble(Latitude),Double.parseDouble(Longitude));
        this.AQI=Double.parseDouble(AQI);
    }
}
