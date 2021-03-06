package com.kkzz.mall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kkzz.common.to.es.SkuEsModel;
import com.kkzz.common.utils.R;
import com.kkzz.mall.search.config.MallElasticSearchConfig;
import com.kkzz.mall.search.constant.EsConstant;
import com.kkzz.mall.search.feign.ProductFeignService;
import com.kkzz.mall.search.service.MallSearchService;
import com.kkzz.mall.search.vo.AttrResponseVo;
import com.kkzz.mall.search.vo.BrandVo;
import com.kkzz.mall.search.vo.SearchParam;
import com.kkzz.mall.search.vo.SearchResult;
import org.apache.lucene.search.TotalHits;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MallSearchServiceImpl implements MallSearchService {

    @Autowired
    private RestHighLevelClient restClient;

    @Autowired
    ProductFeignService productFeignService;

    @Override
    public SearchResult search(SearchParam param) {
        SearchResult result = null;
        //????????????????????????????????????
        //??????????????????
        SearchRequest searchRequest = buildSearchRequest(param);
        try {
            //??????????????????
            SearchResponse response = restClient.search(searchRequest, MallElasticSearchConfig.COMMON_OPTIONS);

            //??????????????????,????????????
            result = buildSearchResult(response, param);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private SearchResult buildSearchResult(SearchResponse response, SearchParam param) {
        //??????????????????,????????????
        SearchResult result = new SearchResult();
        //?????????????????????hits?????????hits?????????????????????,????????????hits?????????hit????????????????????????????????????source
        SearchHits hits = response.getHits();
        SearchHit[] products = hits.getHits();
        /**
         * ????????????????????????
         */
        List<SkuEsModel> skus = new ArrayList<>();
        if (products != null && products.length > 0) {
            for (SearchHit product : products) {
                String source = product.getSourceAsString();
                SkuEsModel skuEsModel = JSON.parseObject(source, SkuEsModel.class);
                if (!StringUtils.isEmpty(param.getKeyword())) {
                    HighlightField skuTitle = product.getHighlightFields().get("skuTitle");
                    skuEsModel.setSkuTitle(skuTitle.getFragments()[0].toString());
                }
                skus.add(skuEsModel);
            }
        }
        result.setProducts(skus);
        /**
         * ??????????????????
         */
        long total = hits.getTotalHits().value;
        result.setTotal(total);
        //???????????????
        int totalPages;
        if (total % EsConstant.PRODUCT_PAGESIZE == 0) {
            totalPages = (int) total / EsConstant.PRODUCT_PAGESIZE;
        } else {
            totalPages = (int) total / EsConstant.PRODUCT_PAGESIZE + 1;
        }
        result.setTotalPages(totalPages);
        result.setPageNum(param.getPageNum());
        List<Integer> pageNavs = new ArrayList<>();
        for (int i = 1; i < totalPages; i++) {
            pageNavs.add(i);
        }
        result.setPageNavs(pageNavs);

        /**
         * ???????????????????????????
         */
        Aggregations aggregations = response.getAggregations();
        //?????????????????? ????????????????????????debug????????????response??????????????????ParsedLongTerms
        ParsedLongTerms catalog_agg = aggregations.get("catalog_agg");
        List<? extends Terms.Bucket> buckets = catalog_agg.getBuckets();
        List<SearchResult.CatalogVo> catalogVos = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo();
            catalogVo.setCatalogId(bucket.getKeyAsNumber().longValue());
            ParsedStringTerms catalog_name_agg = bucket.getAggregations().get("catalog_name_agg");
            catalogVo.setCatalogName(catalog_name_agg.getBuckets().get(0).getKeyAsString());
            catalogVos.add(catalogVo);
        }
        result.setCatalogs(catalogVos);
        //??????????????????
        ParsedLongTerms brand_agg = aggregations.get("brand_agg");
        List<SearchResult.BrandVo> brandVos = new ArrayList<>();
        for (Terms.Bucket bucket : brand_agg.getBuckets()) {
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
            brandVo.setBrandId(bucket.getKeyAsNumber().longValue());
            ParsedStringTerms brand_img_agg = bucket.getAggregations().get("brand_img_agg");
            brandVo.setBrandImg(brand_img_agg.getBuckets().get(0).getKeyAsString());
            ParsedStringTerms brand_name_agg = bucket.getAggregations().get("brand_name_agg");
            brandVo.setBrandName(brand_name_agg.getBuckets().get(0).getKeyAsString());
            brandVos.add(brandVo);
        }
        result.setBrands(brandVos);
        //?????????????????? ???????????????nested??????
        ParsedNested attr_agg = aggregations.get("attr_agg");
        ParsedLongTerms attr_id_agg = attr_agg.getAggregations().get("attr_id_agg");
        List<SearchResult.AttrVo> attrVos = new ArrayList<>();
        for (Terms.Bucket bucket : attr_id_agg.getBuckets()) {
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
            attrVo.setAttrId(bucket.getKeyAsNumber().longValue());
            ParsedStringTerms attr_name_agg = bucket.getAggregations().get("attr_name_agg");
            attrVo.setAttrName(attr_name_agg.getBuckets().get(0).getKeyAsString());
            ParsedStringTerms attr_value_agg = bucket.getAggregations().get("attr_value_agg");
            List<String> values = attr_value_agg.getBuckets().stream().map(item -> item.getKeyAsString()).collect(Collectors.toList());
            attrVo.setAttrValue(values);
            attrVos.add(attrVo);
        }
        result.setAttrs(attrVos);

        //6????????????????????????
        if (param.getAttrs() != null && param.getAttrs().size() > 0) {
            List<SearchResult.NavVo> collect = param.getAttrs().stream().map(attr -> {
                //1??????????????????attrs?????????????????????
                SearchResult.NavVo navVo = new SearchResult.NavVo();
                String[] s = attr.split("_");
                navVo.setNavValue(s[1]);
                R r = productFeignService.attrInfo(Long.parseLong(s[0]));
                if (r.getCode() == 0) {
                    AttrResponseVo data = r.getData("attr", new TypeReference<AttrResponseVo>() {
                    });
                    navVo.setNavName(data.getAttrName());
                } else {
                    navVo.setNavName(s[0]);
                }

                //2???????????????????????????????????????????????????????????????????????????????????????url?????????????????????
                //??????????????????????????????????????????
                String encode = null;
                try {
                    encode = URLEncoder.encode(attr, "UTF-8");
                    encode = encode.replace("+", "%20");  //??????????????????????????????Java???????????????????????????
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String replace = param.get_queryString().replace("&attrs=" + encode, "");
                navVo.setLink("http://search.mall.com/list.html?" + replace);
                return navVo;
            }).collect(Collectors.toList());

            result.setNavs(collect);
        }

        return result;
    }

    private SearchRequest buildSearchRequest(SearchParam param) {
        //??????????????????
        //??????dsl??????
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //??????boolQuery
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //??????must
        if (!StringUtils.isEmpty(param.getKeyword())) {
            boolQuery.must(QueryBuilders.matchQuery("skuTitle", param.getKeyword()));
        }
        //??????filter
        if (param.getCatalog3Id() != null) {
            boolQuery.filter(QueryBuilders.termQuery("catalogId", param.getCatalog3Id()));
        }
        if (param.getBrandId() != null && param.getBrandId().size() > 0) {
            boolQuery.filter(QueryBuilders.termsQuery("brandId", param.getBrandId()));
        }
        if (param.getAttrs() != null && param.getAttrs().size() > 0) {
            for (String attr : param.getAttrs()) {
                BoolQueryBuilder nestBoolQuery = QueryBuilders.boolQuery();
//                attr=1_??????:??????
                String[] s = attr.split("_");
                String attrId = s[0];
                String[] values = s[1].split(":");
                nestBoolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                nestBoolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", values));
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestBoolQuery, ScoreMode.None);
                boolQuery.filter(nestedQuery);
            }
        }
        if (param.getHasStock() != null) {
            boolQuery.filter(QueryBuilders.termQuery("hasStock", param.getHasStock() == 1));
        }
        //a_b _b a_
        if (!StringUtils.isEmpty(param.getSkuPrice())) {
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            String[] s = param.getSkuPrice().split("_", -1);
            if (param.getSkuPrice().startsWith("_")) {
                rangeQuery.lte(s[1]);
            } else if (param.getSkuPrice().endsWith("_")) {
                rangeQuery.gte(s[0]);
            } else {
                rangeQuery.gte(s[0]).lte(s[1]);
            }
            boolQuery.filter(rangeQuery);
        }
        //??????????????????????????????
        sourceBuilder.query(boolQuery);

        //sort=xxx_asc/desc
        if (!StringUtils.isEmpty(param.getSort())) {
            String[] s = param.getSort().split("_");
            sourceBuilder.sort(s[0], SortOrder.fromString(s[1]));
        }
        //??????
        sourceBuilder.from((param.getPageNum() - 1) * EsConstant.PRODUCT_PAGESIZE);
        sourceBuilder.size(EsConstant.PRODUCT_PAGESIZE);
        //??????
        if (!StringUtils.isEmpty(param.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style=color:red>");
            highlightBuilder.postTags("</b>");
            sourceBuilder.highlighter(highlightBuilder);
        }
        /**
         * ????????????
         */
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg").field("brandId").size(50);
        //?????????????????????
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        sourceBuilder.aggregation(brand_agg);

        /**
         * ????????????
         */
        TermsAggregationBuilder catalog_agg = AggregationBuilders.terms("catalog_agg").field("catalogId").size(20);
        //?????????????????????
        catalog_agg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(1));
        sourceBuilder.aggregation(catalog_agg);

        /**
         * ????????????
         */
        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId").size(20);
        attr_agg.subAggregation(attr_id_agg);
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50));
        sourceBuilder.aggregation(attr_agg);
        return new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, sourceBuilder);
    }

}
