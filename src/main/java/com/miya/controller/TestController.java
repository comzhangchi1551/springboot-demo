package com.miya.controller;

import com.github.pagehelper.PageInfo;
import com.miya.common.BaseResult;
import com.miya.entity.dto.TempUserInsertDTO;
import com.miya.entity.dto.TempUserUpdateDTO;
import com.miya.entity.model.TempUser;
import com.miya.service.TempUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * Auth: 张翅
 * Date: 2022/1/26
 * Desc:
 */
@RestController
@RequestMapping("test")
@Validated
public class TestController {

    @Autowired
    private TempUserService tempUserService;

    @GetMapping("t1")
    public String t1(String name){
        System.out.println("name = " + name);
        return name;
    }

    @GetMapping("list")
    public BaseResult tempUserList(@RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<TempUser> pageInfo = tempUserService.selectByPage(pageNum, pageSize);
        return BaseResult.success(pageInfo);
    }


    @PostMapping("insert")
    public BaseResult insert(@RequestBody @Validated TempUserInsertDTO insertDTO){
        tempUserService.insert(insertDTO);
        return BaseResult.success();
    }

    @PostMapping("update")
    public BaseResult update(TempUserUpdateDTO updateDTO) {
        tempUserService.update(updateDTO);
        return BaseResult.success();
    }


    @GetMapping("detail")
    public BaseResult selectTempUserDetail(@RequestParam String name){
        TempUser tempUser = tempUserService.detail(name);
        return BaseResult.success(tempUser);
    }

    @GetMapping("data")
    public BaseResult getData(){
        TempUser tempUser = new TempUser();
        tempUser.setId(12L);
        tempUser.setName("镇恶犯");
        tempUser.setAge(14);
        return BaseResult.success(tempUser);
    }
}
