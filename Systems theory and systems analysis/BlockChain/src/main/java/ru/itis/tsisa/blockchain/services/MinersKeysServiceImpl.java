package ru.itis.tsisa.blockchain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.tsisa.blockchain.exceptions.EntityNotFoundException;
import ru.itis.tsisa.blockchain.models.UserKeys;
import ru.itis.tsisa.blockchain.repositories.MinersKeysRepository;

@Service
@RequiredArgsConstructor
public class MinersKeysServiceImpl implements MinersKeysService {

    private final MinersKeysRepository repository;

    @Override
    public void add(UserKeys userKeys) {
        repository.save(userKeys);
    }

    @Override
    public UserKeys getByUserId(Integer userId) {
        return repository.findByUserId(userId).orElseThrow(EntityNotFoundException::new);
    }

}
