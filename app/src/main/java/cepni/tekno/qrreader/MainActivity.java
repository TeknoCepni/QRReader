package cepni.tekno.qrreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ArrayList<String> addedHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView)findViewById(R.id.listView);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new ArrayList<String>());
        addedHistory = new ArrayList<String>();
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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.remove(adapter.getItem(position));
                addedHistory.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this,R.string.qr_remove,Toast.LENGTH_SHORT).show();
                controlAdapterIsEmpty();
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,addedHistory.get(position),Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if ((scanResult != null) && (scanResult.getContents() != null)) {
            String data = scanResult.getContents();
            adapter.add(data);
            addHistory();
            adapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this,R.string.toast_read,Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this,R.string.toast_notread,Toast.LENGTH_SHORT).show();
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

    /*
    * Add history when qr code read.
    * */
    public void addHistory()
    {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss");
        addedHistory.add(getString(R.string.qr_added_history)+" "+sdf.format(new Date()));
    }
}
