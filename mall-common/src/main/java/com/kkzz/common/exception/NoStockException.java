package com.kkzz.common.exception;

import lombok.Data;

@Data
public class NoStockException extends RuntimeException {

    private Long skuId;

    public NoStockException(Long skuId) {
        super("商品id：" + skuId + "库存不足！");
    }

    public NoStockException(String msg) {
        super(msg);
    }


}
