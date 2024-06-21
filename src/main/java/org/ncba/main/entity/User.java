package org.ncba.main.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @ Author Nephat Muchiri
 * Date 20/06/2024
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class User {

    @Id
    @Column("user_id")
    @JsonProperty("userId")
    private Long id;
    @NotEmpty(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name should have 2 to 100 characters")
    @Column("name")
    private String name;
    @NotEmpty(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column("email")
    private String email;
    @Column("msisdn")
    private String msisdn;
    @Column("document_type")
    private String documentType;
    @Column("document_number")
    @JsonProperty("documentNumber")
    private String documentNumber;
    @Column("deleted")
    private boolean deleted;

}
