package com.gauvainseigneur.stylizednavdrawer.menulist;

/**
 * Created by gse on 10/03/2017.
 */

public class NavMenuItemObject {

    private int menuIconBackground;
    private int menuIcon;
    private String menuTitle;

    public NavMenuItemObject(int _menuIconBackground, int _menuIcon, String _menuTitle) {
        this.menuIconBackground = _menuIconBackground;
        this.menuIcon= _menuIcon;
        this.menuTitle= _menuTitle;
    }

    public int getMenuIconBackground() {
        return menuIconBackground;
    }

    public void setMenuIconBackground (int _menuIconBackground) {
        this.menuIconBackground = _menuIconBackground;
    }

    public int getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon (int _menuIcon) {
        this.menuIcon = _menuIcon;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle (String _menuTitle) {
        this.menuTitle = _menuTitle;
    }




}