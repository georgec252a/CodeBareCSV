package com.DSIRO.BareCode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.DSIRO.BareCode.dataBase_SQLite.ScanareHelper;
import com.DSIRO.BareCode.dataBase_SQLite.ScanareModel;
import com.DSIRO.BareCode.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {


    private static int second_scan;
    private final String sharedPref = "sharedPref";
    String nr_stampila;
    PopupWindow popUp;
    LinearLayout layout;
    EditText tv;
    boolean click = true;
    int intUv;
    private FragmentFirstBinding binding;
    private SharedPreferences sharedPreferences;
    private int id;
    private String cod_scanat_bon_piking;
    private String cod_scanat_eticheta_origine;
    private String cod_uv;
    private String culoare_background;
    private ScanareModel scanareModel;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String resultScan;
    private MediaPlayer mp;



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {




        binding = FragmentFirstBinding.inflate(inflater, container, false);

//**************************************************************************************
//**** Preluare din SharedPreferences a variabilelor shared
// *************************************************************************************

        sharedPreferences = getActivity().getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
        nr_stampila = sharedPreferences.getString("nr_stampila", "");
        cod_scanat_eticheta_origine = sharedPreferences.getString("cod_scanat_eticheta_origine", "Not Found!!");
//        cod_scanat_eticheta_origine="222";
        cod_scanat_bon_piking = sharedPreferences.getString("cod_scanat_bon_piking", "Not Found!!");
        cod_uv = sharedPreferences.getString("cod_uv", "Not Found!!");
        culoare_background = sharedPreferences.getString("culoare_background", "Not Found!!");
        binding.tvUser.setText(nr_stampila);
        binding.tvCodEticheta.setText(cod_scanat_eticheta_origine, TextView.BufferType.EDITABLE);
        binding.etBonPiking.setText(cod_scanat_bon_piking);
        binding.tvCodEticheta.setText(cod_scanat_eticheta_origine, TextView.BufferType.EDITABLE);
        binding.etUv.setText(cod_uv, TextView.BufferType.EDITABLE);


        if (culoare_background.equals("green")) {
            binding.constraintLayoutFirstFragment.setBackgroundResource(R.color.green);
            binding.tvCodEticheta.setBackgroundColor(getResources().getColor(R.color.gray));
        } else if (culoare_background.equals("red")) {
            binding.constraintLayoutFirstFragment.setBackgroundResource(R.color.red);




            mp = MediaPlayer.create(getContext(), R.raw.eroare_s1);


            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                }
            });








            binding.etUv.setText("", TextView.BufferType.EDITABLE);
            Log.d("ERR", "onCreateView: ERR");

        } else {
            binding.constraintLayoutFirstFragment.setBackgroundResource(R.color.background_color);
        }


        try {

            if (binding.etUv.getText().toString().length() < 5) {
                intUv = Integer.parseInt(binding.etUv.getText().toString());
            } else {
                intUv = -1;
            }

            binding.etUv.setBackgroundColor(getResources().getColor(R.color.orange));
            Log.d("Test -E", "onCreateView: intUv:"+ intUv);
            if ((!culoare_background.equals("red")) && (intUv > 1)) {

                mp = MediaPlayer.create(getContext(), R.raw.uv_mai_mare_unu);

                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }


                });
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.reset();
                    }
                });



            }
        } catch (NumberFormatException e) {
            binding.etUv.setBackgroundColor(getResources().getColor(R.color.gray));
        }


//**************************************************************************************
//**************************************************************************************
//**************************************************************************************


// **************************************************************************************
//**** Realizare scanare cu Cititor cod Bare - Zebra TC51
// *************************************************************************************

