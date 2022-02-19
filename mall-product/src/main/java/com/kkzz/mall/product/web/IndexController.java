package com.kkzz.mall.product.web;

import com.kkzz.mall.product.entity.CategoryEntity;
import com.kkzz.mall.product.service.CategoryService;
import com.kkzz.mall.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    @Autowired
    CategoryService categoryService;

    @GetMapping({"/","/index.html"})
    public String indexPage(Model model){
        //1.todo 查出所有一级分类
        List<CategoryEntity> category=categoryService.getLevel1Category();
        model.addAttribute("categorys",category);
        return "index";
    }

    //"index/catalog.json"
    @ResponseBody
    @GetMapping("/index/catalog.json")
    public  Map<String,List<Catelog2Vo>> getCatalogJson(){
        Map<String,List<Catelog2Vo>> map=categoryService.getCatalogJson();
        return map;
    }
    @ResponseBody
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
