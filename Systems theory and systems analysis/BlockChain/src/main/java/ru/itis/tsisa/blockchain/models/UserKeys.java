package ru.itis.tsisa.blockchain.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "user_keys")
public class UserKeys {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Integer userId;

    @Column(name = "public_key", nullable = false, unique = true)
    private byte[] publicKey;

    @Column(name = "private_key", nullable = false, unique = true)
    private byte[] privateKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserKeys userKeys = (UserKeys) o;
        return id != null && Objects.equals(id, userKeys.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
