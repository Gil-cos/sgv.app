package br.com.gilberto.sgv.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import br.com.gilberto.sgv.R;
import br.com.gilberto.sgv.client.SgvClient;
import br.com.gilberto.sgv.domain.address.Address;
import br.com.gilberto.sgv.domain.user.User;
import br.com.gilberto.sgv.domain.user.driver.DriverInfo;
import br.com.gilberto.sgv.domain.user.driver.Vehicle;
import br.com.gilberto.sgv.util.SharedPreferencesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VehicleActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private SgvClient sgvClient;
    private SharedPreferencesUtils preferencesUtils = new SharedPreferencesUtils();
    private TextInputEditText brand, model, licensePlate, numberOfSeats;
    private Button saveBtn;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8090")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        sgvClient = retrofit.create(SgvClient.class);

        brand = findViewById(R.id.brandEditText);
        model = findViewById(R.id.modelEditText);
        licensePlate = findViewById(R.id.licensePlateEditText);
        numberOfSeats = findViewById(R.id.numberOfSeatsEditText);
        saveBtn = findViewById(R.id.buttonUpdateVehicle);

        final Bundle data = getIntent().getExtras();
        user = (User) data.getSerializable("user");
        setVehicleValues(user.getDriverInfo());

        saveBtn.setOnClickListener(v -> {
            updateDriverInfo(user);
        });
    }

    private void updateDriverInfo(User user) {
        user.update(brand.getText().toString(), model.getText().toString(), licensePlate.getText().toString(),
                Integer.parseInt(numberOfSeats.getText().toString()));
        Call<User> userCall = sgvClient.updateUser(preferencesUtils.retrieveToken(this.getSharedPreferences(getString(R.string.authenticationInfo), 0)), user);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.updatedVehicle, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.updatedVehicleError, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void setVehicleValues(final DriverInfo driverInfo) {
        if (driverInfo != null) {
            final Vehicle vehicle = driverInfo.getVehicle();
            brand.setText(vehicle.getBrand());
            model.setText(vehicle.getModel());
            licensePlate.setText(vehicle.getLicensePlate());
            numberOfSeats.setText(vehicle.getNumberOfSeats().toString());
        }
    }
}