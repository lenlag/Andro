package formation.afpa.natspecies.model;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpRetryException;

public class ListSpeciesThread extends Thread {

    private InetworkListener in;

    public ListSpeciesThread(InetworkListener in) {
        this.in = in;
    }

    @Override
    public void run() {
        super.run();

        String msgText = "";

        //appel de l'url
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet("http://10.111.61.48:8080/FrontRestNoSecurity/rest/species");

        //pour faire fonctionner HttpGet, il faut rajouter useLibrary + compileSdkVersion dans build.gradle Module:app
        try {
            HttpResponse response = httpClient.execute(getRequest);
            if (response.getStatusLine().getStatusCode() != 200) {
                //erreur
                System.out.println("******Error code : " + response.getStatusLine().getStatusCode());
                throw new Exception();
                //response = 200 => OK, si !=200 => NotOK
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            response.getEntity().getContent()
                    )
            );

            String text;

            //on lit chaque ligne et l'ajoute dans msgTxt
            // on stop la boucle quand "text" recoit une valeur NULL
            while((text = br.readLine()) != null){
                msgText = msgText + text;
            }

            //Thread a FINI => appel de la f-on setNetAdapter depuis le MainActivity
            in.setNetAdapter(msgText);

        } catch (Exception ex) {
            //si erreur => appel de la f-on setNetException depus le MainACtivity
            in.setNetException(ex);

        }

    }

}
