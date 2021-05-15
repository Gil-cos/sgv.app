package br.com.gilberto.sgv.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import br.com.gilberto.sgv.R;
import br.com.gilberto.sgv.client.CepClient;
import br.com.gilberto.sgv.client.SgvClient;
import br.com.gilberto.sgv.domain.address.Address;
import br.com.gilberto.sgv.domain.institution.Institution;
import br.com.gilberto.sgv.domain.route.Period;
import br.com.gilberto.sgv.domain.route.Route;
import br.com.gilberto.sgv.domain.user.User;
import br.com.gilberto.sgv.dto.CepDto;
import br.com.gilberto.sgv.util.RetrofitClientsUtils;
import br.com.gilberto.sgv.util.SharedPreferencesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RouteActivity extends AppCompatActivity {

    private RetrofitClientsUtils retrofitClientsUtils = new RetrofitClientsUtils();
    private SgvClient sgvClient = retrofitClientsUtils.createSgvClient();
    private CepClient cepClient = retrofitClientsUtils.createCepClient();
    private SharedPreferencesUtils preferencesUtils = new SharedPreferencesUtils();
    private TextInputEditText routeName, cep, street, number, neighborhood, city, institutionName;
    private RadioButton dayTime, nocturnal;
    private Button saveBtn;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        final Bundle data = getIntent().getExtras();
        user = (User) data.getSerializable("user");
        final Boolean editable = data.getBoolean("editable");

//      setRouteValues(user.getAddress());

        routeName = findViewById(R.id.routeNameEditText);
        institutionName = findViewById(R.id.institutionEditText);
        cep = findViewById(R.id.institutionCepEditText);
        street = findViewById(R.id.institutionStreetEditText);
        number = findViewById(R.id.institutionNumberEditText);
        neighborhood = findViewById(R.id.institutionNeihgborEditText);
        city = findViewById(R.id.institutionCityEditText);
        dayTime = findViewById(R.id.dayTimeRadio);
        nocturnal = findViewById(R.id.nocturnalRadio);
        saveBtn = findViewById(R.id.buttonUpdateRoute);

        cep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Call<CepDto> cepCall = cepClient.getAddressInfoByCep(s.toString());
                cepCall.enqueue(new Callback<CepDto>() {
                    @Override
                    public void onResponse(Call<CepDto> call, Response<CepDto> response) {
                        if (response.isSuccessful()) {
                            final CepDto cepDto = response.body();
                            street.setText(cepDto.getLogradouro());
                            neighborhood.setText(cepDto.getBairro());
                            city.setText(cepDto.getLocalidade());
                        }
                    }

                    @Override
                    public void onFailure(Call<CepDto> call, Throwable t) {

                    }
                });
            }
        });

        saveBtn.setOnClickListener(v -> {
            if (editable.equals(true)) {
                createRoute();
            }
        });

    }

    private void createRoute() {
        final Route route = buildRoute();

        Call<Route> routeCall = sgvClient.createRoute(preferencesUtils.retrieveToken(this.getSharedPreferences(getString(R.string.authenticationInfo), 0)), route);

        routeCall.enqueue(new Callback<Route>() {
            @Override
            public void onResponse(Call<Route> call, Response<Route> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.route_crated, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.crate_route_error, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Route> call, Throwable t) {

            }
        });
    }

    private Route buildRoute() {
        final Address address = Address.builder()
                .cep(cep.getText().toString())
                .number(number.getText().toString())
                .city(city.getText().toString())
                .neighborhood(neighborhood.getText().toString())
                .street(street.getText().toString())
                .build();

        final Institution institution = Institution.builder()
                .name(institutionName.getText().toString())
                .address(address)
                .build();

        return Route.builder()
                .description(routeName.getText().toString())
                .period(getPeriod())
                .institution(institution)
                .driver(user)
                .build();
    }

    private Period getPeriod() {
        return dayTime.isChecked() ? Period.DAYTIME : Period.NOCTURNAL;
    }

    private void setRouteValues(final Address address) {
        if (address != null) {
            cep.setText(address.getCep());
            number.setText(address.getNumber());
            city.setText(address.getCity());
            street.setText(address.getStreet());
            neighborhood.setText(address.getNeighborhood());
        }
    }
}