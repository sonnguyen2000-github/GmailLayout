package com.example.gmaillayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity{
    Map<Integer, Boolean> starMap;
    List<Faker> data;
    List<Integer> colors;
    ScrollView scrollView;
    LinearLayout linearLayout;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        Integer color[] = {Color.RED, Color.BLUE, Color.GREEN, Color.CYAN, Color.YELLOW, Color.GRAY, Color.DKGRAY, Color.MAGENTA, Color.LTGRAY, Color.BLACK};
        colors = new ArrayList<>();
        for(int e : color){
            colors.add(e);
        }
        //
        data = new ArrayList<>();
        starMap = new LinkedHashMap<>();
        for(int i = 0; i < 20; i++){
            data.add(new Faker());
            Log.v("person" + i, data.get(i).name().fullName());
        }
        //
        scrollView = findViewById(R.id.body);
        linearLayout = findViewById(R.id.linear_layout);
        linearLayout.setOnClickListener(v -> {
        });
        //
        for(int i = 0; i < data.size(); i++){
            View view = LayoutInflater.from(this).inflate(R.layout.info_layout, linearLayout, false);
            view.setId(i);
            TextView email = view.findViewById(R.id.email);
            TextView detail = view.findViewById(R.id.detail);
            TextView time = view.findViewById(R.id.time);
            TextView avatar = view.findViewById(R.id.avatar_text);
            starMap.put(view.getId(), false);
            view.setOnClickListener(v -> {
                System.out.println(v.getId());
                TextView starred = v.findViewById(R.id.star);
                if(!starMap.get(v.getId())){
                    starMap.replace(v.getId(), true);
                    starred.setBackgroundResource(android.R.drawable.star_on);
                }else{
                    starMap.replace(v.getId(), false);
                    starred.setBackgroundResource(android.R.drawable.star_off);
                }

            });
            detail.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                System.out.println(v.getRootView().getId());
                startActivity(intent);
            });
            email.setOnClickListener(v -> {
            });
            time.setOnClickListener(v -> {
            });
            avatar.setOnClickListener(v -> {
            });
            //
            Faker temp = data.get(i);
            email.setText(temp.internet().emailAddress());
            detail.setText("I'm " + temp.name().fullName() + " from " + temp.address().city() + ", " + temp.address().country());
            time.setText(temp.date().birthday().toString());
            avatar.setText(temp.internet().emailAddress().toUpperCase().charAt(0) + "");
            //
            int random = new Random().nextInt(colors.size() - 1);
            avatar.getBackground().setColorFilter(colors.get(random), PorterDuff.Mode.SRC);
            linearLayout.addView(view);
            //
        }
    }
}