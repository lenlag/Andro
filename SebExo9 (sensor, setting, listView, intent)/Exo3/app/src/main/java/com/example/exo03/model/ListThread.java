package com.example.exo03.model;

import android.content.Context;
import android.preference.PreferenceManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ListThread extends Thread {
    InetworkListener inetworkListener;
//    Context context;
    String url;

    public ListThread(InetworkListener inetworkListener, String url) {
        this.inetworkListener = inetworkListener;
//        this.context = context;
        this.url = url;
    }

    @Override
    public void run() {
        super.run();
        String msgTxt = "";


        if (url != null && !url.equals("")) {

            //appel de l'url
            HttpGet getRequest = new HttpGet(url);
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                //execution de la request
                HttpResponse response = httpClient.execute(getRequest);
                if (response.getStatusLine().getStatusCode() != 200) {
                    // erreur
                    System.out.println("********** Code : " + response.getStatusLine().getStatusCode());
                    throw new Exception();
                }


                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(
                                response.getEntity().getContent()
                        )
                );
                String text;

                //on lis chaque ligne et l'ajoute dans msgTxt
                // on stop la boucle quand "text" recois une valeur NULL
                while ((text = bufferedReader.readLine()) != null) {
                    msgTxt = msgTxt + text;
                }

                //appel de la fonction setNetAdapter dans le MainActivity quand le thread a fini
                inetworkListener.setNetAdapter(msgTxt);
            } catch (Exception ex) {
                //si erreur
                inetworkListener.setNetException(ex);

            }

        }
    }


}
