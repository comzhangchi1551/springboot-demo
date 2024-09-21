package com.miya.controller;

import com.miya.common.BaseResult;
import com.miya.entity.es.TempUserEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("elastic")
public class ElasticSearchTestController {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    @PostMapping("add")
    public BaseResult add(@RequestBody @Validated TempUserEO tempUserEO){
        elasticsearchRestTemplate.save(tempUserEO);
        return BaseResult.success();
    }
}
