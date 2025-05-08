package ca.mcgill.ecse321.sportCenterRegistration.controller;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.sportCenterRegistration.service.ShiftService;
import ca.mcgill.ecse321.sportCenterRegistration.dto.ShiftDto;
import ca.mcgill.ecse321.sportCenterRegistration.model.Shift;
import ca.mcgill.ecse321.sportCenterRegistration.model.Staff;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RestController
@Tag(name = "Shift Management", description = "Operations for managing instructor shifts")
public class ShiftRestController {
    @Autowired
    private ShiftService shiftService;

	/**
     * This method creates a shift for a staff given its name
     * @author Ming Xuan Yue
     * 
	 * @param startTime
	 * @param endTime
     * @param date
     * @param staff
     * @return ShiftDto
     */

    @PostMapping(value={"/shift/{staff}", "/shift/{staff}/"})
    @Operation(summary = "Create a new shift", description = "Creates a new shift for a staff member")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Shift created successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = ShiftDto.class)) }),
        @ApiResponse(responseCode = "400", description = "Failed to create shift", 
                    content = @Content)
    })
    public ShiftDto createShift(
            @Parameter(description = "Start time of the shift (HH:mm)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
            @Parameter(description = "End time of the shift (HH:mm)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime,
            @Parameter(description = "Date of the shift") @RequestParam Date date,
            @Parameter(description = "Staff member for the shift") @PathVariable("staff") Staff staff) 
            throws IllegalArgumentException {
        Shift shift = shiftService.createShift(Time.valueOf(startTime), Time.valueOf(endTime), date, staff);
        return convertToDto(shift);
    }

    /**
     * This method gets the list of shifts given a date
     * @author Ming Xuan Yue
     * 
     * @param date
     * @return List<ShiftDto>
     */

    @GetMapping(value={"/shift/{date}", "/shift/{date}/"})
    @Operation(summary = "Get shifts by date", description = "Retrieves all shifts scheduled for a specific date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Shifts retrieved successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = ShiftDto.class)) }),
        @ApiResponse(responseCode = "400", description = "Error retrieving shifts", 
                    content = @Content)
    })
    public List<ShiftDto> getShiftByDay(
            @Parameter(description = "Date to filter shifts by") @PathVariable("date") Date date) 
            throws IllegalArgumentException {
        ArrayList <ShiftDto> shiftDtos = new ArrayList<>();
        for (var shift: shiftService.getShiftByDate(date)){
            shiftDtos.add(convertToDto(shift));
        }
        return shiftDtos;
    }

    /**
     * This method gets the list of shifts given a name of the staff
     * @author Ming Xuan Yue
     * 
     * @param staff
     * @return List<ShiftDto>
     */

    @GetMapping(value={"/shift/{staff}", "/shift/{staff}/"})
    @Operation(summary = "Get shifts by staff", description = "Retrieves all shifts assigned to a specific staff member")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Shifts retrieved successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = ShiftDto.class)) }),
        @ApiResponse(responseCode = "400", description = "Error retrieving shifts", 
                    content = @Content)
    })
    public List<ShiftDto> getShiftByStaff(
            @Parameter(description = "Staff member to filter shifts by") @PathVariable("staff") Staff staff) 
            throws IllegalArgumentException {
        ArrayList <ShiftDto> shiftDtos = new ArrayList<>();
        for (var shift: shiftService.getShiftByStaff(staff)){
            shiftDtos.add(convertToDto(shift));
        }
        return shiftDtos;
    }

    /**
     * This method gets the list of shifts given a name of the staff and date
     * @author Ming Xuan Yue
     * 
     * @param staff
     * @param date
     * @return List<ShiftDto>
     */

    @GetMapping(value={"/shift/{staff}/{date}", "/shift/{staff}/{date}/"})
    @Operation(summary = "Get shifts by staff and date", description = "Retrieves shifts for a specific staff member on a specific date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Shifts retrieved successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = ShiftDto.class)) }),
        @ApiResponse(responseCode = "400", description = "Error retrieving shifts", 
                    content = @Content)
    })
    public List<ShiftDto> getShiftByStaffAndDate(
            @Parameter(description = "Staff member to filter shifts by") @PathVariable("staff") Staff staff, 
            @Parameter(description = "Date to filter shifts by") @PathVariable("date") Date date) 
            throws IllegalArgumentException {
        ArrayList <ShiftDto> shiftDtos = new ArrayList<>();
        for (var shift: shiftService.getShiftByStaffandDate(staff, date)){
            shiftDtos.add(convertToDto(shift));
        }
        return shiftDtos;
    }

    /**
     * This method gets the list of all shifts
     * @author Ming Xuan Yue
     * 
     * @return List<ShiftDto>
     */

    @GetMapping(value={"/shift", "/shift/"})
    @Operation(summary = "Get all shifts", description = "Retrieves all shifts in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Shifts retrieved successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = ShiftDto.class)) }),
        @ApiResponse(responseCode = "400", description = "Error retrieving shifts", 
                    content = @Content)
    })
    public List<ShiftDto> getAllShifts() throws IllegalArgumentException{
        ArrayList <ShiftDto> shiftDtos = new ArrayList<>();
        for (var shift: shiftService.getAllShift()){
            shiftDtos.add(convertToDto(shift));
        }
        return shiftDtos;
    }

        /**
     * This method gets the shift by id
     * @author Ming Xuan Yue
     * 
     * @param id
     * @return ShiftDto
     */

     @GetMapping(value={"/shift/{id}", "/shift/{id}/"})
     @Operation(summary = "Get shift by ID", description = "Retrieves a specific shift by its ID")
     @ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "Shift retrieved successfully", 
                     content = { @Content(mediaType = "application/json", 
                                 schema = @Schema(implementation = ShiftDto.class)) }),
         @ApiResponse(responseCode = "400", description = "Error retrieving shift", 
                     content = @Content)
     })
     public ShiftDto getShiftById(
             @Parameter(description = "ID of the shift to retrieve") @PathVariable("id") Integer id) 
             throws IllegalArgumentException {
         Shift shift= shiftService.getShiftById(id);
         return convertToDto(shift);
     }    

    /**
     * This method updates a shift 
     * @author Ming Xuan Yue
     * 
     * @param date
     * @param startTime
     * @param endTime
     * @param staff
     * @param id
     * @return ShiftDto
     */

    @PutMapping(value={"/shift", "/shift/"})
    @Operation(summary = "Update a shift", description = "Updates an existing shift with new details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Shift updated successfully", 
                    content = { @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = ShiftDto.class)) }),
        @ApiResponse(responseCode = "400", description = "Error updating shift", 
                    content = @Content)
    })
    public ShiftDto updateShiftById(
            @Parameter(description = "New start time of the shift (HH:mm)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
            @Parameter(description = "New end time of the shift (HH:mm)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime,
            @Parameter(description = "New date of the shift") @RequestParam Date date,
            @Parameter(description = "New staff member for the shift") @RequestParam Staff staff, 
            @Parameter(description = "ID of the shift to update") @RequestParam Integer id) throws IllegalArgumentException{
        return convertToDto(shiftService.updateShift(date, Time.valueOf(startTime), Time.valueOf(endTime), staff, id));
    }

    /**
     * This method deletes a shift by id
     * @author Ming Xuan Yue
     *
     * @param id
     * @return boolean
     */

    @DeleteMapping(value={"/shift", "/shift/"})
    @Operation(summary = "Delete a shift", description = "Deletes a shift by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Shift deleted successfully", 
                    content = @Content),
        @ApiResponse(responseCode = "400", description = "Error deleting shift", 
                    content = @Content)
    })
    public boolean deleteShiftById(
            @Parameter(description = "ID of the shift to delete") @RequestParam Integer id) {
        return shiftService.deleteShiftById(id);
    }

    
	/**
     * Converts Shift object to ShiftDto
     * @author Ming Xuan Yue
     * @param shift
     * @return ShiftDto
     */

    private ShiftDto convertToDto (Shift s){
        if (s == null){
            throw new IllegalArgumentException("The shift does not exist");
        }
        ShiftDto shiftDto = new ShiftDto(s.getDate(), s.getStartTime(), s.getEndTime(), s.getId(), s.getStaff());
        return shiftDto;
    }
}
