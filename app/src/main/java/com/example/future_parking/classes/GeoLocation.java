package com.example.future_parking.classes;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeoLocation {
    private String result;

    public void getAddress(String locationAddress, final Context context){
//        Thread thread = new Thread(){
//            @Override
//            public void run(){
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                try {
                    List addressList = geocoder.getFromLocationName(locationAddress,1);
                    if(addressList != null && addressList.size() > 0){
                        Address address = (Address) addressList.get(0);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(address.getLatitude()).append(",");
                        stringBuilder.append(address.getLongitude());
                        result = stringBuilder.toString();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    if(result != null){
                        Bundle bundle = new Bundle();
                        bundle.putString("address", result);
                    }
                }
            }
//        };
//        thread.start();
//    }


    public String getResult() {
        return result;
    }
}
