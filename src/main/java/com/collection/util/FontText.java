package com.collection.util;


public class FontText {
    
    private String text;
    
    private int pos;
    
    private String color;
    
    private Integer size;
    
    private String font;//字体  “黑体，Arial”

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getWm_text_pos() {
        return pos;
    }

    public void setWm_text_pos(int wm_text_pos) {
        this.pos = wm_text_pos;
    }

    public String getWm_text_color() {
        return color;
    }

    public void setWm_text_color(String wm_text_color) {
        this.color = wm_text_color;
    }

    public Integer getWm_text_size() {
        return size;
    }

    public void setWm_text_size(Integer wm_text_size) {
        this.size = wm_text_size;
    }

    public String getWm_text_font() {
        return font;
    }

    public void setWm_text_font(String wm_text_font) {
        this.font = wm_text_font;
    }

    public FontText(String text, int wm_text_pos, String wm_text_color,
            Integer wm_text_size, String wm_text_font) {
        super();
        this.text = text;
        this.pos = wm_text_pos;
        this.color = wm_text_color;
        this.size = wm_text_size;
        this.font = wm_text_font;
    }
    
    public FontText(String text, String wm_text_color,
            Integer wm_text_size, String wm_text_font) {
        super();
        this.text = text;
        this.color = wm_text_color;
        this.size = wm_text_size;
        this.font = wm_text_font;
    }
    
    public FontText(){}
    
}