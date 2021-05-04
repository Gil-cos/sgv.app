package br.com.gilberto.sgv.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import br.com.gilberto.sgv.R;
import br.com.gilberto.sgv.client.CepClient;
import br.com.gilberto.sgv.client.SgvClient;
import br.com.gilberto.sgv.domain.address.Address;
import br.com.gilberto.sgv.domain.user.User;
import br.com.gilberto.sgv.dto.CepDto;
import br.com.gilberto.sgv.util.SharedPreferencesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddressActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private SgvClient sgvClient;
    private CepClient cepClient;
    private SharedPreferencesUtils preferencesUtils = new SharedPreferencesUtils();
    private TextInputEditText cep, number, city, street, neighborhood;
    private Button saveBtn;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8090")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        sgvClient = retrofit.create(SgvClient.class);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        cepClient = retrofit.create(CepClient.class);

        cep = findViewById(R.id.brandEditText);
        number = findViewById(R.id.modelEditText);
        city = findViewById(R.id.licensePlateEditText);
        street = findViewById(R.id.userInfoStreet);
        neighborhood = findViewById(R.id.numberOfSeatsEditText);
        saveBtn = findViewById(R.id.buttonUpdateVehicle);

        final Bundle data = getIntent().getExtras();
        user = (User) data.getSerializable("user");
        setAddressValues(user.getAddress());

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
            updateUserInfo(user);
        });
    }

    private void updateUserInfo(User user) {
        user.update(cep.getText().toString(), number.getText().toString(), city.getText().toString(),
                street.getText().toString(), neighborhood.getText().toString());
        Call<User> userCall = sgvClient.updateUser(preferencesUtils.retrieveToken(this.getSharedPreferences(getString(R.string.authenticationInfo), 0)), user);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.updatedUser, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.updateUserError, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void setAddressValues(final Address address) {
        if (address != null) {
            cep.setText(address.getCep());
            number.setText(address.getNumber());
            city.setText(address.getCity());
            street.setText(address.getStreet());
            neighborhood.setText(address.getNeighborhood());
        }
    }
}