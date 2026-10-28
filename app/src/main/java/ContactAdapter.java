package com.example.quanlydanhba;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<Contact> {

    private final Context context;
    private final ArrayList<Contact> contacts;
    private final String[] avatarColors = {"#1565C0", "#EF6C00", "#6A1B9A", "#00838F", "#2E7D32"};

    public ContactAdapter(@NonNull Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);
        this.context = context;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        }

        Contact contact = contacts.get(position);

        TextView tvAvatar = convertView.findViewById(R.id.tvAvatar);
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvPhone = convertView.findViewById(R.id.tvPhone);
        TextView tvGroup = convertView.findViewById(R.id.tvGroup);
        TextView tvFavoriteStar = convertView.findViewById(R.id.tvFavoriteStar);

        tvAvatar.setText(contact.getAvatarLetter());
        GradientDrawable bgShape = (GradientDrawable) tvAvatar.getBackground().mutate();
        bgShape.setColor(Color.parseColor(avatarColors[position % avatarColors.length]));

        tvName.setText(contact.getName());
        tvPhone.setText(contact.getPhone());
        tvGroup.setText(contact.getGroup());

        if (contact.isFavorite()) {
            tvFavoriteStar.setVisibility(View.VISIBLE);
        } else {
            tvFavoriteStar.setVisibility(View.GONE);
        }

        return convertView;
    }
}