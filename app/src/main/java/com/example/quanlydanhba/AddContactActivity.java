package com.example.quanlydanhba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddContactActivity extends AppCompatActivity {

    private EditText etFullName;
    private EditText etPhoneNumber;
    private Spinner spGroup;
    private RadioGroup rgGender;
    private CheckBox cbFavoriteAdd;
    private EditText etNote;
    private Button btnSaveContact;
    private TextView tvBackAdd;
    private ArrayList<String> existingPhones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        etFullName = findViewById(R.id.etFullName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        spGroup = findViewById(R.id.spGroup);
        rgGender = findViewById(R.id.rgGender);
        cbFavoriteAdd = findViewById(R.id.cbFavoriteAdd);
        etNote = findViewById(R.id.etNote);
        btnSaveContact = findViewById(R.id.btnSaveContact);
        tvBackAdd = findViewById(R.id.tvBackAdd);

        existingPhones = getIntent().getStringArrayListExtra("existing_phones");
        if (existingPhones == null) {
            existingPhones = new ArrayList<>();
        }

        String[] groups = {"Gia đình", "Bạn bè", "Công việc", "Khác"};
        ArrayAdapter<String> groupAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, groups);
        spGroup.setAdapter(groupAdapter);

        tvBackAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSaveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContact();
            }
        });
    }

    private void saveContact() {
        String name = etFullName.getText().toString().trim();
        String phone = etPhoneNumber.getText().toString().trim();
        String group = spGroup.getSelectedItem().toString();
        String note = etNote.getText().toString().trim();
        boolean isFavorite = cbFavoriteAdd.isChecked();

        String gender = "Nam";
        int checkedId = rgGender.getCheckedRadioButtonId();
        if (checkedId == R.id.rbFemale) {
            gender = "Nữ";
        } else if (checkedId == R.id.rbOther) {
            gender = "Khác";
        }

        if (name.isEmpty()) {
            etFullName.setError("Họ và tên không được để trống");
            etFullName.requestFocus();
            Toast.makeText(this, "Vui lòng nhập họ và tên", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!phone.matches("^0\\d{9}$")) {
            etPhoneNumber.setError("Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0");
            etPhoneNumber.requestFocus();
            Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isPhoneDuplicate(phone)) {
            etPhoneNumber.setError("Số điện thoại đã tồn tại trong danh bạ");
            etPhoneNumber.requestFocus();
            Toast.makeText(this, "Số điện thoại đã tồn tại trong danh bạ", Toast.LENGTH_SHORT).show();
            return;
        }

        Contact newContact = new Contact(name, phone, group, gender, isFavorite, note);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("new_contact", newContact);
        setResult(RESULT_OK, resultIntent);
        Toast.makeText(this, "Đã lưu liên hệ thành công", Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean isPhoneDuplicate(String phone) {
        if (existingPhones.contains(phone)) {
            return true;
        }
        for (Contact c : MainActivity.contactList) {
            if (c.getPhone().equals(phone)) {
                return true;
            }
        }
        return false;
    }
}