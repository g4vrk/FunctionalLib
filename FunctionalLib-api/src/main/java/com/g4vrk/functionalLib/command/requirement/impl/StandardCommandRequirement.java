package com.g4vrk.functionalLib.command.requirement.impl;

import com.g4vrk.functionalLib.command.requirement.CommandRequirement;
import org.bukkit.command.CommandSender;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class StandardCommandRequirement implements CommandRequirement {

    private final BiPredicate<CommandSender, String[]> testPredicate;
    private final BiConsumer<CommandSender, String[]> failConsumer;

    public StandardCommandRequirement(BiPredicate<CommandSender, String[]> testPredicate) {
        this(testPredicate, (sender, strings) -> {});
    }

    public StandardCommandRequirement(BiPredicate<CommandSender, String[]> testPredicate, BiConsumer<CommandSender, String[]> failConsumer) {
        this.testPredicate = testPredicate;
        this.failConsumer = failConsumer;
    }

    @Override
    public boolean test(CommandSender sender, String[] args) {
        return testPredicate.test(sender, args);
    }

    @Override
    public void onFail(CommandSender sender, String[] args) {
        failConsumer.accept(sender, args);
    }
}
