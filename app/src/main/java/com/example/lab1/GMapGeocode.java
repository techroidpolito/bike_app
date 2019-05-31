package com.example.lab1;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class GMapGeocode extends AsyncTask<String, Void, LatLng> {
    private String API_key;
    private Context context;

    public GMapGeocode(String key, Context context) {
        this.API_key = key;
        this.context = context;
    }

    @Override
    protected LatLng doInBackground(String... strings) {
        String address = strings[0];

        String url = "https://maps.googleapis.com/maps/api/geocode/xml?"
                + "address=" + address.replaceAll("\\s","+")
                + "&language=en"
                + "&key=" + API_key;

        Log.d("url", url);
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse response = httpClient.execute(httpPost, localContext);
            InputStream in = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = builder.parse(in);
            Log.d("geocode parsing", "ok");
            return getCoordinates(doc);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("error", java.lang.String.valueOf(e));
        }
        return null;
    }

    // after geocoding request
    public LatLng getCoordinates(Document doc){
        try {
            Node lat = doc.getElementsByTagName("lat").item(0);
            Node lng = doc.getElementsByTagName("lng").item(0);
            LatLng coordinates = new LatLng(Double.parseDouble(lat.getTextContent()),Double.parseDouble(lng.getTextContent()));
            Log.i("Coordinates", String.valueOf(coordinates.latitude)+"°, "+String.valueOf(coordinates.longitude)+"°");
            return coordinates;
        } catch (Exception e) {
            return null;
        }
    }
}
