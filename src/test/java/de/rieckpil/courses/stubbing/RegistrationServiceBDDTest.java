package de.rieckpil.courses.stubbing;

import de.rieckpil.courses.BannedUsersClient;
import de.rieckpil.courses.RegistrationService;
import de.rieckpil.courses.User;
import de.rieckpil.courses.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.print.MultiDocPrintService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RegistrationServiceBDDTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private BannedUsersClient bannedUsersClient;

  @InjectMocks
  private RegistrationService cut;

  @Test
  void basicStubbingWithBDD() {
    User user = new User();
    given(userRepository.findByUsername("duke"))
      .willReturn(user);

    given(userRepository.save(any(User.class)))
      .willAnswer(invocation -> {
        User userSaved =invocation.getArgument(0);
        userSaved.setId(42L);
        return userSaved;
      });

    given(userRepository.findByUsername("mike"))
      .willThrow(new RuntimeException("Error"));

    assertEquals(user, userRepository.findByUsername("duke"));
    assertEquals(42L, userRepository.save(new User()).getId());
    assertThrows(RuntimeException.class, () -> userRepository.findByUsername("mike"));
  }
}
