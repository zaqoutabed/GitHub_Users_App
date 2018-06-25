package com.zaqoutabed.github.github_users_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.zaqoutabed.github.github_users_app.model.Item;

import java.util.List;

public class GitHubUsersAdapter extends Adapter<GitHubUsersAdapter.GitHubViewHolder>{

    private List<Item> itemList;
    private OnListItemClickListener mOnListItemClickListener;
    private Context mContext;

    GitHubUsersAdapter(Context context, List<Item> itemList) {
        this.mContext = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public GitHubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_row, parent, false);
        return new GitHubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GitHubViewHolder holder, int position) {
        holder.bind(position);
        holder.itemCardView.setOnClickListener(v->{
            if (mOnListItemClickListener != null)
                mOnListItemClickListener.onListItemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return itemList!=null?itemList.size():0;
    }

    public void setOnListItemClickListener(OnListItemClickListener onListItemClickListener) {
        if (this.mOnListItemClickListener == null)
            this.mOnListItemClickListener = onListItemClickListener;
    }

    class GitHubViewHolder extends ViewHolder{
        private ImageView mAvatarImageView;
        private TextView  mUserNameTextView,mUrlTextView;
        CardView itemCardView;
        GitHubViewHolder(View itemView) {
            super(itemView);
            this.mAvatarImageView = itemView.findViewById(R.id.avatar_image_view);
            this.mUserNameTextView = itemView.findViewById(R.id.user_name_text_view);
            this.mUrlTextView = itemView.findViewById(R.id.url_text_view);
            itemCardView = itemView.findViewById(R.id.item_card_view);
        }

        void bind(int position) {
            this.mUserNameTextView.setText(itemList.get(position).getLogin());
            this.mUrlTextView.setText(itemList.get(position).getUrl());
            Log.d("avatar", itemList.get(position).getAvatarUrl());
            Picasso.with(mContext)
                    .load(itemList.get(position).getAvatarUrl())
                    .placeholder(R.drawable.launcher)
                    .error(R.drawable.launcher)
                    .into(this.mAvatarImageView);
        }
    }

    interface OnListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }
}
