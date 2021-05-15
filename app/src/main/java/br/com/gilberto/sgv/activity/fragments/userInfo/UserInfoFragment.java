package br.com.gilberto.sgv.activity.fragments.userInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import br.com.gilberto.sgv.R;
import br.com.gilberto.sgv.activity.AddressActivity;
import br.com.gilberto.sgv.activity.VehicleActivity;
import br.com.gilberto.sgv.client.SgvClient;
import br.com.gilberto.sgv.domain.user.User;
import br.com.gilberto.sgv.util.RetrofitClientsUtils;
import br.com.gilberto.sgv.util.SharedPreferencesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoFragment extends Fragment {

    private UserInfoViewModel userInfoViewModel;
    private RetrofitClientsUtils retrofitClientsUtils = new RetrofitClientsUtils();
    private SgvClient sgvClient = retrofitClientsUtils.createSgvClient();
    private SharedPreferencesUtils preferencesUtils = new SharedPreferencesUtils();
    private TextView name, email, phone, vehicle;
    private ImageButton editAddressBtn, editVehicleBtn;
    private User user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userInfoViewModel =
                new ViewModelProvider(this).get(UserInfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user_info, container, false);

        name = root.findViewById(R.id.nameTextView);
        email = root.findViewById(R.id.emailTextView);
        phone = root.findViewById(R.id.phoneTextView);
        vehicle = root.findViewById(R.id.vehicleTextView);
        editAddressBtn = root.findViewById(R.id.editAddressButton);
        editVehicleBtn = root.findViewById(R.id.editVehicleButton);

        Call<User> userCall = sgvClient.getUserInfo(preferencesUtils.retrieveToken(this.getActivity().getSharedPreferences(getString(R.string.authenticationInfo), 0)));
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    name.setText(user.getName());
                    email.setText(user.getEmail());
                    phone.setText(user.getPhone());

                    if (!user.isDriver()){
                        vehicle.setVisibility(View.GONE);
                        editVehicleBtn.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        editAddressBtn.setOnClickListener(v -> {
            editAddress();
        });

        editVehicleBtn.setOnClickListener(v -> {
            editVehicle();
        });

        userInfoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }

    private void editAddress() {
        Intent intent = new Intent(this.getActivity(), AddressActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void editVehicle() {
        Intent intent = new Intent(this.getActivity(), VehicleActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

}