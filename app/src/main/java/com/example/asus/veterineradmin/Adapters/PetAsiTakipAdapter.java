package com.example.asus.veterineradmin.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.veterineradmin.Models.AsiOnaylaModel;
import com.example.asus.veterineradmin.Models.KampanyaModel;
import com.example.asus.veterineradmin.Models.KampanyaSilModel;
import com.example.asus.veterineradmin.Models.PetAsiTakipModel;
import com.example.asus.veterineradmin.R;
import com.example.asus.veterineradmin.RestApi.ManagerAll;
import com.example.asus.veterineradmin.Utils.Warnings;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetAsiTakipAdapter extends RecyclerView.Adapter<PetAsiTakipAdapter.ViewHolder> {

    List<PetAsiTakipModel> list;
    Context context;
    Activity activity;

    public PetAsiTakipAdapter(List<PetAsiTakipModel> list, Context context, Activity activitiy) {
        this.list = list;
        this.context = context;
        this.activity = activitiy;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //layout tanımlaması yapılır
        View view = LayoutInflater.from(context).inflate(R.layout.asitakiplayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //atama işlemleri gerçekleştirilir
        holder.asiTakipPetName.setText(list.get(position).getPetisim());
        holder.asiTakipBilgiText.setText(list.get(position).getKadi() + " isimli kullanıcının " + list.get(position).getPetisim() + " isimli petinin "
                + list.get(position).getAsiisim() + " aşısı yapılacaktır.");

        Picasso.get().load(list.get(position).getPetresim()).resize(200, 200).into(holder.asiTakipImage);
        holder.asiTakipAraButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ara(list.get(position).getTelefon().toString());
            }
        });
        holder.asiTakipOkButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asiOnayla(list.get(position).getAsiid().toString(), position);
            }
        });
        holder.asiTakipCancelButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onaylama(list.get(position).getAsiid().toString(), position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView asiTakipPetName, asiTakipBilgiText;
        ImageView asiTakipOkButon, asiTakipCancelButon, asiTakipAraButon, asiTakipImage;
        // CardView kampanyaCardView;

        //itemview ile listview in her elemanı için layout ile oluşturduğumuz view tanımlanması gerçekleştirilecek
        public ViewHolder(View itemView) {
            super(itemView);
            asiTakipPetName = itemView.findViewById(R.id.asiTakipPetName);
            asiTakipBilgiText = itemView.findViewById(R.id.asiTakipBilgiText);
            asiTakipOkButon = itemView.findViewById(R.id.asiTakipOkButon);
            asiTakipCancelButon = itemView.findViewById(R.id.asiTakipCancelButon);
            asiTakipAraButon = itemView.findViewById(R.id.asiTakipAraButon);
            asiTakipImage = itemView.findViewById(R.id.asiTakipImage);
        }
    }


    public void deleteToList(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();//silindikden sonra itemlerin indexlerinin yeniden düzenlenmesi yani listenin yenilenmesi için kullandık


    }

    public void ara(String numara) {
        Intent ıntent = new Intent(Intent.ACTION_VIEW);
        ıntent.setData(Uri.parse("tel:" + numara));
        activity.startActivity(ıntent);
    }

    public void asiOnayla(String id, final int position) {
        Call<AsiOnaylaModel> req = ManagerAll.getInstance().asiOnayla(id);
        req.enqueue(new Callback<AsiOnaylaModel>() {
            @Override
            public void onResponse(Call<AsiOnaylaModel> call, Response<AsiOnaylaModel> response) {
                Toast.makeText(context, response.body().getText().toString(), Toast.LENGTH_LONG).show();
                deleteToList(position);
            }

            @Override
            public void onFailure(Call<AsiOnaylaModel> call, Throwable t) {
                Toast.makeText(context, Warnings.internetProblemText, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onaylama(String id, final int position) {
        Call<AsiOnaylaModel> req = ManagerAll.getInstance().asiIptal(id);
        req.enqueue(new Callback<AsiOnaylaModel>() {
            @Override
            public void onResponse(Call<AsiOnaylaModel> call, Response<AsiOnaylaModel> response) {
                Toast.makeText(context, response.body().getText().toString(), Toast.LENGTH_LONG).show();
                deleteToList(position);
            }

            @Override
            public void onFailure(Call<AsiOnaylaModel> call, Throwable t) {
                Toast.makeText(context, Warnings.internetProblemText, Toast.LENGTH_LONG).show();
            }
        });
    }

}
