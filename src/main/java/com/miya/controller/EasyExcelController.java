package com.miya.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.cache.MapCache;
import com.alibaba.fastjson.JSON;
import com.miya.common.BaseException;
import com.miya.common.easyexcel.TempUserListener;
import com.miya.entity.model.mysql.TempUser;
import com.miya.service.TempUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @Desc:
 * @Author: 张翅
 * @Date: 2021/8/7 1:00
 */
@RestController
@RequestMapping("easy/excel")
public class EasyExcelController {


    @Autowired
    private TempUserService tempUserService;

    /**
     * 使用easyExcel生成excel文件；
     * @param response
     * @throws IOException
     */
    @GetMapping("download")
    public void download(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyExcel没有关系
            String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), TempUser.class)
                    .autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(tempUserService.getAll(1, 10000).getRecords());
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }


    /**
     * easyExcel的读；
     * @param file
     * @throws IOException
     */
    @PostMapping("upload")
    public void upload(MultipartFile file) throws IOException {
        long fileSize = file.getSize();
        if (fileSize > 50 * 1024 * 1024) {
            throw new BaseException("文件太大；");
        }

        EasyExcel.read(file.getInputStream(), TempUser.class, new TempUserListener(tempUserService)).sheet().doRead();
    }
}
