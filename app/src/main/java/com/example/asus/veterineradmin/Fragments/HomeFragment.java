package com.example.asus.veterineradmin.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.asus.veterineradmin.R;
import com.example.asus.veterineradmin.Utils.ChangeFragments;


public class HomeFragment extends Fragment {
    private LinearLayout kampanyaLayout,asiTakipLayout,soruLayout,kullanicilarLayout;
    private  View view;
    private ChangeFragments changeFragments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);
        tanimla();
        clickToLayout();
        return view;
    }
    public void tanimla(){

        kampanyaLayout=view.findViewById(R.id.kampanyaLayout);
        asiTakipLayout=view.findViewById(R.id.asiTakipLayout);
        soruLayout=view.findViewById(R.id.soruLayout);
        kullanicilarLayout=view.findViewById(R.id.kullanicilarLayout);
        changeFragments=new ChangeFragments(getContext());
    }
    public void clickToLayout(){

        kampanyaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragments.change(new KampanyaFragment());

            }
        });
        asiTakipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragments.change(new AsiTakipFragment());
            }
        });
        soruLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragments.change(new SorularFragment());
            }
        });
        kullanicilarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragments.change(new KullanicilarFragment());
            }
        });
    }

}
