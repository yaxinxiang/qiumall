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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/findall/{page}/{size}")
    public ResponseEntity<Page<Product>> findall(@PathVariable("page") Integer page, @PathVariable("size") Integer size){
        Pageable pageable = PageRequest.of(page - 1, size);
        return new ResponseEntity<>(productRepository.findAll(pageable), HttpStatus.OK);
    }

    //查询单个商品
    @PassToken
    @GetMapping("/findbyid/{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") Integer id){
        try{
            return new ResponseEntity<>(productRepository.findById(id).get(), HttpStatus.OK);
        }catch (Exception e){
            //406 id 错误
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    //根据类别查询商品
    @PassToken
    @GetMapping("/findbycategory")
    public ResponseEntity<List<Product>> findByCategory(@RequestParam("category") String category){
        return new ResponseEntity<>(productRepository.findProductsByCategory(category), HttpStatus.OK);
    }

    //获取商品图片
    @PassToken
    @GetMapping("/selfimg")
    public ResponseEntity<List<Productimg>> getProductImg(@RequestParam("pid") Integer pid){
        return new ResponseEntity<>(productimgRepository.findProductimgsBypId(pid), HttpStatus.OK);
    }

    //增加商品
    @PassToken//之后增加token判断（加入用户的status）是否是管理员或者商家之类的才能添加商品
    @ResponseBody//返回json对象
    @PostMapping("/add")
    public ResponseEntity<String> save(@RequestBody Product product){
        String msg = null;
        Product re = null;
        try{
            re = productRepository.save(product);
            msg = "添加成功";
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }catch (Exception e){
            //500
            msg = "添加失败";
            return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //修改商品
    @PassToken//之后增加token判断（加入用户的status）是否是管理员或者商家之类的才能修改商品
    @ResponseBody//返回json对象
    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody Product product){
        String msg = null;
        try{
            //根据id找不到对应产品时
            productRepository.findById(product.getId()).get();
        }catch (Exception e){
            msg = "数据错误，修改失败！刷新产品信息后再修改！";
            return new ResponseEntity<>(msg, HttpStatus.FORBIDDEN);
        }
        if(product.getId() == null){
            msg = "数据错误，修改失败！刷新产品信息后再修改！";
            return new ResponseEntity<>(msg, HttpStatus.FORBIDDEN);
        }
        Product re = null;
        try{
            re = productRepository.save(product);
            msg = "修改成功！";
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }catch (Exception e){
            msg = "存入出现问题，修改失败！";
            return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
