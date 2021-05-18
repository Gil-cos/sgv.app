package br.com.gilberto.sgv.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.gilberto.sgv.R;
import br.com.gilberto.sgv.client.SgvClient;
import br.com.gilberto.sgv.domain.address.Address;
import br.com.gilberto.sgv.domain.route.Route;
import br.com.gilberto.sgv.domain.user.User;
import br.com.gilberto.sgv.util.RetrofitClientsUtils;
import br.com.gilberto.sgv.util.SharedPreferencesUtils;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerListAdapter extends RecyclerView.Adapter<PassengerListAdapter.MyViewHolder> {

    private RetrofitClientsUtils retrofitClientsUtils = new RetrofitClientsUtils();
    private SgvClient sgvClient = retrofitClientsUtils.createSgvClient();
    private SharedPreferencesUtils preferencesUtils = new SharedPreferencesUtils();
    private Long routeId;
    private List<User> passengers;
    private Context context;

    public PassengerListAdapter(Long routeId, List<User> passengers, Context context) {
        this.routeId = routeId;
        this.passengers = passengers;
        this.context = context;
    }

    @NonNull
    @Override
    public PassengerListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_passenger_list, parent, false);
        return new PassengerListAdapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final User passenger = passengers.get(position);
        Address address = passenger.getAddress();

        if (passenger != null) {

            holder.id.setText(passenger.getId().toString());
            holder.name.setText(passenger.getName());
            holder.phone.setText(passenger.getPhone());
            holder.number.setText(address != null ? address.getNumber() : null);
            holder.street.setText(address != null ? address.getStreet() : null);
            holder.neighborhood.setText(address != null ? address.getNeighborhood() : null);
            holder.city.setText(address != null ? address.getCity() : null);
        }
    }

    @Override
    public int getItemCount() {
        return passengers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id, name, phone, number, street, neighborhood, city;

        public MyViewHolder(View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.passengerAdapterId);
            street = itemView.findViewById(R.id.passengerAdapterStreet);
            neighborhood = itemView.findViewById(R.id.passengerAdapterNeighborhood);
            name = itemView.findViewById(R.id.passengerAdapterName);
            phone = itemView.findViewById(R.id.passengerAdapterPhone);
            number = itemView.findViewById(R.id.passengerAdapterNumber);
            city = itemView.findViewById(R.id.passengerAdapterCity);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getRootView().getContext());

                    dialog.setTitle(R.string.add_passenger);
                    dialog.setMessage(context.getString(R.string.want_to_add_passenger) + " " + name.getText().toString() + "?");

                    dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Call<Route> routeCall = sgvClient.addPassenger(preferencesUtils.retrieveToken(context.getSharedPreferences(context.getString(R.string.authenticationInfo), 0)),
                                    routeId, Long.parseLong(id.getText().toString()));

                            routeCall.enqueue(new Callback<Route>() {
                                @SneakyThrows
                                @Override
                                public void onResponse(Call<Route> call, Response<Route> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(context, R.string.passenger_added, Toast.LENGTH_SHORT).show();
                                        ((Activity)v.getContext()).finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Route> call, Throwable t) {

                                }
                            });
                        }
                    });

                    dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    dialog.create();
                    dialog.show();
                }
            });
        }

    }
}

