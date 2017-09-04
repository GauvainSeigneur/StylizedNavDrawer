package com.gauvainseigneur.stylizednavdrawer.menulist;

/**
 * Created by gauvain on 16/06/16.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gauvainseigneur.stylizednavdrawer.R;

import java.util.List;


public class NavMenuRVAdapter extends RecyclerView.Adapter<NavMenuViewHolder>  {
    private List<NavMenuItemObject> itemListNavMenuItem;
    private Context context;

    private int position;


    public NavMenuRVAdapter(Context context, List<NavMenuItemObject> itemListNavMenuItem) {
        this.itemListNavMenuItem = itemListNavMenuItem;
        this.context = context;

    }
    @Override
    public NavMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_navdrawer, parent, false);
        NavMenuViewHolder rcv = new NavMenuViewHolder(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(final NavMenuViewHolder holder, int position) {
        holder.menuIconBackground.setColorFilter(itemListNavMenuItem.get(position).getMenuIconBackground());
        holder.menuIcon.setImageResource(itemListNavMenuItem.get(position).getMenuIcon());
        holder.menuTitle.setText(itemListNavMenuItem.get(position).getMenuTitle());

    }



    @Override
    public int getItemCount() {
        return this.itemListNavMenuItem.size();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


}