//        binding.etCodReperScanat.requestFocus();
//        binding.etCodReperScanat.setFocusable(true);
//        binding.etCodReperScanat.setFocusableInTouchMode(true);


        binding.etCodEtichetaOrigine.setText("", TextView.BufferType.EDITABLE);

        binding.etCodEtichetaOrigine.isFocusableInTouchMode();
        binding.etCodEtichetaOrigine.isFocusable();
        binding.etCodEtichetaOrigine.requestFocus();

        binding.etCodEtichetaOrigine.requestFocus();

        binding.etCodEtichetaOrigine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {

                    if (binding.etUv.getText().toString().length() < 5) {
                        intUv = Integer.parseInt(binding.etUv.getText().toString());
                    } else {
                        intUv = -1;
                    }
                }catch(NumberFormatException e){
                    binding.etUv.setBackgroundColor(getResources().getColor(R.color.gray));
                }

                if (binding.etCodEtichetaOrigine.getText().toString().length() > 9) {

                    pref = getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
                    editor = pref.edit();


                    if (binding.etCodEtichetaOrigine.getText().toString().substring(0, 10).equals(cod_scanat_bon_piking) && binding.etCodEtichetaOrigine.getText().toString().substring(10).length()<5) {
                        Log.d("Test", "afterTextChanged: " + binding.etCodEtichetaOrigine.getText().toString());
                        Log.d("Test", "afterTextChanged: " + cod_scanat_bon_piking);
                        Log.d("Test", "afterTextChanged: " + binding.etCodEtichetaOrigine.getText().toString().substring(10).length());
                        binding.constraintLayoutFirstFragment.setBackgroundResource(R.color.green);
                        binding.tvCodEticheta.setBackgroundColor(getResources().getColor(R.color.gray));
                        editor.putString("culoare_background", "green");
                    } else if (!binding.etCodEtichetaOrigine.getText().toString().substring(0, 10).equals(cod_scanat_bon_piking)||intUv==-1 || binding.etCodEtichetaOrigine.getText().toString().substring(10).length()>5 ) {
                        binding.constraintLayoutFirstFragment.setBackgroundResource(R.color.red);
                        culoare_background="red";
                        Log.d("Test", "afterTextChanged: " + binding.etCodEtichetaOrigine.getText().toString());

                        Log.d("Test", "afterTextChanged: " + cod_scanat_bon_piking);
                        editor.putString("culoare_background", "red");
//                        MediaPlayer mp;
//                        mp = MediaPlayer.create(getContext(), R.raw.eroare_s1);
//                        mp.start();

                    } else if (cod_scanat_eticheta_origine.equals("")) {

                    }

                    editor.putString("cod_scanat_eticheta_origine", binding.etCodEtichetaOrigine.getText().toString().substring(0, 10));
                    editor.putString("cod_uv", binding.etCodEtichetaOrigine.getText().toString().substring(10));


                    if (binding.etCodEtichetaOrigine.getText().toString().charAt(0) == 'P') {
                        editor.putString("cod_scanat_eticheta_origine", binding.etCodEtichetaOrigine.getText().toString().substring(1, 11));
                        editor.putString("cod_uv", binding.etCodEtichetaOrigine.getText().toString().substring(11));
                        cod_scanat_eticheta_origine = binding.etCodEtichetaOrigine.getText().toString().substring(1, 11);
                        cod_uv = binding.etCodEtichetaOrigine.getText().toString().substring(11);
                        editor.commit();
                        //binding.etCodEtichetaOrigine.setText(binding.etCodEtichetaOrigine.getText().toString().substring(1, 11));
                        //binding.etUv.setText(binding.etCodEtichetaOrigine.getText().toString().substring(11));
                    } else {
                        editor.putString("cod_scanat_eticheta_origine", binding.etCodEtichetaOrigine.getText().toString().substring(0, 10));
                        editor.putString("cod_uv", binding.etCodEtichetaOrigine.getText().toString().substring(10));
                        cod_scanat_eticheta_origine = binding.etCodEtichetaOrigine.getText().toString().substring(0, 10);
                        cod_uv = binding.etCodEtichetaOrigine.getText().toString().substring(10);
                        editor.commit();
                        Log.d("Test", "afterTextChanged: " + binding.etCodEtichetaOrigine.getText().toString().substring(0, 10));
                        //binding.etCodEtichetaOrigine.setText(binding.etCodEtichetaOrigine.getText().toString().substring(0, 10));
                        //binding.etUv.setText(binding.etCodEtichetaOrigine.getText().toString().substring(10));
                    }
                    editor.commit();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("fragment_to_load", 2);
                    startActivity(intent);


                }


//                binding.buttonSalvare.performClick();
            }
        });


//**************************************************************************************
//**************************************************************************************
//**************************************************************************************

//**************************************************************************************
//**** Functionalitate buton Inchidere
// *************************************************************************************

        binding.inchidere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (nr_stampila != null) {
                    Toast.makeText(getContext(), "Utilizator " + nr_stampila + " deconectat", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Aplicatie Reinitializata!", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });

//**************************************************************************************
//**************************************************************************************
//**************************************************************************************


//**************************************************************************************
//**** Functionalitate buton Salvare
// *************************************************************************************

        binding.buttonSalvare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                salvareInDB();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("fragment_to_load", 3);
                startActivity(intent);



//                 Intent intent = new Intent(getActivity(), SaveSuccess.class);
//
//                 startActivity(intent);


            }
        });

//**************************************************************************************
//**************************************************************************************
//**************************************************************************************


//**************************************************************************************
//**** Functionalitate TextView tvCodEticheta
// *************************************************************************************

        binding.tvCodEticheta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                second_scan = 1;
                Log.d("On Click", String.valueOf(second_scan));

                Intent intent = new Intent(getActivity(), Scanner_zxing.class);
                intent.putExtra("fragment_origin", 200);
                startActivity(intent);

            }
        });

//**************************************************************************************
//**************************************************************************************
//**************************************************************************************


