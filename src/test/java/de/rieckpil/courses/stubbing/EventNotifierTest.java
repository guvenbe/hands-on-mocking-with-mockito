package de.rieckpil.courses.stubbing;

import de.rieckpil.courses.BannedUsersClient;
import de.rieckpil.courses.EventNotifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventNotifierTest {

  @Mock
  private EventNotifier eventNotifier;

  @Mock
  private BannedUsersClient bannedUsersClient;

  @Test
  void voidMethodStubbing() {

    doNothing()
      .doThrow(new RuntimeException("Error"))
      .when(eventNotifier).notifyNewUserCreation("duke");
    eventNotifier.notifyNewUserCreation("duke");

    assertThrows(RuntimeException.class, () -> eventNotifier.notifyNewUserCreation("duke"));

  }

  @Test
  void doReturnExample() {
    doReturn(42).when(bannedUsersClient).amountOfBannedAccounts();
    assertEquals(42, bannedUsersClient.amountOfBannedAccounts());
  }
}
