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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.veterineradmin.Fragments.KullaniciPetlerFragment;
import com.example.asus.veterineradmin.Models.KampanyaModel;
import com.example.asus.veterineradmin.Models.KampanyaSilModel;
import com.example.asus.veterineradmin.Models.KullaniciSilModel;
import com.example.asus.veterineradmin.Models.KullanicilarModel;
import com.example.asus.veterineradmin.R;
import com.example.asus.veterineradmin.RestApi.ManagerAll;
import com.example.asus.veterineradmin.Utils.ChangeFragments;
import com.example.asus.veterineradmin.Utils.Warnings;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    List<KullanicilarModel> list;
    Context context;
    Activity activity;
    private ChangeFragments changeFragments;


    public UserAdapter(List<KullanicilarModel> list, Context context, Activity activitiy) {
        this.list = list;
        this.context = context;
        this.activity = activitiy;
        changeFragments = new ChangeFragments(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //layout tanımlaması yapılır
        View view = LayoutInflater.from(context).inflate(R.layout.kullaniciitemlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //atama işlemleri gerçekleştirilir
        holder.kullanicilarNameText.setText(list.get(position).getKadi().toString());
        holder.userAramaYapButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ara(list.get(position).getTelefon().toString());
            }
        });
        holder.userPetlerButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragments.changeWithParameter(new KullaniciPetlerFragment(), list.get(position).getId().toString());
            }
        });
        holder.kullanicilarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kullaniciSilOpenAlert(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView kullanicilarNameText;
        Button userPetlerButon, userAramaYapButon;
        CardView userCardView;
        LinearLayout kullanicilarLayout;

        //itemview ile listview in her elemanı için layout ile oluşturduğumuz view tanımlanması gerçekleştirilecek
        public ViewHolder(View itemView) {
            super(itemView);
            kullanicilarNameText = itemView.findViewById(R.id.kullanicilarNameText);
            userPetlerButon = itemView.findViewById(R.id.userPetListButon);
            userAramaYapButon = itemView.findViewById(R.id.userAramaYapButon);
            userCardView = itemView.findViewById(R.id.userCardView);
            kullanicilarLayout = itemView.findViewById(R.id.kullanicilarLayout);
        }
    }

    public void ara(String numara) {
        Intent ıntent = new Intent(Intent.ACTION_VIEW);
        ıntent.setData(Uri.parse("tel:" + numara));
        activity.startActivity(ıntent);
    }

    public void kullaniciSilOpenAlert(final int position) {
        //alert diyalog acilması icin kodlama yapmamız lazım
        LayoutInflater layoutInflater = activity.getLayoutInflater();//?
        View view = layoutInflater.inflate(R.layout.kullanicisillayout, null);

        Button kullaniciSilIptalButon = view.findViewById(R.id.kullaniciSilIptalButon);
        Button kullaniciSilButon = view.findViewById(R.id.kullaniciSilButon);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(view);
        alert.setCancelable(true);
        //artık alert dialogumuzu açabiliriz
        final AlertDialog alertDialog = alert.create();


        kullaniciSilButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                kullaniciSil(list.get(position).getId().toString(), position);
                alertDialog.cancel();
            }
        });
        kullaniciSilIptalButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }

    public void kullaniciSil(String id, final int position) {
        Call<KullaniciSilModel> req = ManagerAll.getInstance().kadiSil(id);
        req.enqueue(new Callback<KullaniciSilModel>() {
            @Override
            public void onResponse(Call<KullaniciSilModel> call, Response<KullaniciSilModel> response) {
                if (response.body().isTf()) {
                    Toast.makeText(context, response.body().getText().toString(), Toast.LENGTH_LONG).show();
                    deleteToList(position);
                } else {
                    Toast.makeText(context, response.body().getText().toString(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<KullaniciSilModel> call, Throwable t) {
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
