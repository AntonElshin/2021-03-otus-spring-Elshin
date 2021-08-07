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
public class SmsDeliveredMessage {

    @JsonProperty("quid")
    protected String quid;

}
