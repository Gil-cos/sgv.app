package br.com.gilberto.sgv.util;

import android.content.SharedPreferences;

import br.com.gilberto.sgv.client.CepClient;
import br.com.gilberto.sgv.client.NotificationClient;
import br.com.gilberto.sgv.client.SgvClient;
import br.com.gilberto.sgv.dto.TokenDto;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientsUtils {

    public SgvClient createSgvClient() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8090")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(SgvClient.class);
    }

    public CepClient createCepClient() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(CepClient.class);
    }

    public NotificationClient createNotificationClient() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/fcm/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(NotificationClient.class);
    }

}
