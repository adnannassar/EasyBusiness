package com.myapps.easybusiness.ui;

import com.bumptech.glide.Glide;
import com.myapps.easybusiness.Item;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapps.easybusiness.MainActivity;
import com.myapps.easybusiness.R;
import com.myapps.easybusiness.displayItem;
import com.myapps.easybusiness.main_menu_Activity;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class Fragment_Selling extends Fragment implements View.OnClickListener {
    // vars
    private ArrayList<String> imagesUrls = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> preises = new ArrayList<>();
    private ArrayList<String> objectIdes = new ArrayList<>();
    private ArrayList<String> descreptions = new ArrayList<>();
    private ArrayList<Double> latitudes = new ArrayList<>();
    private ArrayList<Double> longitudes = new ArrayList<>();
    RecyclerView recyclerViewSelling;

    public Fragment_Selling() { }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selling_layout, container, false);
        recyclerViewSelling = view.findViewById(R.id.layoutSelling);
     //  fillLinearLayoutSelling();
        inintObjectsInRecyclerView();
        return view;

    }


    public void fillLinearLayoutSelling() {
        ParseQuery<ParseObject> query = new ParseQuery<>("Item");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.orderByAscending("createdAt");
        query.setLimit(6);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        // Toast.makeText(getApplicationContext(),"We feound "+objects.size()+" Items",Toast.LENGTH_SHORT).show();
                        for (final ParseObject object : objects) {
                            ParseFile file = (ParseFile) object.get("photo1");
                            file.getDataInBackground(new GetDataCallback() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null && data != null && data.length > 0) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                                        // CardView vorbereiten
                                        final float scale = getResources().getDisplayMetrics().density;
                                        final CardView cardView = new CardView(getContext());
                                        LinearLayout.LayoutParams cardViewLayoutParametrs = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        cardViewLayoutParametrs.setMargins((int) (5 * scale), (int) (5 * scale), (int) (5 * scale), (int) (5 * scale));
                                        cardView.setLayoutParams(cardViewLayoutParametrs);
                                        cardView.setCardBackgroundColor(Color.parseColor("#212121"));
                                        cardView.setRadius((int) (15 * scale));
                                        cardView.setPreventCornerOverlap(false);
                                        cardView.setUseCompatPadding(false);

                                        //LinearLayout vorbereiten
                                        LinearLayout linearLayout = new LinearLayout(getContext());
                                        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                        linearLayout.setOrientation(LinearLayout.VERTICAL);

                                        //ImageView vorbereiten
                                        ImageView imageView = new ImageView(getContext());
                                        LinearLayout.LayoutParams imageViewLayoutParametrs = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        imageViewLayoutParametrs.gravity = Gravity.TOP;
                                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                        imageView.setLayoutParams(imageViewLayoutParametrs);
                                        imageView.setImageBitmap(bitmap);
                                        imageView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.imageview_height);

                                        //TextView for Title vorbereiten
                                        TextView textViewTitle = new TextView(getContext());
                                        textViewTitle.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                        textViewTitle.setPadding((int) (8 * scale), (int) (2 * scale), (int) (2 * scale), (int) (2 * scale));
                                        textViewTitle.setText(object.getString("title"));
                                        textViewTitle.setTextColor(Color.WHITE);
                                        textViewTitle.setTextSize(16);

                                        //TextView for Preis vorbereiten
                                        TextView textViewPreis = new TextView(getContext());
                                        textViewPreis.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                        textViewPreis.setPadding((int) (8 * scale), 0, 0, (int) (2 * scale));
                                        textViewPreis.setText(object.getInt("price") + " " + object.getString("currency"));
                                        textViewPreis.setTextColor(Color.WHITE);
                                        textViewPreis.setTextSize(14);

                                        //TextView for date vorbereiten
                                        TextView textViewDate = new TextView(getContext());
                                        textViewDate.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                        textViewDate.setPadding((int) (8 * scale), 0, 0, (int) (4 * scale));

                                        // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
                                        //  String date =  simpleDateFormat.format(object.getCreatedAt());

                                        LocalDate dateOfCreation = object.getCreatedAt().toInstant()
                                                .atZone(ZoneId.systemDefault())
                                                .toLocalDate();

                                        LocalDate dateOfNow = LocalDate.now();

                                        String dateString = "since " + (dateOfNow.getDayOfMonth() - dateOfCreation.getDayOfMonth()) + " days";
                                        textViewDate.setText(dateString);
                                        textViewDate.setTextColor(Color.WHITE);
                                        textViewDate.setTextSize(14);

                                        // Button vorbereiten
                                        Button btnDeleteItem = new Button(getContext());
                                        LinearLayout.LayoutParams linearParametsForBtnDelete = new LinearLayout.LayoutParams((int) (30 * scale), (int) (30 * scale));
                                        linearParametsForBtnDelete.setMargins((int) (5 * scale), (int) (5 * scale), (int) (5 * scale), (int) (5 * scale));
                                        linearParametsForBtnDelete.gravity = Gravity.END;
                                        btnDeleteItem.setLayoutParams(linearParametsForBtnDelete);
                                        btnDeleteItem.setClickable(true);
                                     // btnDeleteItem.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#59000000")));
                                        btnDeleteItem.setBackgroundResource(R.drawable.delete_sign_red_reounded_big);
                                        btnDeleteItem.setForegroundGravity(Gravity.END);
                                        btnDeleteItem.setClickable(true);
                                        btnDeleteItem.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                new AlertDialog.Builder(getContext())
                                                        .setTitle("Really Delete?")
                                                        .setMessage("Are you sure you want to Delete?")
                                                        .setNegativeButton(android.R.string.no, null)
                                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                                            public void onClick(DialogInterface arg0, int arg1) {
                                                                object.deleteInBackground();
                                                                recyclerViewSelling.removeView(cardView);
                                                            }
                                                        }).create().show();


                                            }
                                        });



                                        //Order elements to each other
                                        linearLayout.addView(imageView);
                                        linearLayout.addView(textViewTitle);
                                        linearLayout.addView(textViewPreis);
                                        linearLayout.addView(textViewDate);

                                        cardView.addView(linearLayout);
                                        cardView.addView(btnDeleteItem);

                                        recyclerViewSelling.addView(cardView);

                                    } else {
                                        e.printStackTrace();
                                        Toast.makeText(getContext(), "Could not download the Image" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(getContext(), "There are no Items to be displayed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Server is busy... please try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderSelling> {

        private ArrayList<String> images;
        private ArrayList<String> titles;
        private ArrayList<String> preices;
        private ArrayList<String> objectIds;
        private ArrayList<String> descreptions;
        private ArrayList<Double> latitudes;
        private ArrayList<Double> longitudes;

        private Context context;

        public RecyclerViewAdapter(Context context, ArrayList<String> images, ArrayList<String> titles, ArrayList<String> preices,
                                   ArrayList<String> objectIds, ArrayList<String> descreptions
                , ArrayList<Double> latitudes, ArrayList<Double> longitudes) {
            this.images = images;
            this.titles = titles;
            this.preices = preices;
            this.context = context;
            this.objectIds = objectIds;
            this.descreptions = descreptions;
            this.latitudes = latitudes;
            this.longitudes = longitudes;
        }

        @NonNull
        @Override
        public ViewHolderSelling onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_selling, parent, false);
            ViewHolderSelling viewHolder = new ViewHolderSelling(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderSelling holder, final int position) {

            Glide.with(context)
                    .asBitmap()
                    .load(images.get(position))
                    .into(holder.imageView);
            holder.title.setText(titles.get(position));
            holder.preise.setText(preices.get(position));
            holder.objectId = objectIds.get(position);
            holder.descreption = descreptions.get(position);
            holder.latitude = latitudes.get(position);
            holder.longitude = longitudes.get(position);

            if (holder.imageView != null) {
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), displayItem.class);
                        intent.putExtra("objectId", objectIdes.get(position));
                        intent.putExtra("title", titles.get(position));
                        intent.putExtra("price", preices.get(position));
                        intent.putExtra("descreption", descreptions.get(position));
                        intent.putExtra("latitude", latitudes.get(position));
                        intent.putExtra("longitude", longitudes.get(position));

                        startActivity(intent);
                    }
                });
            } else {
                Toast.makeText(context, "NULL", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public int getItemCount() {
            return images.size();
        }

        public class ViewHolderSelling extends RecyclerView.ViewHolder {
            String objectId, descreption;
            ImageView imageView;
            TextView title;
            TextView preise;
            CardView parentLayout;
            double latitude, longitude;

            public ViewHolderSelling(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageViewItemInRecyclerView_selling);
                title = itemView.findViewById(R.id.txttitleInRecyclerView_selling);
                preise = itemView.findViewById(R.id.txtpreisInRecyclerView_selling);
                parentLayout = itemView.findViewById(R.id.parent_layout_In_RecyclerView_selling);

            }
        }
    }

    private void inintObjectsInRecyclerView() {
        int c = 0;
        for (ParseObject object : main_menu_Activity.objectList) {
            ArrayList<String> objectPhotos = new ArrayList<>();
            c++;
            final ParseFile photo1 = (ParseFile) object.get("photo1");
            imagesUrls.add(photo1.getUrl());
            objectPhotos.add(photo1.getUrl());
            titles.add(object.getString("title"));
            preises.add(object.getInt("price") + " " + object.getString("currency"));
            descreptions.add(object.getString("descreption"));
            objectIdes.add(object.getObjectId());
            ParseGeoPoint itemLocation = object.getParseGeoPoint("itemLocation");
            latitudes.add(itemLocation.getLatitude());
            longitudes.add(itemLocation.getLongitude());

            final ParseFile photo2 = (ParseFile) object.get("photo2");
            if (photo2 != null) objectPhotos.add(photo2.getUrl());
            final ParseFile photo3 = (ParseFile) object.get("photo3");
            if (photo3 != null) objectPhotos.add(photo3.getUrl());
            final ParseFile photo4 = (ParseFile) object.get("photo4");
            if (photo4 != null) objectPhotos.add(photo4.getUrl());
            final ParseFile photo5 = (ParseFile) object.get("photo5");
            if (photo5 != null) objectPhotos.add(photo5.getUrl());

            main_menu_Activity.objectsMap.put(object.getObjectId(), objectPhotos);
        }

        Toast.makeText(getContext(), String.valueOf(c), Toast.LENGTH_LONG).show();
        inintRecyclerVIew();

    }

    private void inintRecyclerVIew() {

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), imagesUrls, titles, preises, objectIdes, descreptions, latitudes, longitudes);
        recyclerViewSelling.setAdapter(adapter);
        recyclerViewSelling.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    @Override
    public void onClick(View v) {

    }

    //Custom Adapter for Items List
    private class ItemsAdapter extends ArrayAdapter<Item> {
        public ItemsAdapter(Context context, ArrayList<Item> categoryItemArrayList) {
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
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_items_in_list_view_item, parent, false);
            }

            ImageView imageViewItem = convertView.findViewById(R.id.imageViewItem);
            ImageView btnDeleteItem = convertView.findViewById(R.id.btnDeleteItem);
            TextView textViewTitle = convertView.findViewById(R.id.txtPreis);
            TextView textViewPreis = convertView.findViewById(R.id.EditTextPrice);
            TextView textViewDate = convertView.findViewById(R.id.txtDate);

            Item currentItem = getItem(poition);
            if (currentItem != null) {
                imageViewItem.setImageResource(currentItem.getItemImageFlag());
                btnDeleteItem.setImageResource(currentItem.getItemImageDeleteButton());
                textViewTitle.setText(currentItem.getItemTitle());
                textViewPreis.setText(currentItem.getItemPrice());
                textViewDate.setText(currentItem.getItemDate());

            }

            return convertView;
        }
    }
}
