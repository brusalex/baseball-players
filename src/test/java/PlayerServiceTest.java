import com.intuit.players.Player;
import com.intuit.players.PlayerNotFoundException;
import com.intuit.players.PlayerRepository;
import com.intuit.players.PlayerService;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerServiceTest {
    PlayerRepository repositoryMock = mock();
    PlayerService playerService = new PlayerService(repositoryMock);

    @Test
    void getAll_ShouldReturnAllPlayers() {
        List<Player> expected = List.of(
                Player.builder().playerID("1").build(),
                Player.builder().playerID("2").build()
        );

        when(repositoryMock.findAll()).thenReturn(expected);
        Collection<Player> all = playerService.getAll();
        assertEquals(expected, all);
    }

    @Test
    void getById_ShouldReturnPlayerWhenExists() {
        Player existPlayer = Player.builder().playerID("1").build();
        when(repositoryMock.findById("1")).thenReturn(Optional.ofNullable(existPlayer));
        Player result = playerService.getById("1");
        assertEquals(existPlayer, result);
    }

    @Test
    void getById_ShouldThrowPlayerNotFoundExceptionWhenNotExists() {
        String unknownId = "unknownId";
        PlayerNotFoundException thrown = assertThrows(PlayerNotFoundException.class,
                () -> playerService.getById(unknownId),
                "Expected getById to throw");
        assertTrue(thrown.getMessage().contains("Player not found with ID: " + unknownId));
    }
}
