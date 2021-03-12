package br.com.gilberto.sgv.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

import br.com.gilberto.sgv.R;

public class MainActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.register_intro)
                .build());
    }

    public void login(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void register(View view){
        startActivity(new Intent(this, RegisterActivity.class));
    }
}