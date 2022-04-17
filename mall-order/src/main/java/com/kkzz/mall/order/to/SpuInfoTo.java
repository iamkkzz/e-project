package com.kkzz.mall.order.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SpuInfoTo {
    private Long id;

    private String spuName;

    private String spuDescription;

    private Long catalogId;

    private Long brandId;

    private BigDecimal weight;

    private Integer publishStatus;

    private Date createTime;

    private Date updateTime;

    private String brandName;
}
