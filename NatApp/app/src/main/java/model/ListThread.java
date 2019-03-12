package model;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import formation.afpa.natapp.MainActivity;

public class ListThread extends Thread {

    InetworkListener in;
    String WSurl;

    public ListThread(InetworkListener in, String WSurl) {
        this.in = in;
        this.WSurl = WSurl;
    }

    @Override
    public void run() {
        super.run();
        String msgText = "";

        DefaultHttpClient httpClient = new DefaultHttpClient();
        //appel de l'URL
        if (WSurl != null) {
System.out.println("********************* WSurl : " + WSurl);
            HttpGet getRequest = new HttpGet(WSurl);

            //WS a besoin de 2 libraries : HTTP et JSON . On va utiliser httpClient
            try {
                HttpResponse response = httpClient.execute(getRequest);
                if (response.getStatusLine().getStatusCode() != 200) {
                    //on affiche le code de l'erreur
                    System.out.println("####### Error code : " + response.getStatusLine().getStatusCode());
                    throw new Exception();
                }

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                response.getEntity().getContent())
                );


                //on lit chaque ligne et l'ajoute dans msgTxt
                // on stop la boucle quand "text" reçoit une valeur NULL
                String line;

                while ((line = br.readLine()) != null) {
                    msgText = msgText + line; //le contenu ligne par ligne
                }

                //appel de la fonction setNetAdapter dans le MainActivity quand le thread a fini
                //si pas d'erreur
                in.setNetAdapter(msgText); //le contenu ligne par ligne

            } catch (Exception ex) {
                //si erreur le Thread appelle setNetException
                in.setNetException(ex);
            }
        }
    }

}
