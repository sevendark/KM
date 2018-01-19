package com.asiainfo.km.util;

import com.asiainfo.km.pojo.TableResult;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiyuze on 2017/8/3.
 */
public class TableResultUtil {
    public static<T> TableResult<T> page2Result(Page<T> page, Integer draw, Long allNum){
        TableResult<T> result = new TableResult<>();
        List<T> dataList = new ArrayList<>();
        if(page instanceof HighlightPage) {
            HighlightPage<T> highlightPage = (HighlightPage<T>) page;
            List<HighlightEntry<T>> entityList = highlightPage.getHighlighted();
            if (entityList.size() == 0) {
                dataList = page.getContent();
            }else{
                List<T> finalDataList = dataList;
                entityList.forEach(e->{
                    Class<?> clzz = e.getEntity().getClass();
                    List<HighlightEntry.Highlight> highlights = e.getHighlights();
                    highlights.forEach(b->{
                        String fieldName = b.getField().getName();
                        try {
                            Field field = clzz.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            StringBuilder sb = new StringBuilder("");
                            b.getSnipplets().forEach(sb::append);
                            field.set(e.getEntity(),sb.toString());
                        } catch (NoSuchFieldException | IllegalAccessException e1) {
                            e1.printStackTrace();
                        }
                    });
                    finalDataList.add(e.getEntity());
                });
            }
        } else{
            dataList = page.getContent();
        }
        result.setData(dataList);
        result.setRecordsFiltered(page.getTotalElements());
        result.setRecordsTotal(allNum);
        result.setDraw(draw);
        return result;
    }


}
