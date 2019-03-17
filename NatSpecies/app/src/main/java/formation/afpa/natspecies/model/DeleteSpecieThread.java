package formation.afpa.natspecies.model;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DeleteSpecieThread extends Thread {

    private InetworkListener in;
    private int i;

    public DeleteSpecieThread(InetworkListener in, int i) {
        this.in = in;
        this.i = i;
    }

    @Override
    public void run() {
        super.run();

        //appel de l'url
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpDelete delete = new HttpDelete("http://10.111.61.48:8080/FrontRestNoSecurity/rest/species/" + i);

        try

        {
            HttpResponse response = httpClient.execute(delete);
            if (response.getStatusLine().getStatusCode() != 200) {
                //erreur
                System.out.println("******Error code : " + response.getStatusLine().getStatusCode());
                throw new Exception();
                //response = 200 => OK, si !=200 => NotOK
            }

            String content= EntityUtils.toString(response.getEntity());
            //Thread a FINI => appel de la f-on setNetAdapter
            in.setNetAdapter(content);

        } catch(Exception e) {
            e.printStackTrace();
            //si erreur => appel de la f-on setNetException
            in.setNetException(e);


        }

    }

}
