# Currency Exchange Application

This project is a `Java`-based **currency converter application** which allows users to convert between different currencies, view historical exchange rates as well as summary statistics, and perform administrative tasks such as adding new currencies and configuring existing exchange rates.

This application uses `Swing` for the GUI and reads data from `JSON` and `CSV` files to manage exchange rates and history.

## Table of Contents

- [Getting Started](#getting-started)
  - [Set up the Repository](#set-up-the-repository)
  - [Running the Project](#running-the-project)
- [Testing the Program](#testing-the-program)
- [Contributing to the Project](#contributing-to-the-project)


## Getting Started 

Ensure the following is installed on your machine:
- Java SDK 17+
- Gradle

### Set up the Repository

```bash
git clone  https://github.sydney.edu.au/SOFT2412-COMP9412-2024Sem2/Behroz_Lab01_Group01_CE_A1.git
```

Then build the project using Gradle:

```bash
gradle clean build
```

### Running the Project
Then run the main program from the root directory of the project:

```bash
gradle run
```

The application will start with a small window where you log in using an admin or user profile, log-in details can be found in `/src/main/java/currency/user/User.java`.

#### Configuration
`src/main/resources/staticCurrencies.txt`: Contains the initial set of currency exchange rates loaded when the application first runs.

`src/main/resources/currencies.json`: A dynamic JSON file where the application stores and retrieves current exchange rates.

`src/main/resources/rateHistory.csv`: A CSV file used to keep a history of all currency exchange rate changes.

## Testing the Program

We use `JaCoCo` for test coverage reports. The `jacocoTestReport` task is configured to run automatically after the `test` task. It generates the test coverage report in the `build/reports/jacoco/test/html/` directory. To test, navigate to the root directory and run:

```bash
./gradlew build
```

To view the coverage report, open the index.html file located in `build/reports/jacoco/test/html/`.

Note: build.gradle is set to exclude certain directories such as `currency/gui/**` from coverage reports as `JUnit` does not test GUI.

## Contributing to the Project

We welcome any contributions to our project. If you want to contribute:

1. Fork the repository on GitHub.
2. Clone your forked repository onto your local machine.
3. Create a new branch for your added work:
```bash
git checkout -b <branch_name>
```
4. Commit your changes.
5. Push to your forked repository:
```bash
git push origin <branch_name>
```
6. Create a pull request on GitHub.

Please use the GitHub issue tracker to report any bugs or feature requests.



## report : https://docs.google.com/document/d/12GqPXvWWFifxqzq-cbgRGDwF0E53GnDoD5dLOdvjyIk/edit

## assessment specs
