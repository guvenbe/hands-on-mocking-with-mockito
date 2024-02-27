package de.rieckpil.courses.advanced;

import de.rieckpil.courses.Address;
import de.rieckpil.courses.ContactInformation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeepStubTest {

  @Test
  void withoutDeepStubs() {
    ContactInformation contactInformation = mock(ContactInformation.class);
    Address address = mock(Address.class);

    when(contactInformation.getAddress()).thenReturn(address);
    when(address.getCity()).thenReturn("Berlin");
    assertEquals("Berlin", contactInformation.getAddress().getCity());
  }

  @Test
  void deepStubs() {
    ContactInformation contactInformation = mock(ContactInformation.class, Answers.RETURNS_DEEP_STUBS);
    when(contactInformation.getAddress().getCity()).thenReturn("Berlin");
    assertEquals("Berlin", contactInformation.getAddress().getCity());
  }
}
