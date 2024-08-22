package com.miya;

import cn.hutool.core.util.NumberUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MainTest {


    @SneakyThrows
    public static void main(String[] args) {
        Integer a = 10;
        System.out.println("NumberUtil.getBinaryStr(a) = " + NumberUtil.getBinaryStr(a));
        Integer b = 7;
        System.out.println("NumberUtil.getBinaryStr(b) = " + NumberUtil.getBinaryStr(b));

        String binaryStr = NumberUtil.getBinaryStr(a & b);
        System.out.println("binaryStr = " + binaryStr);


    }



}
