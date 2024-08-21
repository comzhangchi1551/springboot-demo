package com.miya.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miya.entity.model.TestInsert;
import com.miya.service.TestInsertService;
import com.miya.dao.TestInsertMapper;
import org.springframework.stereotype.Service;

/**
* @author zhangchi
* @description 针对表【test_insert】的数据库操作Service实现
* @createDate 2024-08-21 04:28:41
*/
@Service
public class TestInsertServiceImpl extends ServiceImpl<TestInsertMapper, TestInsert>
    implements TestInsertService{

}




