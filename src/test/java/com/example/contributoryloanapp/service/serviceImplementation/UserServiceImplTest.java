package com.example.contributoryloanapp.service.serviceImplementation;



import com.example.contributoryloanapp.repository.UserRepository;



import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.context.ContextConfiguration;


import static org.junit.jupiter.api.Assertions.*;


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



}