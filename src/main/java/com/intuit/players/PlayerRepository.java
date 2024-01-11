package com.intuit.players;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class PlayerRepository  {

    private final CsvMapper csvMapper;
    @Getter(lazy = true, value = AccessLevel.PRIVATE)
    private final Map<String, Player> playersById = loadPlayersFromCsvFile();

    public PlayerRepository() {
        csvMapper = new CsvMapper();
        csvMapper.registerModule(new JavaTimeModule());
        csvMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public Collection<Player> findAll() {
        return getPlayersById().values();
    }

    public Optional<Player> findById(String id) {
        return Optional.ofNullable(getPlayersById().get(id));
    }


    @SneakyThrows
    private  Map<String, Player> loadPlayersFromCsvFile() {
        Map<String, Player> playerMap = new HashMap<>();
        CsvSchema schema = csvMapper.schemaWithHeader();
        try (Reader reader = new InputStreamReader(new ClassPathResource("player.csv").getInputStream())) {
            MappingIterator<Player> playerIterator;
            playerIterator = csvMapper.readerFor(Player.class).with(schema).readValues(reader);
            playerIterator.forEachRemaining(player -> playerMap.put(player.getPlayerID(), player));
        }
        return playerMap;
    }
}
