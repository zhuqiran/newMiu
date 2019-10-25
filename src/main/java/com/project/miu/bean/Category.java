package com.project.miu.bean;


import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@DynamicUpdate
public class Category {
    @Id
    @GeneratedValue
    private long id;
    private long categoryUuid;
    private String categoryName;
    private Date createTime;
    private Date updateTime;
    private long delete;
}
