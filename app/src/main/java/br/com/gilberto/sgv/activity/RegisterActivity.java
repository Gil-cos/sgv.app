package br.com.gilberto.sgv.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import br.com.gilberto.sgv.R;
import br.com.gilberto.sgv.domain.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, email, password, cpf, phone;
    private Button registerBtn;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.editName);
        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPassword);
        cpf = findViewById(R.id.editCpf);
        phone = findViewById(R.id.editPhone);
        registerBtn = findViewById(R.id.buttonRegister);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        addFormValidations();

        registerBtn.setOnClickListener(v -> {
            if (awesomeValidation.validate()) {
                final User user = new User(
                        null,
                        name.getText().toString(),
                        phone.getText().toString(),
                        cpf.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString(),
                        null);
                registerUser(user);
            } else {
                Toast.makeText(getApplicationContext(), "Formulario Invalido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addFormValidations() {
        awesomeValidation.addValidation(this, R.id.editName,
                RegexTemplate.NOT_EMPTY, R.string.invalid_name);
        awesomeValidation.addValidation(this, R.id.editEmail,
                Patterns.EMAIL_ADDRESS, R.string.invalid_email);
        awesomeValidation.addValidation(this, R.id.editEmail,
                RegexTemplate.NOT_EMPTY, R.string.invalid_email);
        awesomeValidation.addValidation(this, R.id.editPassword,
                RegexTemplate.NOT_EMPTY, R.string.invalid_password);
        awesomeValidation.addValidation(this, R.id.editCpf,
                RegexTemplate.NOT_EMPTY, R.string.invalid_cpf);
        awesomeValidation.addValidation(this, R.id.editPhone,
                RegexTemplate.NOT_EMPTY, R.string.invalid_phone);
    }

    private void registerUser(final User user) {

    }
}