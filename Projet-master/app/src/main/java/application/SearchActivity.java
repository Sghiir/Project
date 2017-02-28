package application;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.pc.maptest.R;

public class SearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_Search);

        Button bt = (Button) findViewById(R.id.btR);
        final SearchView shV= (SearchView) findViewById(R.id.Sr);
        final String geofilter= shV.getQuery().toString();
        bt.setBackgroundColor(Color.GREEN);
        Button btStation=(Button) findViewById(R.id.BtStation);
        btStation.setBackgroundColor(Color.GREEN);

        final SearchView srStation= (SearchView)findViewById(R.id.SrStation);
        final String station= srStation.toString();
        final EditText edit=(EditText)findViewById(R.id.editText2);
        final EditText editstation=(EditText)findViewById(R.id.editText3);


        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this,MapsActivity.class);
                intent.putExtra("Geofilter_distance",edit.getText().toString());
                startActivity(intent);
                // Toast.makeText(Main2Activity.this, shV.getQuery().toString(), Toast.LENGTH_LONG).show();
            }
        });

        btStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this,SearchActivity.class);
                intent.putExtra("Nom_Station",editstation.getText().toString());
                startActivity(intent);
            }
        });
    }
}
