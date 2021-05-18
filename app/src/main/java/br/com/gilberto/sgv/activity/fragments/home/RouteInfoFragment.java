package br.com.gilberto.sgv.activity.fragments.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.gilberto.sgv.R;
import br.com.gilberto.sgv.domain.institution.Institution;
import br.com.gilberto.sgv.domain.route.Route;
import br.com.gilberto.sgv.domain.user.User;
import br.com.gilberto.sgv.domain.user.driver.DriverInfo;

public class RouteInfoFragment extends Fragment {

    private Route route;
    private TextView routeDescription, driverName, driverPhone, driverEmail, driverStreet, driverNumber, driverNeighborhood, driverCep, driverCity;
    private TextView vehicleBrand, vehicleModel, vehiclePlate, vehicleSeats, institutionName, institutionStreet, institutionNumber, institutionNeighborhood, institutionCep, institutionCity;

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

        return root;
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

        routeDescription.setText(route.getDescription());
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