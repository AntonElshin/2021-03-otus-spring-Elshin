/*
 * Created by DQCodegen
 */
package ru.otus.homework.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsVerificationMessage {

    @JsonProperty("quid")
    protected String quid;

    @JsonProperty("phoneNumber")
    protected String phoneNumber;

    @JsonProperty("code")
    protected String code;

}
