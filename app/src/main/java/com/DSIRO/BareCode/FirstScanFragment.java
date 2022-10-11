package com.DSIRO.BareCode;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.DSIRO.BareCode.databinding.FragmentFirstScanBinding;

public class FirstScanFragment extends Fragment {

    private final String sharedPref = "sharedPref";
    private String codReper;
    private String cod_scanat_bon_piking;
    private String nr_stampila;
    private PopupWindow popUp;
    private LinearLayout layout;
    private EditText tv;
    private EditText et_cod_reper_scanat;
    private boolean click = true;
    private FragmentFirstScanBinding binding;
    private SharedPreferences sharedPreferences;
    private final int EXTERNAL_STORAGE_PERMISSION_CODE = 23;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentFirstScanBinding.inflate(inflater, container, false);


//**************************************************************************************
//**** Preluare din SharedPreferences nr_stampila, cod_stampila si atribuire in tvUser si tvCodReperScanat
// *************************************************************************************

        sharedPreferences = getActivity().getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
        cod_scanat_bon_piking = sharedPreferences.getString("cod_scanat_bon_piking", "");
//        cod_scanat_bon_piking="111";
        nr_stampila = sharedPreferences.getString("nr_stampila", "");
        binding.tvCodReperScanat.setText(cod_scanat_bon_piking);
        binding.tvUser.setText(nr_stampila);

//**************************************************************************************
//**************************************************************************************
//**************************************************************************************


// **************************************************************************************
//**** Realizare scanare cu Cititor cod Bare - Zebra TC51
// *************************************************************************************

//        binding.etCodReperScanat.requestFocus();
//        binding.etCodReperScanat.setFocusable(true);
//        binding.etCodReperScanat.setFocusableInTouchMode(true);


        binding.etCodReperScanatFirst.setText("", TextView.BufferType.EDITABLE);

        binding.etCodReperScanatFirst.isFocusableInTouchMode();
        binding.etCodReperScanatFirst.isFocusable();
        binding.etCodReperScanatFirst.requestFocus();

        binding.etCodReperScanatFirst.requestFocus();

        binding.etCodReperScanatFirst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (binding.etCodReperScanatFirst.getText().toString().length() > 9) {
                    pref = getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("cod_scanat_bon_piking", binding.etCodReperScanatFirst.getText().toString().substring(0, 10));
                    //binding.etCodReperScanatFirst.setText(binding.etCodReperScanatFirst.getText().toString().substring(0,10));
                    editor.putString("culoare_background", "none");
                    editor.commit();
                    binding.btnNext.performClick();
                }
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
                intent.putExtra("fragment_to_load", 4);
                startActivity(intent);
            }
        });

//**************************************************************************************
//**************************************************************************************
//**************************************************************************************


//**************************************************************************************
//**** Functionalitate buton Next
// *************************************************************************************

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("fragment_to_load", 2);
                startActivity(intent);
            }
        });

//**************************************************************************************
//**************************************************************************************
//**************************************************************************************


//**************************************************************************************
//**** Functionalitate TextView tvCodReperScanat
// *************************************************************************************

        binding.tvCodReperScanat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvCodReperScanat.setText("", TextView.BufferType.EDITABLE);

                Intent intent = new Intent(getActivity(), Scanner_zxing.class);
                intent.putExtra("fragment_origin", 100);
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
//                if(cod_scanat_bon_piking=="") {
//                    sharedPreferences = getActivity().getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("cod_scanat_eticheta_origine", "");
//                    editor.putString("cod_uv", "");
//                    editor.putString("culoare_background", "");
//
//                    editor.commit();
//                    Intent intent = new Intent(getContext(), Scanner_zxing.class);
//                    intent.putExtra("fragment_origin", 100);
//                    startActivity(intent);
//                }else {
//                    sharedPreferences = getActivity().getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("cod_scanat_eticheta_origine", "");
//                    editor.putString("cod_uv", "");
//                    editor.putString("culoare_background", "");
//                    editor.commit();
//                    Intent intent = new Intent(getContext(), MainActivity.class);
//                    intent.putExtra("fragment_to_load", 2);
//                    startActivity(intent);
//                }
//
//
//            }
//
//        }, 500);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//**************************************************************************************
//**** Functionalitate PopUp pentru introducere cod de la tastatura
// *************************************************************************************

        popUp = new PopupWindow(getContext());
        layout = new LinearLayout(getContext());
        LinearLayout mainLayout = new LinearLayout(getContext());
        Button add = new Button(getContext());
        add.findViewById(R.id.add);
        tv = new EditText(getContext());
        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        tv.setHint("Introduceti codul scanat:");
        tv.canScrollVertically(1);
        layout.addView(tv, params);
        popUp.setContentView(layout);

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (click) {
                    popUp.showAtLocation(layout, Gravity.TOP, 10, 10);
                    popUp.update(50, 250, 400, 400);
                    popUp.setFocusable(true);
                    popUp.update();
                    click = false;
                } else {
                    popUp.dismiss();
                    click = true;
                    binding.tvCodReperScanat.setText(tv.getText().toString());
                    SharedPreferences pref = getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("cod_scanat_bon_piking", tv.getText().toString());
                    editor.commit();
                }
            }
        });

//**************************************************************************************
//**************************************************************************************
//**************************************************************************************


//**************************************************************************************
//**** Functionalitate Back blocata
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
                return false;
            }
        });

//**************************************************************************************
//**************************************************************************************
//**************************************************************************************

    }

    private void showLoginFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}

