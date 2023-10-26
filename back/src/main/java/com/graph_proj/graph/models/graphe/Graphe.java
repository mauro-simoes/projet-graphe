package com.graph_proj.graph.models.graphe;

import java.util.*;

public class Graphe implements IGraphe{

    private final Map<Integer,List<Arc>> arcs;

    public Graphe(){
        arcs = new LinkedHashMap<>();
    }

    public void addArc(Integer source, Arc arc ){
        if (arc == null){
            arcs.put(source,new ArrayList<>());
        }else if (arcs.containsKey(source))
            arcs.get(source).add(arc);
        else {
            List<Arc> arcList = new ArrayList<>();
            arcList.add(arc);
            arcs.put(source,arcList);
        }
    }

    public List<Arc> getArcs(Integer source){
        return arcs.get(source);
    }

    @Override
    public List<Integer> getSommets() {
        List<Integer> listeSommets = new ArrayList<>();
        for (Object sommet : Arrays.asList(arcs.keySet().toArray()))
            listeSommets.add((Integer) sommet);
        return listeSommets;
    }

    @Override
    public ArrayList<Integer> getSuccesseurs(int source) {
        ArrayList<Integer> sommets = new ArrayList<>();
        for (int i=0;i<arcs.get(source).size();++i) {
            sommets.add(arcs.get(source).get(i).getDestination());
        }
        return sommets;
    }

    @Override
    public List<Integer> getPredecesseurs(int sommet) {
        ArrayList<Integer> sommets = new ArrayList<>();
        for(Integer key : arcs.keySet()) {
            for(Arc arc : arcs.get(key)) {
                if (arc.getDestination()==sommet)
                    sommets.add(key);
            }
        }
        return sommets;
    }

    @Override
    public int getValuation(int sommet1, int sommet2) {
        List<Arc> arcList = arcs.get(sommet1);
        for (Arc arc : arcList)
            if (arc.getDestination() == sommet2)
                return arc.getValuation();
        return Integer.MAX_VALUE;
    }

    @Override
    public int getNbSommets() {
        return arcs.size();
    }

    @Override
    public boolean aArc(int a, int b) {
        return getValuation(a,b) != -1;
    }

    @Override
    public int dIn(int sommet) {
        int d = 0;
        for(Integer key : arcs.keySet()) {
            for(Arc arc : arcs.get(key)) {
                if (arc.getDestination()==sommet) ++d;
            }
        }
        return d;
    }

    @Override
    public int dOut(int source) {
        return arcs.get(source).size();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer sommet : arcs.keySet()){
            stringBuilder.append(sommet).append(" - ").append(arcs.get(sommet)).append("\n");
        }
        return stringBuilder.toString();
    }
}
