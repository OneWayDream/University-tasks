package ru.itis.tsisa.blockchain.services;

import ru.itis.tsisa.blockchain.models.UserKeys;

public interface MinersKeysService {

    void add(UserKeys userKeys);
    UserKeys getByUserId(Integer userId);

}
