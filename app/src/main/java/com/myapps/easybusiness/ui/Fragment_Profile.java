package com.myapps.easybusiness.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myapps.easybusiness.R;

import java.util.ArrayList;
import java.util.Arrays;

public class Fragment_Profile extends Fragment {
    ArrayList<profileItem> itemArrayList ;
    ItemProfileAdapter adapter;
    ListView listView ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_layout, container, false);
        listView = view.findViewById(R.id.listViewProfile);
        itemArrayList = new ArrayList<>(Arrays.asList(
                new profileItem("Promote your item","increase your selling chances",R.drawable.launched_rocket_100px),
                new profileItem("Premium membership","no Ads more",R.drawable.diamond_100px),
                new profileItem("Payment with Paypal","connect with Paypal",R.drawable.paypal_100px),
                new profileItem("Visit Help Center","find answers to your questions",R.drawable.help_100px)
        ));
        adapter = new ItemProfileAdapter(getContext(),itemArrayList);
        listView.setAdapter(adapter);
        return view;
    }
    private class ItemProfileAdapter extends ArrayAdapter<profileItem> {
        public ItemProfileAdapter(Context context, ArrayList<profileItem> categoryItemArrayList) {
            super(context, 0, categoryItemArrayList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return initView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return initView(position, convertView, parent);
        }

        private View initView(int poition, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item, parent, false);
            }

            TextView currentProfileItemTxtName = convertView.findViewById(R.id.txtName);
            TextView currentProfileItemTxtDescreption = convertView.findViewById(R.id.txtDescreption);
            ImageView currentProfileItemImageFlage = convertView.findViewById(R.id.imageViewProfileItem);

            profileItem currentItem = getItem(poition);
            if (currentItem != null) {
                currentProfileItemTxtName.setText(currentItem.getName());
                currentProfileItemTxtDescreption.setText(currentItem.getDescreption());
                currentProfileItemImageFlage.setImageResource(currentItem.getImageFlag());
            }

            return convertView;
        }
    }
}
