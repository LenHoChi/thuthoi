package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "relationship", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Relationship implements Serializable {
    @EmbeddedId
    @NonNull
    private RelationshipPK relationshipPK;
    @Column(name = "arefriends")
    @NonNull
    private Boolean areFriends;
    @Column(name = "issubscriber")
    @NonNull
    private Boolean isSubscriber;
    @Column(name = "isblock")
    @NonNull
    private Boolean isBlock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email", insertable = false, updatable = false)
    @JsonIgnoreProperties("relationships")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;
}
