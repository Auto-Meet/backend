package com.AutoMeet.domain.meet.api;

import com.AutoMeet.domain.meet.dto.response.MeetListResponse;
import com.AutoMeet.domain.meet.dto.response.MeetingResponse;
import com.AutoMeet.domain.meet.service.MeetService;
import com.AutoMeet.global.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meet")
public class MeetController {

    private final MeetService meetService;

    @GetMapping("")
    public ResponseEntity<List<MeetListResponse>> findMeets(@AuthenticationPrincipal PrincipalDetails principal) {
        List<MeetListResponse> meetingList = meetService.findMeets(principal.getUser().getId());

        return ResponseEntity.ok(meetingList);
    }

    @GetMapping("/{meetingId}")
    public ResponseEntity<MeetingResponse> findOne(@PathVariable String meetingId,
                                   @AuthenticationPrincipal PrincipalDetails principal) {
        MeetingResponse meeting = meetService.findOne(meetingId, principal.getUser().getId());
        // 댓글 부분 추가하면 여기에도 넣어줘야 함

        return ResponseEntity.ok(meeting);
    }
}