/*
 * Created by DQCodegen
 */
package ru.otus.homework.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "SmsVerificationCheckResponse", description = "")
public class SmsVerificationCheckResponse implements Serializable {

    @ApiModelProperty(name = "CheckResult", dataType = "java.lang.Boolean", value = "Результат проверки", required = false, position = 1)
    @JsonProperty("CheckResult")
    protected Boolean checkResult;

}
