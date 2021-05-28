package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RelationshipPK implements Serializable {
    @Column(name = "user_email")
    @Email(message = "error email")
    @NotNull(message = "not null for email")
    @NotBlank(message = "must not be empty")
    private String userEmail;

    @Column(name = "friend_email")
    @Email(message = "error email")
    @NotNull(message = "not null for email")
    @NotBlank(message = "must not be empty")
    private String friendEmail;


}
