package formation.afpa.natspecies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import formation.afpa.natspecies.model.Specie;

public class CreateOrUpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_update);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Specie sp = (Specie)getIntent().getSerializableExtra("SPECIE_SELECTED");

        TextView common = findViewById(R.id.textView3);
        common.setText(sp.getCommonName());

        TextView latin = findViewById(R.id.textView4);
        latin.setText(sp.getLatinName());
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent().setClass(this, MainActivity.class);
        startActivity(intent);
    }
}
