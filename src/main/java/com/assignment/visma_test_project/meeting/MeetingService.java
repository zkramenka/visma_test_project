package com.assignment.visma_test_project.meeting;

import com.assignment.visma_test_project.enums.Category;
import com.assignment.visma_test_project.enums.Type;
import com.assignment.visma_test_project.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;

import static java.lang.Long.valueOf;

@Slf4j
@Service
public class MeetingService {

    MeetingRepository meetingRepository;

    @Autowired
    public MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public void addNewMeeting(Meeting meeting) {

        if (Objects.isNull(meeting.getName())) {

            log.error("Name is NULL. Not adding new meeting");
            throw new InvalidRequestException("Name is NULL. Not adding new meeting");
        }
        if (meeting.getName().equals("")) {
            log.error("Name is missing. Not adding new meeting");
            throw new InvalidRequestException("Name is missing. Not adding new meeting");
        }



        if (Objects.isNull(meeting.getResponsiblePerson())) {
            log.error("Responsible Person is NULL. Not adding new meeting");
            throw new InvalidRequestException("Responsible Person is NULL. Not adding new meeting");
        }
        if (meeting.getResponsiblePerson().equals("")){
            log.error("Responsible Person is missing. Not adding new meeting");
            throw new InvalidRequestException("Responsible Person is missing. Not adding new meeting");
        }



        if (Objects.isNull(meeting.getDescription())) {
            log.error("Description is NULL. Not adding new meeting");
            throw new InvalidRequestException("Description is NULL. Not adding new meeting");
        }
        if (meeting.getDescription().equals("")){
            log.error("Description is missing. Not adding new meeting");
            throw new InvalidRequestException("Description is missing. Not adding new meeting");
        }



        if (Objects.isNull(meeting.getCategory())) {
            log.error("Category is NULL. Not adding new meeting");
            throw new InvalidRequestException("Category is NULL. Not adding new meeting");
        }



        if (Objects.isNull(meeting.getType())) {
            log.error("Type is NULL. Not adding new meeting");
            throw new InvalidRequestException("Type is NULL. Not adding new meeting");
        }



        if (Objects.isNull(meeting.getStartDate())) {
            log.error("Start Date is NULL. Not adding new meeting");
            throw new InvalidRequestException("Start Date is NULL. Not adding new meeting");
        }
        if (Objects.isNull(meeting.getEndDate())) {
            log.error("End Date is NULL. Not adding new meeting");
            throw new InvalidRequestException("End Date is NULL. Not adding new meeting");
        }
        if (meeting.getEndDate().before(meeting.getEndDate())) {
            log.error("End Date is Before Start Date. Not adding new meeting");
            throw new InvalidRequestException("End Date is Before Start Date. Not adding new meeting");
        }



        if (!Objects.isNull(meeting.getId())) {
            log.error("ID should not be provided. Not adding new meeting");
            throw new InvalidRequestException("ID should not be provided. Not adding new meeting");
        }


        if (!Objects.isNull(meeting.getAttendees())) {
            log.error("Attendees should not be provided. Not adding new meeting");
            throw new InvalidRequestException("Attendees should not be provided. Not adding new meeting");
        }


        List<Meeting> meetings;
        try {
            meetings = meetingRepository.retrieve();
        } catch (IOException e) {
            log.error("Failed to load meeting from JSON file");
            throw new FailedToRetrieveException("Failed to load meetings from JSON file");
        }

        Long id;
        if (Objects.isNull(meetings)) id = 1L;
        else if (meetings.size() == 0) id = 1L;
        else id = valueOf(meetings.get(meetings.size() - 1).getId().longValue() + 1);

        meeting.setId(id);

        meeting.setName(meeting.getName().toLowerCase());
        meeting.setResponsiblePerson(meeting.getResponsiblePerson().toLowerCase());
        meeting.setDescription(meeting.getDescription().toLowerCase());

        meeting.setAttendees(new ArrayList<String>());
        meeting.getAttendees().add(new String(meeting.getResponsiblePerson()));


        meetings.add(meeting);

        try {
            meetingRepository.save(meetings);
        } catch (IOException e) {
            log.error("Failed to save meetings to the JSON file");
            throw new FailedToSaveException("Failed to save meetings to the JSON file");
        }

        log.info("New meeting saved");
    }

