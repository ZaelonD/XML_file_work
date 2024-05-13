package sql_queries;

public class Queries {
    public static final String SELECT = "SELECT * FROM Departments";
    public static final String INSERT = "INSERT INTO departments (depcode, depjob, description) VALUES (?, ?, ?)";
    public static final String UPDATE = "UPDATE departments SET description = ? WHERE depcode = ? AND depjob = ?";
    public static final String DELETE = "DELETE FROM departments WHERE depcode = ? AND depjob = ?";
}
