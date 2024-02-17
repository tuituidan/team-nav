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
@Table(name = "nav_card", schema = "team_nav")
@DynamicInsert
@DynamicUpdate
public class Card implements ISortEntity<Card> {

    private static final long serialVersionUID = -4281175586987550016L;

    @Id
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "type", length = 100)
    private String type;

    @Column(name = "category", length = 32)
    private String category;

    @Column(name = "icon", length = 400)
    private CardIconDto icon;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "content", length = 400)
    private String content;

    @Column(name = "url", length = 200)
    private String url;

    @Column(name = "zip", length = 600)
    private CardZipDto zip;

    @Column(name = "sort")
    private Integer sort;
}
