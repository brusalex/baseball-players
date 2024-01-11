package com.intuit.players;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Collection;


@RestController
@RequestMapping("/players")
@AllArgsConstructor
public class PlayerController {

    public final PlayerService playerService;

    @GetMapping
    public Collection<Player> getAllPlayers() {
        return playerService.getAll();
    }

    @GetMapping("/{id}")
    public Player getPlayerById(@PathVariable String id) {
        return playerService.getById(id);
    }
}
