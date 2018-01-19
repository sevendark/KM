package com.asiainfo.km.pojo;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by jiyuze on 2017/8/3.
 */
public class TableParam {
    private Integer draw ;
    private Integer start;
    private Integer length;
    private Timestamp startTime;
    private Timestamp endTime;
    private Map<Search, String> search = new HashMap<>();
    private List<Map<Order, String>> order = new ArrayList<>();
    private List<Map<Column, String>> columns = new ArrayList<>();
    private Map<String,Object> other = new HashMap<>();

    public enum Search {
        value,
        regex
    }

    public enum Order {
        column,
        dir
    }

    public enum Column {
        data,
        name,
        searchable,
        orderable
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public List<Map<Column, String>> getColumns() {
        return columns;
    }

    public void setColumns(List<Map<Column, String>> columns) {
        this.columns = columns;
    }

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Map<Search, String> getSearch() {
        return search;
    }

    public void setSearch(Map<Search, String> search) {
        this.search = search;
    }

    public List<Map<Order, String>> getOrder() {
        return order;
    }

    public void setOrder(List<Map<Order, String>> order) {
        this.order = order;
    }

    public Map<String, Object> getOther() {
        return other;
    }

    public void setOther(Map<String, Object> other) {
        this.other = other;
    }

    @Override
    public String toString() {
        return "TableParam[ draw:" + draw + ",start:"+ start + ",length:" + length + ",search:" + search + ",order:" + order + ",columns:" + columns + "]";
    }
}
