package com.kkzz.mall.search.controller;

import com.kkzz.mall.search.service.MallSearchService;
import com.kkzz.mall.search.vo.SearchParam;
import com.kkzz.mall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SearchController {

    @Autowired
    MallSearchService mallSearchService;

    /**
     * 自动将页面请求的参数封装为对象
     */
    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model, HttpServletRequest request){
        String queryString = request.getQueryString();
        param.set_queryString(queryString);
        //根据页面传递来的查询参数,去es中检索商品
        SearchResult result=mallSearchService.search(param);
        model.addAttribute("result",result);
        return "list";
    }
}
