package com.yaxin.qiumall.controller;

import com.yaxin.qiumall.entity.Product;
import com.yaxin.qiumall.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductHander {

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/findall/{page}/{size}")
    public Page<Product> findall(@PathVariable("page") Integer page, @PathVariable("size") Integer size){
        Pageable pageable = PageRequest.of(page - 1, size);
        return productRepository.findAll(pageable);
    }

    @GetMapping("/findbyid/{id}")
    public Product findById(@PathVariable("id") Integer id){
        try{
            return productRepository.findById(id).get();
        }catch (Exception e){
            return null;
        }
    }

    @PostMapping("/save")
    public String save(@RequestBody Product product){
        Product re = productRepository.save(product);
        if(re != null){
            return "{'tips': 添加成功,'id' : "+ re.getId() + "}";
        }
        return "添加失败";
    }

    @PostMapping("/update")
    public String update(@RequestBody Product product){
        if(product.getId() == null || productRepository.findById(product.getId()) == null){
            return "数据错误";
        }
        Product re = productRepository.save(product);
        if(re != null){
            return "修改成功";
        }
        return "修改失败";
    }
}
