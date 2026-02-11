import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.io.FileInputStream;

/**
 * Simple Java program to test Supabase PostgreSQL connection
 * Compile: javac TestConnection.java
 * Run: java TestConnection
 */
public class TestConnection {
    public static void main(String[] args) {
        // Load .env file
        Properties env = new Properties();
        try {
            env.load(new FileInputStream(".env"));
        } catch (Exception e) {
            System.err.println("Error loading .env file: " + e.getMessage());
            System.exit(1);
        }

        // Get connection details
        String url = expandEnvVars(env.getProperty("DB_URL"), env);
        String username = expandEnvVars(env.getProperty("DB_USERNAME"), env);
        String password = expandEnvVars(env.getProperty("DB_PASSWORD"), env);

        System.out.println("Testing Supabase Connection...");
        System.out.println("URL: " + url);
        System.out.println("Username: " + username);
        System.out.println();

        // Test connection
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("✓ PostgreSQL Driver loaded");

            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("✓ Connection established successfully!");

            // Test query
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT version()");

            if (rs.next()) {
                System.out.println("\n✓ Database version:");
                System.out.println("  " + rs.getString(1));
            }

            // Check if tables exist
            rs = stmt.executeQuery("SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public'");
            if (rs.next()) {
                int tableCount = rs.getInt(1);
                System.out.println("\n✓ Tables in database: " + tableCount);
                if (tableCount == 0) {
                    System.out.println("  (Database is empty - Flyway will create tables on first run)");
                }
            }

            rs.close();
            stmt.close();
            conn.close();

            System.out.println("\n✅ Connection test PASSED!");
            System.out.println("Your Supabase connection is working correctly.");

        } catch (ClassNotFoundException e) {
            System.err.println("❌ PostgreSQL Driver not found!");
            System.err.println("Make sure postgresql driver is in your classpath.");
        } catch (Exception e) {
            System.err.println("❌ Connection FAILED!");
            System.err.println("Error: " + e.getMessage());
            System.err.println("\nCommon issues:");
            System.err.println("  1. Check your SUPABASE_DB_PASSWORD in .env");
            System.err.println("  2. Verify your project reference in SUPABASE_DB_URL");
            System.err.println("  3. Ensure your IP is whitelisted in Supabase");
            System.err.println("  4. Check if SSL is required (?sslmode=require)");
        }
    }

    private static String expandEnvVars(String value, Properties env) {
        if (value == null)
            return null;

        // Simple variable expansion for ${VAR_NAME}
        while (value.contains("${")) {
            int start = value.indexOf("${");
            int end = value.indexOf("}", start);
            if (end == -1)
                break;

            String varName = value.substring(start + 2, end);
            String varValue = env.getProperty(varName, "");
            value = value.substring(0, start) + varValue + value.substring(end + 1);
        }
        return value;
    }
}
