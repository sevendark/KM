package com.asiainfo.km.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiyuze on 2017/8/11.
 */
public class Folder {
    private String name;
    private String path;
    private List<Folder> children = new ArrayList<>();

    public List<Folder> getChildren() {
        return children;
    }

    public void setChildren(List<Folder> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Folder{ name: " + name + ", folders: " + children + " }";
    }
}
