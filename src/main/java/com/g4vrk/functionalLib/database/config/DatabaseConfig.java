package com.g4vrk.functionalLib.database.config;

import com.g4vrk.functionalLib.database.DatabaseType;
import lombok.Setter;

import java.io.File;

public record DatabaseConfig(
        @Setter DatabaseType type,
        @Setter String host,
        @Setter int port,
        @Setter String database,
        @Setter String user,
        @Setter String password,
        @Setter File sqliteFile
) {}

