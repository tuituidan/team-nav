package com.tuituidan.openhub.bean.entity;

import java.io.Serializable;

/**
 * BaseSort.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2023/5/27
 */
public interface ISortEntity<T> extends Serializable {

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
     * getSort
     *
     * @return Integer
     */
    Integer getSort();

    /**
     * setSort
     *
     * @param sort v
     * @return T
     */
    T setSort(Integer sort);

}
