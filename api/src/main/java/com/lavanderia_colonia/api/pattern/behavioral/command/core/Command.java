package com.lavanderia_colonia.api.pattern.behavioral.command.core;

public interface Command {
    void execute();
    void undo();
    String getDescription();
}