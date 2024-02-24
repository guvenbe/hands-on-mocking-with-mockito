package de.rieckpil.courses.stubbing;

import de.rieckpil.courses.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RegistrationServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private BannedUsersClient bannedUsersClient;

  @InjectMocks
  private RegistrationService cut;

  @Test
  void defaultBehaviour() {
    System.out.println(userRepository.findByUsername("duke"));
    System.out.println(userRepository.save(new User()));
    System.out.println(bannedUsersClient.isBanned("duke", new Address()));
    System.out.println(bannedUsersClient.amountOfBannedAccounts());
    System.out.println(bannedUsersClient.amountOfGloballyBannedAccounts());
    System.out.println(bannedUsersClient.banRate());
    System.out.println(bannedUsersClient.bannedUserId());

  }

  @Test
  void basicStubbing() {
    when(bannedUsersClient.isBanned("duke", new Address())).thenReturn(true);
    when(userRepository.save(any(User.class))).thenReturn(new User());

    assertTrue(bannedUsersClient.isBanned("duke", new Address()));
    assertFalse(bannedUsersClient.isBanned("duke", null));
    assertFalse(bannedUsersClient.isBanned("mike", new Address()));
  }

  @Test
  void basicStubbingWithArgumentMatchers() {
    when(bannedUsersClient.isBanned(ArgumentMatchers.eq("duke"), any(Address.class))).thenReturn(true);
    when(bannedUsersClient.isBanned(anyString(), isNull())).thenReturn(true);
    when(bannedUsersClient.isBanned(argThat(s -> s.length() <= 3), any(Address.class))).thenReturn(true);
    assertTrue(bannedUsersClient.isBanned("duke", new Address()));

  }

  @Test
  void basicStubbingUsageThrows() {
    when(bannedUsersClient.isBanned(eq("duke"), any())).thenThrow(new RuntimeException("Something went wrong"));
    assertThrows(RuntimeException.class, () -> bannedUsersClient.isBanned("duke", new Address()));
  }

  @Test
  void basicStubbingUsageCallRealMethod() {
    when(bannedUsersClient.isBanned(eq("duke"), any(Address.class))).thenCallRealMethod();
    assertFalse(bannedUsersClient.isBanned("duke", new Address()));
  }

  @Test
  void basicStubbingUsageThenAnswer() {
    when(bannedUsersClient.isBanned(eq("duke"), any(Address.class)))
      .thenAnswer(invocation -> {
        System.out.println("Invoked with: " + invocation.getArgument(0));
        System.out.println("Invoked with: " + invocation.getArgument(1));
        String userName = invocation.getArgument(0);
        String address = invocation.getArgument(1).toString();
        if (userName.contains("d") && address.contains("d")) {
          return true;
        }
        return true;

        });

    Address address = new Address();
    address.setCity("Düsseldorf");
    assertEquals(true, bannedUsersClient.isBanned("duke", address));

    //db example
    when(userRepository.save(any(User.class))).thenAnswer(invocation-> {
        User user = invocation.getArgument(0);
        user.setId(42L);
        return user;
    });
    assertEquals(42L, userRepository.save(new User()).getId());

    when(userRepository.save(argThat(u -> u.getUsername().contains("d"))))
      .thenAnswer(invocation-> {
        User user = invocation.getArgument(0);
        user.setId(42L);
        return user;
    });
}




  @Test
  void shouldNotAllowRegistrationOfBannedUsers() {
    when(bannedUsersClient
      .isBanned(eq("duke"), any(Address.class)))
      .thenReturn(true);

    assertThrows(IllegalArgumentException.class,
      () -> cut.registerUser("duke",
        Utils.createContactInformation("duke@email.com", "1234", "London", 1234)));
  }

  @Test
  void shouldAllowRegistrationOfNewUser() {
  }
}
