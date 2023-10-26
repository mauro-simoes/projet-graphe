package com.graph_proj.graph.models.graphe;

import lombok.Getter;

@Getter
public class Arc {
    private final int valuation;
    private final int destination;

    public Arc (int cible,int valeur) {
        this.destination = cible;
        this.valuation = valeur;
    }

    @Override
    public String toString() {
        return "Dest : " + destination+ ", " + valuation + "s";
    }

}