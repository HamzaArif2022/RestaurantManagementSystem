package com.restaurant.dao;

import com.restaurant.models.Client;
import java.sql.SQLException;
import java.util.List;

public interface ClientDAO {
    void addClient(Client client) throws SQLException;
    void updateClient(Client client) throws SQLException;
    void deleteClient(int clientId) throws SQLException;
    Client getClientById(int clientId) throws SQLException;
    List<Client> getAllClients() throws SQLException;
    List<Client> searchClients(String keyword) throws SQLException;
}