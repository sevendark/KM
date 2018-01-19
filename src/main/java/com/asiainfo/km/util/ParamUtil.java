package com.asiainfo.km.util;

import com.asiainfo.km.pojo.TableParam;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jiyuze on 2017/8/3.
 */
public class ParamUtil {
    public static Pageable param2Page(TableParam param){
        List<Map<TableParam.Order, String>> orders = param.getOrder();
        List<Map<TableParam.Column, String>> columns = param.getColumns();
        List<Sort.Order> orderList = new ArrayList<>();
        orders.forEach(info->{
            Integer columnNum = Integer.valueOf(info.get(TableParam.Order.column));
            Map<TableParam.Column, String> column = columns.get(columnNum);
            if(Boolean.valueOf(column.get(TableParam.Column.orderable))){
                String columnName = column.get(TableParam.Column.data);
                String orderStr = info.get(TableParam.Order.dir);
                Sort.Order order = new Sort.Order(string2Direction(orderStr),columnName);
                orderList.add(order);
            }
        });
        Sort sort = new Sort(orderList);
        Pageable page = new PageRequest(param.getStart()/param.getLength(),param.getLength(),sort);
        return page;
    }

    private static Sort.Direction string2Direction(String order){
        switch (order){
            case "asc": return Sort.Direction.ASC;
            case  "desc": return Sort.Direction.DESC;
            default: return Sort.Direction.ASC;
        }
    }

}
