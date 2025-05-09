package ca.mcgill.ecse321.sportCenterRegistration.controller;

import java.sql.Time;
import java.sql.Date;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.sportCenterRegistration.dto.*;
import ca.mcgill.ecse321.sportCenterRegistration.model.*;
import ca.mcgill.ecse321.sportCenterRegistration.service.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Session Management", description = "Operations for managing sessions")
public class SessionController {

	@Autowired
	private SessionService SessionService;
	@Autowired
	private InstructorService instructorService;
	@Autowired
	private OwnerService ownerService;

	/**
	 * author: Stephen
	 * This method gets all session
	 */
	@GetMapping(value = { "/view_sessions", "/view_sessions/" })
	@Operation(summary = "Get sessions", description = "Retrieves sessions filtered by ID or sport class name")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Sessions retrieved successfully", 
					content = { @Content(mediaType = "application/json", 
								schema = @Schema(implementation = SessionDTO.class)) }),
		@ApiResponse(responseCode = "404", description = "No sessions found", 
					content = @Content)
	})
	public ResponseEntity<?> getSessions(
			@Parameter(description = "ID of the session to retrieve") @RequestParam(value = "sessionId", required = false) String sessionId,
			@Parameter(description = "Name of the sport class to filter by") @RequestParam(value = "sportclassName", required = false) String sportclassName)
			throws IllegalArgumentException {
		if (sessionId == null && sportclassName == null) {
			return ResponseEntity.ok(SessionService.getAllSession().stream().map(session -> convertToDTO(session))
					.collect(Collectors.toList()));
		}
		if (sportclassName != null) {
			return ResponseEntity.ok(
					SessionService.getSessionBySportClass(sportclassName).stream().map(session -> convertToDTO(session))
							.collect(Collectors.toList()));
		}
		if (sessionId != null) {
			return ResponseEntity.ok(SessionService.getSession(Integer.parseInt(sessionId)).stream()
					.map(session -> convertToDTO(session))
					.collect(Collectors.toList()));
		}
		return ResponseEntity.notFound().build();

	}

	/**
	 * author: Stephen
	 * This method gets a specific session schedule given the corresponding id
	 */
	// @GetMapping(value = { "/view_session/{id}", "/view_session/{id}/" })
	// public SessionDTO viewStore(@PathVariable("id") String id) {
	// // Correctly parsing the id from String to int
	// int sessionId = Integer.parseInt(id);

	// // Assuming SessionService.getSession expects an int and returns a Session
	// // object
	// // that needs to be converted to SessionDTO
	// return convertToDTO(SessionService.getSession(sessionId));
	// }

	// @GetMapping(value = "view_sessios/{sportclassName}")
	// public List<SessionDTO>
	// getSessionBySportClass(@PathVariable("sportclassName") String sportclassName)
	// {
	// try {
	// return
	// SessionService.getSessionBySportClass(sportclassName).stream().map(session ->
	// convertToDTO(session))
	// .collect(Collectors.toList());
	// } catch (IllegalArgumentException e) {
	// return null;
	// }
	// }

	/**
	 * author: Stephen
	 * This method creates a daily schedule
	 */
	@PostMapping(value = { "/create_session", "/create_session/" })
	@Operation(summary = "Create a new session", description = "Creates a new session for a sport class with an instructor")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Session created successfully", 
					content = { @Content(mediaType = "application/json", 
								schema = @Schema(implementation = SessionDTO.class)) }),
		@ApiResponse(responseCode = "500", description = "Failed to create session", 
					content = @Content)
	})
	public ResponseEntity<?> createSession(
			@Parameter(description = "Start time of the session (HH:mm)") @RequestParam("startTime") String startTime,
			@Parameter(description = "End time of the session (HH:mm)") @RequestParam("endTime") String endTime,
			@Parameter(description = "Location where the session will be held") @RequestParam("location") String location,
			@Parameter(description = "Date of the session (YYYY-MM-DD)") @RequestParam("date") String date,
			@Parameter(description = "Username of the instructor") @RequestParam("instructorName") String instructorName,
			@Parameter(description = "Name of the sport class") @RequestParam("sportclassName") String sportclassName) {

		Session session = null;

		startTime = startTime + ":00";
		endTime = endTime + ":00";

		try {
			session = SessionService.createSession(Time.valueOf(startTime), Time.valueOf(endTime), location,
					Date.valueOf(date), instructorName, sportclassName);
			return ResponseEntity.ok(convertToDTO(session));
		} catch (IllegalArgumentException exception) {
			return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = { "/update_session/{sessionId}", "/update_session/{sessionId}/" })
	@Operation(summary = "Update session details", description = "Updates an existing session with new details")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Session updated successfully", 
					content = { @Content(mediaType = "application/json", 
								schema = @Schema(implementation = SessionDTO.class)) }),
		@ApiResponse(responseCode = "500", description = "Failed to update session", 
					content = @Content)
	})
	public ResponseEntity<?> updateSession(
			@Parameter(description = "ID of the session to update") @PathVariable("sessionId") String Id,
			@Parameter(description = "New start time (HH:mm)") @RequestParam(value = "startTime", required = false) String startTime,
			@Parameter(description = "New end time (HH:mm)") @RequestParam(value = "endTime", required = false) String endTime,
			@Parameter(description = "New location") @RequestParam(value = "location", required = false) String location,
			@Parameter(description = "New date (YYYY-MM-DD)") @RequestParam(value = "date", required = false) String date,
			@Parameter(description = "New instructor username") @RequestParam(value = "instructorName", required = false) String instructorName) {

		Session session = SessionService.getSession(Integer.parseInt(Id)).get(0);

		if (startTime == null) {
			startTime = session.getStartTime().toString();
		}
		if (endTime == null) {
			endTime = session.getEndTime().toString();
		}

		if (location == null) {
			location = session.getLocation();
		}

		if (date == null) {
			date = session.getDate().toString();
		}

		if (instructorName == null) {
			instructorName = session.getInstructor().getUsername();
		}

		try {
			session = SessionService.updateSession(Integer.parseInt(Id), Time.valueOf(startTime), Time.valueOf(endTime),
					location, Date.valueOf(date), instructorName, session.getSportClass());
			return ResponseEntity.ok(convertToDTO(session));
		} catch (IllegalArgumentException exception) {
			return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(value = { "/delete_session", "/delete_session/" })
	@Operation(summary = "Delete a session", description = "Deletes a session by its ID")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Session deleted successfully", 
					content = @Content),
		@ApiResponse(responseCode = "400", description = "Error deleting session", 
					content = @Content)
	})
	public boolean deleteSession(
			@Parameter(description = "ID of the session to delete") @RequestParam("Id") String Id) {

		return SessionService.deleteSession(Integer.parseInt(Id));

	}

	public static SessionDTO convertToDTO(Session session) {
		if (session == null)
			throw new IllegalArgumentException("Session not found.");

		SessionDTO sessionDto = new SessionDTO(session.getDate(),
				session.getStartTime(), session.getEndTime(), session.getId(), session.getLocation(),
				session.getInstructor(), session.getSportClass());
		return sessionDto;

	}
}