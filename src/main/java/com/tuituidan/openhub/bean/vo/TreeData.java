package com.tuituidan.openhub.bean.vo;

import java.util.List;

/**
 * TreeData.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2023/12/3
 */
public interface TreeData<T> {

    /**
     * getId
     *
     * @return String
     */
    String getId();

    /**
     * setId
     *
     * @param id id
     * @return T
     */
    T setId(String id);

    /**
     * getPid
     *
     * @return String
     */
    String getPid();

    /**
     * setPid
     *
     * @param pid pid
     * @return T
     */
    T setPid(String pid);

    /**
     * getSort
     *
     * @return Integer
     */
    Integer getSort();

    /**
     * setSort
     *
     * @param sort sort
     * @return T
     */
    T setSort(Integer sort);

    /**
     * getChildren
     *
     * @return List
     */
    List<T> getChildren();

    /**
     * setChildren
     *
     * @param children children
     * @return T
     */
    T setChildren(List<T> children);

}
