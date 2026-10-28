package com.example.quanlydanhba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Contact> contactList = new ArrayList<>();

    private TextView tvTotalCount;
    private TextView tvFavoriteCount;
    private ListView lvContacts;
    private Button btnAddContact;
    private ContactAdapter adapter;

    private final ActivityResultLauncher<Intent> addContactLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Contact newContact = (Contact) result.getData().getSerializableExtra("new_contact");
                    if (newContact != null) {
                        contactList.add(newContact);
                        adapter.notifyDataSetChanged();
                        updateStatistics();
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> detailContactLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    int position = result.getData().getIntExtra("position", -1);
                    boolean isDeleted = result.getData().getBooleanExtra("is_deleted", false);
                    boolean isFavorite = result.getData().getBooleanExtra("is_favorite", false);

                    if (position >= 0 && position < contactList.size()) {
                        if (isDeleted) {
                            contactList.remove(position);
                        } else {
                            contactList.get(position).setFavorite(isFavorite);
                        }
                        adapter.notifyDataSetChanged();
                        updateStatistics();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvFavoriteCount = findViewById(R.id.tvFavoriteCount);
        lvContacts = findViewById(R.id.lvContacts);
        btnAddContact = findViewById(R.id.btnAddContact);

        if (contactList.isEmpty()) {
            initSampleData();
        }

        adapter = new ContactAdapter(this, contactList);
        lvContacts.setAdapter(adapter);
        updateStatistics();

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
                ArrayList<String> existingPhones = new ArrayList<>();
                for (Contact c : contactList) {
                    existingPhones.add(c.getPhone());
                }
                intent.putStringArrayListExtra("existing_phones", existingPhones);
                addContactLauncher.launch(intent);
            }
        });

        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact selectedContact = contactList.get(position);
                Intent intent = new Intent(MainActivity.this, ContactDetailActivity.class);
                intent.putExtra("contact", selectedContact);
                intent.putExtra("position", position);
                detailContactLauncher.launch(intent);
            }
        });
    }

    private void initSampleData() {
        contactList.add(new Contact("Nguyễn Văn An", "0901234567", "Bạn bè", "Nam", false, "Bạn học đại học"));
        contactList.add(new Contact("Trần Thị Bình", "0912345678", "Gia đình", "Nữ", true, "Chị gái, hay gọi buổi tối"));
        contactList.add(new Contact("Lê Minh Châu", "0987654321", "Công việc", "Nữ", false, "Đồng nghiệp cùng phòng"));
    }

    private void updateStatistics() {
        int total = contactList.size();
        int favCount = 0;
        for (Contact c : contactList) {
            if (c.isFavorite()) {
                favCount++;
            }
        }
        tvTotalCount.setText(String.valueOf(total));
        tvFavoriteCount.setText(String.valueOf(favCount));
    }
}