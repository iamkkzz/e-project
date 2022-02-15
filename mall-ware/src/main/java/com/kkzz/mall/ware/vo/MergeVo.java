package com.kkzz.mall.ware.vo;

import lombok.Data;

import java.util.List;

@Data
public class MergeVo {
    private Long purchaseId;
    public List<Long> items;
}
