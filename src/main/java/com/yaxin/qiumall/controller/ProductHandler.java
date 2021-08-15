package com.yaxin.qiumall.controller;

import com.yaxin.qiumall.annotation.PassToken;
import com.yaxin.qiumall.entity.Product;
import com.yaxin.qiumall.entity.Productimg;
import com.yaxin.qiumall.repository.ProductRepository;
import com.yaxin.qiumall.repository.ProductimgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductHandler {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductimgRepository productimgRepository;

    //查询商品
    @PassToken
    @ResponseBody//返回json对象
    @GetMapping("/findall/{page}/{size}")
    public Page<Product> findall(@PathVariable("page") Integer page, @PathVariable("size") Integer size){
        Pageable pageable = PageRequest.of(page - 1, size);
        return productRepository.findAll(pageable);
    }

    //查询单个商品
    @PassToken
    @ResponseBody//返回json对象
    @GetMapping("/findbyid/{id}")
    public Product findById(@PathVariable("id") Integer id){
        try{
            return productRepository.findById(id).get();
        }catch (Exception e){
            return null;
        }
    }

    //根据类别查询商品
    @PassToken
    @ResponseBody//返回json对象
    @GetMapping("/findbycategory")
    public List<Product> findByCategory(@RequestParam("category") String category){
        return productRepository.findProductsByCategory(category);
    }

    //获取商品图片
    @PassToken
    @ResponseBody
    @GetMapping("/selfimg")
    public List<Productimg> getProductImg(@RequestParam("pid") Integer pid){
        return productimgRepository.findProductimgsBypId(pid);
    }

    //增加商品
    @PassToken//之后增加token判断（加入用户的status）是否是管理员或者商家之类的才能添加商品
    @ResponseBody//返回json对象
    @PostMapping("/add")
    public Map<String, Object> save(@RequestBody Product product){
        Map<String, Object> map = new HashMap<>();
        Product re = null;
        try{
            re = productRepository.save(product);
            map.put("code",200);
            map.put("msg","添加成功");
            map.put("product",re);
            return map;
        }catch (Exception e){
            map.put("code",403);
            map.put("msg","添加失败");
            return map;
        }
    }

    //修改商品
    @PassToken//之后增加token判断（加入用户的status）是否是管理员或者商家之类的才能修改商品
    @ResponseBody//返回json对象
    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody Product product){
        Map<String, Object> map = new HashMap<>();
        try{
            //根据id找不到对应产品时
            productRepository.findById(product.getId()).get();
        }catch (Exception e){
            map.put("code", 403);
            map.put("msg", "数据错误，修改失败！刷新产品信息后再修改！");
            return map;
        }
        if(product.getId() == null){
            map.put("code", 403);
            map.put("msg", "数据错误，修改失败！刷新产品信息后再修改！");
            return map;
        }
        Product re = null;
        try{
            re = productRepository.save(product);
            map.put("code", 200);
            map.put("msg", "修改成功！");
            map.put("product", re);
            return map;
        }catch (Exception e){
            map.put("code", 403);
            map.put("msg", "存入出现问题，修改失败！");
            return map;
        }
    }
}
