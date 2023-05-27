package com.tuituidan.openhub.bean.entity;

import com.tuituidan.openhub.bean.dto.CardIconDto;
import com.tuituidan.openhub.bean.dto.CardZipDto;
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
 * Card.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/2
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "T_CARD")
@DynamicInsert
@DynamicUpdate
public class Card implements ISortEntity<Card> {

    private static final long serialVersionUID = -4281175586987550016L;

    @Id
    @Column(name = "C_ID", length = 32)
    private String id;

    @Column(name = "C_TYPE", length = 100)
    private String type;

    @Column(name = "C_CATEGORY", length = 32)
    private String category;

    @Column(name = "C_ICON", length = 400)
    private CardIconDto icon;

    @Column(name = "C_TITLE", length = 200)
    private String title;

    @Column(name = "C_CONTENT", length = 400)
    private String content;

    @Column(name = "C_URL", length = 200)
    private String url;

    @Column(name = "C_ZIP", length = 400)
    private CardZipDto zip;

    @Column(name = "N_SORT")
    private Integer sort;
}
