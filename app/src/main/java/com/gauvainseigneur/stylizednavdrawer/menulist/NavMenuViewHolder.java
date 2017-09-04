package com.gauvainseigneur.stylizednavdrawer.menulist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gauvainseigneur.stylizednavdrawer.R;


/**
 * Created by gse on 10/04/2017.
 */

public class NavMenuViewHolder extends RecyclerView.ViewHolder {
    public View view;
    public ImageView menuIconBackground;
    public ImageView menuIcon;
    public TextView menuTitle;
    public RelativeLayout menuBadge;
    public TextView menuBadgeCounter;

    public NavMenuViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        menuIconBackground = (ImageView) itemView.findViewById(R.id.menu_item_icon_background);
        menuIcon = (ImageView) itemView.findViewById(R.id.menu_item_icon);
        menuTitle = (TextView) itemView.findViewById(R.id.menu_item_title);

    }
}
