package com.example.frinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class RegistrationActivity extends AppCompatActivity {


    private EditText mEmail, mPassword, mName, mCity;
    private DatePicker mBirth;
    private Button mRegister;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private CheckBox mOption0, mOption1, mOption2, mOption3, mOption4, mOption5, mOption6, mOption7, mOption8, mOption9;
    int age;

    private RadioGroup mRadioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        System.out.println("entra registro");
        mAuth = FirebaseAuth.getInstance();


        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Toast.makeText(RegistrationActivity.this, "Usuario registrado exitosamente!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };


        mRegister = (Button) findViewById(R.id.register);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mBirth = (DatePicker) findViewById(R.id.birth);

        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mName = (EditText) findViewById(R.id.name);
        mCity = (EditText) findViewById(R.id.city);
        mOption0 = (CheckBox) findViewById(R.id.checkBox0);
        mOption1 = (CheckBox) findViewById(R.id.checkBox1);
        mOption2 = (CheckBox) findViewById(R.id.checkBox2);
        mOption3 = (CheckBox) findViewById(R.id.checkBox3);
        mOption4 = (CheckBox) findViewById(R.id.checkBox4);
        mOption5 = (CheckBox) findViewById(R.id.checkBox5);
        mOption6 = (CheckBox) findViewById(R.id.checkBox6);
        mOption7 = (CheckBox) findViewById(R.id.checkBox7);
        mOption8 = (CheckBox) findViewById(R.id.checkBox8);
        mOption9 = (CheckBox) findViewById(R.id.checkBox9);


        mRegister.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


                // Obtener la fecha seleccionada del DatePicker
                int day = mBirth.getDayOfMonth();
                int month = mBirth.getMonth();
                int year = mBirth.getYear();

// Crear una instancia de Calendar y establecerla en la fecha seleccionada
                Calendar birthDate = Calendar.getInstance();
                birthDate.set(year, month, day);


// Guardar la fecha de nacimiento y la edad en el mapa de información de usuario
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String birthString = dateFormat.format(birthDate.getTime());
                try {
                    age = age(birthString);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                int selectId = mRadioGroup.getCheckedRadioButtonId();

                final RadioButton radioButton = (RadioButton) findViewById(selectId);

                if (radioButton.getText() == null) {
                    return;
                }

                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();

                final String city = mCity.getText().toString();

                final String name = mName.getText().toString();


                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, "sign up error", Toast.LENGTH_SHORT).show();
                        } else {
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                            Map userInfo = new HashMap<>();
                            userInfo.put("name", name);
                            userInfo.put("city", city);
                            userInfo.put("sex", radioButton.getText().toString());
                            userInfo.put("profileImageUrl", "https://firebasestorage.googleapis.com/v0/b/frinder-2bd56.appspot.com/o/currentImage%2FFrinder.jpeg?alt=media&token=6ff2b4d1-0f53-4607-ba2a-199925e3ab26");
                            userInfo.put("birthdate", birthString);
                            userInfo.put("option0", mOption0.isChecked());
                            userInfo.put("option1", mOption1.isChecked());
                            userInfo.put("option2", mOption2.isChecked());
                            userInfo.put("option3", mOption3.isChecked());
                            userInfo.put("option4", mOption4.isChecked());
                            userInfo.put("option5", mOption5.isChecked());
                            userInfo.put("option6", mOption6.isChecked());
                            userInfo.put("option7", mOption7.isChecked());
                            userInfo.put("option8", mOption8.isChecked());
                            userInfo.put("option9", mOption9.isChecked());


                            currentUserDb.updateChildren(userInfo);
                            Intent intent = new Intent(RegistrationActivity.this, ChooseLoginRegistrationActivity.class);
                            startActivity(intent);
                            return;


                        }
                    }
                });
            }
        });

    }

    private int age(String birthString) throws ParseException {

        // Convertir el birthString a un objeto Calendar
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar birthDate = Calendar.getInstance();
        birthDate.setTime(dateFormat.parse(birthString));

// Calcular la edad del usuario
        Calendar currentDate = Calendar.getInstance();
        int age = currentDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        return age;
    }

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

}