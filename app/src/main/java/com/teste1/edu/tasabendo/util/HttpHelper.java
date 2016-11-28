package com.teste1.edu.tasabendo.util;


import android.util.Log;
import com.teste1.edu.tasabendo.model.Evento;

import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;




/**
 * Created by edu on 11/11/16.
 */


public class HttpHelper {
    private static String GET_EVENTO="http://192.168.0.6:9200/tasabendo/evento/_search/";
    private static String POST_EVENTO="http://192.168.0.6:9200/tasabendo/evento/";


    public static String getAllElastic() throws JSONException, IOException, ParseException {

        String url = "http://192.168.0.6:9200/tasabendo/evento/_search/";

        Log.i("url-elastic",url);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        JSONObject json = new JSONObject(response.body().string());

        JSONObject hits = json.getJSONObject("hits");
        JSONArray eventos = hits.getJSONArray("hits");
        Log.i("hits",eventos.toString());
        int tamanho = eventos.length();
        for (int i = 0; i < tamanho ; i++) {

            JSONObject jEvento = eventos.getJSONObject(i);
            Log.i("hits"+i,jEvento.toString());
            Evento evento = new Evento();
            JSONObject source = jEvento.getJSONObject("_source");
            evento.setId(jEvento.getString("_id"));
            evento.setCatecategory(source.getString("category"));
            JSONObject location = source.getJSONObject("location");
            evento.setLatitude(location.getString("lat"));
            evento.setLongitude(location.getString("lon"));

            String [] tokens = source.getString("create_at").split("-");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(tokens[2]+"/"+tokens[1]+"/"+tokens[0]));

            evento.setCreate_at(cal);
            Log.i("evento", evento.getId()+" "+evento.getCatecategory()+" "+evento.getLatitude()+" "+evento.getLongitude());
        }

        Log.i("resposta",eventos.toString());
        return json.toString();
    }

    public static ArrayList<Evento>  getEventos() throws JSONException, IOException, ParseException {

        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        JSONObject location = new JSONObject();
        location.put("lat",-23.475401);
        location.put("lon",-46.746479);

        JSONObject geoDistance = new JSONObject();
        geoDistance.put("distance","5km");
        geoDistance.put("location",location);

        JSONObject t = new JSONObject();
        t.put("geo_distance",geoDistance);
        JSONObject query = new JSONObject();
        query.put("query",t);

        Log.i("query",query.toString());
        String url = "http://192.168.0.6:9200/tasabendo/evento/_search/";
        String uri = url+query.toString();

        Log.i("url-elastic",url);
        Log.i("uri",uri);
        OkHttpClient client = new OkHttpClient();

        //RequestBody body = RequestBody.create( query.toString());
        RequestBody body = RequestBody.create(JSON, query.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        JSONObject json = new JSONObject(response.body().string());

        JSONObject hits = json.getJSONObject("hits");
        JSONArray eventos = hits.getJSONArray("hits");
        Log.i("hits",eventos.toString());
        int tamanho = eventos.length();

        ArrayList <Evento> lista = new ArrayList<>();
        for (int i = 0; i < tamanho ; i++) {

            JSONObject jEvento = eventos.getJSONObject(i);
            Log.i("hits"+i,jEvento.toString());
            Evento evento = new Evento();
            JSONObject source = jEvento.getJSONObject("_source");
            evento.setId(jEvento.getString("_id"));
            evento.setCatecategory(source.getString("category"));
            JSONObject location2 = source.getJSONObject("location");
            evento.setLatitude(location2.getString("lat"));
            evento.setLongitude(location2.getString("lon"));

            String [] tokens = source.getString("create_at").split("-");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(tokens[2]+"/"+tokens[1]+"/"+tokens[0]));

            evento.setCreate_at(cal);
            lista.add(evento);
            Log.i("evento", evento.getId()+" "+evento.getCatecategory()+" "+evento.getLatitude()+" "+evento.getLongitude());
        }

        Log.i("resposta",eventos.toString());
        return lista;
    }

    public static ArrayList<Evento> doGet(double lat, double lon) throws JSONException, IOException, ParseException {

        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        JSONObject location = new JSONObject();
        location.put("lat",lat);
        location.put("lon",lon);

        JSONObject geoDistance = new JSONObject();
        geoDistance.put("distance","5km");
        geoDistance.put("location",location);

        JSONObject t = new JSONObject();
        t.put("geo_distance",geoDistance);
        JSONObject query = new JSONObject();
        query.put("query",t);

        Log.i("query",query.toString());
        String url = "http://192.168.0.6:9200/tasabendo/evento/_search/";
        String uri = url+query.toString();
        Log.i("url-elastic",url);
        Log.i("uri",uri);
        OkHttpClient client = new OkHttpClient();

        //RequestBody body = RequestBody.create( query.toString());
        RequestBody body = RequestBody.create(JSON, query.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        JSONObject json = new JSONObject(response.body().string());

        JSONObject hits = json.getJSONObject("hits");
        JSONArray eventos = hits.getJSONArray("hits");
        Log.i("hits",eventos.toString());
        int tamanho = eventos.length();

        ArrayList <Evento> lista = new ArrayList<>();
        for (int i = 0; i < tamanho ; i++) {

            JSONObject jEvento = eventos.getJSONObject(i);
            Log.i("hits"+i,jEvento.toString());
            Evento evento = new Evento();
            JSONObject source = jEvento.getJSONObject("_source");
            evento.setId(jEvento.getString("_id"));
            evento.setCatecategory(source.getString("category"));
            JSONObject location2 = source.getJSONObject("location");
            evento.setLatitude(location2.getString("lat"));
            evento.setLongitude(location2.getString("lon"));

            String [] tokens = source.getString("create_at").split("-");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(tokens[2]+"/"+tokens[1]+"/"+tokens[0]));

            evento.setCreate_at(cal);
            lista.add(evento);
            Log.i("evento", evento.getId()+" "+evento.getCatecategory()+" "+evento.getLatitude()+" "+evento.getLongitude());
        }

        Log.i("resposta",eventos.toString());
        return lista;
    }

    static public boolean doPost(Evento evento) {

        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        JSONObject location = new JSONObject();
        try {
            location.put("lat",evento.getLatitude());

            location.put("lon",evento.getLongitude());


            JSONObject jEvento = new JSONObject();
            jEvento.put("category",evento.getCatecategory());
            jEvento.put("location",location);
            jEvento.put("create_at",evento.getCreate_at().get(Calendar.YEAR)+"-"+evento.getCreate_at().get(Calendar.MONTH)+"-"+evento.getCreate_at().get(Calendar.DAY_OF_MONTH));

            Log.i("post",jEvento.toString());
            String url = HttpHelper.POST_EVENTO;
            Log.i("url-elastic",url);
            Log.i("uri",url);

            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, jEvento.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            JSONObject json = new JSONObject(response.body().string());

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static  public boolean doDelete(String id){
        try {
            URL url = new URL("http://192.168.0.6:9200/tasabendo/evento/"+id);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestProperty(
                    "Content-Type", "application/json; charset=utf-8");
            httpCon.setRequestMethod("DELETE");
            httpCon.connect();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (ProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}

/*
POST
{
    "category":"incendio",
    "location":{
        "lat":-23.472133,
        "lon":-46.754071
    },
    "create_at":"2016-11-06"
}


 */

/*
       GET
      "query": {
        "geo_distance": {
          "distance": "0.5km",
          "location": {
            "lat": -23.475401,
            "lon": -46.746479
          }
        }

  }
 */

