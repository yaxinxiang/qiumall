package com.yaxin.qiumall.controller;

import com.yaxin.qiumall.annotation.PassToken;
import com.yaxin.qiumall.entity.Productimg;
import com.yaxin.qiumall.entity.Userimg;
import com.yaxin.qiumall.repository.ProductRepository;
import com.yaxin.qiumall.repository.ProductimgRepository;
import com.yaxin.qiumall.repository.UserRepository;
import com.yaxin.qiumall.repository.UserimgRepository;
import com.yaxin.qiumall.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class FileHandler {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserimgRepository userimgRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductimgRepository productimgRepository;

    //静态文件路径(存文件用)
    @Value("${static.profile}")
    private String path;

    @Value("${static.rootURL}")
    private String rootURL;

    @PassToken
    @GetMapping("/staticurl")
    public String url(){
        return path;
    }

    //上传用户图像
    @PostMapping("/userimg")
    @ResponseBody//返回json对象
    public Map<String, Object> upUserImg(@RequestParam("userimg") MultipartFile file, @RequestHeader("token") String token){
        Map<String, Object> map = new HashMap<>();

        Integer userId = JwtUtil.getUserIdByToken(token);
        String username = JwtUtil.getUserNameByToken(token);
        //这里之后要加上上传限制，每人三张照片
        String userimgPath = path+"/userimg";
        String fileName = file.getOriginalFilename();
        String suffixName;
        try{
            suffixName = fileName.substring(fileName.lastIndexOf("."));
        }catch (Exception e){
            map.put("code", 403);
            map.put("msg", "upload failed!");
            return map;
        }

        //之后还要加上图片判断的逻辑防止上传视频等其他文件（做一个工具类）
        fileName = UUID.randomUUID().toString().replace("-","") + suffixName;
        //System.out.println("newfileName:" + fileName);
        File targetFile = new File(userimgPath);
        //System.out.println(path);
        if (!targetFile.exists()) {
            // 判断文件夹是否未空，空则创建
            targetFile.mkdirs();
        }
        File saveFile = new File(targetFile, fileName);
        try {
            //指定本地存入路径
            file.transferTo(saveFile);
            System.out.println("执行成功");

            String imgUrl = rootURL+ "/" + "userimg" +"/" + fileName;
            //打印这个路径：
            System.out.println(imgUrl);

            Userimg userimg = new Userimg();
            userimg.setUserId(userId);
            userimg.setUsername(username);
            userimg.setUImgUrl(imgUrl);

            userimgRepository.save(userimg);
            map.put("code", 200);
            map.put("msg", "upload succes!");
            map.put("userimg", userimg);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("执行失败");
            map.put("code", 403);
            map.put("msg", "upload failed!");
            return map;
        }
    }

    //上传商品图像
    @PostMapping("/productimg")
    @ResponseBody
    public Map<String, Object> upPImg(@RequestParam("productimg") MultipartFile file, @RequestParam("pid") Integer pid, @RequestHeader("token") String token){
        Map<String, Object> map = new HashMap<>();

        //商家以上才能够修改商品，以后还要确定商品和卖家的关系才能够开放使用
        Integer userId = JwtUtil.getUserIdByToken(token);
        if(userRepository.findUserById(userId).getStatus()<2){
            map.put("code",403);
            map.put("msg", "无权添加商品图片！请联系管理员提高权限");
            return map;
        }

        String productimgPath = path+"/productimg";
        String fileName = file.getOriginalFilename();
        String suffixName;
        try{
            suffixName = fileName.substring(fileName.lastIndexOf("."));
        }catch (Exception e){
            map.put("code", 403);
            map.put("msg", "upload failed!");
            return map;
        }

        //之后还要加上图片判断的逻辑防止上传视频等其他文件（做一个工具类）
        fileName = UUID.randomUUID().toString().replace("-","") + suffixName;
        //System.out.println("newfileName:" + fileName);
        File targetFile = new File(productimgPath);
        //System.out.println(path);
        if (!targetFile.exists()) {
            // 判断文件夹是否未空，空则创建
            targetFile.mkdirs();
        }
        File saveFile = new File(targetFile, fileName);
        try {
            //指定本地存入路径
            file.transferTo(saveFile);
            System.out.println("执行成功");

            String imgUrl = rootURL+ "/" + "productimg" +"/" + fileName;
            //打印这个路径：
            System.out.println(imgUrl);

            //存储路径到数据库
            Productimg productimg = new Productimg();
            productimg.setPId(pid);
            productimg.setName(productRepository.findById(pid).get().getName());
            productimg.setPImgUrl(imgUrl);
            productimgRepository.save(productimg);
            map.put("code", 200);
            map.put("msg", "upload succes!");
            map.put("productimg", productimg);
            return map;

        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("执行失败");
            map.put("code", 403);
            map.put("msg", "upload failed!");
            return map;
        }
    }

}
