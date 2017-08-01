package com.diglesia.hw2017mobiledev.lab5;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the fragment into the container.

        FragmentManager fm = getSupportFragmentManager();
        Fragment existingFragment = fm.findFragmentById(R.id.container);
        if (existingFragment == null) {
            Fragment wikipediaArticleListFragment = new WikipediaArticleListFragment();
            fm.beginTransaction().replace(R.id.container, wikipediaArticleListFragment).commit();
        }

    }
}
