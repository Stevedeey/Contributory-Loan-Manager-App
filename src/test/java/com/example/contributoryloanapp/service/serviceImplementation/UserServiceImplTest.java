package com.example.contributoryloanapp.service.serviceImplementation;

import com.example.contributoryloanapp.model.User;
import com.example.contributoryloanapp.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(MockitoExtension.class)

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void justTest() {
        boolean flag = true;
        assertTrue(flag);
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setLastName("Doe");
        user.setPasswordResetExpireDate("2020-03-01");
        user.setDateOfBirth("2020-03-01");
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setPasswordResetToken("ABC123");
        user.setGender("Gender");
        user.setId(123L);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        user.setUpdatedAt(Date.from(atStartOfDayResult.atZone(ZoneId.systemDefault()).toInstant()));
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        user.setCreatedAt(Date.from(atStartOfDayResult1.atZone(ZoneId.systemDefault()).toInstant()));
        user.setFirstName("Jane");
        when(this.userRepository.save((User) any())).thenReturn(user);
        User actualCreateUserResult = this.userServiceImpl
                .createUser(new User("Jane", "Doe", "jane.doe@example.org", "Gender", "2020-03-01", "iloveyou"));
        assertEquals("Doe", actualCreateUserResult.getLastName());
        assertEquals("Gender", actualCreateUserResult.getGender());
        assertEquals("iloveyou", actualCreateUserResult.getFirstName());
        assertEquals("jane.doe@example.org", actualCreateUserResult.getEmail());
        assertEquals("2020-03-01", actualCreateUserResult.getDateOfBirth());
        verify(this.userRepository).save((User) any());
    }

    @Test
    public void testCreateUser2() {
        User user = new User();
        user.setLastName("Doe");
        user.setPasswordResetExpireDate("2020-03-01");
        user.setDateOfBirth("2020-03-01");
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setPasswordResetToken("ABC123");
        user.setGender("Gender");
        user.setId(123L);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        user.setUpdatedAt(Date.from(atStartOfDayResult.atZone(ZoneId.systemDefault()).toInstant()));
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        user.setCreatedAt(Date.from(atStartOfDayResult1.atZone(ZoneId.systemDefault()).toInstant()));
        user.setFirstName("Jane");
        when(this.userRepository.save((User) any())).thenReturn(user);
        User user1 = mock(User.class);
        when(user1.getGender()).thenReturn("foo");
        when(user1.getDateOfBirth()).thenReturn("foo");
        when(user1.getPassword()).thenReturn("foo");
        when(user1.getEmail()).thenReturn("foo");
        when(user1.getLastName()).thenReturn("foo");
        when(user1.getFirstName()).thenReturn("foo");
        User actualCreateUserResult = this.userServiceImpl.createUser(user1);
        assertEquals("foo", actualCreateUserResult.getLastName());
        assertEquals("foo", actualCreateUserResult.getGender());
        assertEquals("foo", actualCreateUserResult.getFirstName());
        assertEquals("foo", actualCreateUserResult.getEmail());
        assertEquals("foo", actualCreateUserResult.getDateOfBirth());
        verify(this.userRepository).save((User) any());
        verify(user1).getDateOfBirth();
        verify(user1).getEmail();
        verify(user1).getFirstName();
        verify(user1).getGender();
        verify(user1).getLastName();
        verify(user1).getPassword();
    }

}