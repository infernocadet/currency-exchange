# Currency Exchange Application

This project is a `Java`-based **currency converter application** which allows users to convert between different currencies, view historical exchange rates as well as summary statistics, and perform administrative tasks such as adding new currencies and configuring existing exchange rates.

This application uses `Swing` for the GUI and reads data from `JSON` and `CSV` files to manage exchange rates and history.

## Table of Contents

- [Getting Started](#getting-started)
  - [Set up the Repository](#set-up-the-repository)
  - [Running the Project](#running-the-project)
- [Testing the Program](#testing-the-program)
- 


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

To test, navigate to the test director and run all tests using gradle.





## report : https://docs.google.com/document/d/12GqPXvWWFifxqzq-cbgRGDwF0E53GnDoD5dLOdvjyIk/edit

## assessment specs

