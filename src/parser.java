import java.io.*;
import java.sql.*;
import java.util.*;
import com.opencsv.*;

/*
Jacob Kokai-Kun, MS3 jr coding challenge 8/11/2020
This project is designed to take in a user's csv file and convert it to an SQLite Database
*/

public class parser {
    public static void main(final String[] args) throws IOException {
     //   final String url = "jdbc:sqlite:C://sqlite/db/ms3Interview_Jr_Challenge_2.db";
        String fileName = "";
        String filePath = "";
 
     

        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the path to the folder containing the .csv file (please use \\\\ example: C:\\\\users\\\\yourUsername\\\\downloads\\\\): ");
        filePath = input.nextLine(); 
        System.out.println("Please enter the .csv file name, including the extension: ");
        fileName = input.nextLine(); 
        String csvFilePath = filePath + fileName;
        String url = "jdbc:sqlite:C:\\sqlite\\db\\" + removeExtension(fileName) + ".db";
        input.close();

 
       

        createNewDatabase(fileName, url);
        createNewTable(fileName);
        insertEntries(csvFilePath, url, removeExtension(fileName));
        
        
 
}

    public static void createNewDatabase(String fileName, String url) {

        fileName = removeExtension(fileName);
       // final String url = "jdbc:sqlite:C:/sqlite/db/" + fileName + ".db";

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                final DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (final SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewTable(String fileName) {
        fileName = removeExtension(fileName);
        final String url = "jdbc:sqlite:C://sqlite/db/" + fileName + ".db";
        // SQL statement for creating a new table
        final String deleteSql = "DROP TABLE IF EXISTS `People`;"; // this is used to clear the table if it already exist;
        final String sql = "CREATE TABLE IF NOT EXISTS People (\n" + "	A varchar,\n" + "	B varchar,\n"
                + "	C varchar,\n" + "	D varchar,\n" + "	E image,\n" + "	F varchar,\n" + "	G varchar,\n"
                + "	H varchar,\n" + "	I varchar,\n" + "	J varchar\n" + ");";

        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(deleteSql);
            stmt.execute(sql);
            
            System.out.println("Table Created: People");
        } catch (final SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void insertEntries(String csvFilePath, String url, String fileName ) throws IOException { 
        final String sql = "INSERT INTO People (A,B,C,D,E,F,G,H,I,J) VALUES (?,?,?,?,?,?,?,?,?,?)";
        int batchSize = 1000;
        int entriesRecieved = 0;
        int badEntries = 0;
        int goodEntries = 0;
        Connection connection = null;
        
        PrintWriter pw = new PrintWriter(fileName + "-bad.csv");
        FileWriter outputFile = new FileWriter(fileName + "-bad.csv");
        CSVWriter writer = new CSVWriter(outputFile);
       

        try {
 
            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(false);
 
           
            PreparedStatement ps = connection.prepareStatement(sql);
 
            BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
            String lineText = null;
 
            int count = 0;
 
            lineReader.readLine(); // skip header line
            String[] header = { "A", "B", "C","D","E","F","G","H","I","J" }; 
            writer.writeNext(header); 

            while ((lineText = lineReader.readLine()) != null) {
                String[] lineString = lineText.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                entriesRecieved++;
                boolean goodEntrie = true;

                for(int i = 0; i < lineString.length; i++){
                    if (lineString[i].equals("")){
                    goodEntrie = false;
                    }
                }

                if (goodEntrie == false){
                    writer.writeNext(lineString);
                    badEntries++; // if it has a empty column
                }

                else if(lineString.length != 10){ // testing for bad entries that have extra columns
                  badEntries++; // if there are extra entries
                writer.writeNext(lineString);
            }

            else { // otherwise prepare for sql
                ps.setString(1, lineString[0]);
                ps.setString(2, lineString[1]);
                ps.setString(3, lineString[2]);
                ps.setString(4, lineString[3]);
                ps.setString(5, lineString[4]);
                ps.setString(6, lineString[5]);
                ps.setString(7, lineString[6]);
                ps.setString(8, lineString[7]);
                ps.setString(9, lineString[8]);
                ps.setString(10, lineString[9]);
                ps.addBatch();
                count++;
                goodEntries++;
            }
 
                if (count % batchSize == 0) { // excutes a batch every 1000 instructions
                    ps.executeBatch();
                    System.out.println("batch inserted");
                }
            }
            pw.close();
            writer.close();
            lineReader.close();
 
            // execute the remaining queries not in a previous batch
            ps.executeBatch();
 
            connection.commit();
            connection.close(); // close sql connection
 
        } catch (IOException ex) {
            System.err.println(ex);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        BufferedWriter logWriter = new BufferedWriter(new FileWriter(fileName + ".log"));
        logWriter.write("Entries Recieved: " + entriesRecieved + "\nGood Entries: " + goodEntries + "\nBad Entries: " + badEntries);
        logWriter.close();
    }
    
    public static String removeExtension(String fname) { //used to remove the .csv for naming purpouses
        int pos = fname.lastIndexOf('.');
        if(pos > -1)
           return fname.substring(0, pos);
        else
           return fname;
     }

}