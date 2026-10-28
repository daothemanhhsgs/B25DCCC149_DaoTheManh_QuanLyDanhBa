package com.example.quanlydanhba;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class ContactDetailActivity extends AppCompatActivity {

    private TextView tvDetailAvatar;
    private TextView tvDetailName;
    private TextView tvDetailPhone;
    private TextView tvDetailGroup;
    private TextView tvDetailGender;
    private TextView tvDetailNote;
    private CheckBox cbDetailFavorite;
    private Button btnCall;
    private Button btnBack;
    private Button btnDelete;
    private TextView tvBackDetail;

    private Contact contact;
    private int position;
    private final String[] avatarColors = {"#1565C0", "#EF6C00", "#6A1B9A", "#00838F", "#2E7D32"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        tvDetailAvatar = findViewById(R.id.tvDetailAvatar);
        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailPhone = findViewById(R.id.tvDetailPhone);
        tvDetailGroup = findViewById(R.id.tvDetailGroup);
        tvDetailGender = findViewById(R.id.tvDetailGender);
        tvDetailNote = findViewById(R.id.tvDetailNote);
        cbDetailFavorite = findViewById(R.id.cbDetailFavorite);
        btnCall = findViewById(R.id.btnCall);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);
        tvBackDetail = findViewById(R.id.tvBackDetail);

        contact = (Contact) getIntent().getSerializableExtra("contact");
        position = getIntent().getIntExtra("position", -1);

        if (contact != null) {
            tvDetailAvatar.setText(contact.getAvatarLetter());
            GradientDrawable bgShape = (GradientDrawable) tvDetailAvatar.getBackground().mutate();
            int colorIndex = position >= 0 ? position % avatarColors.length : 0;
            bgShape.setColor(Color.parseColor(avatarColors[colorIndex]));

            tvDetailName.setText(contact.getName());
            tvDetailPhone.setText(contact.getPhone());
            tvDetailGroup.setText(contact.getGroup());
            tvDetailGender.setText(contact.getGender());

            String noteText = contact.getNote();
            if (noteText == null || noteText.trim().isEmpty()) {
                tvDetailNote.setText("Không có ghi chú");
            } else {
                tvDetailNote.setText(noteText);
            }

            cbDetailFavorite.setChecked(contact.isFavorite());
        }

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contact != null) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + contact.getPhone()));
                    startActivity(callIntent);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnWithFavoriteUpdate();
            }
        });

        tvBackDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnWithFavoriteUpdate();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("position", position);
                resultIntent.putExtra("is_deleted", true);
                setResult(RESULT_OK, resultIntent);
                Toast.makeText(ContactDetailActivity.this, "Đã xóa liên hệ", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                returnWithFavoriteUpdate();
            }
        });
    }

    private void returnWithFavoriteUpdate() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("position", position);
        resultIntent.putExtra("is_deleted", false);
        resultIntent.putExtra("is_favorite", cbDetailFavorite.isChecked());
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}