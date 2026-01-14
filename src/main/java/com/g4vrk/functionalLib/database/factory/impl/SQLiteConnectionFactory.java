package com.g4vrk.functionalLib.database.factory.impl;

import com.g4vrk.functionalLib.database.DatabaseType;
import com.g4vrk.functionalLib.database.config.DatabaseConfig;
import com.g4vrk.functionalLib.database.connection.DatabaseConnection;
import com.g4vrk.functionalLib.database.connection.impl.SQLiteConnection;
import com.g4vrk.functionalLib.database.factory.DatabaseConnectionFactory;

public class SQLiteConnectionFactory implements DatabaseConnectionFactory {

    private final DatabaseConfig config;

    public SQLiteConnectionFactory(DatabaseConfig config) {
        this.config = config;
    }

    @Override
    public DatabaseType getType() {
        return DatabaseType.SQLITE;
    }

    @Override
    public DatabaseConnection create(String poolName) {
        return new SQLiteConnection(poolName, config.sqliteFile());
    }
}

