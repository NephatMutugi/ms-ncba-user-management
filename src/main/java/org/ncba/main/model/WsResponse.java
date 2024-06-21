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
public class WsResponse<T> {
    private WsHeader header;
    private T body;
}
