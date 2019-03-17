package formation.afpa.natspecies;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import formation.afpa.natspecies.model.InetworkListener;
import formation.afpa.natspecies.model.ListSpeciesThread;
import formation.afpa.natspecies.model.Specie;

public class MainActivity extends AppCompatActivity implements InetworkListener {

    private List<Specie> specielist;
    private ListView listViewSpecies;

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////HANDLER/////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////

    //construction du handler (qui fait la lisaison entre le thread et le thread principal
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //si le thread a renvoyé qq chose
            if (msg.obj != null) {

                //appel de ma super fonction qui transforme du Json en ArrayList
                specielist = jsonTranformer((String) msg.obj);

                ArrayAdapter myAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, specielist);
                listViewSpecies.setAdapter(myAdapter);
            }

        }
    };

    ////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////Méthodes d'InteworkListener///////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////

    //if msg OK
    @Override
    public void setNetAdapter(String msgText) {
        Message message = new Message();
        message.obj = msgText;

        //l'envoie du msg est capté par le fonction handleMessage (en haut)
        handler.sendMessage(message);

    }

    //if msg error!
    @Override
    public void setNetException(Exception exception) {
        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //construction et lancement du thread
        ListSpeciesThread listThread = new ListSpeciesThread(this);
        listThread.start();


        listViewSpecies = findViewById(R.id.listView);

        //surcharge de la fonction onItemClick pour pouvoir charger le text de textView avec le common et latin name de Specie cliquée
        listViewSpecies.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Specie sp = (Specie) parent.getAdapter().getItem(position);

                Intent intent = new Intent().setClass(MainActivity.this, CreateOrUpdateActivity.class);
                intent.putExtra("SPECIE_SELECTED", sp); //Specie implements Serializable

                startActivity(intent);
            }
        });


        FloatingActionButton add = findViewById(R.id.addActionButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(MainActivity.this, CreateOrUpdateActivity.class);
               // startActivity(intent);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                ListSpeciesThread listThread = new ListSpeciesThread(this);
                listThread.start();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //fonction qui prend en entrée du Json et retourne un ArrayList de Movies
    public ArrayList<Specie> jsonTranformer(String ret) {
        ArrayList<Specie> species = new ArrayList<>();
        try {

            JSONArray jsonArray = new JSONArray(ret);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                String commonName = jsonObj.getString("commonName");
                String latinName = jsonObj.getString("latinName");
                int id = jsonObj.getInt("id");

                Specie newSpecie = new Specie(id, commonName, latinName);
                specielist.add(newSpecie);
            }
        } catch (Exception JSONException) {

        }
        return species;
    }




}
