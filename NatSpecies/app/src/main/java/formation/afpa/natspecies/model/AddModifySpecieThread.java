package formation.afpa.natspecies.model;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AddModifySpecieThread extends Thread {

   private InetworkListener in;

    private String common;
    private String latin;
    private Integer i;


    public AddModifySpecieThread(InetworkListener in, String common, String latin, Integer i) {
        this.in = in;
        this.common = common;
        this.latin = latin;
        this.i = i;

    }

    public String getCommon() {
        return common;
    }

    public String getLatin() {
        return latin;
    }

     @Override
    public void run() {
        super.run();


        DefaultHttpClient httpClient = new DefaultHttpClient();

        if((common == null) && (latin == null)) {
            HttpPost post = new HttpPost("http://10.111.61.48:8080/FrontRestNoSecurity/rest/species");

            post.setHeader(HTTP.CONTENT_TYPE, "application/json");
            post.setHeader("Accept", "application/json");
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("commonName", this.getCommon()));
                nameValuePairs.add(new BasicNameValuePair("latinName", this.getLatin()));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpClient.execute(post);
                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent()));
                String line;
                String result = "";
                while ((line = rd.readLine()) != null) {
                    result += line;
                }


                //Thread a FINI => appel de la f-on setNetAdapter depuis le MainActivity
                in.setNetAdapter(result);

            } catch (IOException e) {
                e.printStackTrace();
                //si erreur => appel de la f-on setNetException depuis le CreateOrUpdateACtivity
                in.setNetException(e);

            }
        } //else if (i != null) {
//            HttpPut put = new HttpPut("http://10.111.61.48:8080/FrontRestNoSecurity/rest/species" + i);
//            put.setHeader(HTTP.CONTENT_TYPE, "application/json");
//            put.setHeader("Accept", "application/json");
//       }




    }


}

