package ru.vsu.csf.proskuryakov.dominoes.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.vsu.csf.proskuryakov.dominoes.core.GameState;
import ru.vsu.csf.proskuryakov.dominoes.core.Market;
import ru.vsu.csf.proskuryakov.dominoes.core.PlayingField;
import ru.vsu.csf.proskuryakov.dominoes.data.command.Command;
import ru.vsu.csf.proskuryakov.dominoes.data.command.NextMoveCommand;
import ru.vsu.csf.proskuryakov.dominoes.data.essence.Bone;
import ru.vsu.csf.proskuryakov.dominoes.gui.controllers.Game;
import ru.vsu.csf.proskuryakov.dominoes.gui.window.ExitWindow;
import ru.vsu.csf.proskuryakov.dominoes.gui.window.InformationWindow;
import ru.vsu.csf.proskuryakov.dominoes.json.Deserializer;
import ru.vsu.csf.proskuryakov.dominoes.json.Serializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class GameController {

    public static final File GAME_STATE_JSON_FILE = new File(ClassLoader.getSystemClassLoader().getResource("json/jsonGameState.json").getFile());


    public static String nextMove(GameState gameState){

        String move = "";

        try {
            move = gameState.nextMove();
        }catch (Error e){
            System.out.println("НЕ МОГУ СЛЕДУЮЩИЙ ХОД СДЕЛАТЬ");
        }

        if(gameState.isHaveWinner()){
            new InformationWindow("Win",
                    100, 250).display("Победитель: " + gameState.getWinnerName());
        }

        return move;
    }

    public static void undo(){
        if(Dominoes.app.getCommandHistory().isEmpty()){
            System.out.println("История пуста");
            return;
        }

        Command command = Dominoes.app.getCommandHistory().pop();
        if(command != null){
            String undoCommandDescriptione = "Отмена команды: \"" + command.getCommandDescription() + "\"";
            Command undoCommand = new NextMoveCommand(command.getGameState(),
                    command.getJsonString(), undoCommandDescriptione);

            Dominoes.app.getUndoCommandHistory().push(undoCommand);

            command.undo();
            Game.game.setGameState(command.getGameState());

        }
    }

    public static void closeProgram() {

        if(new ExitWindow().displayAndGetAnswer()){
            Dominoes.app.close();
        }

    }

    public static void saveGameState(GameState gameState){

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Bone.class, new Serializer.BoneSerializer())
                .registerTypeAdapter(PlayingField.class, new Serializer.PlayingFieldSerializer())
                .registerTypeAdapter(Market.class, new Serializer.MarketSerializer())
                .registerTypeAdapter(GameState.class, new Serializer.GameStateSerializer())
                .create();

        String jsonGameState = gson.toJson(gameState);

        try(FileWriter writer = new FileWriter(GAME_STATE_JSON_FILE))
        {
            writer.write(jsonGameState);
            System.out.println("В файл записано");
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

    }

    public static GameState openLastGame(){

        String jsonGameState = null;
        try {
            jsonGameState = new String(Files.readAllBytes(Paths.get(GAME_STATE_JSON_FILE.toURI())));
            System.out.println("Игра загружена");
        } catch (IOException e) {
            System.out.println(e);
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Bone.class, new Deserializer.BoneDeserializer())
                .registerTypeAdapter(LinkedList.class, new Deserializer.PlayingFieldDeserializer())
                .registerTypeAdapter(LinkedList.class, new Deserializer.MarketDeserializer())
                .registerTypeAdapter(GameState.class, new Deserializer.GameStateDeserializer())
                .create();

        return gson.fromJson(jsonGameState, GameState.class);

    }
}