//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//
//
//
//                if(cod_scanat_eticheta_origine=="") {
//                    sharedPreferences = getActivity().getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("cod_uv", "");
//                    editor.putString("culoare_background", "");
//                    editor.commit();
//                    Intent intent = new Intent(getContext(), Scanner_zxing.class);
//                    intent.putExtra("fragment_origin", 200);
//                    startActivity(intent);
//                }else {
//
//                    Intent intent = new Intent(getContext(), MainActivity.class);
//                    intent.putExtra("fragment_to_load", 3);
//                    startActivity(intent);
//                }
//
//
//            }
//
//        }, 500);

if(intUv>1) {
    new Handler().postDelayed(new Runnable() {

        @Override
        public void run() {
            Log.d("TEST", "run: |" + cod_scanat_eticheta_origine + "|");
            Log.d("TEST", "run: |" + intUv + "|");
            if (intUv >= 0 && cod_scanat_eticheta_origine.compareToIgnoreCase("") != 0 && !culoare_background.equals("red") && intUv != -1) {
                Log.d("TEST", "run: In IF");
                salvareInDB();

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("fragment_to_load", 3);
                sharedPreferences = getActivity().getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("cod_uv", "");
                editor.putString("culoare_background", "");
                intUv = -1;

                editor.commit();
                startActivity(intent);
            }


        }

    }, 5000);
}
else{
    new Handler().postDelayed(new Runnable() {

        @Override
        public void run() {
            Log.d("TEST", "run: |" + cod_scanat_eticheta_origine + "|");
            Log.d("TEST", "run: |" + intUv + "|");
            if (intUv >= 0 && cod_scanat_eticheta_origine.compareToIgnoreCase("") != 0 && !culoare_background.equals("red") && intUv != -1) {
                Log.d("TEST", "run: In IF");
                salvareInDB();

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("fragment_to_load", 3);
                sharedPreferences = getActivity().getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("cod_uv", "");
                editor.putString("culoare_background", "");
                intUv = -1;

                editor.commit();
                startActivity(intent);
            }


        }

    }, 500);

}

        return binding.getRoot();

    }

    private void salvareInDB() {
        try {
            Time today = new Time(Time.getCurrentTimezone());
            today.setToNow();
            scanareModel = new ScanareModel(id, nr_stampila, cod_scanat_bon_piking, cod_scanat_eticheta_origine, cod_uv, today.monthDay + "_" + (today.month + 1) + "_" + today.year, today.format("%k:%M:%S") + "");
            Toast.makeText(getActivity(), scanareModel.toString(), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            scanareModel = new ScanareModel(1, "error", "error", "error", "error", "error", "error");
            Toast.makeText(getActivity(), "Nu exista date!", Toast.LENGTH_SHORT).show();
        }

        ScanareHelper dataBaseHelper = new ScanareHelper(getActivity());
        boolean success = dataBaseHelper.adaugaScanare(scanareModel);

        Toast.makeText(getActivity(), "Success adaugare in DB!" + success, Toast.LENGTH_SHORT).show();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//**************************************************************************************
//**** Functionalitate PopUp
// *************************************************************************************

        popUp = new PopupWindow(getContext());
        layout = new LinearLayout(getContext());
        LinearLayout mainLayout = new LinearLayout(getContext());
        Button add = new Button(getContext());
        add.findViewById(R.id.add_eticheta_origine);
        tv = new EditText(getContext());

        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);

        tv.setHint("Introduceti codul scanat:");
        tv.canScrollVertically(1);
        layout.addView(tv, params);
        popUp.setContentView(layout);

        binding.addEtichetaOrigine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (click) {
                    popUp.showAtLocation(layout, Gravity.TOP, 10, 10);
                    popUp.update(50, 250, 300, 300);
                    popUp.setFocusable(true);
                    popUp.update();
                    click = false;
                } else {
                    popUp.dismiss();
                    click = true;
                    binding.tvCodEticheta.setText(tv.getText().toString());
                    binding.etEtichetaOrigine.setText(tv.getText().toString(), TextView.BufferType.EDITABLE);
                    cod_scanat_eticheta_origine = tv.getText().toString();
                    SharedPreferences pref = getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("cod_scanat_eticheta_origine", tv.getText().toString());
                    editor.commit();
                }
            }
        });

//**************************************************************************************
//**************************************************************************************
//**************************************************************************************


//**************************************************************************************
//**** Blocare functionalitate buton Back
// *************************************************************************************
//        view.setFocusableInTouchMode(true);
//        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    Log.i("Test", "onKey Back listener is working!!!");
                    return true;
                }
                if (i == KeyEvent.KEYCODE_ALL_APPS && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    Log.i("Test", "ALL Home listener is working!!!");
                    return true;
                }
                return false;
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


}