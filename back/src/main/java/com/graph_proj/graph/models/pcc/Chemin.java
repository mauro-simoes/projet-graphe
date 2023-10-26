package com.graph_proj.graph.models.pcc;

import com.graph_proj.graph.models.graphe.IGraphe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Chemin implements Comparable<Chemin>{
    private final IGraphe graphe;
    private final int source;
    private final int destination;
    private int temps;
    private final List<Integer> chemin;

    public Chemin(IGraphe g,int source, int destination) {
        graphe = g;
        temps = 0;
        this.source = source;
        this.destination =  destination;
        chemin = new ArrayList<>();
    }

    public boolean ajoutPossible(int sommet) {
        return (sommet!=destination && sommet!=source && !chemin.contains(sommet));
    }

    public void ajouterSommet(int sommet) {
        if (ajoutPossible(sommet))chemin.add(sommet);
    }

    private void calculTemps() {
        if(!chemin.isEmpty()) {
            // si le chemin n'est pas dans le bon sens (avant-dernier sommet n'a pas d'arc avec la destination)
            if(!graphe.aArc(chemin.get(chemin.size()-1), destination))
                Collections.reverse(chemin);

            temps=graphe.getValuation(source, chemin.get(0));
            for (int i=1;i<chemin.size();++i) {
                temps+=graphe.getValuation(chemin.get(i-1),chemin.get(i));
            }
            temps+=graphe.getValuation(chemin.get(chemin.size()-1),destination);

        }else temps = graphe.getValuation(source, destination);
    }


    public List<Integer> getChemin(){
        List<Integer> cheminComplet = new ArrayList<>();
        cheminComplet.add(source);
        Collections.reverse(chemin);
        cheminComplet.addAll(chemin);
        cheminComplet.add(destination);
        return cheminComplet;
    }

    public int getTemps() {
        if (temps==0 && source!=destination)calculTemps();
        return temps;
    }

    @Override
    public int compareTo(Chemin o) {
        return Double.compare(this.getTemps(),o.getTemps());
    }

    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }
}
