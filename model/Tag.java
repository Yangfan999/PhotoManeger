package model;

import java.util.Objects;

/**
 * Represents one Tag
 * @author QiZeng, yangfanli, yuyuexin
 * @version IntelliJ IDEA 2017.2.4
 */
public class Tag implements java.io.Serializable {
    private String tagName;

    /**
     * Initiates a new tag
     * @param name: the name of this tag
     */
    Tag(String name){
        this.tagName = name;
    }

    /**
     * Represents this tag by a String
     * @return String tag name
     */
    @Override
    public String toString() {
        return tagName;
    }

    /**
     * Checks either obj is equals to this tag
     * @param obj object to compare
     * @return boolean if two class are equal
     */
    @Override
    public boolean equals(Object obj) {
        return obj != null
                && getClass() == obj.getClass()
                && Objects.equals(tagName, obj.toString());
    }
}
