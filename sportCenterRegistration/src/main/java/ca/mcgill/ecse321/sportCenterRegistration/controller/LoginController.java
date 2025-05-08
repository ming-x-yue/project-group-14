package ca.mcgill.ecse321.sportCenterRegistration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.sportCenterRegistration.dto.CustomerDTO;
import ca.mcgill.ecse321.sportCenterRegistration.dto.InstructorDTO;
import ca.mcgill.ecse321.sportCenterRegistration.dto.OwnerDTO;
import ca.mcgill.ecse321.sportCenterRegistration.model.Account;
import ca.mcgill.ecse321.sportCenterRegistration.model.Customer;
import ca.mcgill.ecse321.sportCenterRegistration.model.Instructor;
import ca.mcgill.ecse321.sportCenterRegistration.model.Owner;
import ca.mcgill.ecse321.sportCenterRegistration.service.LoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Authentication", description = "Operations for user authentication")
public class LoginController {

	@Autowired
	private LoginService loginService;

	@GetMapping(value = { "/login", "/login/" })
	@Operation(summary = "User login", description = "Authenticates a user by email/username and password")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Login successful", 
					content = { @Content(mediaType = "application/json", 
								schema = @Schema(oneOf = {CustomerDTO.class, InstructorDTO.class, OwnerDTO.class})) }),
		@ApiResponse(responseCode = "500", description = "Login failed", 
					content = @Content)
	})
	public ResponseEntity<?> loginByEmail(
			@Parameter(description = "Email of the user") @RequestParam(value = "email", required = false) String email,
			@Parameter(description = "Username of the user") @RequestParam(value = "username", required = false) String username,
			@Parameter(description = "Password of the user") @RequestParam(value = "password", required = true) String password) {
		Account user = null;
		try {
			if (email == null && username == null) {
				throw new IllegalArgumentException("Please provide either an email or a username.");
			}

			if (email != null) {
				System.out.println("email: " + email);
				user = loginService.loginByEmail(email, password);
			} else if (username != null) {
				user = loginService.loginByUsername(username, password);
			}
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Determine what type of user is login in in the system.
		if (user instanceof Customer) {

			return new ResponseEntity<>(convertToCustomerDto((Customer) user), HttpStatus.OK);
		}

		if (user instanceof Instructor) {
			return new ResponseEntity<>(convertToInstructorDto((Instructor) user), HttpStatus.OK);
		}

		if (user instanceof Owner) {
			return new ResponseEntity<>(convertToOwnerDTO((Owner) user), HttpStatus.OK);
		}
		return null;
	}

	public static CustomerDTO convertToCustomerDto(Customer customer) {
		if (customer == null) {
			throw new IllegalArgumentException("There is no such Customer!");
		}
		CustomerDTO customerDto = new CustomerDTO(customer.getId(), customer.getUsername(), customer.getEmail(),
				customer.getPassword(), "Customer");
		return customerDto;
	}

	private InstructorDTO convertToInstructorDto(Instructor instrutor) {
		if (instrutor == null) {
			throw new IllegalArgumentException("There is no such Instrutor!");
		}
		InstructorDTO InstructorDTO = new InstructorDTO(instrutor.getId(), instrutor.getUsername(),
				instrutor.getEmail(), instrutor.getPassword(), "Instructor");
		return InstructorDTO;
	}

	public static OwnerDTO convertToOwnerDTO(Owner owner) {
		if (owner == null)
			return null;
		return new OwnerDTO(owner.getId(), owner.getUsername(), owner.getEmail(), owner.getPassword(), "Owner");
	}

}