    public void removeMeeting(Long id,
                              String requester) {

        if (Objects.isNull(id)) {
            log.error("ID is NULL. Not deleting meeting");
            throw new InvalidRequestException("ID is NULL. Not deleting meeting");
        }

        if (Objects.isNull(requester)) {
            log.error("Requester is NULL. Not deleting meeting");
            throw new InvalidRequestException("Requester is NULL. Not deleting meeting");
        }
        if (requester.equals("")){
            log.error("Requester is missing. Not deleting meeting");
            throw new InvalidRequestException("Requester is missing. Not deleting meeting");
        }

        requester = requester.toLowerCase();

        List<Meeting> meetings;
        try {
            meetings = meetingRepository.retrieve();
        } catch (IOException e) {
            throw new FailedToRetrieveException("Failed to load meetings from JSON file");
        }

        int index;
        for (index = 0; index < meetings.size(); index++) {
            if (meetings.get(index).getId().equals(id)) break;
        }

        if (index == meetings.size()) {
            log.error("No meeting with specified ID. Not deleting meeting");
            throw new MeetingNotFoundException("No meeting with specified ID. Not deleting meeting");
        }
        if (!meetings.get(index).getResponsiblePerson().equals(requester)) {
            log.error("Requester does not math Responsible Person. Not deleting meeting");
            throw new RequesterIsNotResponsiblePersonException("Requester does not math Responsible Person. Not deleting meeting");
        }

        meetings.remove(index);

        try {
            meetingRepository.save(meetings);
        } catch (IOException e) {
            throw new FailedToSaveException("Failed to save meetings to the JSON file");
        }

        log.info("Meeting deleted");
    }

    public void addNewAttendee(Long id,
                               String attendee) {

        if (Objects.isNull(id)) {
            log.error("ID is NULL. Not adding attendee");
            throw new InvalidRequestException("ID is NULL. Not adding attendee");
        }

        if (Objects.isNull(attendee)) {
            log.error("Attendee is NULL. Not adding attendee");
            throw new InvalidRequestException("Attendee is NULL. Not adding attendee");
        }
        if (attendee.equals("")){
            log.error("Attendee is missing. Not adding attendee");
            throw new InvalidRequestException("Attendee is missing. Not adding attendee");
        }

        attendee = attendee.toLowerCase();


        List<Meeting> meetings;
        try {
            meetings = meetingRepository.retrieve();
        } catch (IOException e) {
            throw new FailedToRetrieveException("Failed to load meetings from JSON file");
        }

        int index;
        for (index = 0; index < meetings.size(); index++) {
            if (meetings.get(index).getId().equals(id)) break;
        }

        if (index == meetings.size()) {
            log.error("No meeting with specified ID. Not adding attendee");
            throw new MeetingNotFoundException("No meeting with specified ID. Not adding attendee");
        }
        if (meetings.get(index).getAttendees().contains(attendee)) {
            log.error("Specified Person already attending the meeting. Not adding another instance");
            throw new AttendeeAlreadyInMeetingException("Specified Person already attending the meeting. Not adding another instance");
        }

        // Check overlap
        for (Meeting meeting : meetings) {
            if (meeting.getAttendees().contains(attendee) && meeting.getStartDate().compareTo(meetings.get(index).getEndDate()) <= 0 && meeting.getEndDate().compareTo(meetings.get(index).getStartDate()) >= 0) {
                log.error("Person is already attending an overlapping meeting. Not adding attendee");
                throw new MeetingOverlapException("Person is already attending an overlapping meeting. Not adding attendee");
            }
        }

        meetings.get(index).getAttendees().add(attendee);

        try {
            meetingRepository.save(meetings);
        } catch (IOException e) {
            throw new FailedToSaveException("Failed to save meetings to the JSON file");
        }

        log.info("Attendee added to the meeting");
    }

