package com.funproject.funproject.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.funproject.funproject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListUserResponse {
    @JsonProperty("respCode")
    private String respCode;
    @JsonProperty("status")
    private String status;
    @JsonProperty("respDescription")
    private String respDescription;
    @JsonProperty("data")
    private List<User> data;
}
