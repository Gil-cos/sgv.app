package br.com.gilberto.sgv.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import br.com.gilberto.sgv.R;
import br.com.gilberto.sgv.client.SgvClient;
import br.com.gilberto.sgv.domain.user.Role;
import br.com.gilberto.sgv.domain.user.User;
import br.com.gilberto.sgv.util.RetrofitClientsUtils;
import br.com.gilberto.sgv.util.SharedPreferencesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, email, password, cpf, phone;
    private Button registerBtn;
    private RadioButton passanger, driver;
    private AwesomeValidation awesomeValidation;
    private RetrofitClientsUtils retrofitClientsUtils = new RetrofitClientsUtils();
    private SgvClient sgvClient = retrofitClientsUtils.createSgvClient();
    private SharedPreferencesUtils preferencesUtils = new SharedPreferencesUtils();
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.userInfoName);
        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPassword);
        cpf = findViewById(R.id.editCpf);
        phone = findViewById(R.id.editPhone);
        registerBtn = findViewById(R.id.buttonRegister);
        passanger = findViewById(R.id.passangerRadio);
        driver = findViewById(R.id.driverRadio);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        addFormValidations();
        retrieveToken();

        registerBtn.setOnClickListener(v -> {
            if (awesomeValidation.validate()) {
                final User user = new User(
                        name.getText().toString(),
                        phone.getText().toString(),
                        cpf.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString(),
                        token,
                        getRole());
                registerUser(user);
            } else {
                Toast.makeText(getApplicationContext(), R.string.invalid_form, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Role getRole() {
        return passanger.isChecked() ? Role.PASSENGER : Role.DRIVER;
    }

    private void addFormValidations() {
        awesomeValidation.addValidation(this, R.id.userInfoName,
                RegexTemplate.NOT_EMPTY, R.string.invalid_name);
        awesomeValidation.addValidation(this, R.id.editEmail,
                Patterns.EMAIL_ADDRESS, R.string.invalid_email);
        awesomeValidation.addValidation(this, R.id.editEmail,
                RegexTemplate.NOT_EMPTY, R.string.invalid_email);
        awesomeValidation.addValidation(this, R.id.editPassword,
                RegexTemplate.NOT_EMPTY, R.string.invalid_password);
        awesomeValidation.addValidation(this, R.id.editCpf,
                RegexTemplate.NOT_EMPTY, R.string.invalid_cpf);
        awesomeValidation.addValidation(this, R.id.editPhone,
                RegexTemplate.NOT_EMPTY, R.string.invalid_phone);
    }

    private void registerUser(final User user) {
        Call<User> userCall = sgvClient.createUser(user);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.user_register, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                
            }
        });
    }

    public Task<String> retrieveToken() {
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
        return firebaseMessaging.getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String instanceIdResult) {
                token = instanceIdResult;
            }
        });
    }
}