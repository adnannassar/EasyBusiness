package com.myapps.easybusiness;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;


import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;


public class sellingItem extends AppCompatActivity {
    Spinner spinner_category, spinner_currency;
    private ArrayList<CategoryItem> categoryItemArrayList;
    private ArrayList<String> curencyList, plzList;
    private ArrayList<Bitmap> photosBitMaps;
    private CategoryAdapter categoryAdapter;
    private CurrencyAdapter curencyAdapter, plzAdapter;
    private LinearLayout linearLayoutSelectedImages;
    static int photoCounter;
    AutoCompleteTextView editTextPlz;
    EditText editTextTitle, editTextDescritopn, editTextPrice;
    Button btnSell;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_item);
        getSupportActionBar().hide();
        photoCounter = 0;
        photosBitMaps = new ArrayList<>();


        //General Item declaration
        editTextPlz = findViewById(R.id.editTextPlz);
        linearLayoutSelectedImages = findViewById(R.id.linearLayoutSelectedImages);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescritopn = findViewById(R.id.editTextDescrition);
        editTextPrice = findViewById(R.id.EditTextPrice);
        btnSell = findViewById(R.id.btnSellTheITem);

        // Activate the button for sell because i deactivate it after one time sell to avoid that duplication of Items in Server
        btnSell.setEnabled(true);


        //Spinner declaration
        spinner_category = findViewById(R.id.spinner_category);
        spinner_currency = findViewById(R.id.spinner_currency);

        //ArrayLists initialization
        initCategoryList();
        curencyList = new ArrayList<>(Arrays.asList("EUR", "USD", "GBP", "CHF", "NOK", "SEK"));
        plzList = readFromExcel();

        //Adapters initialization
        categoryAdapter = new CategoryAdapter(this, categoryItemArrayList);
        curencyAdapter = new CurrencyAdapter(this, curencyList);
        plzAdapter = new CurrencyAdapter(this, plzList);

        //Set the Adapters to the Spinners
        spinner_category.setAdapter(categoryAdapter);
        spinner_currency.setAdapter(curencyAdapter);

        editTextPlz.setAdapter(plzAdapter);


        //Spinners Reaction
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //This Method fill the category Array list
    private void initCategoryList() {
        categoryItemArrayList = new ArrayList<>(Arrays.asList(
                new CategoryItem("Phone & Accessories", R.drawable.phonegreen),
                new CategoryItem("Electrics & Computers", R.drawable.laptopgreen),
                new CategoryItem("Cars", R.drawable.caragreen),
                new CategoryItem("Baby & Child", R.drawable.strollergreen),
                new CategoryItem("Fashion & Accessories", R.drawable.fashiongreen),
                new CategoryItem("Pets & Accessories", R.drawable.dog_green),
                new CategoryItem("Movies & Books", R.drawable.moviegreen),
                new CategoryItem("Motorcycles & Scooters", R.drawable.motorgreen),
                new CategoryItem("Houses ", R.drawable.housegreen),
                new CategoryItem("Services", R.drawable.servicesgreen),
                new CategoryItem("Others", R.drawable.othersgreen)
        ));
    }

    // Buttons Functions
    public void go_back_to_mainMenu(View view) {
        Intent intent = new Intent(this, main_menu_Activity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, main_menu_Activity.class);
        startActivity(intent);
    }

    //Custom Adapters for spinners
    private class CategoryAdapter extends ArrayAdapter<CategoryItem> {
        public CategoryAdapter(Context context, ArrayList<CategoryItem> categoryItemArrayList) {
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
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_for_spinner, parent, false);
            }
            ImageView imageViewCategory = convertView.findViewById(R.id.imgCategory);
            TextView textViewCategoryName = convertView.findViewById(R.id.txtCategory);

            CategoryItem currentItem = getItem(poition);
            if (currentItem != null) {
                imageViewCategory.setImageResource(currentItem.getCategoryImageFlag());
                textViewCategoryName.setText(currentItem.getCategoryName());
            }

            return convertView;
        }
    }

    private class CurrencyAdapter extends ArrayAdapter<String> {
        public CurrencyAdapter(Context context, ArrayList<String> categoryItemArrayList) {
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
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_curency_item, parent, false);
            }

            TextView txtCurencyName = convertView.findViewById(R.id.txtCurencyItem);

            String currentItem = getItem(poition);
            if (currentItem != null) {
                txtCurencyName.setText(currentItem);
            }

            return convertView;
        }
    }


    // Heir are the methods that we need to get Photos from the phone storage
    //
    public void addPhoto(View view) {
        if (photoCounter < 5) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    getPhoto();
                    photoCounter++;
                }
            } else {
                getPhoto();
                photoCounter++;
            }
        } else {
            Toast.makeText(this, "Sie können Maximal 5 Fotos auswählen", Toast.LENGTH_SHORT).show();
        }
    }

    public void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto();
                photoCounter++;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {

                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), selectedImage);
                Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                //Bitmap resizedBitMap = Bitmap.createScaledBitmap(bitmap, 143, 143, true);
                photosBitMaps.add(bitmap);
                CircleImageView imageView = new CircleImageView(getApplicationContext());
                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                final float scale = getResources().getDisplayMetrics().density;
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) (88 * scale), LinearLayout.LayoutParams.MATCH_PARENT);
                lp.setMargins((int) (5 * scale), 0, 0, 0);
                imageView.setLayoutParams(lp);


                linearLayoutSelectedImages.addView(imageView);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    //


    public void sellIt(View view) {

        // add to server
        if (photosBitMaps.size() > 0
                && photosBitMaps != null
                && editTextTitle.getText().toString().length() > 5
                && !editTextTitle.getText().toString().equals("     ")
                && editTextDescritopn.getText().toString().length() > 10
                && !editTextDescritopn.getText().toString().equals("          ")
                && plzList.contains(editTextPlz.getText().toString())
                && Integer.parseInt(editTextPrice.getText().toString()) > 0) {

            ParseObject object = new ParseObject("Item");
            Location location = converAdresseTOGeo(editTextPlz.getText().toString());
            ParseGeoPoint parseGeoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
            object.put("itemLocation", parseGeoPoint);
            object.put("username", ParseUser.getCurrentUser().getUsername());
            for (int i = 0; i < photosBitMaps.size(); i++) {
                // send the photos to Parse server
                // covert out selected Image so that we can send it to the Server
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photosBitMaps.get(i).compress(Bitmap.CompressFormat.WEBP, 100, stream); // 100 ist the quality
                byte[] bytesArray = stream.toByteArray();
                ParseFile file = new ParseFile("photo" + (i + 1) + ".WEBP", bytesArray);
                object.put("photo" + (i + 1), file);
            }
            object.put("title", editTextTitle.getText().toString());
            object.put("titleLowCase", editTextTitle.getText().toString().toLowerCase());
            object.put("descreption", editTextDescritopn.getText().toString());
            object.put("price", Integer.valueOf(editTextPrice.getText().toString()));
            object.put("category", spinner_category.getSelectedItem().toString());
            object.put("currency", spinner_currency.getSelectedItem().toString());

            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        FancyToast.makeText(getApplicationContext(), "Item würde erfolgreich veröffentlicht", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

                    } else {
                        FancyToast.makeText(getApplicationContext(), "Server is busy... \nplease try again later", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                    }
                }
            });
            btnSell.setEnabled(false);
            Intent intent = new Intent(this, main_menu_Activity.class);
            startActivity(intent);

        } else {

            if (editTextTitle.getText().toString().length() < 5) {
                Toast.makeText(this, "Title ist erforderlich! mindesten 5 Buchtaben", Toast.LENGTH_SHORT).show();
            } else if (editTextDescritopn.getText().toString().length() < 10) {
                Toast.makeText(this, "Descritopn ist erforderlich! mindesten 10 Buchtaben ", Toast.LENGTH_SHORT).show();
            } else if (!plzList.contains(editTextPlz.getText().toString())) {
                Toast.makeText(this, "Bitte nur gültige PLZ eingebn oder einfach auswählen", Toast.LENGTH_SHORT).show();
            } else if (Integer.parseInt(editTextPrice.getText().toString()) <= 0 || editTextPrice.getText().toString().isEmpty()) {
                Toast.makeText(this, "Bitte nur gültige Preis eingebn", Toast.LENGTH_SHORT).show();
            } else if (photosBitMaps.size() == 0) {
                Toast.makeText(this, "Bitte mindestens ein Foto aushwählen", Toast.LENGTH_SHORT).show();
            }

        }
    }


    public String encodedToUTF8(String response) {
        try {
            response = new String(response.getBytes("iso-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(getApplicationContext(), "EROOOOOOR", Toast.LENGTH_SHORT).show();
        }
        return response;
    }

    public ArrayList<String> readFromExcel() {

        ArrayList<String> result = new ArrayList<>();
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("PlzListe.xls");
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("Cp1252");
            Workbook workbook = Workbook.getWorkbook(inputStream, ws);

            Sheet sheet = workbook.getSheet(0);

            int row = sheet.getRows();
            int columns = sheet.getColumns();

            for (int i = 0; i < row; i++) {
                String line = "";
                for (int j = 0; j < columns; j++) {
                    Cell cell = sheet.getCell(j, i);
                    line += cell.getContents() + " ";
                }
                result.add(line);
            }
            return result;
        } catch (Exception ex) {
            Toast.makeText(this, "ERROR , " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
        return null;
    }

    public class RoundedImageView extends AppCompatImageView {
        private Paint mPaint;
        private Path mPath;
        private Bitmap mBitmap;
        private Matrix mMatrix;
        private int mRadius = convertDpToPixel(10);
        private int mWidth;
        private int mHeight;
        private Drawable mDrawable;

        public RoundedImageView(Context context) {
            super(context);
            init();
        }

        public RoundedImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public RoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init() {
            mPaint = new Paint();
            mPaint.setColor(Color.WHITE);

            mPath = new Path();
        }

        public int convertDpToPixel(int dp) {
            DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
        }

        @Override
        public void setImageDrawable(Drawable drawable) {
            mDrawable = drawable;
            if (drawable == null) {
                return;
            }
            mBitmap = drawableToBitmap(drawable);
            int bDIWidth = mBitmap.getWidth();
            int bDIHeight = mBitmap.getHeight();
            //Fit to screen.
            float scale;
            if ((mHeight / (float) bDIHeight) >= (mWidth / (float) bDIWidth)) {
                scale = mHeight / (float) bDIHeight;
            } else {
                scale = mWidth / (float) bDIWidth;
            }
            float borderLeft = (mWidth - (bDIWidth * scale)) / 2;
            float borderTop = (mHeight - (bDIHeight * scale)) / 2;
            mMatrix = getImageMatrix();
            RectF drawableRect = new RectF(0, 0, bDIWidth, bDIHeight);
            RectF viewRect = new RectF(borderLeft, borderTop, (bDIWidth * scale) + borderLeft, (bDIHeight * scale) + borderTop);
            mMatrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.CENTER);
            invalidate();
        }

        private Bitmap drawableToBitmap(Drawable drawable) {
            Bitmap bitmap;
            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                if (bitmapDrawable.getBitmap() != null) {
                    return bitmapDrawable.getBitmap();
                }
            }
            if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            mWidth = MeasureSpec.getSize(widthMeasureSpec);
            mHeight = MeasureSpec.getSize(heightMeasureSpec);
            if ((mDrawable != null) && (mHeight > 0) && (mWidth > 0)) {
                setImageDrawable(mDrawable);
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (mBitmap == null) {
                return;
            }
            canvas.drawColor(Color.TRANSPARENT);
            mPath.reset();
            mPath.moveTo(0, mRadius);
            mPath.lineTo(0, canvas.getHeight());
            mPath.lineTo(canvas.getWidth(), canvas.getHeight());
            mPath.lineTo(canvas.getWidth(), mRadius);
            mPath.quadTo(canvas.getWidth(), 0, canvas.getWidth() - mRadius, 0);
            mPath.lineTo(mRadius, 0);
            mPath.quadTo(0, 0, 0, mRadius);
            canvas.drawPath(mPath, mPaint);
            canvas.clipPath(mPath);
            canvas.drawBitmap(mBitmap, mMatrix, mPaint);
        }
    }

    public Location converAdresseTOGeo(String adresse) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        Location location = new Location(LocationManager.GPS_PROVIDER);

        try {
            List<Address> addressList = geocoder.getFromLocationName(adresse, 1);
            if (addressList != null && addressList.size() > 0) {
                location.setLatitude(addressList.get(0).getLatitude());
                location.setLongitude(addressList.get(0).getLongitude());
                return location;
            } else {
                Toast.makeText(this, "Error by converting the Adress to Geo", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return location;
    }


}

