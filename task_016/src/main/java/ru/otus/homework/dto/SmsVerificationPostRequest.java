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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "SmsVerificationPostRequest", description = "")
public class SmsVerificationPostRequest implements Serializable {

    @ApiModelProperty(name = "PhoneNumber", dataType = "java.lang.String", value = "Номер телефона", required = false, position = 1)
    @JsonProperty("PhoneNumber")
    protected String phoneNumber;

}
