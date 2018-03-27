package com.bitbill.www.common.base.model.entity;

/**
 * Created by isanwenyu on 2018/3/27.
 */

public class TabItem {
    private String coinId;
    private String text;
    private int iconId;
    private String iconUrl;

    public TabItem(String coinId, String text, int iconId, String iconUrl) {
        this.coinId = coinId;
        this.text = text;
        this.iconId = iconId;
        this.iconUrl = iconUrl;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
}
