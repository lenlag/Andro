package com.example.exo03;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exo03.model.DataManager;
import com.example.exo03.model.InetworkListener;
import com.example.exo03.model.ListThread;
import com.example.exo03.model.Movie;
import com.example.exo03.model.Real;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements InetworkListener {

    //    private DataManager dataManager = new DataManager();
    private Movie[] movies = new Movie[0];

    //construction du handler (qui fait la lisaison entre le thread et le thread principale
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
    private ListView myListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //construction et lancement du thread
        ListThread listThread = new ListThread(this);
        listThread.start();


        myListView = findViewById(R.id.myListView);
        //surcharge de la fonction onItemClick pour pouvoir charger le text de textView avec le titre du film cliqué
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


//                //récupération du Movie cliqué
                Movie movie = (Movie) parent.getAdapter().getItem(position);

//                //récupération du TW et affectation du titre de Movie dans le text du TW
                TextView textView = findViewById(R.id.textView);
                textView.setText(movie.getTitle());


                // pour l'exo 6
                Intent intent = new Intent().setClass(view.getContext(), DetailActivity.class);
                intent.putExtra("SELECTED_MOVIE", movie);
                startActivity(intent);

            }
        });
    }

    //fonction qui prend en entrée du Json et retourne un ArrayList de Movies
    public ArrayList<Movie> jsonTranformer(String ret) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {

            JSONArray jsonArray = new JSONArray(ret);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObj = jsonArray.getJSONObject(i);
                String titreMovie = jsonObj.getString("title");
                String imdbMovie = jsonObj.getString("myImdb");
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
        myListView.setAdapter(adapter);
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


//    @Override
//    public void onBackPressed() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(R.string.titleDialogue).setMessage(R.string.textDialogue);
//        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//
//    }

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