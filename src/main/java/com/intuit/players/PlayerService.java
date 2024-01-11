package com.intuit.players;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Collection;


@Service
@AllArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;

    public Collection<Player> getAll() {
        return playerRepository.findAll();
    }

    public Player getById(String id) {
        return playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id));
    }
}
