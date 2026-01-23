package com.g4vrk.functionalLib.addon;

import com.g4vrk.functionalLib.configuration.Configuration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@AllArgsConstructor
public class AddonDescription {
    private final String name;
    private final String mainClass;
    private final String version;
    private final String description;
    private final Set<String> authors;

    public AddonDescription(YamlConfiguration configuration) {
        this.name = Objects.requireNonNull(configuration.getString("name"), "В описании аддона не найдено имя!");
        this.mainClass = Objects.requireNonNull(configuration.getString("main"), "В описании аддона не найден mainClass!");
        this.version = configuration.getString("version");
        this.description = configuration.getString("description");
        this.authors = new HashSet<>();
        if (configuration.contains("authors")) {
            authors.addAll(configuration.getStringList("authors"));
        }
        String author = configuration.getString("author");
        if (author != null) {
            authors.add(author);
        }
    }
    public AddonDescription(Configuration configuration) {
        this.name = Objects.requireNonNull(configuration.getString("name"), "В описании аддона не найдено имя!");
        this.mainClass = Objects.requireNonNull(configuration.getString("main"), "В описании аддона не найден mainClass!");
        this.version = configuration.getString("version");
        this.description = configuration.getString("description");
        this.authors = new HashSet<>();
        if (configuration.contains("authors")) {
            authors.addAll(configuration.getStringList("authors"));
        }
        String author = configuration.getString("author");
        if (author != null) {
            authors.add(author);
        }
    }

}
