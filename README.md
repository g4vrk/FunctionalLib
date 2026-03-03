<p align="center">
  <img src="https://via.placeholder.com/1200x300/1e293b/ffffff?text=FunctionalLib" alt="FunctionalLib" width="800"/>
  <br><br>
  <img src="https://jitpack.io/v/g4vrk/FunctionalLib.svg" alt="JitPack version"/>
  <img src="https://img.shields.io/badge/Minecraft-1.16–1.21+-brightgreen" alt="Supported versions"/>
  <img src="https://img.shields.io/badge/Platform-Paper%20%7C%20Spigot-blue" alt="Platform"/>
  <img src="https://img.shields.io/badge/Java-17%2B-orange" alt="Java"/>
</p>

<h1 align="center">FunctionalLib</h1>

<p align="center">
  <strong>Лёгкая и удобная утилитная библиотека</strong> для создания современных плагинов под Paper / Spigot
</p>

<p align="center">
  <a href="#установка">Установка</a> •
  <a href="#основные-возможности">Возможности</a> •
  <a href="#примеры">Примеры</a> •
  <a href="#лицензия">Лицензия</a>
</p>

## Установка

### Maven (JitPack)

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.g4vrk</groupId>
        <artifactId>FunctionalLib</artifactId>
        <version>ПОСЛЕДНЯЯ_ВЕРСИЯ</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

plugin.yml:
```yaml
depend: [FunctionalLib]
```

### Основные возможности

- 🎨 Гибкое форматирование текста `(TextFormatter)` — MiniMessage / Legacy / Mixed
- 📋 Красивые цветные логи с поддержкой Adventure Component (PluginLogger)
- 🛍 Мощный билдер предметов + загрузка из конфига (ItemBuilder)
- 🗄 Простое подключение к MySQL / SQLite с пулом соединений
- ⚙ Удобное управление несколькими yaml-конфигами (YamlConfigManager)
- 🎬 Система действий из конфига (ActionExecutor) — message, title, sound, console, actionbar и др.
- 🖥 Умные команды с субкомандами, требованиями, асинхронностью (SmartCommand)

### Примеры

Форматировка текста:
```java
public class Example {

    private TextFormatter textFormatter = TextFormatter.builder()
            .cache(true) // кешировать ли строки или нет?
            .type(TextFormatType.MIXED) //Форматы: MIXED, MINI_MESSAGE, LEGACY
            .build();

    public void example(String text) {

        // форматирует по выбранному формату, возвращает Component
        textFormatter.format(text); 

        // форматирует по выбранному формату + переводит в String с форматами legacy
        textFormatter.legacy(text); 

        // полностью убирает все форматирование текста
        textFormatter.plain(text); 
    }
}
```

Логирование:
```java
public class Example {

    // поддерживает цветное логирование с помощью Component 
    private PluginLogger logger = PluginLoggerFactory.getLogger("<название>");

    public void example() {
        
        // пример логирования с цветами
        logger.info(Component.text("Логирование зеленым!").color(NamedTextColor.GREEN));

        // пример логирования обычной строки, есть поддержка {} как у slf4j Logger 
        logger.info("пример обычного сообщения");
    }
}
```

Билдер предметов:
```java
public class Example {

    public void example() {
        // ItemBuilder.fromConfiguration(ConfigurationSection);
        // возвращает предмет сделанный по отправленной секции
        
        // простой пример 
        // Действий есть на много больше, я использовал их для примера
        new ItemBuilder("DIRT")
                .customModelData(1)
                .allFlags(true)
                .glow(true)
                .hideEnchantments(true)
                .name("предмет")
                .lore(List.of("описание предмета"))
                .itemStack();
    }

}
```

Получение соединений с базами данных:
```java
public class Example {

    public void example() {
        // создание конфигурации базы данных, используя ваши секции
        DatabaseConfig config = DatabaseConfigLoader.load(yourConfigurationSection, yourSqliteFile);

        // создание фабрики подключений
        DatabaseConnectionFactory connectionFactory = switch (DatabaseType.valueOf(youDatabaseType)) {
            case MYSQL -> new MySQLConnectionFactory(config);
            case SQLITE -> new SQLiteConnectionFactory(config);
        };

        // создание сервиса для подключения в выбранную базу данных
        DatabaseService databaseService = new DatabaseService(connectionFactory);

        try (Connection connection = databaseService.connect("yourPoolName")) {
            // ваша логика с подключением!
        } catch (SQLException ignored) {
            // по хорошему нужно логировать любые ошибки
        }
    }
}
```

Работа с конфигурациями:
```java
public class YourPlugin extends JavaPlugin {

    private YamlConfigManager configManager;

    public void example() {
        // в конструктор нужно передать JavaPlugin
        configManager = new YamlConfigManager(this);

        // получение конфига по имени
        Configuration config = configManager.getConfig("main-config.yml");

        // перезагрузка конфига по имени
        configManager.reload("main-config.yml");

        // перезагрузка всех конфигураций
        configManager.reloadAll();
    }
}
```
Работа с Actions:

```java
public class YourPlugin extends JavaPlugin {

    private ActionExecutor actionExecutor;

    public void example() {
        actionExecutor = new ActionExecutor();

        // регистрация действий
        actionExecutor.registerAction(new Action() {
            @Override
            public void execute(ActionContext context, String args) {
                context.player().sendMessage("пример действия");
            }

            @Override
            public String getActionKey() {
                return "example";
            }
        });

        actionExecutor.runActions(yourPlayer, List.of("[message] тест", "[actionbar] проверка"));

        // стандартные действия:
        // [actionbar] сообщение
        // [message] сообщение
        // [close-menu]
        // [update-inventory]
        // [console] команда
        // [title] верхний_текст;нижний_текст(необязателен)
        // [sound] айди;громкость;высота
    }
}
```

Регистрация комманд:

```java
public class Example {
    public void example() {
        // Билдер SmartCommand
        SmartCommand smartAbstractCommand = SmartCommand.builder()
                .name()
                // Требования, можете создавать свои
                .requirement(new CommandRequirement() {
                    @Override
                    public boolean test(CommandSender sender, String[] args) {
                        return sender.hasPermission("example.perm");
                    }

                    @Override
                    public void onFail(CommandSender sender, String[] args) {
                        sender.sendMessage("ты ахуел");
                    }
                })
                // алиасы
                .alias("example")
                // выполнять ли асинхронно
                .runAsync(false)
                // субкомманды
                .argument(new SubCommand() {
                    @Override
                    public String getName() {
                        return "reload";
                    }

                    @Override
                    public List<String> getAliases() {
                        return List.of("reboot");
                    }

                    @Override
                    public Collection<CommandRequirement> getRequirements() {
                        return List.of(new CommandRequirement() {
                            @Override
                            public boolean test(CommandSender sender, String[] args) {
                                return sender.hasPermission("reload");
                            }

                            @Override
                            public void onFail(CommandSender sender, String[] args) {
                                sender.sendMessage("no perms");
                            }
                        });
                    }

                    @Override
                    public void execute(CommandSender sender, String[] args) {
                        reloadLogic();
                    }

                    @Override
                    public List<String> tabComplete(CommandSender sender, String[] args) {
                        return List.of();
                    }
                });
    }
}
```
