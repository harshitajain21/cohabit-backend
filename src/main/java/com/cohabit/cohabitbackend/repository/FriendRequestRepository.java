package com.cohabit.cohabitbackend.repository;

import com.cohabit.cohabitbackend.model.FriendRequest;
import com.cohabit.cohabitbackend.model.User;
import com.cohabit.cohabitbackend.model.enums.FriendRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//Provides database access for friend requests.

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    //Finds a friend request between two users in either direction for a status.
    Optional<FriendRequest> findByRequesterAndRecipientAndStatusOrRequesterAndRecipientAndStatus(
            User requester,
            User recipient,
            FriendRequestStatus status,
            User reverseRequester,
            User reverseRecipient,
            FriendRequestStatus reverseStatus
    );
}
