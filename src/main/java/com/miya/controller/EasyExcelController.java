package com.miya.controller;

import com.alibaba.excel.EasyExcel;
import com.miya.common.BaseException;
import com.miya.common.easyexcel.TempUserListener;
import com.miya.common.utils.CJsonUtils;
import com.miya.entity.easy.excel.TempUserEO;
import com.miya.service.TempUserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("easy/excel")
public class EasyExcelController {


    private final TempUserService tempUserService;

    public EasyExcelController(TempUserService tempUserService){
        this.tempUserService = tempUserService;
    }

    /**
     * 使用easyExcel生成excel文件；
     * @param response 阿达
     * @throws IOException 递四方
     */
    @GetMapping("download")
    public void download(HttpServletResponse response,
                         @RequestParam Integer pageSize) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyExcel没有关系
            String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            tempUserService.exportExcel(response, pageSize);

        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(CJsonUtils.toJson(map));
        }
    }


    /**
     * easyExcel的读；
     * @param file ff
     * @throws IOException ff
     */
    @PostMapping("upload")
    public void upload(MultipartFile file) throws IOException {
        long fileSize = file.getSize();
        if (fileSize > 50 * 1024 * 1024) {
            throw new BaseException("文件太大；");
        }

        EasyExcel.read(file.getInputStream(), TempUserEO.class, new TempUserListener(tempUserService)).sheet().doRead();
    }
}
