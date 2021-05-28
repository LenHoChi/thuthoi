package com.example.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"email"})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestFriendsList {
    @JsonProperty("email")
    @Email(message = "email error")
    @NotNull(message = "not null for email")
    @NotBlank(message = "must not be empty")
    private String email;
}
