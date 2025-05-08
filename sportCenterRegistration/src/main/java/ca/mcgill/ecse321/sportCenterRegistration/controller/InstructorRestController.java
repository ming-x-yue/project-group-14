package ca.mcgill.ecse321.sportCenterRegistration.controller;

import java.util.ArrayList;
import java.util.List;

// import org.checkerframework.checker.units.qual.A; // I don't know what this is, so I just commented it out
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.sportCenterRegistration.dto.InstructorDTO;
import ca.mcgill.ecse321.sportCenterRegistration.model.Instructor;
import ca.mcgill.ecse321.sportCenterRegistration.service.InstructorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RestController
@Tag(name = "Instructor Management", description = "Operations for managing instructors")
public class InstructorRestController {
    @Autowired
    public InstructorService InstructorService;

    /*
     * 
     * @author Muhammad Hammad
     * 
     * method that converts a Instructor object to a Instructor dto
     * 
     */
    private InstructorDTO convertToDTO(Instructor Instructor) {
        if (Instructor == null) {
            throw new IllegalArgumentException("There is no such Instructor!");
        }
        InstructorDTO InstructorDTO = new InstructorDTO(Instructor.getId(), Instructor.getUsername(),
                Instructor.getEmail(), Instructor.getPassword(), "Instructor");
        return InstructorDTO;
    }

    /*
     * 
     * @author Muhammad Hammad
     * 
     * method that converts a list of Instructors into a list of Instructor dtos
     * 
     */
    private List<InstructorDTO> convertListToDto(List<Instructor> listInstructor) {
        List<InstructorDTO> listInstructorDTO = new ArrayList<InstructorDTO>(listInstructor.size());
        for (Instructor Instructor : listInstructor) {
            listInstructorDTO.add(convertToDTO(Instructor));
        }
        return listInstructorDTO;
    }

    /*
     * 
     * @author Muhammad Hammad
     * 
     * Controller method that creates a new Instructor object
     * 
     * 
     */

    @PostMapping(value = { "/instructor", "/instructor/" })
    @Operation(summary = "Create a new instructor", description = "Creates a new instructor with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Instructor created successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = InstructorDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Failed to create instructor", 
                    content = @Content)
    })
    public ResponseEntity<?> createInstructor(
            @Parameter(description = "Username for the new instructor") @RequestParam("username") String username,
            @Parameter(description = "Email for the new instructor") @RequestParam("email") String email,
            @Parameter(description = "Password for the new instructor") @RequestParam("password") String password)
            throws IllegalArgumentException {
        try {
            Instructor Instructor = InstructorService.createInstructor(username, email, password);
            return ResponseEntity.ok(convertToDTO(Instructor));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    /*
     * 
     * @author Muhammad Hammad
     * 
     * Controller method that returns a Instructor object by taking its username
     * 
     * 
     */

    @GetMapping(value = { "/instructor/{username}", "/instructor/{username}/" })
    @Operation(summary = "Get instructor by username", description = "Retrieves an instructor by their username")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Instructor found", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = InstructorDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid username or instructor not found", 
                    content = @Content)
    })
    public ResponseEntity<?> getInstructor(
            @Parameter(description = "Username of the instructor to retrieve") @PathVariable("username") String username) 
            throws IllegalArgumentException {
        try {
            Instructor Instructor = InstructorService.getInstructor(username);
            return ResponseEntity.ok(convertToDTO(Instructor));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = { "/instructor/all", "/instructor/all/" })
    @Operation(summary = "Get all instructors", description = "Retrieves all instructors in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Instructors retrieved successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = InstructorDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Error retrieving instructors", 
                    content = @Content)
    })
    public ResponseEntity<?> getAllInstructors() throws IllegalArgumentException {
        try {
            List<Instructor> Instructors = InstructorService.getAllInstructors();

            return ResponseEntity.ok(convertListToDto(Instructors));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
     * 
     * @author Muhammad Hammad
     * 
     * Controller method that deletes a Instructor with a given username
     * 
     * 
     * 
     */

    @DeleteMapping(value = { "/instructor/{username}", "/instructor/{username}/" })
    @Operation(summary = "Delete an instructor", description = "Deletes an instructor by their username")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Instructor deleted successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = InstructorDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Error deleting instructor", 
                    content = @Content)
    })
    public ResponseEntity<?> deleteInstructor(
            @Parameter(description = "Username of the instructor to delete") @PathVariable("username") String username)
            throws IllegalArgumentException {
        try {
            Instructor instructorDelete = InstructorService.deleteInstructor(username);
            return ResponseEntity.ok(convertToDTO(instructorDelete));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
     * @author Muhammad Hammad
     * 
     * Controller method that updates the information of a Instructor
     * 
     */

    @PutMapping(value = { "/instructor/update/{oldUsername}/{username}/{email}/{password}",
            "/instructor/update/{oldUsername}/{username}/{email}/{password}/" })
    @Operation(summary = "Update instructor information", description = "Updates an instructor's username, email, and password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Instructor updated successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = InstructorDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Error updating instructor", 
                    content = @Content)
    })
    public ResponseEntity<?> updateInstructor(
            @Parameter(description = "Current username of the instructor") @PathVariable("oldUsername") String oldUsername,
            @Parameter(description = "New username for the instructor") @PathVariable("username") String username, 
            @Parameter(description = "New email for the instructor") @PathVariable("email") String email,
            @Parameter(description = "New password for the instructor") @PathVariable("password") String password) throws IllegalArgumentException {
        try {
            Instructor Instructor = InstructorService.updateInstructor(oldUsername, username, email, password);
            return ResponseEntity.ok(convertToDTO(Instructor));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
