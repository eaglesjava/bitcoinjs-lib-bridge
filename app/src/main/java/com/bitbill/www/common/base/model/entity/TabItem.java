package com.bitbill.www.common.base.model.entity;

/**
 * Created by isanwenyu on 2018/3/27.
 */

public class TabItem extends Entity {
    private String coinId;
    private String symbol;
    private int iconId;
    private String iconUrl;
    private boolean isHide;

    public TabItem(String coinId, String text, int iconId, String iconUrl) {
        this.coinId = coinId;
        this.symbol = text;
        this.iconId = iconId;
        this.iconUrl = iconUrl;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String text) {
        this.symbol = text;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public boolean isHide() {
        return isHide;
    }

    public void setHide(boolean hide) {
        isHide = hide;
    }
}
