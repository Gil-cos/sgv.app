package br.com.gilberto.sgv.activity.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import br.com.gilberto.sgv.R;
import br.com.gilberto.sgv.activity.RouteActivity;
import br.com.gilberto.sgv.adapter.RouteAdapter;
import br.com.gilberto.sgv.client.SgvClient;
import br.com.gilberto.sgv.domain.route.Route;
import br.com.gilberto.sgv.domain.user.User;
import br.com.gilberto.sgv.util.RetrofitClientsUtils;
import br.com.gilberto.sgv.util.SharedPreferencesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private RetrofitClientsUtils retrofitClientsUtils = new RetrofitClientsUtils();
    private SgvClient sgvClient = retrofitClientsUtils.createSgvClient();
    private SharedPreferencesUtils preferencesUtils = new SharedPreferencesUtils();
    private User user;
    private Long userId;
    private List<Route> routes = new ArrayList<>();
    private RouteAdapter routeAdapter;
    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        userId = preferencesUtils.retrieveUserId(this.getActivity().getSharedPreferences(getString(R.string.authenticationInfo), 0));

        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root.getContext(), RouteActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("editable", true);
                startActivity(intent);
            }
        });



        recyclerView = root.findViewById(R.id.routesRecyclerView);
        routeAdapter = new RouteAdapter(routes,this.getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter( routeAdapter );

        return root;
    }

    private void getRoutes() {
        Call<List<Route>> routeCall = sgvClient.getRoutes(preferencesUtils.retrieveToken(this.getActivity().getSharedPreferences(getString(R.string.authenticationInfo), 0)), userId);
        routeCall.enqueue(new Callback<List<Route>>() {
            @Override
            public void onResponse(Call<List<Route>> call, Response<List<Route>> response) {
                if (response.isSuccessful()) {
                    routes.addAll(response.body());
                    routeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Route>> call, Throwable t) {
                Log.d("Retrofit", t.getLocalizedMessage());
            }
        });
    }

    private void getUser() {
        routes.clear();
        Call<User> userCall = sgvClient.getUserInfo(preferencesUtils.retrieveToken(this.getActivity().getSharedPreferences(getString(R.string.authenticationInfo), 0)));
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    if (user.isPassenger()) {
                        fab.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getRoutes();
        getUser();

    }
}