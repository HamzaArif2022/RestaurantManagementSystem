package com.restaurant.dao;

import com.restaurant.config.DBConfig;
import com.restaurant.models.Client;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClientDAOImpl implements com.restaurant.dao.ClientDAO {
    @Override
    public void addClient(Client client) throws SQLException {
        String sql = "INSERT INTO clients (name, phone, email, join_date, loyalty_points) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, client.getName());
            stmt.setString(2, client.getPhone());
            stmt.setString(3, client.getEmail());
            stmt.setDate(4, Date.valueOf(client.getJoinDate()));
            stmt.setInt(5, client.getLoyaltyPoints());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    client.setClientId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void updateClient(Client client) throws SQLException {
        String sql = "UPDATE clients SET name = ?, phone = ?, email = ?, loyalty_points = ? WHERE client_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getName());
            stmt.setString(2, client.getPhone());
            stmt.setString(3, client.getEmail());
            stmt.setInt(4, client.getLoyaltyPoints());
            stmt.setInt(5, client.getClientId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteClient(int clientId) throws SQLException {
        String sql = "DELETE FROM clients WHERE client_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            stmt.executeUpdate();
        }
    }

    @Override
    public Client getClientById(int clientId) throws SQLException {
        String sql = "SELECT * FROM clients WHERE client_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToClient(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Client> getAllClients() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";

        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
        }
        System.out.println("Loaded " + clients.size() + " clients.");
        return clients;
    }

    @Override
    public List<Client> searchClients(String keyword) throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients WHERE name LIKE ? OR phone LIKE ? OR email LIKE ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clients.add(mapResultSetToClient(rs));
                }
            }
        }
        return clients;
    }

    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setClientId(rs.getInt("client_id"));
        client.setName(rs.getString("name"));
        client.setPhone(rs.getString("phone"));
        client.setEmail(rs.getString("email"));
        client.setJoinDate(rs.getDate("join_date").toLocalDate());
        client.setLoyaltyPoints(rs.getInt("loyalty_points"));
        return client;
    }
}