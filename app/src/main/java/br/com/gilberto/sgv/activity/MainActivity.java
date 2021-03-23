package br.com.gilberto.sgv.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

import br.com.gilberto.sgv.R;
import br.com.gilberto.sgv.util.SharedPreferencesUtils;

public class MainActivity extends IntroActivity {

    private SharedPreferencesUtils preferencesUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferencesUtils = new SharedPreferencesUtils();
        verifyUserIsLogged();

        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.register_intro)
                .build());
    }

    private void verifyUserIsLogged() {
        String token = preferencesUtils.retrieveToken(getSharedPreferences(getString(R.string.authenticationInfo), 0));
        if (!token.isEmpty()) {
            startActivity(new Intent(this, UserActivity.class));
        }
    }

    public void login(View view){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void register(View view){
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }
}