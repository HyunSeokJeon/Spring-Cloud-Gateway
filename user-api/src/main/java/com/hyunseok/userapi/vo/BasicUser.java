package com.hyunseok.userapi.vo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BasicUser {

    private String name;
    private String email;
}
