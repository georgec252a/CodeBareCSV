package com.DSIRO.BareCode;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.DSIRO.BareCode.dataBase_SQLite.ScanareHelper;
import com.DSIRO.BareCode.databinding.ActivityMainBinding;
import com.DSIRO.BareCode.ui.login.LoginFragment;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_WRITE_STORAGE = 1;
    private static final String TAG = "MainActivity";
    private static boolean WRITE_STORAGE = false;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private final int EXTERNAL_STORAGE_PERMISSION_CODE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//**************************************************************************************
//**** Navigare intre Fragmentele din MainActivity
// *************************************************************************************

        if (getIntent().getExtras() != null) {
            int intentFragment = getIntent().getExtras().getInt("fragment_to_load");
            Fragment fragment;
            Bundle args = new Bundle();
            FragmentManager fManager = getSupportFragmentManager();
            FragmentTransaction fTransaction = fManager.beginTransaction();
            Log.d("FirstScanFragment", "onCreate: " + intentFragment);
            switch (intentFragment) {
                case 1:
                    // Incarca Fragmentul - FirstScanFragment -
                    fragment = new FirstScanFragment();
                    fragment.setArguments(args);
                    fTransaction.replace(R.id.container, fragment,
                            "FTAG").commit();
                    break;
                case 2:
                    // Incarca Fragmentul - FirstFragment -
                    fragment = new FirstFragment();
                    fragment.setArguments(args);
                    fTransaction.replace(R.id.container, fragment,
                            "FTAG").commit();
                    break;
                case 3:
                    // Incarca Fragmentul - SecondFragment -
                    fragment = new SecondFragment();
                    fragment.setArguments(args);
                    fTransaction.replace(R.id.container, fragment,
                            "FTAG").commit();
                    break;
                case 4:
                    // Incarca Fragmentul - LoginFragment -
                    fragment = new LoginFragment();
                    fragment.setArguments(args);
                    fTransaction.replace(R.id.container, fragment,
                            "FTAG").commit();
                    break;

            }
        }

//**************************************************************************************
//**************************************************************************************
//**************************************************************************************


//**************************************************************************************
//**** Adaugare Meniu Contextual pe fiecare pagina a aplicatiei
// *************************************************************************************

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

//**************************************************************************************
//**************************************************************************************
//**************************************************************************************


//**************************************************************************************
//**** Adaugare Meniu Email Send to
// *************************************************************************************

        binding.floatingEmailAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Trimite email catre dezvoltator...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, EmailActivity.class);
                startActivity(intent);
            }
        });

//**************************************************************************************
//**************************************************************************************
//**************************************************************************************

    }


//**************************************************************************************
//**** Adaugare layout menu_main la obiectul menu
// *************************************************************************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

//**************************************************************************************
//**************************************************************************************
//**************************************************************************************


//**************************************************************************************
//**** Adaugare actiuni pe fiecare item al meniului
// *************************************************************************************

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_export_piking) {
            Toast.makeText(this, "Export CSV", Toast.LENGTH_LONG).show();
            ScanareHelper scanareHelper = new ScanareHelper(getApplicationContext());

            int hasWritePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            Log.d(TAG, "onCreate: checkSelfPermission= " + hasWritePermission);

            if (hasWritePermission == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onCreate:  permission granted!");
                WRITE_STORAGE = true;
            } else {
                Log.d(TAG, "onCreate: requesting permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_STORAGE);

            }

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


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult: starts");
        switch (requestCode) {
            case REQUEST_CODE_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    WRITE_STORAGE = true;
                } else {
                    //permission denied
                    Log.d(TAG, "onRequestPermissionsResult: permission denied");
                    finish();
                    System.exit(0);
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: ended");
    }


}