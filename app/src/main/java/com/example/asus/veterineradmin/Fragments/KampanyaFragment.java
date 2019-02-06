package com.example.asus.veterineradmin.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asus.veterineradmin.Adapters.KampanyaAdapter;
import com.example.asus.veterineradmin.Models.KampanyaEkleModel;
import com.example.asus.veterineradmin.Models.KampanyaModel;
import com.example.asus.veterineradmin.R;
import com.example.asus.veterineradmin.RestApi.ManagerAll;
import com.example.asus.veterineradmin.Utils.ChangeFragments;
import com.example.asus.veterineradmin.Utils.Warnings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class KampanyaFragment extends Fragment {
    private View view;
    private RecyclerView kampanyaRecView;
    private List<KampanyaModel> kampanyaList;
    private KampanyaAdapter kampanyaAdapter;
    private ChangeFragments changeFragments;
    private Button kampanyaEkleButon;
    private ImageView kampanyaEkleImageView,kampanyaBackImage;
    private Bitmap bitmap;
    private String imageString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_kampanya, container, false);
        tanimla();
        getKampanya();
        click();
        return view;
    }

    public void tanimla() {
        kampanyaRecView = view.findViewById(R.id.kampanyaRecView);
        kampanyaRecView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        kampanyaEkleButon = view.findViewById(R.id.kampanyaEkleButon);
        kampanyaBackImage = view.findViewById(R.id.kampanyaBackImage);
        kampanyaList = new ArrayList<>();
        changeFragments = new ChangeFragments(getContext());
        bitmap = null;
        imageString = "";

    }

    public void click() {
        kampanyaEkleButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addKampanya();
            }
        });
        kampanyaBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragments.change(new HomeFragment());
            }
        });

    }

    public void getKampanya() {

        Call<List<KampanyaModel>> req = ManagerAll.getInstance().getKampanya();
        req.enqueue(new Callback<List<KampanyaModel>>() {
            @Override
            public void onResponse(Call<List<KampanyaModel>> call, Response<List<KampanyaModel>> response) {
                if (response.body().get(0).isTf()) {
                    kampanyaList = response.body();
                    kampanyaAdapter = new KampanyaAdapter(kampanyaList, getContext(),getActivity());
                    kampanyaRecView.setAdapter(kampanyaAdapter);

                } else {
                    Toast.makeText(getContext(), "Herhangi bir kampanya bulunmamaktadır...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<KampanyaModel>> call, Throwable t) {
                Toast.makeText(getContext(), Warnings.internetProblemText, Toast.LENGTH_LONG).show();
                changeFragments.change(new HomeFragment());
            }
        });
    }

    public void addKampanya() {
        //alert diyalog acilması icin kodlama yapmamız lazım
        LayoutInflater layoutInflater = this.getLayoutInflater();//?
        View view = layoutInflater.inflate(R.layout.kampanyaeklelayout, null);
        Button kampanyaAddButon = view.findViewById(R.id.kampanyaAddButon);
        Button kampanyaImageEkleButon = view.findViewById(R.id.kampanyaImageEkleButon);
        kampanyaEkleImageView = view.findViewById(R.id.kampanyaEkleImageView);
        final EditText kampanyaIcerikEditText = view.findViewById(R.id.kampanyaIcerikEditText);
        final EditText kampanyabaslikEditText = view.findViewById(R.id.kampanyabaslikEditText);


        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(view);
        alert.setCancelable(true);
        //artık alert dialogumuzu açabiliriz
        final AlertDialog alertDialog = alert.create();

        kampanyaImageEkleButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galeriAc();
            }
        });
        kampanyaAddButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!imageToString().equals("") && !kampanyabaslikEditText.getText().toString().equals("") && !kampanyaIcerikEditText.getText().toString().equals("")) {

                    kampanyaEkle(kampanyabaslikEditText.getText().toString(), kampanyaIcerikEditText.getText().toString(),
                            imageToString(), alertDialog);
                    kampanyabaslikEditText.setText("");
                    kampanyaIcerikEditText.setText("");

                } else {
                    Toast.makeText(getContext(), "Tüm alanların doldurulması ve resmin seçilmesi zorunludur", Toast.LENGTH_LONG).show();

                }
            }
        });

        alertDialog.show();

    }

    public void galeriAc() {

        Intent ıntent = new Intent();
        ıntent.setType("image/*");//galeri açtırır daha sonra ıntent e action verelim
        ıntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(ıntent, 777);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 777 && data != null) {


            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                kampanyaEkleImageView.setImageBitmap(bitmap);
                kampanyaEkleImageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String imageToString() {
        if (bitmap != null) {


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byt = byteArrayOutputStream.toByteArray();
            imageString = Base64.encodeToString(byt, Base64.DEFAULT);
            return imageString;
        } else
            return imageString;
    }

    public void kampanyaEkle(String baslik, String icerik, String imageString, final AlertDialog alertDialog) {
        Call<KampanyaEkleModel> req = ManagerAll.getInstance().addKampanya(baslik, icerik, imageString);
        req.enqueue(new Callback<KampanyaEkleModel>() {
            @Override
            public void onResponse(Call<KampanyaEkleModel> call, Response<KampanyaEkleModel> response) {
                if (response.body().isTf()) {
                    Toast.makeText(getContext(), response.body().getSonuc().toString(), Toast.LENGTH_LONG).show();
                    getKampanya();
                    alertDialog.cancel();
                } else {
                    Toast.makeText(getContext(), response.body().getSonuc().toString(), Toast.LENGTH_LONG).show();
                    alertDialog.cancel();
                }
            }

            @Override
            public void onFailure(Call<KampanyaEkleModel> call, Throwable t) {
                Toast.makeText(getContext(), Warnings.internetProblemText, Toast.LENGTH_LONG).show();
            }
        });

    }
}
