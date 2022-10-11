package com.DSIRO.BareCode;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.DSIRO.BareCode.dataBase_SQLite.ScanareHelper;
import com.DSIRO.BareCode.dataBase_SQLite.ScanareModel;

import java.util.ArrayList;
import java.util.List;

public class AfisareInregistrari extends AppCompatActivity {

    private final String sharedPref = "sharedPref";
    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private ScanareHelper scanareHelper;
    private ArrayAdapter<ScanareModel> adapter;
    private ScanareModel scanareModel;
    private Button buttonOk;
    private SharedPreferences sharedPreferences;
    private ListView lv_inregistrari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afisare_inregistrari);

        buttonOk = findViewById(R.id.button_second);


        scanareHelper = new ScanareHelper(AfisareInregistrari.this);
        afisareInregistrari(scanareHelper);
        lv_inregistrari = findViewById(R.id.lv_inregistrari);
        lv_inregistrari.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ScanareModel userModel = (ScanareModel) adapterView.getItemAtPosition(i);
//                scanareHelper.deleteScanare(userModel);
                afisareInregistrari(scanareHelper);
            }
        });


//**************************************************************************************
//**** Functionalitate buton OK
// *************************************************************************************

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanareHelper scanareHelper = new ScanareHelper(AfisareInregistrari.this);

                checkPermissions();
//                scanareHelper.exportDB(AfisareInregistrari.this);
//                Toast.makeText(AfisareInregistrari.this, "S-a realizat exportul in csv!!!!", Toast.LENGTH_LONG).show();
//
                Intent intent = new Intent(AfisareInregistrari.this, MainActivity.class);

//**************************************************************************************
//**** Initializare SharedPreferences
// *************************************************************************************

                sharedPreferences = getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("cod_scanat_bon_piking", " ");
                editor.putString("cod_scanat_eticheta_origine", " ");
                editor.putString("nr_stampila", " ");
                editor.putString("cod_uv", " ");
                editor.putString("culoare_background", " ");
                editor.commit();

//**************************************************************************************
//**************************************************************************************
//**************************************************************************************

                intent.putExtra("fragment_to_load", 4);
                startActivity(intent);
            }
        });

//**************************************************************************************
//**************************************************************************************
//**************************************************************************************

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //**************************************************************************************
//**** Adaugare actiuni pe fiecare item al meniului
// *************************************************************************************
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_export_piking) {
            Toast.makeText(this, "Export CSV", Toast.LENGTH_LONG).show();
            ScanareHelper scanareHelper = new ScanareHelper(getApplicationContext());
            scanareHelper.exportDB(getApplicationContext());
            Toast.makeText(this, "S-a realizat exportul in csv!!!!", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_stergere_piking) {
            Toast.makeText(this, "Stergere Piking", Toast.LENGTH_LONG).show();
            ScanareHelper scanareHelper = new ScanareHelper(getApplicationContext());
            scanareHelper.deleteAll();
            Toast.makeText(this, "S-a realizat stergerea tuturor inregistrarilor!!!!", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_afisareDB) {
            Toast.makeText(this, "Afisare DB", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,
                    AfisareInregistrari.class);
            startActivity(intent);
            return true;

//**************************************************************************************
// Blocare functionare buton back din meniul de sus al aplicatiei
//**************************************************************************************

        }
        if (id == android.R.id.home) {
            Toast.makeText(this, "Back", Toast.LENGTH_LONG).show();
            return true;
        }

//**************************************************************************************
//**************************************************************************************
//**************************************************************************************


        return super.onOptionsItemSelected(item);
    }


//**************************************************************************************
//**************************************************************************************
//**************************************************************************************


    private void afisareInregistrari(ScanareHelper dataBaseHelper2) {
        lv_inregistrari = findViewById(R.id.lv_inregistrari);
        adapter = new ArrayAdapter<ScanareModel>(AfisareInregistrari.this, android.R.layout.simple_list_item_1, dataBaseHelper2.getScanari());
        lv_inregistrari.setAdapter(adapter);
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(AfisareInregistrari.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(AfisareInregistrari.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }
}