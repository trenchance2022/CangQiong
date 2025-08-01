package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        try {
            String originalFilename=file.getOriginalFilename();
            String extension=originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName=UUID.randomUUID().toString() + extension;
            String filePath=aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
