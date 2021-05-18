package br.com.gilberto.sgv.activity.fragments.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import br.com.gilberto.sgv.R;
import br.com.gilberto.sgv.activity.PassengersListActivity;
import br.com.gilberto.sgv.activity.RouteActivity;
import br.com.gilberto.sgv.activity.RouteDetailsActivity;
import br.com.gilberto.sgv.adapter.PassengerAdapter;
import br.com.gilberto.sgv.adapter.RouteAdapter;
import br.com.gilberto.sgv.client.SgvClient;
import br.com.gilberto.sgv.domain.route.Route;
import br.com.gilberto.sgv.domain.user.User;
import br.com.gilberto.sgv.util.RetrofitClientsUtils;
import br.com.gilberto.sgv.util.SharedPreferencesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutePassengersFragment extends Fragment {

    private RetrofitClientsUtils retrofitClientsUtils = new RetrofitClientsUtils();
    private SgvClient sgvClient = retrofitClientsUtils.createSgvClient();
    private SharedPreferencesUtils preferencesUtils = new SharedPreferencesUtils();
    private List<User> passengers = new ArrayList<>();
    private PassengerAdapter passengerAdapter;
    private RecyclerView recyclerView;
    private Route route;

    public RoutePassengersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_route_passengers, container, false);

        FloatingActionButton fab = root.findViewById(R.id.passengersfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PassengersListActivity.class);
                intent.putExtra("routeId", route.getId());
                startActivity(intent);
            }
        });

        Bundle data = getArguments();
        route = (Route) data.getSerializable("route");

        recyclerView = root.findViewById(R.id.passengersRecyclerView);
        passengerAdapter = new PassengerAdapter(route.getId(), passengers, this.getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter( passengerAdapter );

        return root;
    }

    private void getPassengers() {
        passengers.clear();
        Call<List<User>> passengersCall = sgvClient.getPassengers(preferencesUtils.retrieveToken(this.getActivity().getSharedPreferences(getString(R.string.authenticationInfo), 0)), route.getId());
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

    @Override
    public void onResume() {
        super.onResume();
        getPassengers();
    }
}