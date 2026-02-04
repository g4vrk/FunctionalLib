package com.g4vrk.functionalLib.database.connection;

import java.sql.Connection;
import java.util.Optional;

public interface DatabaseConnection {
    Optional<Connection> connect();
    String getPoolName();
}
