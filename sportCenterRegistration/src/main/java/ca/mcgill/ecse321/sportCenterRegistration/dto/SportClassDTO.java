package ca.mcgill.ecse321.sportCenterRegistration.dto;

public class SportClassDTO {
    private String name;
    private Boolean approved;

    // Default constructor required for JSON deserialization
    public SportClassDTO() {
    }

    public SportClassDTO(String name, Boolean approved) {
        this.name = name;
        this.approved = approved;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
}
