package com.graph_proj.graph.models.metro;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Station {

    private List<Integer> ids;

    private String nom;

    private Map<String,Boolean> lignes;


    public Station(String nom) {
        ids = new ArrayList<>();
        this.nom = nom;
        this.lignes = new LinkedHashMap<>();
    }

    public void addId(int id){
        ids.add(id);
    }

    public void addLigne(String ligne, boolean terminus){
        lignes.put(ligne,terminus);
    }

    public boolean isTerminus(String ligne){
        return lignes.get(ligne);
    }

    @Override
    public String toString() {
        return "{" + nom + ", lignes : " + lignes.keySet() + "}";
    }

}
