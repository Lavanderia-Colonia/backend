package com.lavanderia_colonia.api.pattern.behavioral.command.receiver;

import java.util.Stack;

import com.lavanderia_colonia.api.pattern.behavioral.command.core.Command;

public class CommandManager {
    private final Stack<Command> executedHistory;
    private final Stack<Command> undoHistory;

    public CommandManager() {
        this.executedHistory = new Stack<>();
        this.undoHistory = new Stack<>();
    }

    public void executeCommand(Command comand) {
        comand.execute();
        executedHistory.push(comand);
        undoHistory.clear();
        System.out.println("  [Comando executado: " + comand.getDescription() + "]");
    }

    public void undo() {
        if (executedHistory.isEmpty()) {
            System.out.println(" Nenhum comando para desfazer");
            return;
        }
        
        Command comando = executedHistory.pop();
        comando.undo();
        undoHistory.push(comando);
        System.out.println("  [Comando desfeito: " + comando.getDescription() + "]");
    }
}
