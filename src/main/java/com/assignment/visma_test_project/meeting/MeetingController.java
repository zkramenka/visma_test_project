package com.assignment.visma_test_project.meeting;

import com.assignment.visma_test_project.enums.Category;
import com.assignment.visma_test_project.enums.Type;
import com.assignment.visma_test_project.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class MeetingController {

    private final MeetingService meetingService;

    @Autowired
    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @Validated
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/meetings")
    public void postMeeting(@RequestBody Meeting meeting) {
        meetingService.addNewMeeting(meeting);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/meetings/{id}")
    public void deleteMeeting(@PathVariable(name = "id") Long id,
                              @RequestParam(name = "requester") String requester) {
        meetingService.removeMeeting(id, requester);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/meetings/{id}/addAttendee")
    public void addAttendee(@PathVariable(name = "id") Long id,
                            @RequestParam String attendee) {
        meetingService.addNewAttendee(id, attendee);
    }

    @PatchMapping("/meetings/{id}/deleteAttendee")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAttendee(@PathVariable(name = "id") Long id,
                               @RequestParam String attendee) {
        meetingService.removeAttendee(id, attendee);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/meetings")
    public List<Meeting> getMeetings(@RequestParam(value = "description", required = false) String description,
                            @RequestParam(value = "responsiblePerson", required = false) String responsiblePerson,
                            @RequestParam(value = "category", required = false) Category category,
                            @RequestParam(value = "type", required = false) Type type,
                            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                            @RequestParam(value = "minimumAttendees", required = false) Long minimumAttendees,
                            @RequestParam(value = "maximumAttendees", required = false) Long maximumAttendees) {
        return meetingService.getAllMeetings(description, responsiblePerson, category, type, startDate, endDate, minimumAttendees, maximumAttendees);
    }

    @ExceptionHandler({
            InvalidRequestException.class,
            FailedToRetrieveException.class,
            FailedToSaveException.class,
            MeetingNotFoundException.class,
            RequesterIsNotResponsiblePersonException.class,
            MeetingOverlapException.class,
            AttendeeAlreadyInMeetingException.class,
            AttendeeNotInMeetingException.class,
            SpecifiedPersonIsResponsiblePersonException.class
    })
    public ResponseEntity<String> handleException(ResponseStatusException exception) {
        return ResponseEntity
                .status(exception.getStatus())
                .body(exception.getMessage());
    }
}
