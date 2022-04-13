package com.sellertool.server.domain.invite_member.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invite_member")
public class InviteMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Column(name = "id")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "user_id")
    @Type(type = "uuid-char")
    private UUID userId;

    @Column(name = "workspace_id")
    @Type(type = "uuid-char")
    private UUID workspaceId;

}
