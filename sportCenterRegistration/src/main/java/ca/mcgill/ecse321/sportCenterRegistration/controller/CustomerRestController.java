package ca.mcgill.ecse321.sportCenterRegistration.controller;

import java.util.ArrayList;
import java.util.List;

// import org.checkerframework.checker.units.qual.A; // I don't know what this is, so I just commented it out
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.sportCenterRegistration.dto.CustomerDTO;
import ca.mcgill.ecse321.sportCenterRegistration.model.Customer;
import ca.mcgill.ecse321.sportCenterRegistration.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Customer Management", description = "Operations for managing customers")
public class CustomerRestController {
    @Autowired
    public CustomerService customerService;

    /*
     * 
     * @author Muhammad Hammad
     * 
     * method that converts a customer object to a customer dto
     * 
     */
    private CustomerDTO convertToDTO(Customer customer) {
        return new CustomerDTO(customer.getId(), customer.getUsername(), customer.getEmail(), customer.getPassword(),
                "Customer");
    }

    /*
     * 
     * @author Muhammad Hammad
     * 
     * method that converts a list of customers into a list of customer dtos
     * 
     */
    private List<CustomerDTO> convertListToDto(List<Customer> listCustomer) {
        List<CustomerDTO> listCustomerDTO = new ArrayList<CustomerDTO>(listCustomer.size());
        for (Customer customer : listCustomer) {
            listCustomerDTO.add(convertToDTO(customer));
        }
        return listCustomerDTO;
    }

    /*
     * 
     * @author Muhammad Hammad
     * 
     * Controller method that creates a new customer object
     * 
     * 
     */

    @PostMapping(value = { "/customer", "/customer/" })
    @Operation(summary = "Create a new customer", description = "Creates a new customer with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer created successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = CustomerDTO.class)) }),
        @ApiResponse(responseCode = "404", description = "Failed to create customer", 
                    content = @Content)
    })
    public ResponseEntity<?> createCustomer(
            @Parameter(description = "Username for the new customer") @RequestParam("username") String username,
            @Parameter(description = "Email for the new customer") @RequestParam("email") String email,
            @Parameter(description = "Password for the new customer") @RequestParam("password") String password)
            throws IllegalArgumentException {
        System.out.println("hh");
        try {
            Customer customer = customerService.createCustomer(username, email, password);
            return ResponseEntity.ok(convertToDTO(customer));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    /*
     * 
     * @author Muhammad Hammad
     * 
     * Controller method that returns a customer object by taking its username
     * 
     * 
     */

    @GetMapping(value = { "/customer/{username}", "/customer/{username}/" })
    @Operation(summary = "Get customer by username", description = "Retrieves a customer by their username")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer found", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = CustomerDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid username or customer not found", 
                    content = @Content)
    })
    public ResponseEntity<?> getCustomer(
            @Parameter(description = "Username of the customer to retrieve") @PathVariable("username") String username) 
            throws IllegalArgumentException {
        try {
            Customer customer = customerService.getCustomer(username);
            return ResponseEntity.ok(convertToDTO(customer));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping(value = { "/customer/all", "/customer/all/" })
    @Operation(summary = "Get all customers", description = "Retrieves all customers in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customers retrieved successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = CustomerDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Error retrieving customers", 
                    content = @Content)
    })
    public ResponseEntity<?> getAllCustomers() throws IllegalArgumentException {
        try {
            List<Customer> customers = customerService.getAllCustomers();

            return ResponseEntity.ok(convertListToDto(customers));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
     * 
     * @author Muhammad Hammad
     * 
     * Controller method that deletes a customer with a given username
     * 
     * 
     * 
     */

    @DeleteMapping(value = { "/customer/{username}", "/customer/{username}/" })
    @Operation(summary = "Delete a customer", description = "Deletes a customer by their username")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer deleted successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = CustomerDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Error deleting customer", 
                    content = @Content)
    })
    public ResponseEntity<?> deleteCustomer(
            @Parameter(description = "Username of the customer to delete") @PathVariable("username") String username) 
            throws IllegalArgumentException {
        try {
            Customer deleteCustomer = customerService.deleteCustomer(username);
            return ResponseEntity.ok(convertToDTO(deleteCustomer));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
     * @author Muhammad Hammad
     * 
     * Controller method that updates the information of a customer
     * 
     */

    @PutMapping(value = { "/customer/update/{oldUsername}/{username}/{email}/{password}",
            "/customer/update/{oldUsername}/{username}/{email}/{password}/" })
    @Operation(summary = "Update customer information", description = "Updates a customer's username, email, and password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer updated successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = CustomerDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Error updating customer", 
                    content = @Content)
    })
    public ResponseEntity<?> updateCustomer(
            @Parameter(description = "Current username of the customer") @PathVariable("oldUsername") String oldUsername,
            @Parameter(description = "New username for the customer") @PathVariable("username") String username, 
            @Parameter(description = "New email for the customer") @PathVariable("email") String email,
            @Parameter(description = "New password for the customer") @PathVariable("password") String password) 
            throws IllegalArgumentException {
        try {
            Customer customer = customerService.updateCustomer(oldUsername, username, email, password);
            return ResponseEntity.ok(convertToDTO(customer));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
