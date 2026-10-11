
# WattWise - Smart Electricity Usage & Bill Analyzer

WattWise is a Core Java console application that estimates
electricity bills, tracks monthly consumption, compares usage
patterns, and provides energy-saving suggestions.

## Features

- Monthly electricity meter reading entry
- Progressive slab-based bill estimation
- Permanent storage using file handling
- Month-to-month consumption comparison
- Percentage increase and decrease analysis
- Rule-based high-consumption alerts
- Personalized electricity-saving tips
- Input validation and duplicate-month prevention
- Monthly history report

## Technologies Used

- Java
- Object-Oriented Programming
- ArrayList and Collections
- File Handling
- Exception Handling
- Basic Data Analysis

## Project Structure

WattWise/
- EnergyRecord.java
- WattWise.java
- README.md
- .gitignore

The file wattwise_history.txt is created automatically.

## Requirements

- Java JDK 8 or later
- Command Prompt or a Java IDE

## How to Run

Compile:

javac EnergyRecord.java WattWise.java

Run:

java WattWise

## Bill Calculation

The application uses illustrative progressive slab rates.
They are not official electricity tariffs.

- First 100 units: Rs. 1.50 per unit
- Next 100 units: Rs. 2.50 per unit
- Next 300 units: Rs. 4.00 per unit
- Above 500 units: Rs. 6.00 per unit

## Data Storage

Monthly readings are saved in wattwise_history.txt.
The application loads saved records when it starts.

## Limitations

- Console-based interface
- Sample tariff rates only
- No live connection to an electricity board
- Saving tips are general suggestions

## Future Enhancements

- Configurable tariff rates
- Appliance-level consumption tracking
- Export reports to CSV
- Graphical dashboard
- Database integration

## Author

Bharrathi T