    public void removeAttendee(Long id,
                               String attendee) {

        if (Objects.isNull(id)) {
            log.error("ID is NULL. Not removing attendee");
            throw new InvalidRequestException("ID is NULL. Not removing attendee");
        }

        if (Objects.isNull(attendee)) {
            log.error("Attendee is NULL. Not removing attendee");
            throw new InvalidRequestException("Attendee is NULL. Not removing attendee");
        }
        if (attendee.equals("")){
            log.error("Attendee is missing. Not removing attendee");
            throw new InvalidRequestException("Attendee is missing. Not removing attendee");
        }

        attendee = attendee.toLowerCase();


        List<Meeting> meetings;
        try {
            meetings = meetingRepository.retrieve();
        } catch (IOException e) {
            throw new FailedToRetrieveException("Failed to load meetings from JSON file");
        }

        int index;
        for (index = 0; index < meetings.size(); index++) {
            if (meetings.get(index).getId().equals(id)) break;
        }

        if (index == meetings.size()) {
            log.error("No meeting with specified ID. Not removing attendee");
            throw new MeetingNotFoundException("No meeting with specified ID. Not removing attendee");
        }


        if (!meetings.get(index).getAttendees().contains(attendee)) {
            log.error("Specified Person is not attending the meeting. Not removing attendee");
            throw new AttendeeNotInMeetingException("Specified Person is not attending the meeting. Not removing attendee");
        }

        if (meetings.get(index).getResponsiblePerson().equals(attendee)) {
            log.error("Specified Person is the Responsible Person of the Meeting. Not removing attendee");
            throw new SpecifiedPersonIsResponsiblePersonException("Specified Person is the Responsible Person of the Meeting. Not removing attendee");
        }

        meetings.get(index).getAttendees().remove(attendee);

        try {
            meetingRepository.save(meetings);
        } catch (IOException e) {
            throw new FailedToSaveException("Failed to save meetings to the JSON file");
        }

        log.info("Attendee removed from the meeting");
    }

    List<Meeting> getAllMeetings(String description,
                              String responsiblePerson,
                              Category category,
                              Type type,
                              Date startDate,
                              Date endDate,
                              Long minimumAttendees,
                              Long maximumAttendees) {
        List<Meeting> meetings;
        try {
            meetings = meetingRepository.retrieve();
        } catch (IOException e) {
            throw new FailedToRetrieveException("Failed to load meetings from JSON file");
        }


        // No need to create deep copies
        List<Meeting> filteredMeetings = new ArrayList<Meeting>();

        // Null if not here during serialization
        for (Meeting meeting : meetings) {
            if (!Objects.isNull(description)) {
                description = description.toLowerCase();
                if (!meeting.getDescription().contains(description)) continue;
            }

            if (!Objects.isNull(responsiblePerson)) {
                responsiblePerson = responsiblePerson.toLowerCase();
                if (!meeting.getResponsiblePerson().equals(responsiblePerson)) continue;
            }

            if (!Objects.isNull(category)) {
                if (!meeting.getCategory().equals(category)) continue;
            }

            if (!Objects.isNull(type)) {
                if (!meeting.getType().equals(type)) continue;
            }


            // Here I am Assuming that Date Filter Bounds Refer to Meeting Start Dates (not End Dates)
            if (!Objects.isNull(startDate)) {
                if (meeting.getStartDate().before(startDate)) continue;
            }

            // Here I am Assuming that Date Filter Bounds Refer to Meeting Start Dates (not End Dates)
            if (!Objects.isNull(endDate)) {
                if (meeting.getStartDate().after(endDate)) continue;
            }

            if (!Objects.isNull(minimumAttendees)) {
                if (meeting.getAttendees().size() < minimumAttendees.longValue()) continue;
            }

            if (!Objects.isNull(maximumAttendees)) {
                if (meeting.getAttendees().size() > maximumAttendees.longValue()) continue;
            }

            filteredMeetings.add(meeting);
        }

        return filteredMeetings;
    }
}
