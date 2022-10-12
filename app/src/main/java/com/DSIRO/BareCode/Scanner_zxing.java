package com.DSIRO.BareCode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
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
    private TextView messageText, messageFormat, alertaText;
    private String resultScan;
    private boolean isPresent, scanareOK;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_zxing);

        layout = findViewById(R.id.ScannerZxingLayout);
        alertaText = findViewById(R.id.alerta_text);
        alertaText.setVisibility(View.INVISIBLE);

        if (isPresent == false) {

            layout.setBackgroundResource(R.color.green);

        } else if (isPresent) {

            layout.setBackgroundResource(R.color.red);
        }


//**************************************************************************************
//**** Initializare view-uri
// *************************************************************************************

        scanBtn = findViewById(R.id.scan_button);
        okBtn = findViewById(R.id.okButton);
        messageText = findViewById(R.id.scan_content);
        messageFormat = findViewById(R.id.scan_format);

        scanBtn.setOnClickListener(this);
        okBtn.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();

        layout = findViewById(R.id.ScannerZxingLayout);
        alertaText = findViewById(R.id.alerta_text);

        if (scanareOK) {
            if (isPresent == false) {

                layout.setBackgroundResource(R.color.red);
                alertaText.setVisibility(View.VISIBLE);
                alertaText.setText("NU SE TRIMITE");

            } else if (isPresent) {

                layout.setBackgroundResource(R.color.green);
                alertaText.setVisibility(View.VISIBLE);
                alertaText.setText("DE TRIMIS LA ATELIER!!!");
            }
        } else {
            layout.setBackgroundResource(R.color.white);
            alertaText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

//**************************************************************************************
//**** Functionalitati butoane OK si Scan
// *************************************************************************************

        switch (v.getId()) {


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
                if (intentResult.getContents().length() > 31) {
                    resultScan = intentResult.getContents().substring(16, 32);


                    String fisierStr = "/sdcard/Download/Dacia/BareCode/DB_codes.txt";
                    String deCautat = resultScan;

                    if (isCodeinCSV(fisierStr, deCautat)) {
                        isPresent = true;


                    } else {
                        isPresent = false;

                    }
                    scanareOK = true;
                } else {
                    resultScan = intentResult.getContents();
                    scanareOK = false;
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


//**************************************************************************************
//**************************************************************************************
//**************************************************************************************


    public boolean isCodeinCSV(String fileName, String deCautat) {
        List<String[]> resultList = new ArrayList<String[]>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String csvLine = null;
        while (true) {
            try {
                if (!((csvLine = reader.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] row = csvLine.split(",");
            // resultList.add(row);

            String rex = deCautat;
            for (int col = 0; col < row.length; col++) {

                if (row[col].contains(rex)) {
                    resultList.add(row);
                    return true;
                }
            }
        }


        return false;
    }

}