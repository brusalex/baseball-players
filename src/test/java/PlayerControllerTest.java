import com.intuit.players.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



@ExtendWith(MockitoExtension.class)
public class PlayerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(playerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllPlayersTest() throws Exception {
        List<Player> expectedPlayers = List.of(
                Player.builder().playerID("1").build(),
                Player.builder().playerID("2").build()
        );
        when(playerService.getAll()).thenReturn(expectedPlayers);
        mockMvc.perform(get("/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].playerID", is(expectedPlayers.get(0).getPlayerID())))
                .andExpect(jsonPath("$[1].playerID", is(expectedPlayers.get(1).getPlayerID())));
    }

    @Test
    void getPlayerByIdTest_Success() throws Exception {
        Player testPlayer = Player.builder().playerID("1").build();
        when(playerService.getById("1")).thenReturn(testPlayer);
        mockMvc.perform(MockMvcRequestBuilders.get("/players/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.playerID", is(testPlayer.getPlayerID())));
    }

    @Test
    void getPlayerByIdTest_NotFound() throws Exception {
        String nonexistentId = "nonexistentId";
        when(playerService.getById(nonexistentId)).thenThrow(new PlayerNotFoundException("Player not found with ID: " + nonexistentId));

        mockMvc.perform(MockMvcRequestBuilders.get("/players/" + nonexistentId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getSomeException_InternalServerError() throws Exception {
        String nonexistentId = "nonexistentId";
        when(playerService.getById(nonexistentId)).thenThrow(new RuntimeException("some exception"));
        mockMvc.perform(MockMvcRequestBuilders.get("/players/" + nonexistentId))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

}
