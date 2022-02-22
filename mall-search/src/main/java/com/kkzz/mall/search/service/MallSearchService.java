package com.kkzz.mall.search.service;

import com.kkzz.mall.search.vo.SearchParam;
import com.kkzz.mall.search.vo.SearchResult;

public interface MallSearchService {
    /**
     *
     * @param param 检索的所有参数
     * @return   返回的检索结果
     */
    SearchResult search(SearchParam param);
}
