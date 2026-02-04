package com.g4vrk.functionalLib.database.factory;

import com.g4vrk.functionalLib.database.DatabaseType;
import com.g4vrk.functionalLib.database.connection.DatabaseConnection;

public interface DatabaseConnectionFactory {
    DatabaseType getType();
    DatabaseConnection create(String poolName);
}
