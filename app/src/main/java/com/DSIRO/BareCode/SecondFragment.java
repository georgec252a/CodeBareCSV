package com.DSIRO.BareCode;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.DSIRO.BareCode.dataBase_SQLite.ScanareHelper;
import com.DSIRO.BareCode.dataBase_SQLite.ScanareModel;
import com.DSIRO.BareCode.databinding.FragmentSecondBinding;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    private final String sharedPref = "sharedPref";
    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private ArrayAdapter<ScanareModel> adapter;
    private ScanareHelper scanareHelper;
    private ListView myList;
    private ScanareModel scanareModel;
    private SharedPreferences sharedPreferences;
    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        binding = FragmentSecondBinding.inflate(inflater, container, false);
        checkPermissions();


//**************************************************************************************
//**** Afisare inregistrari din DB
// *************************************************************************************

        scanareHelper = new ScanareHelper(getContext());
        afisareInregistrari(scanareHelper);
        binding.lvInregistrari.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ScanareModel userModel = (ScanareModel) adapterView.getItemAtPosition(i);
                scanareHelper.deleteScanare(userModel);
                afisareInregistrari(scanareHelper);
            }
        });

//**************************************************************************************
//**************************************************************************************
//**************************************************************************************

//**************************************************************************************
//**** Functionalitate buton OK
// *************************************************************************************

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanareHelper scanareHelper = new ScanareHelper(getContext());

                checkPermissions();
                //scanareHelper.exportDB(getContext());

                Intent intent = new Intent(getActivity(), MainActivity.class);

//**************************************************************************************
//**** Initializare SharedPreferences
// *************************************************************************************

                sharedPreferences = getActivity().getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
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

                intent.putExtra("fragment_to_load", 1);
                startActivity(intent);
            }
        });

//**************************************************************************************
//**************************************************************************************
//**************************************************************************************

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent intent = new Intent(getContext(), MainActivity.class);
                sharedPreferences = getActivity().getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("cod_scanat_bon_piking", "");
                editor.putString("cod_scanat_eticheta_origine", "");
                editor.putString("cod_uv", "");
                editor.putString("culoare_background", "");
                editor.commit();
                intent.putExtra("fragment_to_load", 1);
                startActivity(intent);


            }

        }, 200);


        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//**************************************************************************************
//**** Blocare functie Back
// *************************************************************************************

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP;
            }
        });

//**************************************************************************************
//**************************************************************************************
//**************************************************************************************


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
            return;
        }
    }

    private void afisareInregistrari(ScanareHelper dataBaseHelper2) {
        adapter = new ArrayAdapter<ScanareModel>(getContext(), android.R.layout.simple_list_item_1, dataBaseHelper2.getScanari());
        binding.lvInregistrari.setAdapter(adapter);
    }

}