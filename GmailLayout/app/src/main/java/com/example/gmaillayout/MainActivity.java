package com.example.gmaillayout;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity{
    private InboxFragment inboxFragment;
    private OutboxFragment outboxFragment;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private boolean isShownFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        viewPager2 = findViewById(R.id.fragment_holder);
        ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(this);
        viewPager2.setAdapter(viewPager2Adapter);
        viewPager2.setCurrentItem(1);
        viewPager2.setCurrentItem(0);
        //
        tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch(position){
                case 0:
                    tab.setText("INBOX");
                    break;
                case 1:
                    tab.setText("OUTBOX");
                    break;
            }
        }).attach();
        /*
        *
        * fragmentTransaction = getSupportFragmentManager().beginTransaction();
        inboxFragment = InboxFragment.newInstance();
        fragmentTransaction.replace(R.id.fragment_holder, inboxFragment);
        fragmentTransaction.commit();
        //
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        outboxFragment = OutboxFragment.newInstance();
        fragmentTransaction.commit();*/
        isShownFavorite = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume(){
        super.onResume();
        System.out.println("Resume");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.gmail_actionbar, menu);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("GMAIL_LAYOUT");
        actionBar.setDisplayShowCustomEnabled(true);
        /**/
        SearchView searchView = (SearchView) menu.findItem(R.id.search_button).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                search(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText){
                Log.v("Typing", newText);
                search(newText);
                return true;
            }
        });
        searchView.setOnCloseListener(() -> {
            searchView.clearFocus();
            reset();
            return false;
        });
        MenuItem favoriteItem = menu.findItem(R.id.favorite);
        favoriteItem.setOnMenuItemClickListener(item -> {
            favoriteItem.setIcon(isShownFavorite ? android.R.drawable.btn_star_big_off :
                                 android.R.drawable.btn_star_big_on);
            isShownFavorite = !isShownFavorite;
            search(isShownFavorite);
            Log.v("Favorite", isShownFavorite + "");
            return true;
        });
        return true;
    }

    public void search(String keyword){
        Log.v("Searching", keyword);
        if(inboxFragment.isVisible()){
            inboxFragment.search(keyword);
        }else if(outboxFragment.isVisible()){
            outboxFragment.search(keyword);
        }
        //
    }

    public void search(boolean starred){
        if(inboxFragment.isVisible()){
            inboxFragment.search(starred);
        }
    }

    public void reset(){
        if(inboxFragment.isVisible()){
            inboxFragment.reset();
        }else if(outboxFragment.isVisible()){
            outboxFragment.reset();
        }
    }

    public void newOutbox(Bundle bundle){
        outboxFragment.newOutbox(bundle);
    }

    class ViewPager2Adapter extends FragmentStateAdapter{

        public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity){
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position){
            switch(position){
                case 0:
                    inboxFragment = InboxFragment.newInstance();
                    return inboxFragment;
                case 1:
                    outboxFragment = OutboxFragment.newInstance();
                    return outboxFragment;
            }
            return null;
        }

        @Override
        public int getItemCount(){
            return 2;
        }
    }
}