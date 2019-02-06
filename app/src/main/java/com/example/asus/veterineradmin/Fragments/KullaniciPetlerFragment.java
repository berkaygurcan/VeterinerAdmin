package com.example.asus.veterineradmin.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.veterineradmin.Adapters.PetAdapter;
import com.example.asus.veterineradmin.Models.PetEkleModel;
import com.example.asus.veterineradmin.Models.PetModel;
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


public class KullaniciPetlerFragment extends Fragment {
    private View view;
    private String musid;
    private ChangeFragments changeFragments;
    private RecyclerView userPetListRecView;
    private ImageView petEkleResimYok, petEkleImageView,kullaniciBackImage;
    private Button userPetEkle;
    private TextView petEkleUyariText;
    private List<PetModel> list;
    private PetAdapter petAdapter;
    private Bitmap bitmap;
    private String imageString="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_kullanici, container, false);
        tanimla();
        getPets(musid);
        click();
        return view;
    }

    public void tanimla() {
        musid = getArguments().getString("userid").toString();
        changeFragments = new ChangeFragments(getContext());
        userPetListRecView = view.findViewById(R.id.userPetListRecView);
        userPetListRecView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        petEkleResimYok = view.findViewById(R.id.petEkleResimYok);
        kullaniciBackImage = view.findViewById(R.id.kullaniciBackImage);
        userPetEkle = view.findViewById(R.id.userPetEkle);
        petEkleUyariText = view.findViewById(R.id.petEkleUyariText);
        list = new ArrayList<>();
        bitmap = null;

    }

    public void click() {
        userPetEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                petEkleAlert();
            }
        });
        kullaniciBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragments.change(new KullanicilarFragment());
            }
        });

    }

    public void getPets(String id) {
        Call<List<PetModel>> req = ManagerAll.getInstance().getPets(id);
        req.enqueue(new Callback<List<PetModel>>() {
            @Override
            public void onResponse(Call<List<PetModel>> call, Response<List<PetModel>> response) {
                if (response.body().get(0).isTf()) {
                    userPetListRecView.setVisibility(View.VISIBLE);
                    petEkleResimYok.setVisibility(View.GONE);
                    petEkleUyariText.setVisibility(View.GONE);
                    list = response.body();
                    petAdapter = new PetAdapter(list, getContext(), getActivity(), musid);
                    userPetListRecView.setAdapter(petAdapter);

                    Toast.makeText(getContext(), "Kullanıcıya ait " + response.body().size() + " tane pet bulunmuştur.", Toast.LENGTH_LONG).show();


                } else {

                    Toast.makeText(getContext(), "Kullanıcıya ait pet bulunamamıştır. ", Toast.LENGTH_LONG).show();
                    petEkleResimYok.setVisibility(View.VISIBLE);
                    petEkleUyariText.setVisibility(View.VISIBLE);
                    userPetListRecView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<PetModel>> call, Throwable t) {
                Toast.makeText(getContext(), Warnings.internetProblemText, Toast.LENGTH_LONG).show();
            }
        });

    }


    public void petEkleAlert() {
        //alert diyalog acilması icin kodlama yapmamız lazım
        LayoutInflater layoutInflater = this.getLayoutInflater();//?
        View view = layoutInflater.inflate(R.layout.peteklelayout, null);
        Button petEkleResimSecButon = view.findViewById(R.id.petEkleResimSecButon);
        Button petEkleEkleButon = view.findViewById(R.id.petEkleEkleButon);
        petEkleImageView = view.findViewById(R.id.petEkleImageView);
        final EditText petEkleNameEditText = view.findViewById(R.id.petEkleNameEditText);
        final EditText petEkleTurEditText = view.findViewById(R.id.petEkleTurEditText);
        final EditText petEkleCinsEditText = view.findViewById(R.id.petEkleCinsEditText);


        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(view);
        alert.setCancelable(true);
        //artık alert dialogumuzu açabiliriz
        final AlertDialog alertDialog = alert.create();

        petEkleResimSecButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galeriAc();
            }
        });
        petEkleEkleButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!imageToString().equals("") && !petEkleNameEditText.getText().toString().equals("") && !petEkleTurEditText.getText().toString().equals("")
                        && !imageToString().equals("") && !petEkleCinsEditText.getText().toString().equals("")) {

                    petEkle(musid, petEkleNameEditText.getText().toString(), petEkleTurEditText.getText().toString(),
                            petEkleCinsEditText.getText().toString(), imageToString(), alertDialog);
                    petEkleNameEditText.setText("");
                    petEkleTurEditText.setText("");
                    petEkleCinsEditText.setText("");

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 777 && data != null) {


            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                petEkleImageView.setImageBitmap(bitmap);
                petEkleImageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void petEkle(final String musid, String petismi, String pettur, String petcins, String imageString, final AlertDialog alertDialog) {
        Call<PetEkleModel> req = ManagerAll.getInstance().petEkle(musid, petismi, pettur, petcins, imageString);
        req.enqueue(new Callback<PetEkleModel>() {
            @Override
            public void onResponse(Call<PetEkleModel> call, Response<PetEkleModel> response) {

                if (response.body().isTf()) {
                    getPets(musid);
                    Toast.makeText(getContext(), response.body().getText().toString(), Toast.LENGTH_LONG).show();
                    alertDialog.cancel();
                } else {
                    Toast.makeText(getContext(), response.body().getText().toString(), Toast.LENGTH_LONG).show();
                    alertDialog.cancel();

                }
            }

            @Override
            public void onFailure(Call<PetEkleModel> call, Throwable t) {
                Toast.makeText(getContext(), Warnings.internetProblemText, Toast.LENGTH_LONG).show();
            }
        });
    }

}
