package com.miya;

import com.miya.entity.model.TempUser;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamTemp {


    /**
     * 将列表进行分组，并且自定义value的返回；
     */
    public void t1 () {
        List<TempUser> testTempUserList = TempUser.getTestTempUserList(5);
        Map<Integer, List<String>> collect = testTempUserList.stream().collect(Collectors.groupingBy(TempUser::getAge, LinkedHashMap::new,Collectors.mapping(TempUser::getUsername, Collectors.toList())));
        System.out.println(collect);
    }
}
