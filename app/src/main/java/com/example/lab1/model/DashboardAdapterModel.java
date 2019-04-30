package com.example.lab1.model;

import java.io.Serializable;

public class DashboardAdapterModel implements Serializable {
    private Integer layoutType;
    private Integer bg_color;
    private Integer dh_icon;
    private String text;

    public DashboardAdapterModel(Integer layoutType, Integer bg_color, Integer dh_icon, String text) {
        this.layoutType = layoutType;
        this.bg_color = bg_color;
        this.dh_icon = dh_icon;
        this.text = text;
    }

    public Integer getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(Integer layoutType) {
        this.layoutType = layoutType;
    }

    public Integer getBg_color() {
        return bg_color;
    }

    public void setBg_color(Integer bg_color) {
        this.bg_color = bg_color;
    }

    public Integer getDh_icon() {
        return dh_icon;
    }

    public void setDh_icon(Integer dh_icon) {
        this.dh_icon = dh_icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
