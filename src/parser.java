import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
public class parser {
    public static void main(final String[] args) throws IOException {
        final String url = "jdbc:sqlite:C://sqlite/db/ms3Interview_Jr_Challenge_2.db";
        final String username = "user";
        final String password = "password";
        int badEntries = 0;
 
        final String csvFilePath = "C:\\users\\jacob\\downloads\\ms3Interview_Jr_Challenge_2.csv";
 
        final int batchSize = 1000;
 
        Connection connection = null;

        createNewDatabase("ms3Interview_Jr_Challenge_2");
        createNewTable("ms3Interview_Jr_Challenge_2");
 
        int batchNum = 100;
        int turn = 1;
        
        final String sql = "INSERT INTO People (A,B,C,D,E,F,G,H,I,J) VALUES (?,?,?,?,?,?,?,?,?,?)";
        // SQL statement for creating a new table
        
        BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
        String currentLine;

        try {
 
            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(false);
 
           
            PreparedStatement ps = connection.prepareStatement(sql);
 
            BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
            String lineText = null;
 
            int count = 0;
 
            lineReader.readLine(); // skip header line
 
            while ((lineText = lineReader.readLine()) != null) {
                String[] lineString = lineText.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
             
                if(lineString.length != 10){ // testing for bad entries that have extra columns
                  badEntries++;
                    try (PrintWriter writer = new PrintWriter(new File("test.txt"))) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(lineText);
                        writer.write(sb.toString());
                        lineText = lineReader.readLine(); // skip if bad entrie
                }
            }
            else{ // otherwise prepare for sql
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
            }
               // System.out.println(count);
               // System.out.println("line inserted");
 
                if (count % batchSize == 0) {
                    ps.executeBatch();
                    System.out.println("batch inserted");
                }
            }
 
            lineReader.close();
 
            // execute the remaining queries
            ps.executeBatch();
 
            connection.commit();
            connection.close();
 
        } catch (IOException ex) {
            System.err.println(ex);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
 
}

    public static void createNewDatabase(final String fileName) {

        final String url = "jdbc:sqlite:C:/sqlite/db/" + fileName + ".db";

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

    public static void createNewTable(final String fileName) {
        // SQLite connection string
        final String url = "jdbc:sqlite:C://sqlite/db/" + fileName + ".db";
        // SQL statement for creating a new table
        final String sql = "CREATE TABLE IF NOT EXISTS People (\n" + "	A varchar,\n" + "	B varchar,\n"
                + "	C varchar,\n" + "	D varchar,\n" + "	E image,\n" + "	F varchar,\n" + "	G varchar,\n"
                + "	H varchar,\n" + "	I varchar,\n" + "	J varchar\n" + ");";

        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            System.out.println("Table Created");
        } catch (final SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void writeToTable(final String dbName, final String[] lineString) {
        final int batchNum = 100;
        final int turn = 1;
        // SQLite connection string
        final String url = "jdbc:sqlite:C://sqlite/db/" + dbName + ".db";
        final String sql = "INSERT INTO People (A,B,C,D,E,F,G,H,I,J) VALUES (?,?,?,?,?,?,?,?,?,?)";
        // SQL statement for creating a new table

        try (Connection conn = DriverManager.getConnection(url); PreparedStatement ps = conn.prepareStatement(sql);) {
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
            ps.executeUpdate();
            System.out.println("line inserted");

            // batchNum++;
            // turn++;

            // if (turn % batchNum == 0){
            // ps.executeBatch();
            // System.out.println("batch Inserted");
            // }
            //
        } catch (final SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}