package com.densoft.saccoapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCustomerReq {
    @NotBlank(message = "firstname is required")
    private String firstName;
    @NotBlank(message = "lastname is required")
    private String lastName;
    @NotBlank(message = "id number is required")
    private int idNo;
    @Email(message = "invalid email")
    @NotBlank(message = "email is required")
    private String email;
    @NotBlank(message = "phone number is required")
    private String phoneNumber;

}
