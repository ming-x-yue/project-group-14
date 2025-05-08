package ca.mcgill.ecse321.sportCenterRegistration.dto;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import ca.mcgill.ecse321.sportCenterRegistration.model.Instructor;
import ca.mcgill.ecse321.sportCenterRegistration.model.SportClass;

public class SessionDTO {

	private int id;
	private Time startTime;
	private Time endTime;
	private String location;
	private Date date;
	private InstructorDTO instructor;
	private SportClassDTO sportClass;
	private List<String> errors;

	public SessionDTO() {
	}

	public SessionDTO(String error) {
		this.errors = List.of(error);
	}

	public SessionDTO(Date date, Time startTime, Time endTime, int id, String location, Instructor instructor,
			SportClass sportClass) {
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.location = location;
		this.instructor = new InstructorDTO(instructor.getId(), instructor.getUsername(), instructor.getEmail(),
				instructor.getPassword(), "instructor");
		this.sportClass = new SportClassDTO(sportClass.getName(), sportClass.getApproved());
		this.id = id;
	}

	public Time getEndTime() {
		return endTime;
	}

	public Time getStartTime() {
		return startTime;
	}

	public Date getDate() {
		return date;
	}

	public int getId() {
		return id;
	}

	public String getLocation() {
		return location;
	}

	public InstructorDTO getInstructor() {
		return instructor;
	}

	public SportClassDTO getSportClass() {
		return sportClass;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
}
