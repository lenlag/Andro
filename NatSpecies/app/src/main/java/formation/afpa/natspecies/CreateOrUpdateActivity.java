package formation.afpa.natspecies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import formation.afpa.natspecies.model.AddModifySpecieThread;
import formation.afpa.natspecies.model.DeleteSpecieThread;
import formation.afpa.natspecies.model.InetworkListener;
import formation.afpa.natspecies.model.Specie;

public class CreateOrUpdateActivity extends AppCompatActivity implements InetworkListener {
    String myCommon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_update);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Specie sp = (Specie)getIntent().getSerializableExtra("SPECIE_SELECTED");

        final TextInputEditText commonName = findViewById(R.id.textInputCommonName);
        final TextInputEditText latinName = findViewById(R.id.textInputLatinName);

        if(sp != null) {

            commonName.setText(sp.getCommonName());

            latinName.setText(sp.getLatinName());
        }

        final String myCommon = commonName.getText().toString();
        final String myLatin = latinName.getText().toString();

        FloatingActionButton add = findViewById(R.id.addActionButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddModifySpecieThread thread = new AddModifySpecieThread(CreateOrUpdateActivity.this, myCommon, myLatin, sp.getId());
                thread.start();
            }
        });



        FloatingActionButton delete = findViewById(R.id.deleteActionButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteSpecieThread thread = new DeleteSpecieThread(CreateOrUpdateActivity.this, sp.getId());
                thread.start();
            }
        });
    }

    @Override
    public void setNetAdapter(String ret) {
        Message message = new Message();
        message.obj = ret;

        //l'envoie du msg est capté par le fonction handleMessage (en haut)
        handler.sendMessage(message);
    }

    @Override
    public void setNetException(Exception exception) {
        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
    }

    //construction du handler (qui fait la lisaison entre le thread et le thread principal
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //si le thread a renvoyé qq chose
            if (msg.obj != null) {
               Intent returnIntent = new Intent ();
               setResult(Activity.RESULT_OK, returnIntent);

            }

        }
    };


    @Override
    public void onBackPressed() {
        Intent intent = new Intent().setClass(this, MainActivity.class);
        startActivity(intent);
    }
}
