package com.kkzz.mall.search.vo;

import com.kkzz.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchResult {
    //查询到的所有商品信息
    private List<SkuEsModel> products;

    /**
     * 分页信息
     */
    private Integer pageNum;
    private Long total;//总记录数
    private Integer totalPages;//总页码
    private List<Integer> pageNavs;
    private List<BrandVo> brands;//当前查询结果所有涉及到的品牌
    private List<AttrVo> attrs;//当前查询到的结果,所有涉及到的属性
    private List<CatalogVo> catalogs;//当前查询到的结果,所有涉及到的分类

    //面包屑导航
    private List<NavVo> navs=new ArrayList<>();

    @Data
    public static class NavVo{
        private String navName;
        private String navValue;
        private String link;
    }
    @Data
    public static class BrandVo{
        private Long brandId;
        private String brandName;
        private String brandImg;
    }
    @Data
    public static class CatalogVo{
        private Long catalogId;
        private String catalogName;
    }
    @Data
    public static class AttrVo{
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }
}
