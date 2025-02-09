This Java application analyzes data from the Google Play Store to provide insights about applications, their categories, pricing, and developers.

This application processes a CSV file containing Google Play Store app data and generates various statistical analyses, including:
- Number of apps per category
- Distribution of free vs paid apps and their installation numbers
- Number of apps per company
- Developer statistics
- Price-based app distribution analysis

## Features

- **Category Analysis**: Counts and categorizes apps based on their respective categories
- **Free vs Paid Apps Analysis**: Tracks the number of installations for both free and paid applications
- **Company Analysis**: Analyzes the distribution of apps across different companies
- **Developer Statistics**: Generates statistics about app developers
- **Price Analysis**: Creates reports about app distribution across different price points

The program generates several CSV files with analysis results:
- `NumberOfAppsPerCategory.csv`: Distribution of apps across categories
- `NumberOfFreeApps.csv`: Installation statistics for free vs paid apps
- `NumberOfAppsPerCompany.csv`: Number of apps published by each company
- `NumberOfAppsPerDeveloperNotInCompany.csv`: Developer-specific statistics
- `HowManyAppsAccordingToBudget.csv`: App distribution across different price points

## Requirements

- Java Development Kit (JDK)
- CSV input file: "Google Play Store Apps.csv" (should be placed in the src directory)


## How to Run

1. Ensure you have JDK installed on your system
2. Place the "Google Play Store Apps.csv" file in the src directory
3. Run the program


## Output Format

All output files are in CSV format with appropriate headers.

## Notes

The 'Google Play Store Apps.csv' file is not pushed to the repository, it should be placed in the src directory.