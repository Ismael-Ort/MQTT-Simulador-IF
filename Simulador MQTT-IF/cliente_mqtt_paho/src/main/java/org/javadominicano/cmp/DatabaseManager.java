package org.javadominicano.cmp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/estaciones_m";
    private static final String USER = "root"; 
    private static final String PASSWORD = "IF10131014"; 

    private Connection getConnection() throws SQLException {
        try {
            // Cargar explícitamente el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver de MySQL", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public int insertStation(String stationModel, String location) {
        String sql = "INSERT INTO Station (station_model, creation_date, location, maintenance_date) VALUES (?, CURDATE(), ?, CURDATE())";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, stationModel);
            stmt.setString(2, location);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Devuelve el station_id generado
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int insertSensor(int stationId, String sensorModel, String sensorType) {
        String sql = "INSERT INTO Sensor (station_id, sensor_model, sensor_type, creation_date) VALUES (?, ?, ?, CURDATE())";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, stationId);
            stmt.setString(2, sensorModel);
            stmt.setString(3, sensorType);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Devuelve el sensor_id generado
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void insertRecord(int sensorId, double value, Date recordDateTime) {
        String sql = "INSERT INTO Record (sensor_id, value, record_datetime) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sensorId);
            stmt.setDouble(2, value);
            stmt.setTimestamp(3, new java.sql.Timestamp(recordDateTime.getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}