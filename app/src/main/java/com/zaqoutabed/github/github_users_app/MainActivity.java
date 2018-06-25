package com.zaqoutabed.github.github_users_app;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zaqoutabed.github.github_users_app.api.RetrofitClientInstance;
import com.zaqoutabed.github.github_users_app.model.Item;
import com.zaqoutabed.github.github_users_app.model.Response;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private GitHubUsersAdapter gitHubUsersAdapter;
    private List<Item> itemList;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView githubUsersRecyclerView;
    private ProgressBar loadingProgressBar;
    private TextView errorTextView;
    private int cPage = 1, scrolledItems, visibleItems, totalItems;
    private boolean isScrolling = false;
    private SwipeRefreshLayout parentSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        githubUsersRecyclerView = findViewById(R.id.github_users_recycler_view);
        loadingProgressBar = findViewById(R.id.loading_progress_bar);
        parentSwipeRefreshLayout = findViewById(R.id.parent_swipe_refresh_layout);
        errorTextView = findViewById(R.id.error_text_view);

        itemList = new ArrayList<>();
        gitHubUsersAdapter = new GitHubUsersAdapter(this, itemList);
        linearLayoutManager = new LinearLayoutManager(this);
        githubUsersRecyclerView.setHasFixedSize(true);
        githubUsersRecyclerView.setLayoutManager(linearLayoutManager);
        githubUsersRecyclerView.setAdapter(gitHubUsersAdapter);

        parentSwipeRefreshLayout.setOnRefreshListener(() -> {
            cPage = 1;
            itemList.clear();
            loadData(cPage);
            errorTextView.setVisibility(View.GONE);
        });
        githubUsersRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    isScrolling = true;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrolledItems = linearLayoutManager.findFirstVisibleItemPosition();
                if (isScrolling && (visibleItems + scrolledItems == totalItems)){
                    isScrolling = false;
                    loadData(++cPage);
                }

            }
        });

        gitHubUsersAdapter.setOnListItemClickListener(clickedItemIndex ->{
            Intent intent = new Intent(this, UserPageActivity.class);
            intent.putExtra("USER_NAME", itemList.get(clickedItemIndex).getLogin());
            startActivity(intent);
        });

        loadData(cPage);
    }

    private void loadData(int cPage) {
        errorTextView.setVisibility(View.GONE);
        githubUsersRecyclerView.setVisibility(View.VISIBLE);
        if (cPage != 1)
            loadingProgressBar.setVisibility(View.VISIBLE);

        //Log.d("loadData", "loadData: " + cPage);
        //
        //new Handler().postDelayed(() -> {
        //           for (int i = 0 ; i < 20; i++){
        //               Item item = new Item();
        //               int mm = (int) ((Math.random() * 100) + 200);
        //                item.setId(mm);
        //                item.setLogin("Item user " + mm);
        //               item.setAvatarUrl("url");
        //                item.setUrl("www.www.www");
        //                itemList.add(item);
        //                gitHubUsersAdapter.notifyDataSetChanged();
        //                //gitHubUsersAdapter.addUserItem(new Item());
        //                loadingProgressBar.setVisibility(View.GONE);
        //            }
        //        }, 5000);

        Call<Response> responseCall = RetrofitClientInstance.getRetrofitClientInstance().getUsers("tom", 20, cPage);
        responseCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                loadingProgressBar.setVisibility(View.GONE);
                errorTextView.setVisibility(View.GONE);
                if (parentSwipeRefreshLayout.isRefreshing())
                    parentSwipeRefreshLayout.setRefreshing(false);
                Response response1 = response.body();

                if ((response1 != null ? response1.getItems() : null) != null)
                    itemList.addAll(response1.getItems());
                gitHubUsersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                loadingProgressBar.setVisibility(View.GONE);
                errorTextView.setVisibility(View.VISIBLE);
                githubUsersRecyclerView.setVisibility(View.GONE);
                if (parentSwipeRefreshLayout.isRefreshing())
                    parentSwipeRefreshLayout.setRefreshing(false);
                Log.d("onFailure", "onFailure: something went wrong !");
            }
        });
    }
}
