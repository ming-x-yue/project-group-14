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

import ca.mcgill.ecse321.sportCenterRegistration.dto.OwnerDTO;
import ca.mcgill.ecse321.sportCenterRegistration.model.Owner;
import ca.mcgill.ecse321.sportCenterRegistration.service.OwnerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RestController
@Tag(name = "Owner Management", description = "Operations for managing owners")
public class OwnerRestController {
    @Autowired
    public OwnerService OwnerService;

    /*
     * 
     * @author Muhammad Hammad
     * 
     * method that converts a Owner object to a Owner dto
     * 
     */
    private OwnerDTO convertToDTO(Owner Owner) {
        if (Owner == null) {
            throw new IllegalArgumentException("There is no such Owner!");
        }
        OwnerDTO OwnerDTO = new OwnerDTO(Owner.getId(), Owner.getUsername(), Owner.getEmail(), Owner.getPassword(),
                "Owner");
        return OwnerDTO;
    }

    /*
     * 
     * @author Muhammad Hammad
     * 
     * method that converts a list of Owners into a list of Owner dtos
     * 
     */
    private List<OwnerDTO> convertListToDto(List<Owner> listOwner) {
        List<OwnerDTO> listOwnerDTO = new ArrayList<OwnerDTO>(listOwner.size());
        for (Owner Owner : listOwner) {
            listOwnerDTO.add(convertToDTO(Owner));
        }
        return listOwnerDTO;
    }

    /*
     * 
     * @author Muhammad Hammad
     * 
     * Controller method that creates a new Owner object
     * 
     * 
     */

    @PostMapping(value = { "/owner", "/owner/" })
    @Operation(summary = "Create a new owner", description = "Creates a new owner with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Owner created successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = OwnerDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Failed to create owner", 
                    content = @Content)
    })
    public ResponseEntity<?> createOwner(
            @Parameter(description = "Username for the new owner") @RequestParam("username") String username,
            @Parameter(description = "Email for the new owner") @RequestParam("email") String email,
            @Parameter(description = "Password for the new owner") @RequestParam("password") String password) 
            throws IllegalArgumentException {
        try {
            Owner Owner = OwnerService.createOwner(username, email, password);
            return ResponseEntity.ok(convertToDTO(Owner));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    /*
     * 
     * @author Muhammad Hammad
     * 
     * Controller method that returns a Owner object by taking its username
     * 
     * 
     */

    @GetMapping(value = { "/owner/{username}", "/owner/{username}/" })
    @Operation(summary = "Get owner by username", description = "Retrieves an owner by their username")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Owner found", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = OwnerDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid username or owner not found", 
                    content = @Content)
    })
    public ResponseEntity<?> getOwner(
            @Parameter(description = "Username of the owner to retrieve") @PathVariable("username") String username) 
            throws IllegalArgumentException {
        try {
            Owner Owner = OwnerService.getOwner(username);
            return ResponseEntity.ok(convertToDTO(Owner));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = { "/owner/all", "/owner/all/" })
    @Operation(summary = "Get all owners", description = "Retrieves all owners in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Owners retrieved successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = OwnerDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Error retrieving owners", 
                    content = @Content)
    })
    public ResponseEntity<?> getAllOwners() throws IllegalArgumentException {
        try {
            List<Owner> Owners = OwnerService.getAllOwners();

            return ResponseEntity.ok(convertListToDto(Owners));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
     * 
     * @author Muhammad Hammad
     * 
     * Controller method that deletes a Owner with a given username
     * 
     * 
     * 
     */

    @DeleteMapping(value = { "/owner/{username}", "/owner/{username}/" })
    @Operation(summary = "Delete an owner", description = "Deletes an owner by their username")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Owner deleted successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = OwnerDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Error deleting owner", 
                    content = @Content)
    })
    public ResponseEntity<?> deleteOwner(
            @Parameter(description = "Username of the owner to delete") @PathVariable("username") String username) 
            throws IllegalArgumentException {
        try {
            Owner ownerDelete = OwnerService.deleteOwner(username);
            return ResponseEntity.ok(convertToDTO(ownerDelete));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    /*
     * @author Muhammad Hammad
     * 
     * Controller method that updates the information of a Owner
     * 
     */

    @PutMapping(value = { "/owner/update/{oldUsername}/{username}/{email}/{password}",
            "/owner/update/{oldUsername}/{username}/{email}/{password}/" })
    @Operation(summary = "Update owner information", description = "Updates an owner's username, email, and password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Owner updated successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = OwnerDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Error updating owner", 
                    content = @Content)
    })
    public ResponseEntity<?> updateOwner(
            @Parameter(description = "Current username of the owner") @PathVariable("oldUsername") String oldUsername,
            @Parameter(description = "New username for the owner") @PathVariable("username") String username, 
            @Parameter(description = "New email for the owner") @PathVariable("email") String email,
            @Parameter(description = "New password for the owner") @PathVariable("password") String password) throws IllegalArgumentException {
        try {
            Owner Owner = OwnerService.updateOwner(oldUsername, username, email, password);
            return ResponseEntity.ok(convertToDTO(Owner));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
