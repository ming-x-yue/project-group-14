Feature: User Account Management
  As a user
  I want to manage my account
  So that I can access the system and use its features

  Scenario: Successfully register as a new customer
    Given I am on the signup page
    When I register with the following details:
      | username | email           | password | confirmPassword |
      | newuser  | new@example.com | pass123  | pass123         |
    Then the registration should be successful
    And I should be logged in as a customer
    And I should be redirected to the customer dashboard

  Scenario: Cannot register with mismatched passwords
    Given I am on the signup page
    When I register with the following details:
      | username | email           | password | confirmPassword |
      | newuser  | new@example.com | pass123  | differentpass   |
    Then the registration should fail with error "Passwords do not match."

  Scenario: Cannot register with an existing username
    Given the following accounts exist in the system:
      | username  | email           | password  | type     |
      | customer1 | cust1@email.com | password1 | Customer |
    And I am on the signup page
    When I register with the following details:
      | username  | email           | password | confirmPassword |
      | customer1 | new@example.com | pass123  | pass123         |
    Then the registration should fail with error "Account with this username already exists"

  Scenario: Successfully login as a customer
    Given the following accounts exist in the system:
      | username  | email           | password  | type     |
      | customer1 | cust1@email.com | password1 | Customer |
    And I am on the login page
    When I login with username "customer1" and password "password1"
    Then the login should be successful
    And I should be redirected to the customer dashboard

  Scenario: Successfully login as an instructor
    Given the following accounts exist in the system:
      | username | email          | password  | type       |
      | instruc1 | ins1@email.com | password1 | Instructor |
    And I am on the login page
    When I login with username "instruc1" and password "password1"
    Then the login should be successful
    And I should be redirected to the instructor dashboard

  Scenario: Successfully login as an owner
    Given the following accounts exist in the system:
      | username | email          | password  | type  |
      | owner1   | own1@email.com | password1 | Owner |
    And I am on the login page
    When I login with username "owner1" and password "password1"
    Then the login should be successful
    And I should be redirected to the owner dashboard

  Scenario: Cannot login with incorrect credentials
    Given the following accounts exist in the system:
      | username  | email           | password  | type     |
      | customer1 | cust1@email.com | password1 | Customer |
    And I am on the login page
    When I login with username "customer1" and password "wrongpassword"
    Then the login should fail with error "Invalid username or password. Please try again."
