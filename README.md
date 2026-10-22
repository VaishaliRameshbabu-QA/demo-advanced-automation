# Demo Advanced - A-001 Automation Framework Setup

## Environment
- Java JDK 17
- Maven
- Eclipse IDE
- Selenium Java 4.27.0
- TestNG 7.10.2
- Chrome browser

## Import into Eclipse
1. Extract this ZIP file.
2. In Eclipse, select File > Import.
3. Choose Maven > Existing Maven Projects.
4. Browse to the extracted `A-001_Demo_Advanced_Automation_Framework` folder.
5. Select the `pom.xml` and finish.
6. Right-click the project > Maven > Update Project.
7. Confirm the project JRE is JavaSE-17.

## Run the smoke test
1. Right-click `testng.xml` > Run As > TestNG Suite.
2. Chrome should open the demo login page.
3. The test checks that the email field, password field, and Login button are visible.
4. The browser closes after the test.

You can also run `mvn clean test` from a terminal in the project folder.

## Notes
- Selenium Manager manages the browser driver automatically in supported Selenium versions.
- If a selector changes on the website, update the locator in `LoginPage.java`.
- This is the initial framework setup. Additional waits, reporting, test data, and more page classes can be added in later tasks.
