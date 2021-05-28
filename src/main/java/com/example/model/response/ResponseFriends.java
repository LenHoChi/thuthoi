package com.example.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"success", "friends", "count", "recipients"})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFriends {
    @JsonProperty("success")
    private boolean success;
    @JsonProperty("friends")
    private List<String> friends;
    @JsonProperty("count")
    private Integer count;
    @JsonProperty("recipients")
    private List<String> recipients;
}
