package com.g4vrk.functionalLib.database.connection.impl;

import com.g4vrk.functionalLib.FunctionalLibPlugin;
import com.g4vrk.functionalLib.database.connection.DatabaseConnection;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@Getter
public class SQLiteConnection implements DatabaseConnection {

    private final HikariDataSource source;
    private final String poolName;

    public SQLiteConnection(String poolName, File baseFile) {
        this.poolName = poolName;
        String url = "jdbc:sqlite:" + baseFile.getAbsolutePath();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setPoolName(poolName);
        config.setMaximumPoolSize(5);
        config.setConnectionTestQuery("SELECT 1");

        this.source = new HikariDataSource(config);
    }

    @Override
    public Optional<Connection> connect() {
        try {
            return Optional.of(source.getConnection());
        } catch (SQLException e) {
            FunctionalLibPlugin.logger().error("Не удалось подключиться к базе данных", e);
        }

        return Optional.empty();
    }
}
