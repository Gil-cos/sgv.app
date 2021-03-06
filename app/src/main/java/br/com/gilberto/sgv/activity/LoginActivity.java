package br.com.gilberto.sgv.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import br.com.gilberto.sgv.R;
import br.com.gilberto.sgv.client.SgvClient;
import br.com.gilberto.sgv.dto.TokenDto;
import br.com.gilberto.sgv.util.RetrofitClientsUtils;
import br.com.gilberto.sgv.util.SharedPreferencesUtils;
import br.com.gilberto.sgv.wrapper.LoginWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button loginBtn;
    private AwesomeValidation awesomeValidation;
    private RetrofitClientsUtils retrofitClientsUtils = new RetrofitClientsUtils();
    private SgvClient sgvClient = retrofitClientsUtils.createSgvClient();
    private SharedPreferencesUtils preferencesUtils = new SharedPreferencesUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPassword);
        loginBtn = findViewById(R.id.buttonLogin);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        addFormValidations();

        loginBtn.setOnClickListener(v -> {
            if (awesomeValidation.validate()) {
                login(new LoginWrapper(email.getText().toString(), password.getText().toString()));
            }
        });
    }

    private void login(final LoginWrapper loginWrapper) {

        Call<TokenDto> call = sgvClient.login(loginWrapper);

        call.enqueue(new Callback<TokenDto>() {
            @Override
            public void onResponse(Call<TokenDto> call, Response<TokenDto> response) {
                if (response.isSuccessful()) {
                    TokenDto tokenDto = response.body();
                    saveToken(tokenDto);
                    startActivity(new Intent(getApplicationContext(), UserActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.login_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TokenDto> call, Throwable t) {
                 Toast.makeText(getApplicationContext(), R.string.login_error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveToken(TokenDto tokenDto) {
        preferencesUtils.saveToken(tokenDto, getSharedPreferences(getString(R.string.authenticationInfo), 0));
    }

    private void addFormValidations() {
        awesomeValidation.addValidation(this, R.id.editEmail,
                Patterns.EMAIL_ADDRESS, R.string.invalid_email);
        awesomeValidation.addValidation(this, R.id.editEmail,
                RegexTemplate.NOT_EMPTY, R.string.invalid_email);
        awesomeValidation.addValidation(this, R.id.editPassword,
                RegexTemplate.NOT_EMPTY, R.string.invalid_password);

    }
}