package com.dmsc.libraryserviceapi.service.notification;

import com.dmsc.libraryserviceapi.model.book.BookResponse;
import com.dmsc.libraryserviceapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MockNotificationServiceImpl implements NotificationService {
    private static final Logger LOG = LoggerFactory.getLogger(MockNotificationServiceImpl.class);

    private final UserRepository userRepository;

    public MockNotificationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void sendEmailNotification(BookResponse book) {
        userRepository.findAllByAdminEquals(true)
            .forEach(adminUser ->
                LOG.info("Mock email to user: {} for book: {}", null, book)
            );
    }
}
