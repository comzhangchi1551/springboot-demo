package com.miya.entity.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TempUserFather implements Serializable {
    private TempUser tempUser;
}