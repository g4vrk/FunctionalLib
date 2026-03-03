package com.g4vrk.functionalLib;

import com.g4vrk.functionalLib.audience.creator.AudienceCreator;
import com.g4vrk.functionalLib.command.StandardCommand;
import com.g4vrk.functionalLib.command.argument.StandardArgument;
import com.g4vrk.functionalLib.command.requirement.impl.StandardCommandRequirement;
import com.g4vrk.functionalLib.util.text.TextUtil;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public final class FunctionalLibPlugin extends AbstractPlugin {

    private Loader loader;

    public FunctionalLibPlugin() {
    }

    @Override
    public void onPluginLoad() {
        this.loader = new Loader(this);

        var services = Bukkit.getServicesManager();

        services.register(
                FunctionalLibAPI.class,
                loader,
                this,
                ServicePriority.Highest
        );

        loader.onLoad();
    }

    @Override
    public void onPluginEnable() {
        loader.onEnable();
        StandardCommand command = StandardCommand.builder("functionallib")
                .executionStrategy(StandardCommand.ExecutionStrategy.ASYNC)
                .onNoArguments(context -> context.sender().sendMessage("executed no args"))
                .usage("/functionallib")
                .description("test command")
                .argument(StandardArgument.of("test")
                        .argument(StandardArgument.of("test1")
                                .executes((sender, command1, label, args) -> {
                                    sender.sendMessage("test1 executed");
                                    return true;
                                })
                        )
                        .requires(
                                new StandardCommandRequirement(
                                        (sender, args) -> sender.isOp(),
                                        (sender, strings) -> sender.sendMessage("ты не оп")
                                )
                        )
                )
                .argument(StandardArgument.of("second")
                        .alias("secondalias")
                        .executes((sender, command1, label, args) -> {
                            sender.sendMessage("secondalias executed");
                            return true;
                        })
                )
                .build();

        command.register(this, false);
        getSLF4JLogger().info("Тестовая команда зарегистрирована! /functionalLib");
    }

    @Override
    public void onPluginDisable() {
        loader.onDisable();
    }
}
