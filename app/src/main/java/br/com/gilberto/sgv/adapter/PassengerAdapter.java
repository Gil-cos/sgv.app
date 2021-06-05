package br.com.gilberto.sgv.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.gilberto.sgv.R;
import br.com.gilberto.sgv.activity.RouteDetailsActivity;
import br.com.gilberto.sgv.client.SgvClient;
import br.com.gilberto.sgv.domain.address.Address;
import br.com.gilberto.sgv.domain.route.Period;
import br.com.gilberto.sgv.domain.route.Route;
import br.com.gilberto.sgv.domain.user.Role;
import br.com.gilberto.sgv.domain.user.User;
import br.com.gilberto.sgv.util.RetrofitClientsUtils;
import br.com.gilberto.sgv.util.SharedPreferencesUtils;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerAdapter extends RecyclerView.Adapter<PassengerAdapter.MyViewHolder> {

    private RetrofitClientsUtils retrofitClientsUtils = new RetrofitClientsUtils();
    private SgvClient sgvClient = retrofitClientsUtils.createSgvClient();
    private SharedPreferencesUtils preferencesUtils = new SharedPreferencesUtils();
    private Route route;
    private Role role;
    private Long userId;
    private List<User> passengers;
    private Context context;

    public PassengerAdapter(Route route, Role role, Long userId, List<User> passengers, Context context) {
        this.route = route;
        this.passengers = passengers;
        this.context = context;
        this.role = role;
        this.userId = userId;
    }

    @NonNull
    @Override
    public PassengerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_passenger, parent, false);
        return new PassengerAdapter.MyViewHolder(item);
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


            if (role.equals(Role.PASSENGER) && route.isPreparing()) {
                if (passenger.getId().equals(userId)) {
                    if (passenger.getIsConfirmed() != null && passenger.getIsConfirmed().equals(true)) {
                        holder.thumbUp.setVisibility(View.VISIBLE);
                        holder.thumbDownBtn.setVisibility(View.VISIBLE);

                    } else if (passenger.getIsConfirmed() != null && passenger.getIsConfirmed().equals(false)) {
                        holder.thumbDown.setVisibility(View.VISIBLE);
                        holder.thumbUpBtn.setVisibility(View.VISIBLE);

                    } else {
                        holder.thumbDownBtn.setVisibility(View.VISIBLE);
                        holder.thumbUpBtn.setVisibility(View.VISIBLE);
                    }
                }
            } else if (role.equals(Role.DRIVER) && route.isPreparing()) {
                if (passenger.getIsConfirmed() != null) {
                    if (passenger.getIsConfirmed().equals(true)) {
                        holder.thumbUp.setVisibility(View.VISIBLE);

                    } else if (passenger.getIsConfirmed().equals(false)) {
                        holder.thumbDown.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return passengers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id, name, phone, number, street, neighborhood, city;
        ImageView delete, thumbUp, thumbDown, thumbUpBtn, thumbDownBtn;

        public MyViewHolder(View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.passengerAdapterId);
            street = itemView.findViewById(R.id.passengerAdapterStreet);
            neighborhood = itemView.findViewById(R.id.passengerAdapterNeighborhood);
            name = itemView.findViewById(R.id.passengerAdapterName);
            phone = itemView.findViewById(R.id.passengerAdapterPhone);
            number = itemView.findViewById(R.id.passengerAdapterNumber);
            city = itemView.findViewById(R.id.passengerAdapterCity);
            delete = itemView.findViewById(R.id.imageViewDelete);
            thumbDown = itemView.findViewById(R.id.imageViewThumbDown);
            thumbUp = itemView.findViewById(R.id.imageViewThumbUp);
            thumbUpBtn = itemView.findViewById(R.id.imageViewThumbUpBtn);
            thumbDownBtn = itemView.findViewById(R.id.imageViewThumbDownBtn);

            if (role.equals(Role.PASSENGER) || route.isActivated()) {
                delete.setVisibility(View.GONE);
            }

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getRootView().getContext());

                    dialog.setTitle(R.string.remove_passenger);
                    dialog.setMessage(context.getString(R.string.want_to_remove_passenger) + " " + name.getText().toString() + "?");

                    dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Call<Route> routeCall = sgvClient.removePassenger(preferencesUtils.retrieveToken(context.getSharedPreferences(context.getString(R.string.authenticationInfo), 0)),
                                    route.getId(), Long.parseLong(id.getText().toString()));

                            routeCall.enqueue(new Callback<Route>() {
                                @SneakyThrows
                                @Override
                                public void onResponse(Call<Route> call, Response<Route> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(context, R.string.passenger_removed, Toast.LENGTH_SHORT).show();
                                        ((Activity) v.getContext()).finish();
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

            thumbUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getRootView().getContext());

                    dialog.setTitle(R.string.confirm_presence);
                    dialog.setMessage(context.getString(R.string.want_to_confirm_presence));

                    dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Call<User> routeCall = sgvClient.confirmPresence(preferencesUtils.retrieveToken(context.getSharedPreferences(context.getString(R.string.authenticationInfo), 0)),
                                    Long.parseLong(id.getText().toString()), route.getId());

                            routeCall.enqueue(new Callback<User>() {
                                @SneakyThrows
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(context, R.string.presence_confirmed, Toast.LENGTH_SHORT).show();
                                        thumbUp.setVisibility(View.VISIBLE);
                                        thumbUpBtn.setVisibility(View.GONE);
                                        thumbDownBtn.setVisibility(View.VISIBLE);
                                        thumbDown.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {

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

            thumbDownBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getRootView().getContext());

                    dialog.setTitle(R.string.confirm_presence);
                    dialog.setMessage(context.getString(R.string.want_to_decline_presence));

                    dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Call<User> routeCall = sgvClient.declinePresence(preferencesUtils.retrieveToken(context.getSharedPreferences(context.getString(R.string.authenticationInfo), 0)),
                                    Long.parseLong(id.getText().toString()), route.getId());

                            routeCall.enqueue(new Callback<User>() {
                                @SneakyThrows
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if (response.isSuccessful()) {
                                        thumbDown.setVisibility(View.VISIBLE);
                                        thumbDownBtn.setVisibility(View.GONE);
                                        thumbUpBtn.setVisibility(View.VISIBLE);
                                        thumbUp.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {

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

