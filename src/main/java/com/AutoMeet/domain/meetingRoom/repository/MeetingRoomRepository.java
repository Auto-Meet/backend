package com.AutoMeet.domain.meetingRoom.repository;

import com.AutoMeet.domain.meetingRoom.model.MeetingRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MeetingRoomRepository extends MongoRepository<MeetingRoom, String> {
}
