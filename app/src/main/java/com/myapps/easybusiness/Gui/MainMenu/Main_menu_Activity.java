package com.myapps.easybusiness.Gui.MainMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.myapps.easybusiness.R;
import com.myapps.easybusiness.Gui.SellingItems.sellingItem;
import com.myapps.easybusiness.Gui.DisplyItems.DisplyItemsActivity;
import com.myapps.easybusiness.Gui.User.userTapped;
import com.myapps.easybusiness.FachLogic.ItemForRecyclerView;
import com.myapps.easybusiness.Utilites.LoadMore;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import jxl.biff.drawing.TextObjectRecord;

public class Main_menu_Activity extends AppCompatActivity {


    //vars

    SearchView searchView;
    static Button btnDiscover, btnSell, btnUser;
    public static List<ParseObject> objectList = getObjects();
    public static LinkedHashMap<String, ArrayList<String>> objectsMap;
    private ArrayList<String> imagesUrls = new ArrayList<>();
    RecyclerView recyclerView;
    ArrayList<ItemForRecyclerView> itemArrayList = new ArrayList<>();
    public RecyclerViewAdapter myAdapter;
    RecyclerView.LayoutManager myLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main_menu_);

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        recyclerView = findViewById(R.id.main_recycler_view);
        searchView = findViewById(R.id.searchview);
        btnDiscover = findViewById(R.id.btnDiscover);
        btnSell = findViewById(R.id.btnSell);
        btnUser = findViewById(R.id.btnUser);

        objectList = getObjects();
        objectsMap = new LinkedHashMap<>();

        initMainMenu();
        inintObjectsInRecyclerView(0, 10);
        inintRecyclerVIew();

        //Search Functions
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.getFilter().filter(newText);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                return true;
            }
        });

    }

    public void initMainMenu() {
        searchView.setQueryHint(Html.fromHtml("<font color = #959595>" + getResources().getString(R.string.search_hint) + "</font>"));
        btnDiscover.setTextColor(Color.parseColor("#ffffff"));
        btnUser.setTextColor(Color.parseColor("#227671"));
        btnSell.setTextColor(Color.parseColor("#227671"));
        // btnDiscover.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_button_green_darkl) );

    }

    public void goToUserActivity(View view) {
        Intent intent = new Intent(this, userTapped.class);
        btnUser.setTextColor(Color.parseColor("#ffffff"));
        btnDiscover.setTextColor(Color.parseColor("#227671"));
        startActivity(intent);
    }

    public void startSellingActivity(View view) {
        Intent intent = new Intent(this, sellingItem.class);
        btnSell.setTextColor(Color.parseColor("#ffffff"));
        btnDiscover.setTextColor(Color.parseColor("#227671"));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                }).create().show();

    }


    public void refreshPage() {
        finish();
        startActivity(this.getIntent());
    }

    public void discover(View view) {
        refreshPage();

    }

    public void sayHi(View view) {
        Toast.makeText(this,"Hi",Toast.LENGTH_SHORT).show();
    }


    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
        private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
        LoadMore loadMore;
        boolean isLoading;
        Activity activity;
        int visibleThreshold = 5;
        int lastVisibleItem, totalItemCount;


        ArrayList<ItemForRecyclerView> itemArrayList;
        ArrayList<ItemForRecyclerView> itemArrayListFull; // we need this list for search

        public RecyclerViewAdapter(RecyclerView recyclerView, Activity activity, ArrayList<ItemForRecyclerView> itemArrayList) {
            this.activity = activity;
            this.itemArrayList = itemArrayList;
            itemArrayListFull = new ArrayList<>(itemArrayList); // this is the copy of list, if we write itemArrayListFull  = itemArrayList then we have one pointer to both lists
            final GridLayoutManager  gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = gridLayoutManager.getItemCount();
                    lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (loadMore != null) {
                            loadMore.onLoadMore();
                            isLoading = true;
                        }
                    }
                }
            });
        }

        @Override
        public int getItemViewType(int position) {
            return itemArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }

        public void setLoadMore(LoadMore loadMore) {
            this.loadMore = loadMore;
        }

        public void setLoaded() {
            isLoading = false;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_ITEM) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
                ItemViewHolder itemViewHolder = new ItemViewHolder(view);
                return itemViewHolder;
            } else if (viewType == VIEW_TYPE_LOADING) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
                LoadingViewHolder loadingViewHolder = new LoadingViewHolder(view);
                return loadingViewHolder;
            }

            return null;
        }


        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof ItemViewHolder) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                final ItemForRecyclerView currentItem = itemArrayList.get(position);
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(currentItem.getImageView())
                        .into(itemViewHolder.imageView);
                itemViewHolder.title.setText(currentItem.getTitle());
                itemViewHolder.preise.setText(String.valueOf(currentItem.getPreis()));
                itemViewHolder.objectId = currentItem.getObjectId();
                itemViewHolder.descreption = currentItem.getDescreption();
                itemViewHolder.latitude = currentItem.getLatitude();
                itemViewHolder.longitude = currentItem.getLongitude();
                if (itemViewHolder.imageView != null) {
                    itemViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), DisplyItemsActivity.class);
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
                    Toast.makeText(getApplicationContext(), "NULL", Toast.LENGTH_SHORT).show();
                }
            } else if (holder instanceof LoadingViewHolder) {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }

        }

        @Override
        public int getItemCount() {
            return itemArrayList.size();
        }

        // this method for search
        @Override
        public Filter getFilter() {
            return filter;
        }

        private Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<ItemForRecyclerView> filterdList = new ArrayList<>();
                // if search box empty we give all the data that we have without filtering
                if (constraint == null || constraint.length() == 0) {
                    filterdList.addAll(itemArrayListFull);
                } else {
                    String filertPattern = constraint.toString().toLowerCase().trim();
                    for (ItemForRecyclerView item : itemArrayListFull) {
                        if (item.getTitle().toLowerCase().contains(filertPattern)) {
                            filterdList.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filterdList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemArrayList.clear();
                itemArrayList.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            String objectId, descreption;
            ImageView imageView;
            TextView title;
            TextView preise;
            CardView parentLayout;
            double latitude, longitude;


            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageViewItemInRecyclerView);
                title = itemView.findViewById(R.id.txttitleInRecyclerView);
                preise = itemView.findViewById(R.id.txtpreisInRecyclerView);
                parentLayout = findViewById(R.id.parent_layout_In_RecyclerView);

            }
        }

        public class LoadingViewHolder extends RecyclerView.ViewHolder {
            ProgressBar progressBar;

            public LoadingViewHolder(@NonNull View itemView) {
                super(itemView);
                progressBar = itemView.findViewById(R.id.progress_Bar);
            }
        }
    }


    private void inintObjectsInRecyclerView(int start, int end) {
        int c = 0;
        for (int i = start; i < end; i++) {
            ArrayList<String> objectPhotos = new ArrayList<>();
            c++;
            ParseGeoPoint itemLocation = objectList.get(i).getParseGeoPoint("itemLocation");

            final ParseFile photo1 = (ParseFile) objectList.get(i).get("photo1");
            imagesUrls.add(photo1.getUrl());
            objectPhotos.add(photo1.getUrl());
            final ParseFile photo2 = (ParseFile) objectList.get(i).get("photo2");
            if (photo2 != null) objectPhotos.add(photo2.getUrl());
            final ParseFile photo3 = (ParseFile) objectList.get(i).get("photo3");
            if (photo3 != null) objectPhotos.add(photo3.getUrl());
            final ParseFile photo4 = (ParseFile) objectList.get(i).get("photo4");
            if (photo4 != null) objectPhotos.add(photo4.getUrl());
            final ParseFile photo5 = (ParseFile) objectList.get(i).get("photo5");
            if (photo5 != null) objectPhotos.add(photo5.getUrl());
            itemArrayList.add(new ItemForRecyclerView(objectList.get(i).getObjectId(), objectList.get(i).getString("descreption"), photo1.getUrl(),
                    objectList.get(i).getString("title"), objectList.get(i).getInt("price") + " " + objectList.get(i).getString("currency"), itemLocation.getLatitude(), itemLocation.getLongitude()));

            Main_menu_Activity.objectsMap.put(objectList.get(i).getObjectId(), objectPhotos);
        }
        if(end == start){
            Toast.makeText(getApplicationContext(), "Load data complete ! No data more", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getApplicationContext(), c + " elements loaded !", Toast.LENGTH_SHORT).show();

        }


    }

    private void inintRecyclerVIew() {
        recyclerView.setHasFixedSize(true);
        myLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(myLayoutManager);
        myAdapter = new RecyclerViewAdapter(recyclerView, this, itemArrayList);
        recyclerView.setAdapter(myAdapter);
        // set Load More Event
        myAdapter.setLoadMore(new LoadMore() {
            @Override
            public void onLoadMore() {
                if (itemArrayList.size() <= objectList.size()) {
                    itemArrayList.add(null);
                    myAdapter.notifyItemInserted(itemArrayList.size() - 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            itemArrayList.remove(itemArrayList.size() - 1);
                            myAdapter.notifyItemRemoved(itemArrayList.size());

                            // load more data
                            int index = itemArrayList.size();
                            int end ;
                            if (objectList.size() - index >= 10) {
                                end = index + 10;
                            } else {
                                end =index + (objectList.size() - index);
                            }

                            inintObjectsInRecyclerView(index, end);
                            myAdapter.notifyDataSetChanged();
                            //runAnimation(recyclerView, 1);
                            myAdapter.setLoaded();
                        }

                    }, 2000);
                } else {
                    Toast.makeText(getApplicationContext(), "Load data complete !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        runAnimation(recyclerView, 2);
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

    public static List<ParseObject> getObjects() {
        ParseQuery<ParseObject> query = new ParseQuery<>("Item");
        query.orderByDescending("createdAt");

        try {
            return query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    public void fillGridLayoutMainMenuFromArray() {


        if (objectList.size() > 0) {
            int id = -1;
            for (final ParseObject object : objectList) {
                id++;
                final ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
                final ParseFile photo1 = (ParseFile) object.get("photo1");
                final ParseFile photo2 = (ParseFile) object.get("photo2");
                final ParseFile photo3 = (ParseFile) object.get("photo3");
                final ParseFile photo4 = (ParseFile) object.get("photo4");
                final ParseFile photo5 = (ParseFile) object.get("photo5");

                final int finalId = id;

                if (photo1 != null) {
                    photo1.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if (e == null && data != null && data.length > 0) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                bitmapArrayList.add(bitmap);
                                // CardView vorbereiten
                                final float scale = getResources().getDisplayMetrics().density;
                                CardView cardView = new CardView(getApplicationContext());
                                LinearLayout.LayoutParams cardViewLayoutParametrs = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                cardViewLayoutParametrs.setMargins((int) (5 * scale), (int) (5 * scale), (int) (5 * scale), (int) (5 * scale));
                                cardView.setLayoutParams(cardViewLayoutParametrs);
                                cardView.getLayoutParams().width = (int) getResources().getDimension(R.dimen.cardViewWidth);
                                cardView.setCardBackgroundColor(Color.BLACK);
                                cardView.setRadius((int) (15 * scale));
                                cardView.setPreventCornerOverlap(false);
                                cardView.setUseCompatPadding(false);

                                //LinearLayout vorbereiten
                                LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                                linearLayout.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                linearLayout.setOrientation(LinearLayout.VERTICAL);

                                //ImageView vorbereiten
                                ImageView imageView = new ImageView(getApplicationContext());
                                LinearLayout.LayoutParams imageViewLayoutParametrs = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                imageViewLayoutParametrs.gravity = Gravity.TOP;
                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                imageView.setLayoutParams(imageViewLayoutParametrs);
                                imageView.setImageBitmap(bitmap);
                                imageView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.imageview_height);

                                //TextView for Title vorbereiten
                                TextView textViewTitle = new TextView(getApplicationContext());
                                textViewTitle.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                textViewTitle.setPadding((int) (8 * scale), (int) (2 * scale), (int) (2 * scale), (int) (2 * scale));
                                textViewTitle.setText(object.getString("title"));
                                textViewTitle.setTextColor(Color.WHITE);
                                textViewTitle.setTextSize(16);

                                //TextView for Preis vorbereiten
                                TextView textViewPreis = new TextView(getApplicationContext());
                                textViewPreis.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                textViewPreis.setPadding((int) (8 * scale), 0, 0, (int) (4 * scale));
                                textViewPreis.setText(object.getInt("price") + " " + object.getString("currency"));
                                textViewPreis.setTextColor(Color.WHITE);
                                textViewPreis.setTextSize(12);


                                //Order elements to each other
                                linearLayout.addView(imageView);
                                linearLayout.addView(textViewTitle);
                                linearLayout.addView(textViewPreis);
                                cardView.addView(linearLayout);
                                gridLayoutMainMenu.addView(cardView);

                                // set OnClickListener for each item that we download
                                cardView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(), DisplyItemsActivity.class);
                                        intent.putExtra("title", object.getString("title"));
                                        intent.putExtra("descreption", object.getString("descreption"));
                                        intent.putExtra("price", object.getInt("price"));
                                        intent.putExtra("currency", object.getString("currency"));
                                        intent.putExtra("id", finalId);
                                        startActivity(intent);
                                        // but bitmap

                                    }
                                });

                            } else {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Could not download the Image" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                if (photo2 != null) {
                    photo2.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if (e == null && data != null && data.length > 0) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                bitmapArrayList.add(bitmap);
                            }
                        }
                    });
                }
                if (photo3 != null) {
                    photo3.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if (e == null && data != null && data.length > 0) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                bitmapArrayList.add(bitmap);
                            }
                        }
                    });
                }
                if (photo4 != null) {
                    photo4.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if (e == null && data != null && data.length > 0) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                bitmapArrayList.add(bitmap);
                            }
                        }
                    });
                }
                if (photo5 != null) {
                    photo5.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if (e == null && data != null && data.length > 0) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                bitmapArrayList.add(bitmap);
                            }
                        }
                    });
                }
                objectsArrayList.add(new objectFromServer(finalId, bitmapArrayList));
            }

        } else {
            Toast.makeText(getApplicationContext(), "Ther are no Items to be displayed", Toast.LENGTH_SHORT).show();
        }


    }
    */
    /*
    public void fillGridLayoutMainMenu() {
        ParseQuery<ParseObject> query = new ParseQuery<>("Item");
        //query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.orderByAscending("createdAt");
        query.setLimit(10);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        int id = -1;
                        for (final ParseObject object : objects) {
                            id++;
                            final ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
                            final ParseFile photo1 = (ParseFile) object.get("photo1");
                            final ParseFile photo2 = (ParseFile) object.get("photo2");
                            final ParseFile photo3 = (ParseFile) object.get("photo3");
                            final ParseFile photo4 = (ParseFile) object.get("photo4");
                            final ParseFile photo5 = (ParseFile) object.get("photo5");

                            final int finalId = id;

                            if (photo1 != null) {
                                photo1.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        if (e == null && data != null && data.length > 0) {
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                            bitmapArrayList.add(bitmap);
                                            // CardView vorbereiten
                                            final float scale = getResources().getDisplayMetrics().density;
                                            CardView cardView = new CardView(getApplicationContext());
                                            LinearLayout.LayoutParams cardViewLayoutParametrs = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                            cardViewLayoutParametrs.setMargins((int) (5 * scale), (int) (5 * scale), (int) (5 * scale), (int) (5 * scale));
                                            cardView.setLayoutParams(cardViewLayoutParametrs);
                                            cardView.getLayoutParams().width = (int) getResources().getDimension(R.dimen.cardViewWidth);
                                            cardView.setCardBackgroundColor(Color.BLACK);
                                            cardView.setRadius((int) (15 * scale));
                                            cardView.setPreventCornerOverlap(false);
                                            cardView.setUseCompatPadding(false);

                                            //LinearLayout vorbereiten
                                            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                                            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                            linearLayout.setOrientation(LinearLayout.VERTICAL);

                                            //ImageView vorbereiten
                                            ImageView imageView = new ImageView(getApplicationContext());
                                            LinearLayout.LayoutParams imageViewLayoutParametrs = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                            imageViewLayoutParametrs.gravity = Gravity.TOP;
                                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                            imageView.setLayoutParams(imageViewLayoutParametrs);
                                            imageView.setImageBitmap(bitmap);
                                            imageView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.imageview_height);

                                            //TextView for Title vorbereiten
                                            TextView textViewTitle = new TextView(getApplicationContext());
                                            textViewTitle.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                            textViewTitle.setPadding((int) (8 * scale), (int) (2 * scale), (int) (2 * scale), (int) (2 * scale));
                                            textViewTitle.setText(object.getString("title"));
                                            textViewTitle.setTextColor(Color.WHITE);
                                            textViewTitle.setTextSize(16);

                                            //TextView for Preis vorbereiten
                                            TextView textViewPreis = new TextView(getApplicationContext());
                                            textViewPreis.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                            textViewPreis.setPadding((int) (8 * scale), 0, 0, (int) (4 * scale));
                                            textViewPreis.setText(object.getInt("price") + " " + object.getString("currency"));
                                            textViewPreis.setTextColor(Color.WHITE);
                                            textViewPreis.setTextSize(12);


                                            //Order elements to each other
                                            linearLayout.addView(imageView);
                                            linearLayout.addView(textViewTitle);
                                            linearLayout.addView(textViewPreis);
                                            cardView.addView(linearLayout);
                                            gridLayoutMainMenu.addView(cardView);

                                            // set OnClickListener for each item that we download
                                            cardView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(getApplicationContext(), DisplyItemsActivity.class);
                                                    intent.putExtra("title", object.getString("title"));
                                                    intent.putExtra("descreption", object.getString("descreption"));
                                                    intent.putExtra("price", object.getInt("price"));
                                                    intent.putExtra("currency", object.getString("currency"));
                                                    intent.putExtra("id", finalId);
                                                    startActivity(intent);
                                                    // but bitmap

                                                }
                                            });

                                        } else {
                                            e.printStackTrace();
                                            Toast.makeText(getApplicationContext(), "Could not download the Image" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            if (photo2 != null) {
                                photo2.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        if (e == null && data != null && data.length > 0) {
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                            bitmapArrayList.add(bitmap);
                                        }
                                    }
                                });
                            }
                            if (photo3 != null) {
                                photo3.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        if (e == null && data != null && data.length > 0) {
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                            bitmapArrayList.add(bitmap);
                                        }
                                    }
                                });
                            }
                            if (photo4 != null) {
                                photo4.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        if (e == null && data != null && data.length > 0) {
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                            bitmapArrayList.add(bitmap);
                                        }
                                    }
                                });
                            }
                            if (photo5 != null) {
                                photo5.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        if (e == null && data != null && data.length > 0) {
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                            bitmapArrayList.add(bitmap);
                                        }
                                    }
                                });
                            }
                            objectsArrayList.add(new objectFromServer(finalId, bitmapArrayList));
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Ther are no Items to be displayed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server is busy... please try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    public void fillGridLayoutMainMenuAfterSearch(String searchQuery) {
        ParseQuery<ParseObject> query = new ParseQuery<>("Item");
        query.whereContains("titleLowCase", searchQuery.toLowerCase());
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        // if we found results after search then we clear the gridLayoutMainMenu to display only the searched Items
                        gridLayoutMainMenu.removeAllViews();
                        for (final ParseObject object : objects) {
                            final ParseFile photo1 = (ParseFile) object.get("photo1");


                            photo1.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null && data != null && data.length > 0) {
                                        final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                                        // CardView vorbereiten
                                        final float scale = getResources().getDisplayMetrics().density;
                                        CardView cardView = new CardView(getApplicationContext());
                                        LinearLayout.LayoutParams cardViewLayoutParametrs = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        cardViewLayoutParametrs.setMargins((int) (5 * scale), (int) (5 * scale), (int) (5 * scale), (int) (5 * scale));
                                        cardView.setLayoutParams(cardViewLayoutParametrs);
                                        cardView.getLayoutParams().width = (int) getResources().getDimension(R.dimen.cardViewWidth);
                                        cardView.setCardBackgroundColor(Color.BLACK);
                                        cardView.setRadius((int) (15 * scale));
                                        cardView.setPreventCornerOverlap(false);
                                        cardView.setUseCompatPadding(false);

                                        //LinearLayout vorbereiten
                                        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                                        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                        linearLayout.setOrientation(LinearLayout.VERTICAL);

                                        //ImageView vorbereiten
                                        ImageView imageView = new ImageView(getApplicationContext());
                                        LinearLayout.LayoutParams imageViewLayoutParametrs = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        imageViewLayoutParametrs.gravity = Gravity.TOP;
                                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                        imageView.setLayoutParams(imageViewLayoutParametrs);
                                        imageView.setImageBitmap(bitmap);
                                        imageView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.imageview_height);

                                        //TextView for Title vorbereiten
                                        TextView textViewTitle = new TextView(getApplicationContext());
                                        textViewTitle.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                        textViewTitle.setPadding((int) (8 * scale), (int) (2 * scale), (int) (2 * scale), (int) (2 * scale));
                                        textViewTitle.setText(object.getString("title"));
                                        textViewTitle.setTextColor(Color.WHITE);
                                        textViewTitle.setTextSize(16);

                                        //TextView for Preis vorbereiten
                                        TextView textViewPreis = new TextView(getApplicationContext());
                                        textViewPreis.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                        textViewPreis.setPadding((int) (8 * scale), 0, 0, (int) (4 * scale));
                                        textViewPreis.setText(object.getInt("price") + " " + object.getString("currency"));
                                        textViewPreis.setTextColor(Color.WHITE);
                                        textViewPreis.setTextSize(12);


                                        //Order elements to each other
                                        linearLayout.addView(imageView);
                                        linearLayout.addView(textViewTitle);
                                        linearLayout.addView(textViewPreis);
                                        cardView.addView(linearLayout);
                                        gridLayoutMainMenu.addView(cardView);


                                        // set the interaction
                                        cardView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getApplicationContext(), DisplyItemsActivity.class);
                                                intent.putExtra("title", object.getString("title"));
                                                intent.putExtra("descreption", object.getString("descreption"));
                                                intent.putExtra("price", object.getInt("price"));
                                                intent.putExtra("currency", object.getString("currency"));
                                                startActivity(intent);
                                            }
                                        });


                                    } else {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "Could not download the Image" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "There are no Items founded!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Server is busy... please try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
*/
    /*
    public class objectFromServer {
        int id;
        ArrayList<Bitmap> photosList;

        public objectFromServer(int id, ArrayList<Bitmap> photosList) {
            this.id = id;
            this.photosList = photosList;
        }

        public int getId() {
            return id;
        }

        public ArrayList<Bitmap> getPhotosList() {
            return photosList;
        }
    }

    static public List<ParseObject> getObjects() {
        ParseQuery<ParseObject> query = new ParseQuery<>("Item");
        query.orderByAscending("createdAt");
        try {
            return query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    */
}
