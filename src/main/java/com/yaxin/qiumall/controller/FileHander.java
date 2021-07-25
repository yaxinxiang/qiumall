package com.yaxin.qiumall.controller;

import com.yaxin.qiumall.annotation.PassToken;
import com.yaxin.qiumall.entity.User;
import com.yaxin.qiumall.entity.Userimg;
import com.yaxin.qiumall.repository.ProductRepository;
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
public class FileHander {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserimgRepository userimgRepository;

    @Autowired
    ProductRepository productRepository;

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

    @PostMapping("/userimg")
    @ResponseBody
    public Map upFile(@RequestParam("file") MultipartFile file, @RequestHeader("token") String token){
        Map<String, Object> map = new HashMap<>();

        String username = JwtUtil.getUserNameByToken(token);
        //这里之后要加上上传限制，每人三张照片
        String userimgPath = path+"/userimg";
        String fileName = file.getOriginalFilename();

        String suffixName = fileName.substring(fileName.lastIndexOf("."));
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
            //System.out.println("执行成功");

            //打印这个路径：
            String imgUrl = rootURL+ "/" + "userimg" +"/" + fileName;
            System.out.println(imgUrl);

            Userimg userimg = new Userimg();
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
}
