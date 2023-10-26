package com.graph_proj.graph.models.metro;


import com.graph_proj.graph.models.graphe.Graphe;
import com.graph_proj.graph.models.graphe.IGraphe;
import lombok.Getter;

import java.util.*;

@Getter
public class Metro {

    private final static int MINUTE = 60;

    private final IGraphe plan;

    private final List<Station> stations;

    public Metro(){
        plan = new Graphe();
        stations = new ArrayList<>();
    }

    public void addStation(Station station){
        stations.add(station);
    }

    public Station getStationFromId(int stationID){
        for (Station station : stations){
            if(station.getIds().contains(stationID))
                return station;
        }
        return null;
    }

    public Station getStationFromName(String nom){
        for (Station station : stations){
            if(station.getNom().equalsIgnoreCase(nom))
                return station;
        }
        return null;
    }

    public String getLigne(int stationID){
        Station station = getStationFromId(stationID);
        return Arrays.asList(station.getLignes().keySet().toArray())
                .get(station.getIds().indexOf(stationID)).toString();
    }

}
