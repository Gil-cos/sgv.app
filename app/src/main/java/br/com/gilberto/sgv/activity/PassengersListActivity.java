package br.com.gilberto.sgv.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import br.com.gilberto.sgv.R;
import br.com.gilberto.sgv.adapter.PassengerAdapter;
import br.com.gilberto.sgv.adapter.PassengerListAdapter;
import br.com.gilberto.sgv.client.SgvClient;
import br.com.gilberto.sgv.domain.route.Route;
import br.com.gilberto.sgv.domain.user.User;
import br.com.gilberto.sgv.dto.CepDto;
import br.com.gilberto.sgv.util.RetrofitClientsUtils;
import br.com.gilberto.sgv.util.SharedPreferencesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengersListActivity extends AppCompatActivity {

    private RetrofitClientsUtils retrofitClientsUtils = new RetrofitClientsUtils();
    private SgvClient sgvClient = retrofitClientsUtils.createSgvClient();
    private SharedPreferencesUtils preferencesUtils = new SharedPreferencesUtils();
    private List<User> passengers = new ArrayList<>();
    private TextInputEditText passengerFilter;
    private PassengerListAdapter passengerAdapter;
    private RecyclerView recyclerView;
    private Long routeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passengers_list);

        passengerFilter = findViewById(R.id.passengerFilter);
        recyclerView = findViewById(R.id.passengersListRecyclerView);

        final Bundle data = getIntent().getExtras();
        routeId = data.getLong("routeId");

        passengerAdapter = new PassengerListAdapter(routeId, passengers, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter( passengerAdapter );

        passengerFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getPassengers();
            }
        });


    }

    private void getPassengers() {
        passengers.clear();
        Call<List<User>> passengersCall = sgvClient.getPassengersByFilter(preferencesUtils.retrieveToken(getSharedPreferences(getString(R.string.authenticationInfo), 0)), passengerFilter.getText().toString());
        passengersCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    passengers.addAll(response.body());
                    passengerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("Retrofit", t.getLocalizedMessage());
            }
        });
    }
}