package com.example.model.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.Empty;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "friends"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestFriends {
    @JsonProperty("friends")
    private List<@NotNull(message = "not null for email") @Email(message = "email error") @NotEmpty(message = "must not be empty") String> emails;
}