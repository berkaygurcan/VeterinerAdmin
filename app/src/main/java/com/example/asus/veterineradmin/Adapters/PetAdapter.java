package com.example.asus.veterineradmin.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.veterineradmin.Fragments.KullaniciPetlerFragment;
import com.example.asus.veterineradmin.Models.AsiEkleModel;
import com.example.asus.veterineradmin.Models.KullaniciSilModel;
import com.example.asus.veterineradmin.Models.KullanicilarModel;
import com.example.asus.veterineradmin.Models.PetModel;
import com.example.asus.veterineradmin.Models.PetSilModel;
import com.example.asus.veterineradmin.R;
import com.example.asus.veterineradmin.RestApi.ManagerAll;
import com.example.asus.veterineradmin.Utils.ChangeFragments;
import com.example.asus.veterineradmin.Utils.Warnings;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.squareup.picasso.Picasso;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {

    List<PetModel> list;
    Context context;
    Activity activity;
    private ChangeFragments changeFragments;
    private String musid;
    private String tarih = "", formatliTarih = "";

    public PetAdapter(List<PetModel> list, Context context, Activity activitiy, String musid) {
        this.list = list;
        this.context = context;
        this.activity = activitiy;
        this.musid = musid;
        changeFragments = new ChangeFragments(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //layout tanımlaması yapılır
        View view = LayoutInflater.from(context).inflate(R.layout.userpetitemlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //atama işlemleri gerçekleştirilir
        holder.petNameText.setText(list.get(position).getPetisim().toString());
        holder.petBilgiText.setText(list.get(position).getPetisim()+" petini silmek için buraya tıklayın");
        holder.petBilgiText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                petSilOpenAlert(position);
            }
        });
        holder.petAsiEkleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAsiEkle(list.get(position).getPetid().toString());
            }
        });

        Picasso.get().load(list.get(position).getPetresim().toString()).resize(200, 200).into(holder.petImage);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView petBilgiText, petNameText;
        LinearLayout petAsiEkleLayout;
        ImageView petImage;


        //itemview ile listview in her elemanı için layout ile oluşturduğumuz view tanımlanması gerçekleştirilecek
        public ViewHolder(View itemView) {
            super(itemView);
            petBilgiText = itemView.findViewById(R.id.petBilgiText);
            petNameText = itemView.findViewById(R.id.petNameText);
            petAsiEkleLayout = itemView.findViewById(R.id.petAsiEkleLayout);
            petImage = itemView.findViewById(R.id.petImage);
        }
    }

    public void addAsiEkle(final String petid) {
        //alert diyalog acilması icin kodlama yapmamız lazım
        LayoutInflater layoutInflater = activity.getLayoutInflater();//?
        View view = layoutInflater.inflate(R.layout.asieklelayout, null);

        CalendarView calendarView = view.findViewById(R.id.takvim);
        final EditText asiEkleAsiName = view.findViewById(R.id.asiEkleAsiName);

        Button asiEkleButon = view.findViewById(R.id.asiEkleButon);


        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(view);
        alert.setCancelable(true);
        //artık alert dialogumuzu açabiliriz
        final AlertDialog alertDialog = alert.create();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                tarih = i2 + "/" + i1 + "/" + i;

                try {
                    Date date = inputFormat.parse(tarih);//ilk string i date e sonra date i tekrar string e cevirdik!
                    formatliTarih = format.format(date);//başlara 0 eklenmesi için db ile eşleşme dogru olsun diye yapıyoruz
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });
        asiEkleButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!formatliTarih.equals("") && !asiEkleAsiName.getText().toString().equals("")) {
                    addAsi(musid, petid, asiEkleAsiName.getText().toString(), formatliTarih, alertDialog);
                } else {
                    Toast.makeText(context, "Tarih seçiniz veya aşı ismi giriniz", Toast.LENGTH_LONG).show();
                }
            }
        });

        alertDialog.show();

    }

    public void addAsi(String musid, String petid, String asiName, String tarih, final AlertDialog alertDialog) {

        Call<AsiEkleModel> req = ManagerAll.getInstance().asiEkle(musid, petid, asiName, tarih);
        req.enqueue(new Callback<AsiEkleModel>() {
            @Override
            public void onResponse(Call<AsiEkleModel> call, Response<AsiEkleModel> response) {
                if (response.body().isTf()) {

                    alertDialog.cancel();
                    Toast.makeText(context, response.body().getText(), Toast.LENGTH_LONG).show();
                } else {
                    alertDialog.cancel();
                    Toast.makeText(context, response.body().getText(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AsiEkleModel> call, Throwable t) {
                Toast.makeText(context, Warnings.internetProblemText, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void petSilOpenAlert(final int position) {
        //alert diyalog acilması icin kodlama yapmamız lazım
        LayoutInflater layoutInflater = activity.getLayoutInflater();//?
        View view = layoutInflater.inflate(R.layout.petsillayout, null);

        Button petSilIptalButon = view.findViewById(R.id.petSilIptalButon);
        Button petSilButon = view.findViewById(R.id.petSilButon);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(view);
        alert.setCancelable(true);
        //artık alert dialogumuzu açabiliriz
        final AlertDialog alertDialog = alert.create();


        petSilButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                petSil(list.get(position).getPetid().toString(), position);
                alertDialog.cancel();
            }
        });
        petSilIptalButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }

    public void petSil(String id, final int position) {
        Call<PetSilModel> req = ManagerAll.getInstance().petSil(id);
        req.enqueue(new Callback<PetSilModel>() {
            @Override
            public void onResponse(Call<PetSilModel> call, Response<PetSilModel> response) {
                if (response.body().isTf()) {
                    Toast.makeText(context, response.body().getText().toString(), Toast.LENGTH_LONG).show();
                    deleteToList(position);
                } else {
                    Toast.makeText(context, response.body().getText().toString(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<PetSilModel> call, Throwable t) {
                Toast.makeText(context, Warnings.internetProblemText, Toast.LENGTH_LONG).show();
            }
        });


    }

    public void deleteToList(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();//silindikden sonra itemlerin indexlerinin yeniden düzenlenmesi yani listenin yenilenmesi için kullandık


    }


}
