package uz.arena.stadium;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import uz.arena.stadium.language.Appcompat;

public class InfoActivity extends Appcompat {
    TextInputEditText name, lost;
    Button button;
    CheckBox checkBox;
    String number;
    DatabaseReference reference;
    private String authUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        reference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        authUi = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        number = Objects.requireNonNull(auth.getCurrentUser()).getPhoneNumber();
        button = findViewById(R.id.info_btn);
        checkBox = findViewById(R.id.checkBy);
        name = findViewById(R.id.name_info);
        lost = findViewById(R.id.lost_info);

        checkBox.setMovementMethod(LinkMovementMethod.getInstance());

        button.setOnClickListener(view ->
                SaveValue());
    }

    @SuppressLint("CheckResult")
    void SaveValue() {
        String Name = Objects.requireNonNull(name.getText()).toString();
        String Lost = Objects.requireNonNull(lost.getText()).toString();
        if (TextUtils.isEmpty(Name)) {
            name.setError("Name");
            return;
        } else if (TextUtils.isEmpty(Lost)) {
            lost.setError("Lost");
            return;
        } else {
            HashMap<String, String> malumotHas = new HashMap<>();
            malumotHas.put("name", Name);
            malumotHas.put("lost", Lost);
            malumotHas.put("phone", number);

            reference.child("User").child(authUi).setValue(malumotHas)
                    .addOnCompleteListener(task -> {
                        Toasty.success(InfoActivity.this, R.string.muvofaqiyatli, Toasty.LENGTH_SHORT, true).show();
                        Intent intent = new Intent(InfoActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    });
        }
    }
}