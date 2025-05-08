Feature: User Account Management
  As a potential user
  I want to be able to create an account and log in
  So that I can use the Sports Center Registration system

  Background:
    Given the following accounts exist in the system:
      | username    | email                   | password    | type       |
      | customer1   | customer1@example.com   | password123 | Customer   |
      | instructor1 | instructor1@example.com | password123 | Instructor |
      | owner1      | owner1@example.com      | password123 | Owner      |

  Scenario: Successful customer registration
    Given I am on the signup page
    When I register with the following details:
      | username    | email                   | password    |
      | newcustomer | newcustomer@example.com | password123 |
    Then the registration should be successful
    And I should be redirected to the customer dashboard

  Scenario: Failed registration due to existing username
    Given I am on the signup page
    When I register with the following details:
      | username  | email                 | password    |
      | customer1 | different@example.com | password123 |
    Then the registration should fail with error "Username is already taken"

  Scenario: Successful customer login
    Given I am on the login page
    When I login with username "customer1" and password "password123"
    Then the login should be successful
    And I should be logged in as a customer
    And I should be redirected to the customer dashboard

  Scenario: Successful instructor login
    Given I am on the login page
    When I login with username "instructor1" and password "password123"
    Then the login should be successful
    And I should be redirected to the instructor dashboard

  Scenario: Successful owner login
    Given I am on the login page
    When I login with username "owner1" and password "password123"
    Then the login should be successful
    And I should be redirected to the owner dashboard

  Scenario: Failed login due to invalid credentials
    Given I am on the login page
    When I login with username "customer1" and password "wrongpassword"
    Then the login should fail with error "Invalid credentials"
