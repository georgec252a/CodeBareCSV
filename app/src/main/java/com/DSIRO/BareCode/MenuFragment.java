package com.DSIRO.BareCode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.DSIRO.BareCode.databinding.FragmentMenuBinding;

public class MenuFragment extends Fragment {

    private final String sharedPref = "sharedPref";
    private String nr_stampila;
    private FragmentMenuBinding binding;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentMenuBinding.inflate(inflater, container, false);

//**************************************************************************************
//**** Initializare Shared Preferences la ""
// *************************************************************************************

        initSharedPreferences();

//**************************************************************************************
//**************************************************************************************
//**************************************************************************************


//**************************************************************************************
//**** Preluare din SharedPreferences nr_stampila si atribuire in tvUser
// *************************************************************************************

        sharedPreferences = getActivity().getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
        nr_stampila = sharedPreferences.getString("nr_stampila", "Not Found!!");
        binding.tvUser.setText(nr_stampila);
        binding.tvUser.setTextColor(getResources().getColor(R.color.purple_700));


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
//**** Functionalitate buton btnPiking
// *************************************************************************************

        binding.btnPiking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MenuFragment.this)
                        .navigate(R.id.action_Menu_Fragment_to_First_Scan_Fragment);


            }
        });

//**************************************************************************************
//**************************************************************************************
//**************************************************************************************


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("fragment_to_load", 1);
                startActivity(intent);
            }
        }, 0);

        return binding.getRoot();

    }

    private void initSharedPreferences() {
        sharedPreferences = getActivity().getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cod_scanat_bon_piking", "");
        editor.putString("cod_scanat_eticheta_origine", "");
        editor.putString("cod_uv", "");
        editor.putString("culoare_background", "");
        editor.commit();
        Toast.makeText(getContext(), "Date din SharedPreferences Reinitializate", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//**************************************************************************************
//**** Blocare functionalitate buton Back
// *************************************************************************************
        view.setFocusableInTouchMode(true);
        view.requestFocus();
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
        initSharedPreferences();
        binding = null;
    }
}