package com.example.asus.veterineradmin.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asus.veterineradmin.Adapters.PetAsiTakipAdapter;
import com.example.asus.veterineradmin.Models.PetAsiTakipModel;
import com.example.asus.veterineradmin.R;
import com.example.asus.veterineradmin.RestApi.ManagerAll;
import com.example.asus.veterineradmin.Utils.ChangeFragments;
import com.example.asus.veterineradmin.Utils.Warnings;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AsiTakipFragment extends Fragment {
    private View view;
    private DateFormat format;
    private Date date;
    private String today;
    private ChangeFragments changeFragments;
    private RecyclerView asiTakipRecylerView;
    private List<PetAsiTakipModel> list;
    private PetAsiTakipAdapter petAsiTakipAdapter;
    private ImageView asiTakipBackImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_asi_takip, container, false);
        tanimla();
        click();
        istekAt(today);
        return view;
    }

    public void tanimla() {
        format = new SimpleDateFormat("dd/MM/yyyy");
        date = Calendar.getInstance().getTime();
        today = format.format(date);
        // Log.i("tarih",today);
        asiTakipRecylerView = view.findViewById(R.id.asiTakipRecylerView);
        asiTakipBackImage = view.findViewById(R.id.asiTakipBackImage);
        asiTakipRecylerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        changeFragments = new ChangeFragments(getContext());
        list = new ArrayList<>();

    }
    public void click()
    {
        asiTakipBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragments.change(new HomeFragment());
            }
        });
    }

    public void istekAt(String tarih) {
        Call<List<PetAsiTakipModel>> req = ManagerAll.getInstance().getPetAsi(tarih);
        req.enqueue(new Callback<List<PetAsiTakipModel>>() {
            @Override
            public void onResponse(Call<List<PetAsiTakipModel>> call, Response<List<PetAsiTakipModel>> response) {
                if (response.body().get(0).isTf()) {
                    Toast.makeText(getContext(), "Bugün " + response.body().size() + " pete aşı yapılacaktır.", Toast.LENGTH_LONG).show();
                    list = response.body();
                    petAsiTakipAdapter = new PetAsiTakipAdapter(list, getContext(), getActivity());
                    asiTakipRecylerView.setAdapter(petAsiTakipAdapter);
                } else {
                    Toast.makeText(getContext(), "Bugün aşı yapılacak pet yoktur.", Toast.LENGTH_LONG).show();
                    changeFragments.change(new HomeFragment());
                }
            }

            @Override
            public void onFailure(Call<List<PetAsiTakipModel>> call, Throwable t) {
                Toast.makeText(getContext(), Warnings.internetProblemText, Toast.LENGTH_LONG).show();
            }
        });

    }


}
