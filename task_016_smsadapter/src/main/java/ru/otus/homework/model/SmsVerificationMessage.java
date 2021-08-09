/*
 * Created by DQCodegen
 */
package ru.otus.homework.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SmsVerificationMessage {

    @JsonProperty("quid")
    protected String quid;

    @JsonProperty("phoneNumber")
    protected String phoneNumber;

    @JsonProperty("code")
    protected String code;

}
