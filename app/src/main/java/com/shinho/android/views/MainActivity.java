package com.shinho.android.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.shinho.android.views.widget.SearchBar;

public class MainActivity extends AppCompatActivity {

    SearchBar searchBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchBar = findViewById(R.id.search_bar);
        searchBar.setSearchMode(true);
        searchBar.initSearchBar("测试hint", new SearchBar.OnSearchListener() {
            @Override
            public void onSearchRemove() {
                Log.d("test", "onSearchRemove() called");
            }

            @Override
            public void onSearch(@NonNull String searchKey) {
                Log.d("test", "onSearch() called with: searchKey = [" + searchKey + "]");
            }
        });
    }
}