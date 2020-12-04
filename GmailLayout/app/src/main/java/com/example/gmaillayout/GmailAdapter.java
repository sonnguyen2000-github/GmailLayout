package com.example.gmaillayout;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GmailAdapter extends RecyclerView.Adapter<GmailAdapter.GmailViewHolder>{
    private List<Gmail> emails;
    private GmailCLickListener listener;
    private int position;

    public GmailAdapter(List<Gmail> emails, GmailCLickListener listener){
        this.emails = emails;
        this.listener = listener;
    }

    public int getPosition(){
        return position;
    }

    public void setPosition(int position){
        this.position = position;
    }

    @NonNull
    @Override
    public GmailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.gmail_layout, parent, false);
        return new GmailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GmailViewHolder holder, int position){
        Gmail email = emails.get(position);
        holder.avatar.getBackground().setColorFilter(email.getAvatar(), PorterDuff.Mode.SRC);
        holder.avatar.setText(email.getEmailAddress().toUpperCase().charAt(0) + "");
        holder.emailAddress.setText(email.getEmailAddress());
        holder.emailContent.setText(email.getMailContent());
        holder.time.setText(email.getDate().toString());
        holder.star.setBackgroundResource(
                email.isStarred() ? android.R.drawable.star_on : android.R.drawable.star_off);
    }

    @Override
    public int getItemCount(){
        return emails.size();
    }

    class GmailViewHolder extends RecyclerView.ViewHolder implements
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{
        private TextView avatar, emailAddress, emailContent, time, star;

        public GmailViewHolder(@NonNull View itemView){
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            emailAddress = itemView.findViewById(R.id.email_address);
            emailContent = itemView.findViewById(R.id.email_content);
            time = itemView.findViewById(R.id.time);
            star = itemView.findViewById(R.id.star);
            //
            star.setOnClickListener(v -> {
                Log.v("ID", getAdapterPosition() + "");
                Gmail gmail = emails.get(getAdapterPosition());
                boolean isStarred = gmail.isStarred();
                star.setBackgroundResource(
                        isStarred ? android.R.drawable.star_off : android.R.drawable.star_on);
                gmail.setStarred(!isStarred);
            });
            //
            emailContent.setOnClickListener(v -> {
                Log.v("ID", getAdapterPosition() + "");
                Gmail gmail = emails.get(getAdapterPosition());
                Intent detail = new Intent(itemView.getContext(), DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("detail", "From: " + gmail.getEmailAddress() +
                                           "\nto: ME\n--------------------\n" +
                                           gmail.getMailContent() + "\nSent at " + gmail.getDate());
                detail.putExtras(bundle);
                itemView.getContext().startActivity(detail);
            });
            //
            itemView.setOnLongClickListener(v -> {
                setPosition(getAdapterPosition());
                return false;
            });
            //
            if(listener != null){
                itemView.setOnClickListener(v -> listener.OnItemClick(getAdapterPosition()));
            }
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                ContextMenu.ContextMenuInfo menuInfo){
            menu.add(0, 100, 0, "REPLY");
            menu.add(0, 101, 1, "REMOVE");
            //
        }

        @Override
        public boolean onMenuItemClick(MenuItem item){
            Log.v(emailAddress.getText() + "", item.getItemId() + "");
            return true;
        }
    }

    public interface GmailCLickListener{
        void OnItemClick(int position);
    }
}
