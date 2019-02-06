package com.example.asus.veterineradmin.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.veterineradmin.Models.KampanyaModel;

import com.example.asus.veterineradmin.Models.KampanyaSilModel;
import com.example.asus.veterineradmin.R;
import com.example.asus.veterineradmin.RestApi.ManagerAll;
import com.example.asus.veterineradmin.Utils.Warnings;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KampanyaAdapter extends RecyclerView.Adapter<KampanyaAdapter.ViewHolder> {

    List<KampanyaModel> list;
    Context context;
    Activity activity;

    public KampanyaAdapter(List<KampanyaModel> list, Context context, Activity activitiy) {
        this.list = list;
        this.context = context;
        this.activity = activitiy;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //layout tanımlaması yapılır
        View view = LayoutInflater.from(context).inflate(R.layout.kampanyaitemlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //atama işlemleri gerçekleştirilir
        holder.kampanyaBaslikText.setText(list.get(position).getBaslik());
        holder.kampanyaText.setText(list.get(position).getText());

        Picasso.get().load(list.get(position).getResim()).resize(200, 200).into(holder.kampanyaImageView);

        holder.kampanyaCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteKampanya(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView kampanyaBaslikText, kampanyaText;
        ImageView kampanyaImageView;
        CardView kampanyaCardView;

        //itemview ile listview in her elemanı için layout ile oluşturduğumuz view tanımlanması gerçekleştirilecek
        public ViewHolder(View itemView) {
            super(itemView);
            kampanyaBaslikText = itemView.findViewById(R.id.kampanyaBaslikText);
            kampanyaText = itemView.findViewById(R.id.kampanyaText);
            kampanyaImageView = itemView.findViewById(R.id.kampanyaImageView);
            kampanyaCardView = itemView.findViewById(R.id.kampanyaCardView);
        }
    }

    public void deleteKampanya(final int position) {
        //alert diyalog acilması icin kodlama yapmamız lazım
        LayoutInflater layoutInflater = activity.getLayoutInflater();//?
        View view = layoutInflater.inflate(R.layout.kampanyasillayout, null);

        Button kampanyaSilTamamButon = view.findViewById(R.id.kampanyaSilTamamButon);
        Button kampanyaSilIptalButon = view.findViewById(R.id.kampanyaSilIptalButon);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(view);
        alert.setCancelable(true);
        //artık alert dialogumuzu açabiliriz
        final AlertDialog alertDialog = alert.create();


        kampanyaSilTamamButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                kampanyaSil(list.get(position).getId().toString(), position);
                alertDialog.cancel();
            }
        });
        kampanyaSilIptalButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }

    public void kampanyaSil(String id, final int position) {
        Call<KampanyaSilModel> req = ManagerAll.getInstance().kampanyaSil(id);
        req.enqueue(new Callback<KampanyaSilModel>() {
            @Override
            public void onResponse(Call<KampanyaSilModel> call, Response<KampanyaSilModel> response) {
                if (response.body().isTf()) {

                    Toast.makeText(context, response.body().getText().toString(), Toast.LENGTH_LONG).show();
                    deleteToList(position);
                } else {

                    Toast.makeText(context, response.body().getText().toString(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<KampanyaSilModel> call, Throwable t) {
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
