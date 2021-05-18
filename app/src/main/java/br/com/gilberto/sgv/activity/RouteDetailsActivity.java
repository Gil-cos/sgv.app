package br.com.gilberto.sgv.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import br.com.gilberto.sgv.R;
import br.com.gilberto.sgv.activity.fragments.home.RouteInfoFragment;
import br.com.gilberto.sgv.activity.fragments.home.RoutePassengersFragment;
import br.com.gilberto.sgv.client.SgvClient;
import br.com.gilberto.sgv.domain.route.Route;
import br.com.gilberto.sgv.util.RetrofitClientsUtils;
import br.com.gilberto.sgv.util.SharedPreferencesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteDetailsActivity extends AppCompatActivity {

    private RetrofitClientsUtils retrofitClientsUtils = new RetrofitClientsUtils();
    private SgvClient sgvClient = retrofitClientsUtils.createSgvClient();
    private SharedPreferencesUtils preferencesUtils = new SharedPreferencesUtils();
    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;
    private Long routeId;
    private Route route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details);

        smartTabLayout = findViewById(R.id.viewPagerTab);
        viewPager = findViewById(R.id.viewPager);

        getSupportActionBar().setElevation(0);

        final Bundle data = getIntent().getExtras();
        routeId = data.getLong("routeId");

        getRoute(data);

    }

    private void getRoute(Bundle data) {
        Call<Route> routeCall = sgvClient.getRoute(preferencesUtils.retrieveToken(getSharedPreferences(getString(R.string.authenticationInfo), 0)), routeId);
        routeCall.enqueue(new Callback<Route>() {
            @Override
            public void onResponse(Call<Route> call, Response<Route> response) {
                if (response.isSuccessful()) {
                    route = response.body();
                    data.putSerializable("route", route);
                    FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                            getSupportFragmentManager(),
                            FragmentPagerItems.with(getApplicationContext())
                                    .add(R.string.route_info, RouteInfoFragment.class, data)
                                    .add(R.string.passangers, RoutePassengersFragment.class, data)
                                    .create()
                    );

                    viewPager.setAdapter( adapter );
                    smartTabLayout.setViewPager( viewPager );
                }
            }

            @Override
            public void onFailure(Call<Route> call, Throwable t) {
                Log.d("Retrofit", t.getLocalizedMessage());
            }
        });
    }
}