package com.example.exo03;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
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

import com.example.exo03.model.InetworkListener;
import com.example.exo03.model.ListThread;
import com.example.exo03.model.Movie;
import com.example.exo03.model.Real;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements InetworkListener, SensorEventListener {

    //    private DataManager dataManager = new DataManager();
    private Movie[] movies = new Movie[0];

    //construction du handler (qui fait la lisaison entre le thread et le thread principale
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //si le thread a renvoyer qq chose
            if (msg.obj != null) {

                //appel de ma super fonction qui transforme du Json en ArrayList
                List<Movie> movieList = jsonTranformer((String) msg.obj);
                //cast de l'arrayList en Movie[]
                movies = movieList.toArray(movies);
                //Appel de ma fonction qui met l'Array Movies[] dans l'adapteur de la ListView
                setMyListViewAdapter();
            }
        }
    };
    private ListView myListView;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor proximitySensor;
    private Sensor magSensor;
    private ConstraintLayout layout;
    private TextView tvValDist;
    private TextView tvValAcc;
    private TextView tvValMag;
    private TextView tvColorMag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layout = findViewById(R.id.constraintLayoutMain);
        myListView = findViewById(R.id.myListView);
        tvValDist = findViewById(R.id.tvValDist);
        tvValAcc = findViewById(R.id.tvValAcc);
        tvValMag = findViewById(R.id.tvValMag);
        tvColorMag = findViewById(R.id.tvColorMag);
        //surcharge de la fonction onItemClick pour pouvoir charger le text de textView avec le titre du film cliqué
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


//                //récupération du Movie cliqué
                Movie movie = (Movie) parent.getAdapter().getItem(position);



                // pour l'exo 6
                Intent intent = new Intent().setClass(view.getContext(), DetailActivity.class);
                intent.putExtra("SELECTED_MOVIE", movie);
                startActivity(intent);

            }
        });


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);



        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        tvValAcc.setText(sensorManager.get);


        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (proximitySensor == null) {
            Toast.makeText(this, "No Proximity Sensor!", Toast.LENGTH_LONG).show();
        } else {
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magSensor == null) {
            Toast.makeText(this, "No MAG Sensor!", Toast.LENGTH_LONG).show();
        } else {
            sensorManager.registerListener(this, magSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        startThreadList();
    }


    public void startThreadList() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String url = preferences.getString("urlJson", null);

        //construction et lancement du thread
        ListThread listThread = new ListThread(this, url);
        listThread.start();
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
    public void setMyListViewAdapter() {
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

        //l'envois du msg est capter par le fonction handleMessage (en haut)
        handler.sendMessage(message);
    }

    @Override
    public void setNetException(Exception exception) {
        Toast.makeText(getApplicationContext(), "Error !!", Toast.LENGTH_LONG).show();
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
            Intent i = new Intent(this, MySettingsActivity.class);
            startActivity(i);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        // unregister the sensor (désenregistrer le capteur)
        sensorManager.unregisterListener(this, proximitySensor);
        sensorManager.unregisterListener(this, magSensor);
        sensorManager.unregisterListener(this, accelerometer);
        super.onPause();
    }

    @Override
    protected void onResume() {
        /* Ce qu'en dit Google&#160;dans le cas de l'accéléromètre :
         * «&#160; Ce n'est pas nécessaire d'avoir les évènements des capteurs à un rythme trop rapide.
         * En utilisant un rythme moins rapide (SENSOR_DELAY_UI), nous obtenons un filtre
         * automatique de bas-niveau qui "extrait" la gravité  de l'accélération.
         * Un autre bénéfice étant que l'on utilise moins d'énergie et de CPU.&#160;»
         */
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magSensor, SensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }

    private Float accelationSquareRoot = -1f;
    private Float distance = -1f;
    private Double magneticStrenght = -1d;

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Récupérer les valeurs du capteur
        float xAcc, yAcc, zAcc, xMag, yMag, zMag;

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xAcc = event.values[0];
            yAcc = event.values[1];
            zAcc = event.values[2];

            if (distance != 0) {

                accelationSquareRoot = ((xAcc * xAcc + yAcc * yAcc + zAcc * zAcc) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH));
                if (accelationSquareRoot > 1.3) {
                    layout.setBackgroundResource(R.color.sensor_background_two);
                } else if (accelationSquareRoot < 0.7) {
                    layout.setBackgroundResource(R.color.sensor_background_three);
                } else {
                    layout.setBackgroundResource(R.color.sensor_background_four);
                }
            }

            tvValAcc.setText(accelationSquareRoot.toString());

        }


        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            distance = event.values[0];

            if (distance > 1) {
                layout.setBackgroundResource(R.color.sensor_background_four);
            } else {
                layout.setBackgroundResource(R.color.sensor_background_one);
            }

            tvValDist.setText(distance.toString());
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            // Valeur du vecteur du champ magnétique (xAcc,yAcc,zAcc)
            xMag = event.values[0];
            yMag = event.values[1];
            zMag = event.values[2];
            // Valeur de la norme de ce vecteur
            magneticStrenght = Math.sqrt((double) (xMag * xMag + yMag * yMag + zMag * zMag));
            tvValMag.setText(magneticStrenght.toString());

            if(magneticStrenght > 0d && magneticStrenght <= 50d){

                tvColorMag.setBackgroundResource(R.color.sensor_tv_background_one);

            }else if(magneticStrenght > 50d && magneticStrenght <= 100d){
                tvColorMag.setBackgroundResource(R.color.sensor_tv_background_two);


            }else if(magneticStrenght > 100d && magneticStrenght <= 150d){
                tvColorMag.setBackgroundResource(R.color.sensor_tv_background_three);


            }else if(magneticStrenght > 150d && magneticStrenght <= 200d){
                tvColorMag.setBackgroundResource(R.color.sensor_tv_background_four);


            }else if(magneticStrenght > 200d && magneticStrenght <= 250d){
                tvColorMag.setBackgroundResource(R.color.sensor_tv_background_five);


            }else if(magneticStrenght > 250d && magneticStrenght <= 300d){
                tvColorMag.setBackgroundResource(R.color.sensor_tv_background_six);


            }else{
                tvColorMag.setBackgroundResource(R.color.sensor_tv_background_seven);

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
}