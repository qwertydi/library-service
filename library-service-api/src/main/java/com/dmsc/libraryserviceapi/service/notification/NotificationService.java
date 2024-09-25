package com.dmsc.libraryserviceapi.service.notification;

import com.dmsc.libraryserviceapi.model.book.BookResponse;

/**
 * Interface for sending notifications
 */
public interface NotificationService {

    /**
     * Sends an email notification for a specific book.
     *
     * @param book A {@code LocalBookEntity} object representing the book for which the notification is to be sent.
     */
    void sendEmailNotification(BookResponse book);
}
