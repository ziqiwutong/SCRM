package com.scrm.manage.controller;

import cn.hutool.core.img.ImgUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.scrm.manage.util.MyDateTimeUtil;
import com.scrm.manage.util.resp.CodeEum;
import com.scrm.manage.util.resp.Result;

import java.io.*;
import java.util.Base64;
import java.util.HashSet;
import java.util.UUID;

/**
 * @author fzk
 * @date 2021-11-10 17:14
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Value("${my.file.pic.format}")
    private HashSet<String> format;
    @Value("${my.file.pic.type}")
    private HashSet<String> type;
    @Value("${my.file.pic.picRootPath}")
    private String picRootPath;
    @Value("${my.file.pic.picAccessPath}")
    private String picAccessPath;
    @Value("${my.file.pic.toCompressSize}")
    private long toCompressSize;

    /**
     * 传统方式文件上传
     *
     * @param picFile 文件
     * @return result
     */
    @PostMapping("/pic/upload")
    public Result upload(MultipartFile picFile,
                         @RequestParam("picType") String picType) {
        // 1.参数检查
        if (picFile == null || picFile.isEmpty())        // 判断是否上传了文件
            return Result.error(CodeEum.PARAM_MISS);
        // 判断是否图片
        //获取上传的⽂件名
        String name = picFile.getOriginalFilename();
        if (name == null)
            return Result.error(CodeEum.PARAM_ERROR, "没有文件名");
        int lastIndex = name.lastIndexOf('.');
        if (lastIndex < 0)
            return Result.error(CodeEum.PARAM_ERROR, "只能上传图片，如这些格式：" + format.toString());
        String suffixName = name.substring(lastIndex).toLowerCase();
        if (!format.contains(suffixName))
            return Result.error(CodeEum.PARAM_ERROR, "只能上传图片，如这些格式：" + format.toString());
        if (!type.contains(picType))
            return Result.error(CodeEum.PARAM_ERROR, "图片类型仅能选择：" + type.toString());

        // 2.文件目录处理
        String relativePath = picType + "/" + MyDateTimeUtil.getNowDate().replaceAll("-", "/");
        String path = picRootPath + "/" + relativePath + "/";
        File dir_file = new File(path);
        if (!dir_file.exists()) dir_file.mkdirs();

        // 3.文件名称处理
        name = picType + UUID.randomUUID().toString() + name;
        File img_file = new File(path, name);
        img_file.deleteOnExit();//删除旧文件

        // 4、将文件写入到磁盘
        try {
            // 这里也可以直接处理流就行啦
            myTransferTo(picFile.getInputStream(), new FileOutputStream(img_file), picFile.getSize());
            return Result.success(picAccessPath + "/" + relativePath + "/" + name);
        } catch (IOException e) {
            Result result = Result.error(CodeEum.ERROR);
            result.setMsg(e.getMessage());
            return result;
        }
    }


    /**
     * 将base64字符串转为图片
     *
     * @param picBase64Str 字符串
     * @param picFormat    图片格式
     * @param picType      图片类型
     * @return result
     */
    @PostMapping(path = "/pic/base64StrToPic")
    public Result base64StrToPic(
            @RequestParam("picBase64Str") String picBase64Str,
            @RequestParam("picFormat") String picFormat,
            @RequestParam("picType") String picType)  {

        // 1.参数检查
        if (!type.contains(picType))
            return Result.error(CodeEum.PARAM_ERROR, "图片类型仅能选择：" + type.toString());
        if (!picFormat.startsWith(".")) picFormat = "." + picFormat;
        if (!format.contains(picFormat))
            return Result.error(CodeEum.PARAM_ERROR, "只能上传图片，如这些格式：" + format.toString());
        if (picBase64Str.length() > 4194304)
            return Result.error(CodeEum.PARAM_ERROR, "图片base64字符串不能大于4MB");

        // 1.2 base64处理
        byte[] buffer = Base64.getDecoder().decode(picBase64Str);
        picBase64Str = null;


        // 2.文件目录处理
        String relativePath = picType;
        String path = picRootPath + "/" + relativePath + "/";
        File dir_file = new File(path);
        if (!dir_file.exists()) dir_file.mkdirs();

        // 3.文件名处理
        /* 根据不同文件类型，文件名生成策略应该不同
         * articleImage：
         * userIcon：userIcon+userId
         * productImage：productImage+productId */
        String fileName = picType + UUID.randomUUID().toString() + picFormat;// 目前暂时先用UUID
        File img_file = new File(path, fileName);
        img_file.deleteOnExit();//删除旧文件

        // 4、将文件写入到磁盘
        Result result;
        try (FileOutputStream out = new FileOutputStream(img_file)) {
            out.write(buffer);
            out.flush();
            result = Result.success(picAccessPath + "/" + relativePath + "/" + fileName);
        } catch (Exception e) {
            result = Result.error(CodeEum.ERROR, e.getMessage());
        }
        return result;
    }


    /**
     * 将字符串转为图片
     *
     * @param picStr    字符串
     * @param picFormat 图片格式
     * @param picType   图片类型
     * @return result
     */
    @PostMapping(path = "/pic/strToPic")
    public Result stringToPic(
            @RequestParam("picStr") String picStr,
            @RequestParam("picFormat") String picFormat,
            @RequestParam("picType") String picType) {
        // 1.参数检查
        if (!type.contains(picType))
            return Result.error(CodeEum.PARAM_ERROR, "图片类型仅能选择：" + type.toString());
        if (!picFormat.startsWith(".")) picFormat = "." + picFormat;
        if (!format.contains(picFormat))
            return Result.error(CodeEum.PARAM_ERROR, "只能上传图片，如这些格式：" + format.toString());
        if (picStr.length() > 3145728)
            return Result.error(CodeEum.PARAM_ERROR, "图片不能大于3MB");

        // 2.文件目录处理
        String relativePath = picType;
        String path = picRootPath + "/" + relativePath + "/";
        File dir_file = new File(path);
        if (!dir_file.exists()) dir_file.mkdirs();

        // 3.文件名处理
        /* 根据不同文件类型，文件名生成策略应该不同
         * articleImage：
         * userIcon：userIcon+userId
         * productImage：productImage+productId */
        String fileName = picType + UUID.randomUUID().toString() + picFormat;// 目前暂时先用UUID
        File img_file = new File(path, fileName);
        img_file.deleteOnExit();//删除旧文件

        // 4、将文件写入到磁盘
        Result result;
        try (FileOutputStream out = new FileOutputStream(img_file)) {
            out.write(picStr.getBytes());
            out.flush();
            result = Result.success(picAccessPath + "/" + relativePath + "/" + fileName);
        } catch (Exception e) {
            result = Result.error(CodeEum.ERROR, e.getMessage());
        }
        return result;
    }

    private int myTransferTo(InputStream in, OutputStream out, long size) throws IOException {
        int total = 0;
        try {
            // 大于500KB将进行压缩处理
            if (size > toCompressSize) {
                ImgUtil.scale(in, out, 0.5f);
            }
            // 否则不会压缩
            else {
                if (in == null || out == null)
                    throw new IllegalArgumentException("流不能为空");

                byte[] buffer = new byte[4096];
                for (int len; (len = in.read(buffer)) != -1; total += len) {
                    out.write(buffer);
                }
                out.flush();
            }

        } finally {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        }
        return total;
    }
}