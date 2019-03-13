package com.example.exo03;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.exo03.model.Movie;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final Movie movie = (Movie) getIntent().getSerializableExtra("SELECTED_MOVIE");

        TextView titreValueTV = findViewById(R.id.titreValue);
        TextView nomValueTV = findViewById(R.id.nomRealValue);
        TextView anneeValueTV = findViewById(R.id.anneeValue);

        titreValueTV.setText(movie.getTitle());
        nomValueTV.setText(movie.getReal().getName());
        anneeValueTV.setText(movie.getYear());

        ImageView imageViewTitre = findViewById(R.id.imageViewTitre);
        ImageView imageViewNom = findViewById(R.id.imageViewNom);

        imageViewTitre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://www.imdb.com/title/"+movie.getMyImdb()));
                startActivity(i);
            }
        });

        imageViewNom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://www.imdb.com/name/"+movie.getReal().getImdb()));
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent().setClass(this, MainActivity.class);
        startActivity(intent);
    }
}
