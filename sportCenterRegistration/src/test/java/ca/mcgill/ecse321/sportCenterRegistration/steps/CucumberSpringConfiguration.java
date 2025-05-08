package ca.mcgill.ecse321.sportCenterRegistration.steps;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.sportCenterRegistration.SportCenterRegistrationApplication;

@CucumberContextConfiguration
@SpringBootTest(
    classes = SportCenterRegistrationApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/sport_center_test",
        "spring.jpa.hibernate.ddl-auto=create-drop"
    }
)
public class CucumberSpringConfiguration {
    // This class is intentionally empty
    // Its purpose is to enable Spring Boot test features in Cucumber tests
}