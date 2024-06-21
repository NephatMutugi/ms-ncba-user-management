package org.ncba.main.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @ Author Nephat Muchiri
 * Date 20/06/2024
 */
@Getter
@Setter
public class UserSearchRequest {
    private Long userId;
    private String documentNumber;
    private String msisdn;
}
