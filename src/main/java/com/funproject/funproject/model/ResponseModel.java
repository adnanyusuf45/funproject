package com.funproject.funproject.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModel {
    @JsonProperty("respCode")
    private String respCode;
    @JsonProperty("status")
    private String status;
    @JsonProperty("respDescription")
    private String respDescription;
}
