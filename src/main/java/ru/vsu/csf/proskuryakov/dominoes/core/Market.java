package ru.vsu.csf.proskuryakov.dominoes.core;

import ru.vsu.csf.proskuryakov.dominoes.data.essence.Bone;
import ru.vsu.csf.proskuryakov.dominoes.data.OrderedBones;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Market{

    private LinkedList<Bone> market;
    private Random rnd = new Random();

    public Market() {
        market = new LinkedList<>(OrderedBones.getAllBones());
        randomSort();
    }

    public Market(List<Bone> market) {
        this.market = new LinkedList<>(market);
    }

    public Bone getRandomBone(){
        int index = rnd.nextInt(market.size());
        Bone bone = market.get(index);
        market.remove(index);
        return bone;
    }

    public Bone getBone(){
        return market.poll();
    }

    public List<Bone> getStartBonePack(){
        List<Bone> startBoneList = market.stream().limit(7).collect(Collectors.toList());
        market.removeAll(startBoneList);
        return startBoneList;
    }

    public List<Bone> getMarketList(){
        return market;
    }

    public boolean isEmpty(){
        return market.isEmpty();
    }

    public int size(){
        return market.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Bone bone: market){
            sb.append(bone.toString());
            sb.append(" ");
        }
        return sb.toString();
    }

    public void randomSort(){
        market.sort((a, b) -> rnd.nextInt(2)*2-1);
    }



}
