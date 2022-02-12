package com.kkzz.mall.product.vo;

import com.kkzz.mall.product.entity.AttrEntity;
import com.kkzz.mall.product.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

@Data
public class AttrGroupWithAttrsVo extends AttrGroupEntity {

    private List<AttrEntity> attrs;
}
