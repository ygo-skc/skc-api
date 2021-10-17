package com.rtomyj.skc.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@ApiModel(description = "Return object for test call endpoint.")
public class TestCallResponse {

    @ApiModelProperty(
            value = "The current status of the API."
            , accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    private final String status;
}
