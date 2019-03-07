package formation.afpa.natapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import model.Movie;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      final Movie film = (Movie)getIntent().getSerializableExtra("MY_FILM");

        TextView titre = findViewById(R.id.textView);
        titre.setText(film.getTitle());

        TextView realisateur = findViewById(R.id.textView2);
        realisateur.setText(film.getReal().getName());

        ImageView image1 = findViewById(R.id.imageView);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.imdb.com/title/" + film.getImdb()));
                startActivity(intent);
            }
        });

        ImageButton image2 = findViewById(R.id.imageButton3);
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.imdb.com/name/" + film.getReal().getImdb()));
                startActivity(intent);
            }
        });
    }

}
