package br.com.gilberto.sgv.activity.fragments.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.gilberto.sgv.R;
import br.com.gilberto.sgv.client.NotificationClient;
import br.com.gilberto.sgv.client.SgvClient;
import br.com.gilberto.sgv.domain.institution.Institution;
import br.com.gilberto.sgv.domain.route.Route;
import br.com.gilberto.sgv.domain.route.RouteStatus;
import br.com.gilberto.sgv.domain.user.Role;
import br.com.gilberto.sgv.domain.user.User;
import br.com.gilberto.sgv.dto.NotificationDataDto;
import br.com.gilberto.sgv.dto.NotificationDto;
import br.com.gilberto.sgv.util.RetrofitClientsUtils;
import br.com.gilberto.sgv.util.SharedPreferencesUtils;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteInfoFragment extends Fragment {

    private RetrofitClientsUtils retrofitClientsUtils = new RetrofitClientsUtils();
    private SgvClient sgvClient = retrofitClientsUtils.createSgvClient();
    private NotificationClient notificationClient = retrofitClientsUtils.createNotificationClient();
    private SharedPreferencesUtils preferencesUtils = new SharedPreferencesUtils();
    private Route route;
    private Role role;
    private TextView routeDescription, driverName, driverPhone, driverEmail, driverStreet, driverNumber, driverNeighborhood, driverCep, driverCity, routeStatus, statusTitle;
    private TextView vehicleBrand, vehicleModel, vehiclePlate, vehicleSeats, institutionName, institutionStreet, institutionNumber, institutionNeighborhood, institutionCep, institutionCity;
    private Button initiatePreparationBtn, initiateTravelBtn, finishTravelBtn;
    private List<User> passengers = new ArrayList<>();

    public RouteInfoFragment() {
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
        View root = inflater.inflate(R.layout.fragment_route_info, container, false);

        Bundle data = getArguments();
        route = (Route) data.getSerializable("route");
        final User driver = route.getDriver();
        final Institution institution = route.getInstitution();

        setRouteInfo(root, driver, institution);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.authenticationInfo), 0);
        role = Role.valueOf(preferencesUtils.retrieveUserRole(sharedPreferences));
        getPassengers();
        setStatusButtons();

        initiatePreparationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getRootView().getContext());

                dialog.setTitle(R.string.change_route_status);
                dialog.setMessage("Mudar o status de " + route.getDescription() + " para " + RouteStatus.PREPARING.getPrettyName() + "?");

                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Call<Route> routeCall = sgvClient.changeStatus(preferencesUtils.retrieveToken(sharedPreferences), route.getId(), RouteStatus.PREPARING);
                        routeCall.enqueue(new Callback<Route>() {
                            @Override
                            public void onResponse(Call<Route> call, Response<Route> response) {
                                route = response.body();
                                routeStatus.setText(route.getStatus().getPrettyName());
                                initiatePreparationBtn.setVisibility(View.INVISIBLE);
                                initiateTravelBtn.setVisibility(View.VISIBLE);
                                sendNotification(v);
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

        initiateTravelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getRootView().getContext());

                dialog.setTitle(R.string.change_route_status);
                dialog.setMessage("Mudar o status de " + route.getDescription() + " para " + RouteStatus.TRAVELING.getPrettyName() + "?");

                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Call<Route> routeCall = sgvClient.changeStatus(preferencesUtils.retrieveToken(sharedPreferences), route.getId(), RouteStatus.TRAVELING);
                        routeCall.enqueue(new Callback<Route>() {
                            @Override
                            public void onResponse(Call<Route> call, Response<Route> response) {
                                route = response.body();
                                routeStatus.setText(route.getStatus().getPrettyName());
                                initiateTravelBtn.setVisibility(View.INVISIBLE);
                                finishTravelBtn.setVisibility(View.VISIBLE);
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

        finishTravelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getRootView().getContext());

                dialog.setTitle(R.string.change_route_status);
                dialog.setMessage("Mudar o status de " + route.getDescription() + " para " + RouteStatus.STAND_BY.getPrettyName() + "?");

                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Call<Route> routeCall = sgvClient.changeStatus(preferencesUtils.retrieveToken(sharedPreferences), route.getId(), RouteStatus.STAND_BY);
                        routeCall.enqueue(new Callback<Route>() {
                            @Override
                            public void onResponse(Call<Route> call, Response<Route> response) {
                                route = response.body();
                                routeStatus.setText(route.getStatus().getPrettyName());
                                finishTravelBtn.setVisibility(View.INVISIBLE);
                                initiatePreparationBtn.setVisibility(View.VISIBLE);
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

        return root;
    }

    public void sendNotification(View view){
        for (final User passenger : passengers) {
            final String to = passenger.getNotificationToken();
            NotificationDto notificationDto = new NotificationDto("Rota em Preparação!!", "A sua Rota: " + route.getDescription() + "\n" + "Entrou em preparação, confirme sua presenção");
            NotificationDataDto notificationDataDto = new NotificationDataDto(to, notificationDto );

            Call<NotificationDataDto> call = notificationClient.sendNotification( notificationDataDto );
            call.enqueue(new Callback<NotificationDataDto>() {
                @Override
                public void onResponse(Call<NotificationDataDto> call, Response<NotificationDataDto> response) {
                    if( response.isSuccessful() ){
                        Toast.makeText(view.getContext(), "Notificações Enviadas", Toast.LENGTH_LONG ).show();
                    }
                }
                @Override
                public void onFailure(Call<NotificationDataDto> call, Throwable t) {
                    Log.d("Retrofit", t.getLocalizedMessage());
                }
            });
        }
    }

    private void getPassengers() {
        Call<List<User>> passengersCall = sgvClient.getPassengers(preferencesUtils.retrieveToken(this.getActivity().getSharedPreferences(getString(R.string.authenticationInfo), 0)), route.getId());
        passengersCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    passengers.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("Retrofit", t.getLocalizedMessage());
            }
        });
    }

    private void setStatusButtons() {
        if (route.isStandBy() && role.equals(Role.DRIVER)) {
            initiatePreparationBtn.setVisibility(View.VISIBLE);
        }
        if (route.isPreparing() && role.equals(Role.DRIVER)) {
            initiateTravelBtn.setVisibility(View.VISIBLE);
        }
        if (route.isTraveling() && role.equals(Role.DRIVER)) {
            finishTravelBtn.setVisibility(View.VISIBLE);
        }
    }

    private void setRouteInfo(View root, final User driver, final Institution institution) {
        routeDescription = root.findViewById(R.id.routeInfoDescription);
        driverName = root.findViewById(R.id.routeInfoDriverName);
        driverPhone = root.findViewById(R.id.routeInfoDriverPhone);
        driverEmail = root.findViewById(R.id.routeInfoDriverEmail);
        driverStreet = root.findViewById(R.id.routeInfoDriverStreet);
        driverNumber = root.findViewById(R.id.routeInfoDriverNumber);
        driverNeighborhood = root.findViewById(R.id.routeInfoDriverNeighborhood);
        driverCep = root.findViewById(R.id.routeInfoDriverCep);
        driverCity = root.findViewById(R.id.routeInfoDriverCity);
        vehicleBrand = root.findViewById(R.id.routeInfoVehicleBrand);
        vehicleModel = root.findViewById(R.id.routeInfoVehicleModel);
        vehiclePlate = root.findViewById(R.id.routeInfoVehicleLicensePlate);
        vehicleSeats = root.findViewById(R.id.routeInfoVehicleNumberOfSeats);
        institutionName = root.findViewById(R.id.routeInfoInstitutionName);
        institutionStreet = root.findViewById(R.id.routeInfoInstitutionStreet);
        institutionNumber = root.findViewById(R.id.routeInfoInstitutionNumber);
        institutionNeighborhood = root.findViewById(R.id.routeInfoInstitutionNeighborhood);
        institutionCep = root.findViewById(R.id.routeInfoInstitutionCep);
        institutionCity = root.findViewById(R.id.routeInfoInstitutionCity);
        routeStatus = root.findViewById(R.id.routeInfoStatus);
        statusTitle = root.findViewById(R.id.statusTitle);
        initiatePreparationBtn = root.findViewById(R.id.initiatePreparationButton);
        initiateTravelBtn = root.findViewById(R.id.initiateTravelButton);
        finishTravelBtn = root.findViewById(R.id.finishTravelButton);

        routeDescription.setText(route.getDescription());
        routeStatus.setText(route.getStatus().getPrettyName());
        driverName.setText(driver.getName());
        driverPhone.setText(driver.getPhone());
        driverEmail.setText(driver.getEmail());
        driverStreet.setText(driver.getAddress().getStreet());
        driverNumber.setText(driver.getAddress().getNumber());
        driverNeighborhood.setText(driver.getAddress().getNeighborhood());
        driverCep.setText(driver.getAddress().getCep());
        driverCity.setText(driver.getAddress().getCity());
        vehicleBrand.setText(driver.getDriverInfo().getVehicle().getBrand());
        vehicleModel.setText(driver.getDriverInfo().getVehicle().getModel());
        vehiclePlate.setText(driver.getDriverInfo().getVehicle().getLicensePlate());
        vehicleSeats.setText(driver.getDriverInfo().getVehicle().getNumberOfSeats().toString());
        institutionName.setText(institution.getName());
        institutionStreet.setText(institution.getAddress().getStreet());
        institutionNumber.setText(institution.getAddress().getNumber());
        institutionNeighborhood.setText(institution.getAddress().getNeighborhood());
        institutionCep.setText(institution.getAddress().getCep());
        institutionCity.setText(institution.getAddress().getCity());

    }
}