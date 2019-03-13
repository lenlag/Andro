package com.example.exo03.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ListThread extends Thread {
    InetworkListener in;
    public ListThread(InetworkListener in) {

        this.in = in;
    }

    @Override
    public void run() {
        super.run();
        String msgTxt = "";

        //appel de l'url
        HttpGet getRequest = new HttpGet("http://tarzanman.free.fr/epsi/epsi.html");
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            //execution de la request
            HttpResponse response = httpClient.execute(getRequest);
            if (response.getStatusLine().getStatusCode() != 200) {
                // erreur
                System.out.println("********** Code : " + response.getStatusLine().getStatusCode() );
                throw new Exception();
            }


            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            response.getEntity().getContent()
                    )
            );
            String text ;

            //on lis chaque ligne et l'ajoute dans msgTxt
            // on stop la boucle quand "text" recois une valeur NULL
            while(( text = bufferedReader.readLine()) != null){
                msgTxt = msgTxt + text;
            }

            //appel de la fonction setNetAdapter dans le MainActivity quand le thread a fini
            in.setNetAdapter(msgTxt);
        } catch (Exception ex) {
            //si erreur
            in.setNetException(ex);

        }

    }


}
