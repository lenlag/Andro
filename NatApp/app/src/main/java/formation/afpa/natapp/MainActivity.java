package formation.afpa.natapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.conn.ClientConnectionManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.DataManager;
import model.InetworkListener;
import model.ListThread;
import model.Movie;
import model.Real;

public class MainActivity extends AppCompatActivity implements InetworkListener {

    //  private DataManager data = new DataManager();
    private Movie[] movies = new Movie[0];

    //construction du handler (qui fait la liaison entre le ListThread et ce thread principal
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //si le thread a renvoyé qq chose
            if (msg.obj != null) {

                //appel de ma super fonction qui transforme du Json en ArrayList
                List<Movie> movieList = jsonTranformer((String) msg.obj);
                //cast de l'arrayList en Movie[]
                movies = movieList.toArray(movies);
                //Appel de ma fonction qui met l'Array Movies[] dans l'adapteur de la ListView
                setMyListViewAdapter(movies);
            }
        }
    };


    private ListView list;

    // public String getTheWSurl() {
    //   String url = PreferenceManager.getDefaultSharedPreferences(this).getString("url", null);
    // return url;
    //}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        list = findViewById(R.id.list);

//        List films = Arrays.asList(data.getMovie());
//
//        ArrayAdapter adapter = new ArrayAdapter<Movie>(this, android.R.layout.simple_list_item_1, films);
//
//        list.setAdapter(adapter);


//surcharge de la fonction onItemClick pour pouvoir charger le text de textView avec le titre du film cliqué

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //récupération du Movie cliqué
                Movie movie = (Movie) parent.getAdapter().getItem(position);

                //pour ex.6
                Intent intent = new Intent().setClass(MainActivity.this, DetailActivity.class);
                intent.putExtra("MY_FILM", movie);
                startActivity(intent);
            }
        });

    }

    //on plaçe le lancement du thread dans le onStart pour qu'à la 1ère saisi de l'URL, on n'a pas à redemarrer l'pppli,
    // pour que le redemarrage se fasse automatiquement après la fermeture de MyPrefFrag activity et re-passage au MainActivity
    @Override
    protected void onStart() {
        super.onStart();
        String myUrl = PreferenceManager.getDefaultSharedPreferences(this).getString("urlGood", null);

        //construction et lancement du thread
        ListThread listThread = new ListThread(this, myUrl );
        listThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //fonction qui prend en entrée du Json et retourne un ArrayList de Movies
    public ArrayList<Movie> jsonTranformer(String ret) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {

            JSONArray jsonArray = new JSONArray(ret);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObj = jsonArray.getJSONObject(i);
                String titreMovie = jsonObj.getString("title");
                String imdbMovie = jsonObj.getString("imdb");
                String year = jsonObj.getString("year");


                JSONObject jsonreal = jsonObj.getJSONObject("real");
                String imdbReal = jsonreal.getString("imdb");
                String nameReal = jsonreal.getString("name");


                Real newReal = new Real(nameReal, imdbReal);
                Movie movie = new Movie(titreMovie, imdbMovie, year, newReal);
                movies.add(movie);
            }

        } catch (Exception JSONException) {

        }
        return movies;
    }

    //affecte le tableau movies[] dans l'adapteur de la ListView
    private void setMyListViewAdapter(Movie[] movies) {
        //configuration de l'adapteur avec le tableau des Movie
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, movies);

        //attribution de l'adapteur
        list.setAdapter(adapter);
    }

    //récup le text Json et fait la lisaison avec le thread
    @Override
    public void setNetAdapter(String msgTxt) {
        Message message = new Message();
        message.obj = msgTxt;

        //l'envoie du msg est capté par le fonction handleMessage (en haut)
        handler.sendMessage(message);
    }

    @Override
    public void setNetException(Exception exception) {
        Toast.makeText(getApplicationContext(), "Error !!", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialogTitle).setMessage(R.string.quit);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.super.onBackPressed(); // ne pas utiliser onDestroy()=> l'appli ne cesse de s'arreter
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dlg = builder.create();
        dlg.show();
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
            Intent myIntent = new Intent().setClass(MainActivity.this, MyPrefFragActivity.class);
            startActivity(myIntent);
        }

        return super.onOptionsItemSelected(item);
    }


}
