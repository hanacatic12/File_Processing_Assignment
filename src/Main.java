import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("src/Google Play Store Apps.csv");


        Map<String, Integer> appsPerCategory = new HashMap<>();
        Map<String, Long> numberOfFreeApps = new HashMap<>();
        Map<String, Integer> appsPerCompany = new HashMap<>();
        Map<String, Integer> appsPerDeveloper = new HashMap<>();
        ArrayList<Double> prices = new ArrayList<>();

        try {
            Scanner s = new Scanner(file);
            if(s.hasNextLine()) s.nextLine();
            try {
                while (s.hasNextLine()) {
                    String line = s.nextLine();
                    String[] lineParts = line.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");

                    findNumberOfAppsPerCategory(lineParts, appsPerCategory);
                    findNumberOfFreeApps(lineParts, numberOfFreeApps);
                    findNumberOfAppsPerCompany(lineParts, appsPerCompany);
                    findDevelopers(lineParts, appsPerDeveloper);
                    findPrices(lineParts, prices);

                }
            } catch (Error e) {
                System.out.println(e);
            }
            s.close();

        } catch(FileNotFoundException e) {
            System.out.println(e);
        }


        writeNumberOfAppsPerCategory(appsPerCategory);
        writeNumberOfFreeApps(numberOfFreeApps);
        writeAppsPerCompany(appsPerCompany);
        writeAppsPerDeveloper(appsPerDeveloper);


        try {
            FileWriter writer = new FileWriter("src/HowManyAppsAccordingToBudget.csv");
            writer.write("Budget, Quantity\n");
            findHowManyPurchased(writer, 1000., prices);
            findHowManyPurchased(writer, 10000., prices);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }





    }


    public static void findNumberOfAppsPerCategory(String[] lineParts, Map<String, Integer> appsPerCategory) {
        String category = lineParts[2].strip();

        appsPerCategory.put(category, appsPerCategory.getOrDefault(category, 0) + 1);
    }

    public static void findNumberOfFreeApps(String[] lineParts, Map<String, Long> numberOfFreeApps) {
        String installs = lineParts[5].strip();


        if (installs.startsWith("\"") && installs.endsWith("\"")) {
            installs = installs.substring(1, installs.length() - 1);
        }

            if (installs.contains(".")) installs = installs.replace(".", "");
            if (installs.contains(",")) installs = installs.replace(",", "");
            if (installs.endsWith("+")) installs = installs.replace("+", "");
            if(installs.isEmpty()) installs = "0";

            //Integer downloads = Integer.parseInt(installs);
            Long downloads = Long.parseLong(installs);
            Boolean isFree = Boolean.parseBoolean(lineParts[8].strip());


            if (isFree)
                numberOfFreeApps.put("Free apps", numberOfFreeApps.getOrDefault("Free apps", 0L) + downloads);
            else
                numberOfFreeApps.put("Paid apps", numberOfFreeApps.getOrDefault("Paid apps", 0L) + downloads);

    }


    public static void findNumberOfAppsPerCompany(String[] lineParts, Map<String, Integer> appsPerCompany) {
        String company = lineParts[1].strip();

        String[] id = company.split("\\.");           //razdvoji po tackama
        if(id.length >= 2) {                                //ako string ima samo dva dijela, to je company ime, ako nema
            company = id[0] + "." + id[1];                  //napravi da budu samo prva dva dijela
        }

        appsPerCompany.put(company, appsPerCompany.getOrDefault(company, 0) + 1);
    }

    public static void findDevelopers(String[] lineParts, Map<String, Integer> appsPerDeveloper) {
        String company = lineParts[1].strip();
        String[] id = company.split("\\.");
        if(id.length >= 2)
            company = id[1];

        String developeremail = lineParts[15].strip();
        String developerid = lineParts[13].strip();
        if(!developeremail.contains(company))
            appsPerDeveloper.put(developerid, appsPerDeveloper.getOrDefault(developerid, 0) + 1);
    }

    public static void findPrices(String[] lineParts, ArrayList<Double> prices) {
        String pricestr = lineParts[9].strip();
        if(pricestr.startsWith("\"") || pricestr.endsWith("\"")) pricestr = pricestr.replace("\"", "");

        Double price = Double.parseDouble(pricestr);
        prices.add(price);
    }

    public static void findHowManyPurchased(FileWriter writer, Double budget, ArrayList<Double> prices) {
        int quantity = 0;
        Double remainingBudget = budget;
        for(Double price : prices) {
            if(remainingBudget >= price) {
                remainingBudget -= price;
                quantity++;
            }
        }


        try {
            writer.write(budget + "$, " + quantity + "\n" );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

    public static void writeNumberOfAppsPerCategory(Map<String, Integer> appsPerCategory) {
        try {
            FileWriter writer = new FileWriter("src/NumberOfAppsPerCategory.csv");
            writer.write("Category, Number of apps\n");

            for(Map.Entry<String, Integer> entry : appsPerCategory.entrySet()) {
                writer.write(entry.getKey() + ", " + entry.getValue() + "\n");
            }

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



    public static void writeNumberOfFreeApps(Map<String, Long> numberOfFreeApps) {
        try {
            FileWriter writer = new FileWriter("src/NumberOfFreeApps.csv");
            writer.write("Price, Number of apps\n");

            for(Map.Entry<String, Long> entry : numberOfFreeApps.entrySet()) {
                writer.write(entry.getKey() + ", " + entry.getValue() + "\n");
            }

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeAppsPerCompany(Map<String, Integer> appsPerCompany) {
        try {
            FileWriter writer = new FileWriter("src/NumberOfAppsPerCompany.csv");
            writer.write("Company, Number of apps\n");

            ArrayList<Map.Entry<String, Integer>> listOfAppsPerCompany = new ArrayList<>(appsPerCompany.entrySet());
            listOfAppsPerCompany.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

            for(int i = 0; i < 100; i++) {
                writer.write(listOfAppsPerCompany.get(i).getKey() + ", " + listOfAppsPerCompany.get(i).getValue() + "\n");
            }

            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeAppsPerDeveloper(Map<String, Integer> appsPerDeveloper) {
        try {
            FileWriter writer = new FileWriter("src/NumberOfAppsPerDeveloperNotInCompany.csv");
            writer.write("Developer,Number of apps\n");

            ArrayList<Map.Entry<String, Integer>> developers = new ArrayList<>(appsPerDeveloper.entrySet());
            developers.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

            for(int i = 0; i < 3; i++)
                writer.write(developers.get(i).getKey() + "," + developers.get(i).getValue() + "\n");
            writer.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}