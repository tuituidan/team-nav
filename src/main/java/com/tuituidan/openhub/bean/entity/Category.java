package com.tuituidan.openhub.bean.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Category.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/2
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "T_CATEGORY")
@DynamicUpdate
@DynamicInsert
public class Category implements ISortEntity<Category> {
    private static final long serialVersionUID = -369054106198786491L;

    @Id
    @Column(name = "C_ID", length = 32)
    private String id;

    @Column(name = "C_ICON", length = 400)
    private String icon;

    @Column(name = "C_NAME", length = 100)
    private String name;

    @Column(name = "N_PRIVATE_CARD")
    private Boolean privateCard;

    @Column(name = "N_VALID")
    private Boolean valid;

    @Column(name = "N_SORT")
    private Integer sort;

}
