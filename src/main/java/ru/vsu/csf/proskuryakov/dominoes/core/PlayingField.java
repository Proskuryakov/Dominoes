package ru.vsu.csf.proskuryakov.dominoes.core;

import ru.vsu.csf.proskuryakov.dominoes.data.essence.Bone;

import java.util.LinkedList;
import java.util.List;

public class PlayingField{

    private LinkedList<Bone> playingField;

    public PlayingField() {
        this.playingField = new LinkedList<>();
    }

    public PlayingField(List<Bone> playingField) {
        this.playingField = new LinkedList<>(playingField);
    }

    public void addFirst(Bone bone){
        if(playingField.size() == 0 ||
                bone.getPipsOnSecondHalf() == playingField.getFirst().getPipsOnFirstHalf()){
            playingField.addFirst(bone);
        }else{
            playingField.addFirst(new Bone(bone.getPipsOnSecondHalf(), bone.getPipsOnFirstHalf()));
        }
    }

    public void addLast(Bone bone){
        if(playingField.size() == 0 ||
                bone.getPipsOnFirstHalf() == playingField.getLast().getPipsOnSecondHalf()){
            playingField.addLast(bone);
        }else{
            playingField.addLast(new Bone(bone.getPipsOnSecondHalf(), bone.getPipsOnFirstHalf()));
        }
    }

    public Bone getFirst(){
        return playingField.getFirst();
    }

    public Bone getLast(){
        return playingField.getLast();
    }

    public List<Bone> getPlayingField() {
        return playingField;
    }

}
