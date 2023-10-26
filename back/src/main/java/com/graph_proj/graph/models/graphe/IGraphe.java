package com.graph_proj.graph.models.graphe;

import java.util.List;

public interface IGraphe {

    void addArc(Integer source, Arc arc );

    List<Arc> getArcs(Integer source);

    List<Integer> getSommets();

    List<Integer> getSuccesseurs(int source);

    List<Integer> getPredecesseurs(int sommet);

    int getValuation(int sommet1, int sommet2);

    int getNbSommets();

    boolean aArc(int a, int b);

    int dIn(int sommet);

    int dOut(int source);
}
