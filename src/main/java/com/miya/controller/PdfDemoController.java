package com.miya.controller;

import com.miya.common.BaseResult;
import com.miya.entity.dto.RecombinationPdfDTO;
import com.miya.service.RecombinationPdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("psd")
@Validated
public class PdfDemoController {

    @Autowired
    private RecombinationPdfService recombinationPdfService;

    @PostMapping("addPsdProject")
    public BaseResult addPsdProject(@RequestBody @Validated RecombinationPdfDTO recombinationPdfDTO){
        recombinationPdfService.addPsdProject(recombinationPdfDTO);
        return BaseResult.success();
    }




}
