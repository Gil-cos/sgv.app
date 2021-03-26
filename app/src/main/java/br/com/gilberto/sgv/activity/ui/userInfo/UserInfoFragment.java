package br.com.gilberto.sgv.activity.ui.userInfo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import br.com.gilberto.sgv.R;
import br.com.gilberto.sgv.client.CepClient;
import br.com.gilberto.sgv.client.SgvClient;
import br.com.gilberto.sgv.domain.address.Address;
import br.com.gilberto.sgv.domain.user.User;
import br.com.gilberto.sgv.dto.CepDto;
import br.com.gilberto.sgv.util.SharedPreferencesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserInfoFragment extends Fragment {

    private UserInfoViewModel userInfoViewModel;
    private Retrofit retrofit;
    private SgvClient sgvClient;
    private CepClient cepClient;
    private SharedPreferencesUtils preferencesUtils = new SharedPreferencesUtils();
    private EditText name, cpf, cep, number, city, street, neighborhood;
    private Button saveBtn;
    private Long id;
    private User user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userInfoViewModel =
                new ViewModelProvider(this).get(UserInfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user_info, container, false);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8090")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        sgvClient = retrofit.create(SgvClient.class);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        cepClient = retrofit.create(CepClient.class);

        name = root.findViewById(R.id.userInfoName);
        cpf = root.findViewById(R.id.userInfoCpf);
        cep = root.findViewById(R.id.userInfoCep);
        number = root.findViewById(R.id.userInfoNumber);
        city = root.findViewById(R.id.userInfoCity);
        street = root.findViewById(R.id.userInfoStreet);
        neighborhood = root.findViewById(R.id.userInfoNeighborhood);
        saveBtn = root.findViewById(R.id.buttonUpdateUserInfo);

        Call<User> userCall = sgvClient.getUserInfo(preferencesUtils.retrieveToken(this.getActivity().getSharedPreferences(getString(R.string.authenticationInfo), 0)));
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    id = user.getId();
                    name.setText(user.getName());
                    cpf.setText(user.getCpf());
                    setAddressValues(user.getAddress());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        cep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Call<CepDto> cepCall = cepClient.getAddressInfoByCep(s.toString());
                cepCall.enqueue(new Callback<CepDto>() {
                    @Override
                    public void onResponse(Call<CepDto> call, Response<CepDto> response) {
                        if (response.isSuccessful()) {
                            final CepDto cepDto = response.body();
                            street.setText(cepDto.getLogradouro());
                            neighborhood.setText(cepDto.getBairro());
                            city.setText(cepDto.getLocalidade());
                        }
                    }

                    @Override
                    public void onFailure(Call<CepDto> call, Throwable t) {

                    }
                });
            }
        });

        saveBtn.setOnClickListener(v -> {
                updateUserInfo(user);
        });

        userInfoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }

    private void setAddressValues(final Address address) {
        if (address != null) {
            cep.setText(address.getCep());
            number.setText(address.getNumber());
            city.setText(address.getCity());
            street.setText(address.getStreet());
            neighborhood.setText(address.getNeighborhood());
        }
    }

    private void updateUserInfo(User user) {
        user.update(name.getText().toString(), cpf.getText().toString(), cep.getText().toString(), number.getText().toString(), city.getText().toString(), street.getText().toString(), neighborhood.getText().toString());
        sgvClient.updateUser(user);
    }
}