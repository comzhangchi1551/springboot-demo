package com.miya;

import com.google.common.collect.Lists;
import com.miya.entity.model.TempUser;

import java.util.List;

public class MainTest {

    public static void main(String[] args) {

    }


    public static TempUser getTempUser() {
        return new TempUser(1l, "eee", 21);
    }


    public static List<TempUser> getTempUserList() {
        return Lists.newArrayList(
                new TempUser(1l, "eee", 21),
                new TempUser(2l, "dd", 44),
                new TempUser(3l, "aaa", 13),
                new TempUser(4l, "fff", 13)
        );
    }

}
