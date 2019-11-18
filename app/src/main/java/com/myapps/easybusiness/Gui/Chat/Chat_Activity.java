package com.myapps.easybusiness.Gui.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.myapps.easybusiness.FachLogic.ItemForRecyclerView;
import com.myapps.easybusiness.Gui.DisplyItems.DisplyItemsActivity;
import com.myapps.easybusiness.Gui.MainMenu.Main_menu_Activity;
import com.myapps.easybusiness.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Chat_Activity extends AppCompatActivity {
    static Button btnSendMessage;
    public static List<ParseObject> objectList = getObjects();
    RecyclerView recyclerView;
    ArrayList<MessageItem> messageItemArrayList = new ArrayList<>();
    MessageRecyclerViewAdapter myAdapter;
    RecyclerView.LayoutManager myLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_);
        recyclerView = findViewById(R.id.recyclerViewMessages);

        inintObjectsInRecyclerView();

    }


    class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolderMessage> {
        ArrayList<MessageItem> messageItemArrayList;

        public MessageRecyclerViewAdapter(ArrayList<MessageItem> messageItemArrayList) {
            this.messageItemArrayList = messageItemArrayList;
        }


        @NonNull
        @Override
        public ViewHolderMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_for_recycler_view, parent, false);
            ViewHolderMessage viewHolder = new ViewHolderMessage(view);
            return viewHolder;

        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderMessage holder, int position) {
            final MessageItem currentItem = messageItemArrayList.get(position);
            holder.txtMessageSender.setText(currentItem.getTxtMessageSender());
            holder.txtMessageReceiver.setText(currentItem.getTxtMessageReceiver());
            holder.sender = currentItem.getSender();
            holder.receiver = currentItem.getReceiver();
        }

        @Override
        public int getItemCount() {
            return messageItemArrayList.size();
        }

        class ViewHolderMessage extends RecyclerView.ViewHolder {
            TextView txtMessageSender, txtMessageReceiver;
            String sender, receiver;

            public ViewHolderMessage(@NonNull View itemView) {
                super(itemView);
                txtMessageSender = itemView.findViewById(R.id.txtMessageSender);
                txtMessageReceiver =itemView.findViewById(R.id.txtMessageReceiver);
            }
        }
    }

    class MessageItem {
        private String txtMessageSender, txtMessageReceiver, sender, receiver;

        public MessageItem(String txtMessageSender, String txtMessageReceiver, String sender, String receiver) {
            this.txtMessageSender = txtMessageSender;
            this.txtMessageReceiver = txtMessageReceiver;
            this.sender = sender;
            this.receiver = receiver;
        }

        public String getTxtMessageSender() {
            return txtMessageSender;
        }

        public String getTxtMessageReceiver() {
            return txtMessageReceiver;
        }

        public String getSender() {
            return sender;
        }

        public String getReceiver() {
            return receiver;
        }
    }


    private void inintObjectsInRecyclerView() {
        int c = 0;
        for (ParseObject object : objectList) {
            c++;
            messageItemArrayList.add(new MessageItem(object.getString("title"), object.getString("descreption"), "TEST", "TEST"));
        }

        Toast.makeText(getApplicationContext(), String.valueOf(c), Toast.LENGTH_LONG).show();
        inintRecyclerVIew();

    }

    private void inintRecyclerVIew() {
        recyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(this);
        myAdapter = new MessageRecyclerViewAdapter(messageItemArrayList);
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.setAdapter(myAdapter);
        runAnimation(recyclerView, 3);
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
}
