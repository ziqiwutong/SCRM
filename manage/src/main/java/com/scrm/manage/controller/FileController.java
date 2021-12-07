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
                         @RequestParam("picType") String picType,
                         @RequestParam(name = "isCompress", required = false, defaultValue = "true") boolean isCompress) {
        // 1.参数检查
        if (picFile == null || picFile.isEmpty())        // 判断是否上传了文件
            return Result.error(CodeEum.PARAM_MISS);
        // 判断是否图片
        String filename = picFile.getOriginalFilename();//获取上传的⽂件名
        if (filename == null)
            return Result.error(CodeEum.PARAM_ERROR, "没有文件名");

        int lastIndex = filename.lastIndexOf('.');
        if (lastIndex < 0)
            return Result.error(CodeEum.PARAM_ERROR, "只能上传图片，如这些格式：" + format);
        String suffixName = filename.substring(lastIndex + 1).toLowerCase();
        if (!format.contains(suffixName))
            return Result.error(CodeEum.PARAM_ERROR, "只能上传图片，如这些格式：" + format);

        if (!type.contains(picType))
            return Result.error(CodeEum.PARAM_ERROR, "图片类型仅能选择：" + type);

        // 2.文件目录处理
        String relativePath = picType + "/" + MyDateTimeUtil.getNowDateOrTime("yyyy-MM");
        String path = picRootPath + "/" + relativePath + "/";
        File dir_file = new File(path);
        if (!dir_file.exists()) dir_file.mkdirs();

        // 3.文件名称处理
        filename = picType + UUID.randomUUID() + "." + suffixName;
        File img_file = new File(path, filename);
        img_file.deleteOnExit();//删除旧文件

        // 4、将文件写入到磁盘
        try (
                InputStream in = picFile.getInputStream();
                OutputStream out = new FileOutputStream(img_file)
        ) {
            if (isCompress)
                isCompress = picFile.getSize() > toCompressSize; // 大于1MB进行压缩

            myTransferTo(in, out, isCompress);
            return Result.success(picAccessPath + "/" + relativePath + "/" + filename);
        } catch (IOException e) {
            Result result = Result.error(CodeEum.ERROR);
            result.addMsg(e.getMessage());
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
            @RequestParam(name = "picFormat", required = false, defaultValue = "png") String picFormat,
            @RequestParam("picType") String picType,
            @RequestParam(name = "isCompress", required = false, defaultValue = "true") boolean isCompress) {

        // 1.参数检查
        if (!type.contains(picType))
            return Result.error(CodeEum.PARAM_ERROR, "图片类型仅能选择：" + type);
        if (picBase64Str.length() > 4194304)
            return Result.error(CodeEum.PARAM_ERROR, "图片base64字符串不能大于4MB, 这将要求图片不能大于3MB");

        // 1.1 js处理的base64图片有前置信息，需要处理一下
        if (picBase64Str.startsWith("data:")) {
            // 1.1.1 文件后缀类型获取
            picFormat = picBase64Str.substring(picBase64Str.indexOf("/") + 1, picBase64Str.indexOf(";"));
            // 1.1.2 删除js处理的base64字符前置信息
            picBase64Str = picBase64Str.substring(picBase64Str.indexOf("base64,") + 7);
        }
        if (picFormat.startsWith(".")) picFormat = picFormat.substring(1);
        if (!format.contains(picFormat))
            return Result.error(CodeEum.PARAM_ERROR, "只能上传图片，如这些格式：" + format);


        // 1.2 base64处理
        byte[] buffer = Base64.getDecoder().decode(picBase64Str);
        picBase64Str = null;
        System.gc();


        // 2.文件目录处理
        String relativePath = picType + "/" + MyDateTimeUtil.getNowDateOrTime("yyyy-MM");
        String path = picRootPath + "/" + relativePath + "/";
        File dir_file = new File(path);
        if (!dir_file.exists()) dir_file.mkdirs();

        // 3.文件名处理
        /* 根据不同文件类型，文件名生成策略应该不同
         * articleImage：
         * userIcon：userIcon+userId
         * productImage：productImage+productId */
        String filename = picType + UUID.randomUUID() + "." + picFormat;// 目前暂时先用UUID
        File img_file = new File(path, filename);
        img_file.deleteOnExit();//删除旧文件

        // 4、将文件写入到磁盘
        Result result;
        try (
                ByteArrayInputStream in = new ByteArrayInputStream(buffer);
                FileOutputStream out = new FileOutputStream(img_file)
        ) {
            if (isCompress) isCompress = buffer.length > toCompressSize;// 大于1MB需要压缩

            myTransferTo(in, out, isCompress);
            result = Result.success(picAccessPath + "/" + relativePath + "/" + filename);
        } catch (Exception e) {
            result = Result.error(CodeEum.ERROR, e.getMessage());
        }
        return result;
    }


    /*注意：此方法自动关闭流*/
    private int myTransferTo(InputStream in, OutputStream out, boolean isCompress) throws IOException {
        if (in == null || out == null)
            throw new IllegalArgumentException("流不能为空");
        int total = 0;

        try (in; out) {
            // 大于1MB将进行压缩处理
            if (isCompress) {
                ImgUtil.scale(in, out, 0.5f);
            }
            // 否则不会压缩
            else {
                byte[] buffer = new byte[4096];
                for (int len; (len = in.read(buffer)) != -1; total += len) {
                    out.write(buffer);
                }
                out.flush();
            }
        }
        return total;
    }
}