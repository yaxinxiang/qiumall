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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController//等同于@ResponseBody + @Controller
@RequestMapping("/upload")
public class FileHandler {
    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtil jwtUtil;

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
    public ResponseEntity<String> upUserImg(@RequestParam("userimg") MultipartFile file, @RequestHeader("token") String token){
        String msg = null;
        Integer userId = jwtUtil.getUserIdByToken(token);
        String username = jwtUtil.getUserNameByToken(token);
        //这里之后要加上上传限制，每人三张照片
        String userimgPath = path+"/userimg";
        String fileName = file.getOriginalFilename();
        String suffixName;
        try{
            suffixName = fileName.substring(fileName.lastIndexOf("."));
        }catch (Exception e){
            msg = "upload failed!";
            return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
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
            msg = "upload succes!";
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("执行失败");
            msg = "upload failed!";
            return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //上传商品图像
    @PostMapping("/productimg")
    @ResponseBody
    public ResponseEntity<String> upPImg(@RequestParam("productimg") MultipartFile file, @RequestParam("pid") Integer pid, @RequestHeader("token") String token){
        String msg = null;
        //商家以上才能够修改商品，以后还要确定商品和卖家的关系才能够开放使用
        Integer userId = jwtUtil.getUserIdByToken(token);
        if(userRepository.findUserById(userId).getStatus()<2){
            //403
            msg = "无权添加商品图片！请联系管理员提高权限";
            return new ResponseEntity<>(msg, HttpStatus.FORBIDDEN);
        }

        String productimgPath = path+"/productimg";
        String fileName = file.getOriginalFilename();
        String suffixName;
        try{
            suffixName = fileName.substring(fileName.lastIndexOf("."));
        }catch (Exception e){
            //500: 内部错误
            msg = "服务器内部错误";
            return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
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
            msg = "upload succes!";
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("执行失败");
            //500: 内部错误
            msg = "服务器内部错误";
            return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
