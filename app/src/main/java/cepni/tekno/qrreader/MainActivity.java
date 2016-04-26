package cepni.tekno.qrreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView)findViewById(R.id.listView);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new ArrayList<String>());

        listView.setAdapter(adapter);
        controlAdapterIsEmpty();
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if ((scanResult != null) && (scanResult.getContents() != null)) {
            String data = scanResult.getContents();
            adapter.add(data);
            adapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this,R.string.toast_read,Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this,R.string.toast_notread,Toast.LENGTH_LONG).show();
        }
        controlAdapterIsEmpty();
    }

    /*
    * Adapter control. Write message if is empty.
    * */
    public void controlAdapterIsEmpty()
    {
        if(adapter.isEmpty())
            adapter.add(getString(R.string.adapter_empty));
        else
            adapter.remove(getString(R.string.adapter_empty));

        adapter.notifyDataSetChanged();
    }
}
