package com.densoft.saccoapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCustomerReq {
    @NotBlank(message = "firstname is required")
    private String firstName;
    @NotBlank(message = "lastname is required")
    private String lastName;
    @NotBlank(message = "id number is required")
    @Pattern(regexp = "^[0-9]*\\.?[0-9]*$", message = "Input must be a valid numeric value")
    private String idNo;
    @Email(message = "invalid email")
    @NotBlank(message = "email is required")
    private String email;
    @NotBlank(message = "phone number is required")
    @Pattern(regexp = "\\d+", message = "invalid phone number")
    private String phoneNumber;

}
