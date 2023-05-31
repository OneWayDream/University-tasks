package ru.itis.tsisa.blockchain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.tsisa.blockchain.models.UserKeys;

import java.util.Optional;

public interface MinersKeysRepository extends JpaRepository<UserKeys, Long> {

    Optional<UserKeys> findByUserId(Integer userId);

}
