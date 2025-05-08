package ca.mcgill.ecse321.sportCenterRegistration.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.sportCenterRegistration.dto.RegistrationDTO;
import ca.mcgill.ecse321.sportCenterRegistration.model.Account;
import ca.mcgill.ecse321.sportCenterRegistration.model.Registration;
import ca.mcgill.ecse321.sportCenterRegistration.model.Session;
import ca.mcgill.ecse321.sportCenterRegistration.service.RegistrationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RestController
@Tag(name = "Registration Management", description = "Operations for managing registrations")
public class RegistrationRestController {
    @Autowired
    public RegistrationService registrationService;

    /*
     * 
     * @author Muhammad Hammad
     * 
     * @author David Marji
     * method that converts a Registration object to a Registration dto
     * 
     */
    private RegistrationDTO convertToDTO(Registration registration) {
        if (registration == null) {
            throw new IllegalArgumentException("There is no such Registration!");
        }
        RegistrationDTO RegistrationDTO = new RegistrationDTO(registration.getId(), registration.getDate(),
                registration.getAccount(),
                registration.getSession());
        return RegistrationDTO;
    }

    /*
     * 
     * @author Muhammad Hammad
     * 
     * method that converts a list of Registrations into a list of Registration dtos
     * 
     */
    private List<RegistrationDTO> convertListToDto(List<Registration> listRegistration) {
        List<RegistrationDTO> listRegistrationDTO = new ArrayList<RegistrationDTO>(listRegistration.size());
        for (Registration Registration : listRegistration) {
            listRegistrationDTO.add(convertToDTO(Registration));
        }
        return listRegistrationDTO;
    }

    /*
     * 
     * @author Muhammad Hammad
     * 
     * @author David Marji
     * Controller method that creates a new Registration object
     * 
     * 
     */
    @PostMapping(value = { "/registration/{accountName}/{sessionId}", "/registration/{accountName}/{sessionId}/" })
    @Operation(summary = "Create a new registration", description = "Creates a new registration for a user to a session")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registration created successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = RegistrationDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Failed to create registration", 
                    content = @Content)
    })
    public ResponseEntity<?> createRegistration(
            @Parameter(description = "Username of the account registering") @PathVariable("accountName") String accountName,
            @Parameter(description = "ID of the session to register for") @PathVariable("sessionId") String sessionId) 
            throws IllegalArgumentException {

        try {
            Registration registration = registrationService.createRegistration(new Date(System.currentTimeMillis()),
                    accountName, Integer.parseInt(sessionId));
            return ResponseEntity.ok(convertToDTO(registration));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
     * 
     * @author Muhammad Hammad
     * 
     * @author David Marji
     * Controller method that returns a Registration object by taking its username
     * 
     * 
     */
    @GetMapping(value = { "/registration", "/registration/" })
    @Operation(summary = "Get registrations", description = "Retrieves registrations filtered by account name and/or session ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registrations retrieved successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = RegistrationDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Error retrieving registrations", 
                    content = @Content)
    })
    public ResponseEntity<?> getRegistration(
            @Parameter(description = "Username of the account to filter by") @RequestParam(name = "accountName", required = false) String accountName,
            @Parameter(description = "ID of the session to filter by") @RequestParam(name = "sessionId", required = false) String sessionId)
            throws IllegalArgumentException {

        try {
            List<Registration> registrations = new ArrayList<Registration>();
            if (accountName != null && sessionId != null) {
                registrations.add(registrationService.getRegistrationByAccountAndSession(accountName,
                        Integer.parseInt(sessionId)));
            } else {
                if (accountName != null) {
                    registrations = registrationService.getRegistrationByAccount(accountName);
                } else if (sessionId != null) {
                    registrations = registrationService.getRegistrationBySession(Integer.parseInt(sessionId));
                } else {
                    registrations = registrationService.getAllRegistrations();
                }
            }

            return ResponseEntity.ok(convertListToDto(registrations));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(value = { "/registration/{id}", "/registration/{id}/" })
    @Operation(summary = "Delete registration by ID", description = "Deletes a registration by its unique ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registration deleted successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = Registration.class)) }),
        @ApiResponse(responseCode = "400", description = "Error deleting registration", 
                    content = @Content)
    })
    public ResponseEntity<?> deleteRegistration(
            @Parameter(description = "ID of the registration to delete") @PathVariable("id") String id) 
            throws IllegalArgumentException {
        try {
            Registration deleted = registrationService.deleteById(Integer.parseInt(id));
            return ResponseEntity.ok(deleted);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
     * 
     * @author Muhammad Hammad
     * 
     * @author David Marji
     * Controller method that deletes a Registration with a given username
     * 
     * 
     * 
     */
    @DeleteMapping(value = { "/registration", "/registration/" })
    @Operation(summary = "Delete registration by account and session", description = "Deletes a registration by account name and session ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registration deleted successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = Registration.class)) }),
        @ApiResponse(responseCode = "400", description = "Error deleting registration", 
                    content = @Content)
    })
    public ResponseEntity<?> deleteRegistration(
            @Parameter(description = "Username of the account") @RequestParam(name = "accountName", required = true) String accountName,
            @Parameter(description = "ID of the session") @RequestParam(name = "sessionId", required = true) String sessionId)
            throws IllegalArgumentException {

        try {
            Registration deleted = registrationService.deleteRegistrationByAccountAndSessionId(accountName,
                    Integer.parseInt(sessionId));
            return ResponseEntity.ok(deleted);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
     * @author Muhammad Hammad
     * 
     * @author David Marji
     * 
     * Controller method that updates the information of a Registration
     * 
     */

    // @PutMapping(value = {
    // "/Registration/update/{oldAccount}/{oldSession}/{account}/{session}/{date}",
    // "/Registration/update/{oldAccount}/{oldSession}/{account}/{session}/{date}/"
    // })

    // public ResponseEntity<?> updateRegistration(@PathVariable("oldAccount")
    // Account oldAccount,
    // @PathVariable("oldSession") Session oldSession, @PathVariable("account")
    // Account account,
    // @PathVariable("session") Session session, @PathVariable("date") Date date)
    // throws IllegalArgumentException {

    // try {
    // Registration Registration =
    // registrationService.updateRegistration(oldAccount, oldSession, account,
    // session, date);

    // return ResponseEntity.ok(convertToDTO(Registration));
    // } catch (IllegalArgumentException e) {
    // return ResponseEntity.badRequest().body(e.getMessage());
    // }
    // }
}
