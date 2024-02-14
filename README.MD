# Param Rest Assured Automation Project

### Maven Test Run Inputs

- url

### Maven Run Commands

- mvn test -Durl=https://restful-booker.herokuapp.com


### Generate And Open Allure Report

- After the test run with Maven, the reports are created in the main directory as target-{runname}/allure-results


- Generate ;

  allure generate allure-results --clean -o allure-report


- Open ;

  allure open allure-report