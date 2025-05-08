package ca.mcgill.ecse321.sportCenterRegistration.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.sportCenterRegistration.dto.SportClassDTO;
import ca.mcgill.ecse321.sportCenterRegistration.model.SportClass;
import ca.mcgill.ecse321.sportCenterRegistration.service.SportClassService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Sport Class Management", description = "Operations for managing sport classes")
public class SportClassController {
    @Autowired
    private SportClassService sportClassService;

    @GetMapping(value = { "/sport-class/{name}", "/sport-class/{name}/" })
    @Operation(summary = "Get sport class by name", description = "Retrieves a sport class by its name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sport class found", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = SportClassDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid name or sport class not found", 
                    content = @Content)
    })
    public SportClassDTO getSportClass(
            @Parameter(description = "Name of the sport class to retrieve") @PathVariable("name") String name) 
            throws IllegalArgumentException {
        SportClass sportClass = sportClassService.getSportClass(name);
        return convertToDto(sportClass);
    }

    @PostMapping(value = { "/sport-class/{name}", "/sport-class/{name}/" })
    @Operation(summary = "Create a new sport class", description = "Creates a new sport class with the given name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sport class created successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = SportClassDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Failed to create sport class", 
                    content = @Content)
    })
    public SportClassDTO createSportClass(
            @Parameter(description = "Name of the sport class to create") @PathVariable("name") String name) 
            throws IllegalArgumentException {
        SportClass sportClass = sportClassService.createSportClass(name);
        return convertToDto(sportClass);
    }

    @PutMapping(value = { "/sport-class/approve/{name}", "/sport-class/approve/{name}/" })
    @Operation(summary = "Approve a sport class", description = "Approves a sport class to make it available for scheduling")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sport class approved successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = SportClassDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Failed to approve sport class", 
                    content = @Content)
    })
    public SportClassDTO approveSportClass(
            @Parameter(description = "Name of the sport class to approve") @PathVariable("name") String name) 
            throws IllegalArgumentException {
        SportClass sportClass = sportClassService.approveSportClass(name);
        return convertToDto(sportClass);
    }

    @DeleteMapping(value = { "/sport-class/{name}", "/sport-class/{name}/" })
    @Operation(summary = "Delete a sport class", description = "Deletes a sport class by its name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sport class deleted successfully", 
                    content = @Content),
        @ApiResponse(responseCode = "400", description = "Error deleting sport class", 
                    content = @Content)
    })
    public void delete(
            @Parameter(description = "Name of the sport class to delete") @PathVariable("name") String name) 
            throws IllegalArgumentException {
        sportClassService.deleteSportClass(name);
    }

    @GetMapping(value = { "/sport-class/all", "/sport-class/all/" })
    @Operation(summary = "Get all sport classes", description = "Retrieves all sport classes in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sport classes retrieved successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = SportClassDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Error retrieving sport classes", 
                    content = @Content)
    })
    public List<SportClassDTO> getAllSportClass() throws IllegalArgumentException {
        return sportClassService.getAllSportClass().stream().map(p -> convertToDto(p)).collect(Collectors.toList());
    }

    private SportClassDTO convertToDto(SportClass s) throws IllegalArgumentException {
        return new SportClassDTO(s.getName(), s.getApproved());
    }
}