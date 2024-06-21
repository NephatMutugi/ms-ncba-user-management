package org.ncba.main.model;

import lombok.*;

/**
 * @ Author Nephat Muchiri
 * Date 20/06/2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WsHeader {
    private String requestRefId;
    private int responseCode;
    private String responseMessage;
    private String customerMessage;
    private String timestamp;
}
