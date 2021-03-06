package com.myapps.easybusiness.Gui.DisplyItems;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.myapps.easybusiness.R;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ItemsPager extends PagerAdapter {


    private Context context;
    ArrayList<String> bitmapArrayLsit;

    public ItemsPager(Context context, ArrayList<String> bitmapArrayLsit) {
        this.context = context;
        this.bitmapArrayLsit = bitmapArrayLsit;
        Fresco.initialize(context);
    }

    /*
    This callback is responsible for creating a page. We inflate the layout and set the drawable
    to the ImageView based on the position. In the end we add the inflated layout to the parent
    container .This method returns an object key to identify the page view, but in this example page view
    itself acts as the object key
    */

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
         final View view = LayoutInflater.from(context).inflate(R.layout.display_pager_item, null);
         TouchImageView imageView = view.findViewById(R.id.imageItem);
         //Uri uri = Uri.parse(getImageAt(position));
        // imageView.setImageURI(uri);
         Picasso.with(context).load(getImageAt(position)).into(imageView);
        //Glide.with(context).load(getImageAt(position)).into(imageView);
        // imageView.setImageBitmap(getImageAt(position));
        // imageView.setImageDrawable(context.getResources().getDrawable(getImageAt(position)));
         container.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        return view;
    }

    /*
   This callback is responsible for destroying a page. Since we are using view only as the
   object key we just directly remove the view from parent container
   */
    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    /*
    Returns the count of the total pages
    */
    @Override
    public int getCount() {
        return bitmapArrayLsit.size();
    }

    /*
    Used to determine whether the page view is associated with object key returned by instantiateItem.
    Since here view only is the key we return view==object
    */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    private String getImageAt(int position) {
        return bitmapArrayLsit.get(position);
    }




}




