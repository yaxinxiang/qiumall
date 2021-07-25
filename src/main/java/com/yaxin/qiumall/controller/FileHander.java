package com.yaxin.qiumall.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class FileHander {
    //本地主机静态文件路径
    @Value("${static.profile}")
    private String path;

    @PostMapping("/userimg")
    @ResponseBody
    public String upFile(@RequestParam("file") MultipartFile file, @RequestParam("id") Integer id){
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
            String path1 = userimgPath + fileName;
            //System.out.println(path1);
            return "upload succes!";
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("执行失败");
            return "upload failed!";
        }
    }
}
