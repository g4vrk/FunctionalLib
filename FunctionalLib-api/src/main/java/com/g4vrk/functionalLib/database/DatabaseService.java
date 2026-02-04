package com.g4vrk.functionalLib.database;

import com.g4vrk.functionalLib.database.connection.DatabaseConnection;
import com.g4vrk.functionalLib.database.factory.DatabaseConnectionFactory;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseService {

    private final Map<String, DatabaseConnection> connections = new ConcurrentHashMap<>();
    private final DatabaseConnectionFactory factory;

    public DatabaseService(DatabaseConnectionFactory factory) {
        this.factory = factory;
    }

    public @Nullable Connection connect(String poolName) {
        return connections
                .computeIfAbsent(poolName, factory::create)
                .connect().orElse(null);
    }
}
