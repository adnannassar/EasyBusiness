package com.myapps.easybusiness.Gui.User;

import com.bumptech.glide.Glide;
import com.myapps.easybusiness.FachLogic.Item;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapps.easybusiness.R;
import com.myapps.easybusiness.Gui.DisplyItems.DisplyItemsActivity;
import com.myapps.easybusiness.Gui.MainMenu.Main_menu_Activity;
import com.myapps.easybusiness.FachLogic.ItemForRecyclerView;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;


import java.util.ArrayList;
import java.util.List;

public class Fragment_Selling extends Fragment {
    // vars
    private ArrayList<String> imagesUrls = new ArrayList<>();
    RecyclerView recyclerViewSelling;
    List<ParseObject> parseObjects = Main_menu_Activity.objectList;
    ArrayList<ItemForRecyclerView> itemArrayList = new ArrayList<>();
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager myLayoutManager;


    public Fragment_Selling() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selling_layout, container, false);
        recyclerViewSelling = view.findViewById(R.id.layoutSelling);
        inintObjectsInRecyclerView();
        return view;

    }


    /*
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

     */

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderSelling> {
        ArrayList<ItemForRecyclerView> itemArrayList;

        public RecyclerViewAdapter(ArrayList<ItemForRecyclerView> itemArrayList) {
            this.itemArrayList = itemArrayList;
        }


        @NonNull
        @Override
        public ViewHolderSelling onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_selling, parent, false);
            ViewHolderSelling viewHolder = new ViewHolderSelling(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolderSelling holder, final int position) {
            final ItemForRecyclerView currentItem = itemArrayList.get(position);
            Glide.with(getContext())
                    .asBitmap()
                    .load(currentItem.getImageView())
                    .into(holder.imageView);
            holder.title.setText(currentItem.getTitle());
            holder.preise.setText(String.valueOf(currentItem.getPreis()));
            holder.objectId = currentItem.getObjectId();
            holder.descreption = currentItem.getDescreption();
            holder.latitude = currentItem.getLatitude();
            holder.longitude = currentItem.getLongitude();
            if (holder.imageView != null) {
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), DisplyItemsActivity.class);
                        intent.putExtra("objectId", currentItem.getObjectId());
                        intent.putExtra("title", currentItem.getTitle());
                        intent.putExtra("price", currentItem.getPreis());
                        intent.putExtra("descreption", currentItem.getDescreption());
                        intent.putExtra("latitude", currentItem.getLatitude());
                        intent.putExtra("longitude", currentItem.getLongitude());
                        startActivity(intent);
                    }
                });
            } else {
                Toast.makeText(getContext(), "NULL", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public int getItemCount() {
            // return parseObjects.size();
            // new Way
            return itemArrayList.size();

        }

        public class ViewHolderSelling extends RecyclerView.ViewHolder {
            String objectId, descreption;
            ImageView imageView;
            TextView title;
            TextView preise;
            CardView parentLayout;
            double latitude, longitude;
            Button btnDelete;

            public ViewHolderSelling(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageViewItemInRecyclerView_selling);
                title = itemView.findViewById(R.id.txttitleInRecyclerView_selling);
                preise = itemView.findViewById(R.id.txtpreisInRecyclerView_selling);
                parentLayout = itemView.findViewById(R.id.parent_layout_In_RecyclerView_selling);
                btnDelete = itemView.findViewById(R.id.btnDeleteItem);

                if (btnDelete != null) {
                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Really Delete?")
                                    .setMessage("Are you sure you want to Delete?")
                                    .setNegativeButton(android.R.string.no, null)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            ParseQuery<ParseObject> query = new ParseQuery<>("Item");
                                            if (getAdapterPosition() <= itemArrayList.size() && getAdapterPosition() != RecyclerView.NO_POSITION) {
                                                query.whereEqualTo("objectId", itemArrayList.get(getAdapterPosition()).getObjectId());
                                                query.findInBackground(new FindCallback<ParseObject>() {
                                                    @Override
                                                    public void done(List<ParseObject> objects, ParseException e) {
                                                        if (e == null && objects != null && !objects.isEmpty()) {
                                                            for (ParseObject parseObject : objects) {
                                                                parseObject.deleteInBackground(new DeleteCallback() {
                                                                    @Override
                                                                    public void done(ParseException e) {
                                                                        if (e == null) {
                                                                            itemArrayList.remove(getAdapterPosition());
                                                                            notifyItemRemoved(getAdapterPosition());
                                                                            notifyItemRangeChanged(getAdapterPosition(), itemArrayList.size());
                                                                            Toast.makeText(getContext(), "Item Deleted !", Toast.LENGTH_SHORT).show();
                                                                        } else {
                                                                            Toast.makeText(getContext(), "Item Could not be deleted", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        } else {
                                                            Toast.makeText(getContext(), "Server Error\n item not found in System!", Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }).create().show();
                        }
                    });
                }

            }

        }
    }

    private void inintObjectsInRecyclerView() {
        int c = 0;
        for (ParseObject object : parseObjects) {
            ArrayList<String> objectPhotos = new ArrayList<>();
            c++;
            ParseGeoPoint itemLocation = object.getParseGeoPoint("itemLocation");

            final ParseFile photo1 = (ParseFile) object.get("photo1");
            imagesUrls.add(photo1.getUrl());
            objectPhotos.add(photo1.getUrl());
            final ParseFile photo2 = (ParseFile) object.get("photo2");
            if (photo2 != null) objectPhotos.add(photo2.getUrl());
            final ParseFile photo3 = (ParseFile) object.get("photo3");
            if (photo3 != null) objectPhotos.add(photo3.getUrl());
            final ParseFile photo4 = (ParseFile) object.get("photo4");
            if (photo4 != null) objectPhotos.add(photo4.getUrl());
            final ParseFile photo5 = (ParseFile) object.get("photo5");
            if (photo5 != null) objectPhotos.add(photo5.getUrl());
            itemArrayList.add(new ItemForRecyclerView(object.getObjectId(), object.getString("descreption"), photo1.getUrl(),
                    object.getString("title"), object.getInt("price") + " " + object.getString("currency"), itemLocation.getLatitude(), itemLocation.getLongitude()));

            Main_menu_Activity.objectsMap.put(object.getObjectId(), objectPhotos);
        }

        Toast.makeText(getContext(), String.valueOf(c), Toast.LENGTH_LONG).show();
        inintRecyclerVIew();

    }

    private void inintRecyclerVIew() {
        recyclerViewSelling.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(getContext());
        myAdapter = new RecyclerViewAdapter(itemArrayList);
        recyclerViewSelling.setLayoutManager(myLayoutManager);
        recyclerViewSelling.setAdapter(myAdapter);
        runAnimation(recyclerViewSelling, 0);
    }

    private void runAnimation(RecyclerView recyclerView, int type) {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = null;

        if (type == 0) {       // fall down animation
            controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down);
        } else if (type == 1) {// slide from bottom animation
            controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_slide_from_bottom);
        } else if (type == 2) {// slide from right animation
            controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_slide_from_right);
        } else if (type == 3) {// slide from left animation
            controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_slide_from_left);

        }

        // set anim
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();

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
