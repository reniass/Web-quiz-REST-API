package com.reinke.webquiz.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class UserRequest {

    @Email
    private String email;

    @NotBlank
    @Min(5)
    private String password;
}
