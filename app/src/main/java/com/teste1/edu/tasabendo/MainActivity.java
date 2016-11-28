package com.teste1.edu.tasabendo;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.teste1.edu.tasabendo.model.Evento;
import com.teste1.edu.tasabendo.util.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;

public class MainActivity extends AppCompatActivity {

    TextView txtResposta;

    final String SERVER = "http://192.168.0.56:9000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.txtResposta = (TextView) findViewById(R.id.txtResposta);

        Button btAbrir = (Button) findViewById(R.id.btAbrirMapa);
        btAbrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),MapaActivity.class);
                startActivity(intent);
            }
        });
        Button btRequest = (Button) findViewById(R.id.btPlay);
        btRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HttpRequestElastic http = new HttpRequestElastic();
                http.execute();

            }
        });

        Button btSalvarEvento = (Button) findViewById(R.id.btSalvarEvento);
        btSalvarEvento.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CriaEvento criaEvento = new CriaEvento();
                        criaEvento.execute();
                    }
                }
        );
    }

    private class CriaEvento extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            txtResposta.setText("Salvando Evento......");
        }

        @Override
        protected String doInBackground(String... params) {
            //criado o objeto
            Evento evento1 = new Evento();
            evento1.setCatecategory("Enchente");
            evento1.setLongitude("-23.472730");//-23.472730, -46.749973
            evento1.setLatitude("-46.749973");

            //Conversao para Json
            Gson gson = new Gson();
            String json = gson.toJson(evento1);


            HttpURLConnection conn;
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL(SERVER+"/evento/createEvent");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type","application/json");
                conn.setRequestMethod("POST");

                // Send POST output.
                DataOutputStream printout = new DataOutputStream(conn.getOutputStream ());
                printout.writeBytes(URLEncoder.encode(gson.toString(),"UTF-8"));
                printout.flush ();
                printout.close ();

                int statusCode = conn.getResponseCode();
                Log.i("statuscode",String.valueOf(statusCode));
                if (statusCode != HttpURLConnection.HTTP_OK) {
                    Log.e("HttpError", String.format("Status: %s | Message: %s", statusCode, conn.getResponseMessage()));
                    return String.format("Status: %s | Message: %s", statusCode, conn.getResponseMessage());
                }


                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String ln;

                while ((ln = reader.readLine()) != null) {
                    result.append(ln + "\n");
                }

                reader.close();
            } catch (Exception e) {
                Log.e("doInBackgroundError", e.getMessage() + e.getCause());
                return e.getMessage() +" "+ e.getCause();
            }

            return result.toString();
        }
    }

    private void atualizaTela(final String resp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtResposta.setText(resp);
            }
        });
    }

    private class HttpRequestElastic extends  AsyncTask<String, Void,String>{

        @Override
        protected void onPreExecute() {
            txtResposta.setText("Execultando......");
        }

        @Override
        protected String doInBackground(String... params) {

//            try {
//                return "";//HttpHelper.getEventos();
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Log.w("erro-actv",e.getMessage());
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.w("erro-actv",e.getMessage());
//            } catch (ParseException e) {
//                Log.w("erro-actv",e.getMessage());
//            }
            return "ERRO";

        }

        @Override
        protected void onPostExecute(final String s) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtResposta.setText(s);
                }
            });
        }
    }

    private class HttpRequest extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            txtResposta.setText("Execultando......");
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn;
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL(SERVER+"/evento/list");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int statusCode = conn.getResponseCode();
                Log.i("statuscode",String.valueOf(statusCode));
                if (statusCode != HttpURLConnection.HTTP_OK) {
                    Log.e("HttpError", String.format("Status: %s | Message: %s", statusCode, conn.getResponseMessage()));
                    return String.format("Status: %s | Message: %s", statusCode, conn.getResponseMessage());
                }


                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String ln;

                while ((ln = reader.readLine()) != null) {
                    result.append(ln + "\n");
                }

                reader.close();
            } catch (Exception e) {
                Log.e("doInBackgroundError", e.getMessage() + e.getCause());
                return e.getMessage() +" "+ e.getCause();
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(final String s) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtResposta.setText(s);
                }
            });
        }
    }



    private Context getContext(){
        return this;
    }


}
