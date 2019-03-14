package formation.afpa.natspecies;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import formation.afpa.natspecies.model.InetworkListener;
import formation.afpa.natspecies.model.ListSpeciesThread;
import formation.afpa.natspecies.model.Specie;

public class MainActivity extends AppCompatActivity implements InetworkListener {

    private List<Specie> specielist = new ArrayList<Specie>();

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////HANDLER/////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////

    //construction du handler (qui fait la lisaison entre le thread et le thread principal
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //si le thread a renvoyé qq chose
            if(msg.obj != null) {
                super.handleMessage(msg);
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

//        TextView welcomeText = findViewById(R.id.textViewWelcome);
//        TextView textSpecies = findViewById(R.id.textView2);


        ListView listSpecies = findViewById(R.id.listView);

        //surcharge de la fonction onItemClick pour pouvoir charger le text de textView avec le common et latin name de Specie cliquée
        listSpecies.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Specie sp = (Specie) parent.getAdapter().getItem(position);

                Intent intent = new Intent().setClass(MainActivity.this, CreateOrUpdateActivity.class);
                intent.putExtra("SPECIE_SELECTED", sp); //Specie implements Serializable

                startActivity(intent);
            }
        });

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


}
