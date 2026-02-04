package com.g4vrk.functionalLib.database.connection.impl;

import com.g4vrk.functionalLib.FunctionalLibAPI;
import com.g4vrk.functionalLib.database.connection.DatabaseConnection;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@Getter
public class MySQLConnection implements DatabaseConnection {

    private final HikariDataSource source;
    private final String poolName;

    public MySQLConnection(String poolName, String host, int port, String database, String user, String password) {
        this.poolName = poolName;
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(user);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtsCacheSize", 250);
        config.addDataSourceProperty("prepStmtsCacheSqlLimit", 2048);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setPoolName(poolName);
        config.setConnectionTimeout(10000);
        config.setLeakDetectionThreshold(10000);

        source = new HikariDataSource(config);
    }

    @Override
    public Optional<Connection> connect() {
        try {
            return Optional.of(source.getConnection());
        } catch (SQLException e) {
            FunctionalLibAPI.getAPI().orElseThrow(NullPointerException::new).getLogger().error("Не удалось подключиться к базе данных", e);
        }

        return Optional.empty();
    }
}
