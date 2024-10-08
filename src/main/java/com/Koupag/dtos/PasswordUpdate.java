package com.Koupag.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordUpdate {
    @JsonProperty("newPassword")
    String newPassword;
    @JsonProperty("oldPassword")
    String oldPassword;
}
