package com.DSIRO.BareCode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scanner_zxing extends AppCompatActivity implements View.OnClickListener {


    private static String cod_scanat_bon_piking;
    private static String cod_scanat_eticheta_origine;
    private Button scanBtn;
    private Button okBtn;
    private TextView messageText, messageFormat;
    private String resultScan;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_zxing);


//**************************************************************************************
//**** Initializare view-uri
// *************************************************************************************

        scanBtn = findViewById(R.id.scan_button);
        okBtn = findViewById(R.id.okButton);
        messageText = findViewById(R.id.scan_content);
        messageFormat = findViewById(R.id.scan_format);

        scanBtn.setOnClickListener(this);
        okBtn.setOnClickListener(this);


//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//
//
//
//                if(resultScan==null) {
//
//                    IntentIntegrator intentIntegrator = new IntentIntegrator(Scanner_zxing.this);
//                    intentIntegrator.setPrompt("Scan a barcode or QR Code");
//                    intentIntegrator.setOrientationLocked(true);
//                    intentIntegrator.initiateScan();
//                }else {
//                    okBtn.performClick();
//
//                }
//
//
//            }
//
//        }, 500);


//**************************************************************************************
//**************************************************************************************
//**************************************************************************************

    }

    @Override
    public void onClick(View v) {

//**************************************************************************************
//**** Functionalitati butoane OK si Scan
// *************************************************************************************

        switch (v.getId()) {

            case R.id.okButton: {

                if (getIntent().getExtras() != null) {
                    int intentFragment = getIntent().getExtras().getInt("fragment_origin");
                    Fragment fragment;
                    Bundle args = new Bundle();
                    FragmentManager fManager = getSupportFragmentManager();
                    FragmentTransaction fTransaction = fManager.beginTransaction();
                    Intent intent;

                    switch (intentFragment) {
                        case 100:
                            intentFragment = 0;
                            intent = new Intent(Scanner_zxing.this, MainActivity.class);
                            intent.putExtra("fragment_to_load", 1);
                            startActivity(intent);
                            pref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
                            editor = pref.edit();
                            if (resultScan != null) {
                                Scanner_zxing.cod_scanat_bon_piking = resultScan;
                                editor.putString("cod_scanat_bon_piking", resultScan);
                                editor.putString("culoare_background", "none");
                                editor.commit();


                            }
                            break;
                        case 200:
                            intentFragment = 0;
                            intent = new Intent(Scanner_zxing.this, MainActivity.class);
                            intent.putExtra("fragment_to_load", 2);
                            startActivity(intent);
                            pref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
                            editor = pref.edit();
                            if (resultScan != null) {
                                Scanner_zxing.cod_scanat_eticheta_origine = resultScan;
                                if (cod_scanat_eticheta_origine.equals(cod_scanat_bon_piking)) {
                                    editor.putString("culoare_background", "green");
                                } else if (!cod_scanat_eticheta_origine.equals("")) {
                                    editor.putString("culoare_background", "red");
                                } else if (cod_scanat_eticheta_origine.equals("")) {
                                    editor.putString("culoare_background", "none");
                                }
                                if (resultScan.charAt(0) == 'P') {
                                    editor.putString("cod_scanat_eticheta_origine", resultScan);
                                    editor.putString("cod_uv", resultScan.substring(11));
                                } else {
                                    editor.putString("cod_scanat_eticheta_origine", resultScan);
                                    editor.putString("cod_uv", resultScan.substring(10));
                                }
                                editor.commit();
                            }
                            break;
                    }
                    break;
                }
            }

            case R.id.scan_button: {
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.setPrompt("Scan a barcode or QR Code");
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.initiateScan();
                break;
            }

        }

//**************************************************************************************
//**************************************************************************************
//**************************************************************************************

    }


//**************************************************************************************
//**** Rezultat cititr cod de bare
// *************************************************************************************

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                messageText.setText(intentResult.getContents());
                messageFormat.setText(intentResult.getFormatName());
                resultScan = intentResult.getContents();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



//**************************************************************************************
//**************************************************************************************
//**************************************************************************************

}