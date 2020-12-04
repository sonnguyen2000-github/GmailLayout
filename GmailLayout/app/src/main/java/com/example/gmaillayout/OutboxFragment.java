package com.example.gmaillayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OutboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OutboxFragment extends Fragment implements GmailAdapter.GmailCLickListener{
    private RecyclerView recyclerView;
    private MainActivity main;
    private Context context;
    private List<Gmail> outboxList;
    private List<Integer> colors;
    private GmailAdapter gmailAdapter;

    public OutboxFragment(){
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OutboxFragment newInstance(){
        OutboxFragment fragment = new OutboxFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context = getContext();
        main = (MainActivity) getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_outbox, container, false);
        //
        recyclerView = view.findViewById(R.id.recycle_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,
                                                                           RecyclerView.VERTICAL,
                                                                           false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        //
        Integer[] color = {Color.RED, Color.BLUE, Color.GREEN, Color.CYAN, Color.YELLOW, Color.GRAY,
                           Color.DKGRAY, Color.MAGENTA, Color.LTGRAY, Color.BLACK};
        colors = new ArrayList<>();
        colors.addAll(Arrays.asList(color));
        //
        outboxList = new ArrayList<>();
        reset();
        //
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,
                                                                                     ItemTouchHelper.LEFT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                    @NonNull RecyclerView.ViewHolder viewHolder,
                    @NonNull RecyclerView.ViewHolder target){
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction){
                Dialog dialog = new AlertDialog.Builder(context).setTitle("DELETE CONFIRMATION")
                                                                .setMessage("")
                                                                .setPositiveButton("YES",
                                                                                   (dialog1, which) -> {
                                                                                       Log.v("CONFIRM",
                                                                                             "Yes");
                                                                                       int pos
                                                                                               = viewHolder
                                                                                               .getAdapterPosition();
                                                                                       delete(pos);
                                                                                   })
                                                                .setNegativeButton("NO",
                                                                                   (dialog1, which) -> {
                                                                                       Log.v("CONFIRM",
                                                                                             "No");
                                                                                       gmailAdapter
                                                                                               .notifyDataSetChanged();
                                                                                   }).create();
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setOnCancelListener(dialog13 -> gmailAdapter.notifyDataSetChanged());
                dialog.show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        registerForContextMenu(recyclerView);
        recyclerView.setLongClickable(true);

        return view;
    }

    public void delete(int position){
        Toast.makeText(context, "Item " + position + " deleted", Toast.LENGTH_LONG).show();
        outboxList.remove(position);
        gmailAdapter.notifyItemRemoved(position);
    }

    @Override
    public void OnItemClick(int position){
        Log.v("Item", position + "");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(!this.isVisible()){
            return false;
        }
        int position;
        position = gmailAdapter.getPosition();
        Log.v("context", position + " " + item.getItemId());
        if(item.getItemId() == 101){
            delete(position);
        }
        return super.onContextItemSelected(item);
    }

    public void search(String keyword){
        Log.v("Searching", keyword);
        List<Gmail> resultList = new ArrayList<>();
        for(Gmail gmail : outboxList){
            if(gmail.getMailContent().toUpperCase().contains(keyword.toUpperCase())||
               gmail.getEmailAddress().toUpperCase().contains(keyword.toUpperCase())){
                resultList.add(gmail);
            }
        }
        gmailAdapter = new GmailAdapter(resultList, this);
        recyclerView.setAdapter(gmailAdapter);
    }

    public void reset(){
        gmailAdapter = new GmailAdapter(outboxList, this);
        recyclerView.setAdapter(gmailAdapter);
    }

    public void newOutbox(Bundle bundle){
        Gmail mail = new Gmail();
        //
        mail.setEmailAddress(bundle.getString("email_address"));
        mail.setMailContent(bundle.getString("email_content"));
        mail.setDate(new Date());
        mail.setStarred(false);
        int random = new Random().nextInt(colors.size() - 1);
        mail.setAvatar(colors.get(random));
        //
        outboxList.add(mail);
        gmailAdapter.notifyDataSetChanged();
    }
}