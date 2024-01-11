package com.intuit.players;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String playerId) {
        super("Player not found with ID: " + playerId);
    }
}
