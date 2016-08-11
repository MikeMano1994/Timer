package com.tryking.EasyList._bean.viewHistoryBean;

/**
 * Created by 26011 on 2016/8/3.
 */
public class ViewHistoryGroupData {
    private String date;
    private String oneWord;

    public ViewHistoryGroupData(String date, String oneWord, long id) {
        this.date = date;
        this.oneWord = oneWord;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOneWord() {
        return oneWord;
    }

    public void setOneWord(String oneWord) {
        this.oneWord = oneWord;
    }

    @Override
    public String toString() {
        return "ViewHistoryGroupData{" +
                "date='" + date + '\'' +
                ", oneWord='" + oneWord + '\'' +
                ", id=" + id +
                '}';
    }
}
