package com.example.gmaillayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InboxFragment extends Fragment implements GmailAdapter.GmailCLickListener{
    private RecyclerView recyclerView;
    private MainActivity main;
    private Context context;
    private List<Gmail> inboxList;
    private List<Integer> colors;
    private GmailAdapter gmailAdapter;
    private boolean isFavorites;

    public InboxFragment(){
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InboxFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InboxFragment newInstance(){
        return new InboxFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context = getActivity();
        main = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);
        Integer[] color = {Color.RED, Color.BLUE, Color.GREEN, Color.CYAN, Color.YELLOW, Color.GRAY,
                           Color.DKGRAY, Color.MAGENTA, Color.LTGRAY, Color.BLACK};
        colors = new ArrayList<>();
        colors.addAll(Arrays.asList(color));
        //
        inboxList = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            Faker faker = new Faker();
            Gmail mail = new Gmail();
            int random = new Random().nextInt(colors.size() - 1);
            mail.setAvatar(colors.get(random));
            mail.setFullName(faker.name().fullName());
            mail.setEmailAddress(faker.internet().emailAddress());
            mail.setDate(faker.date().birthday());
            mail.setAddress(faker.address().city() + ", " + faker.address().country());
            mail.setMailContent("I'm " + mail.getFullName() + " from " + mail.getAddress());
            mail.setStarred(false);
            inboxList.add(mail);
            //
            Log.v("person" + i, mail.getFullName());
        }
        //
        recyclerView = view.findViewById(R.id.recycle_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,
                                                                           RecyclerView.VERTICAL,
                                                                           false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        //
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

    public void search(String keyword){
        Log.v("Searching", keyword);
        List<Gmail> resultList = new ArrayList<>();
        for(Gmail gmail : inboxList){
            if(gmail.getMailContent().toUpperCase().contains(keyword.toUpperCase())||
               gmail.getEmailAddress().toUpperCase().contains(keyword.toUpperCase())){
                resultList.add(gmail);
            }
        }
        gmailAdapter = new GmailAdapter(resultList, this);
        recyclerView.setAdapter(gmailAdapter);
    }

    public void search(boolean starred){
        Log.v("Searching", starred ? "favorite" : "");
        isFavorites = starred;
        if(!starred){
            reset();
            return;
        }
        List<Gmail> resultList = new ArrayList<>();
        for(Gmail gmail : inboxList){
            if(gmail.isStarred()){
                resultList.add(gmail);
            }
        }
        gmailAdapter = new GmailAdapter(resultList, this);
        recyclerView.setAdapter(gmailAdapter);
    }

    public void reset(){
        gmailAdapter = new GmailAdapter(inboxList, this);
        recyclerView.setAdapter(gmailAdapter);
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
        if(item.getItemId() == 100){
            Intent compose_intent = new Intent(context, ComposeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("email_address", inboxList.get(position).getEmailAddress());
            compose_intent.putExtras(bundle);
            startActivityForResult(compose_intent, 111);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == 111){
                Bundle result = data.getExtras();
                Toast.makeText(context, result.getString("result"), Toast.LENGTH_LONG).show();
                if(result.getBoolean("new_outbox")){
                    main.newOutbox(result);
                }
            }
        }
    }

    public void delete(int position){
        if(isFavorites){
            gmailAdapter.notifyDataSetChanged();
            Toast.makeText(context, "Operation cancelled", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(context, "Item " + position + " deleted", Toast.LENGTH_LONG).show();
        inboxList.remove(position);
        gmailAdapter.notifyItemRemoved(position);
    }
}