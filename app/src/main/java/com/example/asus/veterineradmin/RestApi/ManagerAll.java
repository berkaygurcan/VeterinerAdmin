package com.example.asus.veterineradmin.RestApi;


import com.example.asus.veterineradmin.Models.AsiEkleModel;
import com.example.asus.veterineradmin.Models.AsiOnaylaModel;
import com.example.asus.veterineradmin.Models.CevaplaModel;
import com.example.asus.veterineradmin.Models.KampanyaEkleModel;
import com.example.asus.veterineradmin.Models.KampanyaModel;
import com.example.asus.veterineradmin.Models.KampanyaSilModel;
import com.example.asus.veterineradmin.Models.KullaniciSilModel;
import com.example.asus.veterineradmin.Models.KullanicilarModel;
import com.example.asus.veterineradmin.Models.PetAsiTakipModel;
import com.example.asus.veterineradmin.Models.PetEkleModel;
import com.example.asus.veterineradmin.Models.PetModel;
import com.example.asus.veterineradmin.Models.PetSilModel;
import com.example.asus.veterineradmin.Models.SoruModel;

import java.util.List;

import retrofit2.Call;

public class ManagerAll extends BaseManager {

    private static ManagerAll ourInstance = new ManagerAll();

    public static synchronized ManagerAll getInstance() {
        return ourInstance;
    }

    public Call<List<KampanyaModel>> getKampanya() {
        Call<List<KampanyaModel>> x = getRestApi().getKampanya();
        return x;
    }

    public Call<KampanyaEkleModel> addKampanya(String baslik, String icerik, String imageString) {
        Call<KampanyaEkleModel> x = getRestApi().addKampanya(baslik, icerik, imageString);
        return x;
    }

    public Call<KampanyaSilModel> kampanyaSil(String id) {
        Call<KampanyaSilModel> x = getRestApi().kampanyaSil(id);
        return x;
    }

    public Call<List<PetAsiTakipModel>> getPetAsi(String tarih) {
        Call<List<PetAsiTakipModel>> x = getRestApi().getPetAsiTakip(tarih);
        return x;
    }

    public Call<AsiOnaylaModel> asiOnayla(String id) {
        Call<AsiOnaylaModel> x = getRestApi().asiOnayla(id);
        return x;
    }

    public Call<AsiOnaylaModel> asiIptal(String id) {
        Call<AsiOnaylaModel> x = getRestApi().asiIptal(id);
        return x;
    }

    public Call<List<SoruModel>> getSoru() {
        Call<List<SoruModel>> x = getRestApi().getSoru();
        return x;
    }

    public Call<CevaplaModel> cevapla(String musid, String soruid, String text) {
        Call<CevaplaModel> x = getRestApi().cevapla(musid, soruid, text);
        return x;
    }

    public Call<List<KullanicilarModel>> getKullanicilar() {
        Call<List<KullanicilarModel>> x = getRestApi().getKullanicilar();
        return x;
    }

    public Call<List<PetModel>> getPets(String musid) {
        Call<List<PetModel>> x = getRestApi().getPets(musid);
        return x;
    }

    public Call<PetEkleModel> petEkle(String musid, String isim, String tur, String cins, String resim) {
        Call<PetEkleModel> x = getRestApi().petEkle(musid, isim, tur, cins, resim);
        return x;
    }

    public Call<AsiEkleModel> asiEkle(String musid, String petid, String name, String tarih) {
        Call<AsiEkleModel> x = getRestApi().asiEkle(musid, petid, name, tarih);
        return x;
    }

    public Call<KullaniciSilModel> kadiSil(String id) {
        Call<KullaniciSilModel> x = getRestApi().kadiSil(id);
        return x;
    }

    public Call<PetSilModel> petSil(String id) {
        Call<PetSilModel> x = getRestApi().petSil(id);
        return x;
    }

}
