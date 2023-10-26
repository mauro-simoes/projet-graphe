package com.graph_proj.graph.services.metro;

import com.graph_proj.graph.models.metro.Metro;
import com.graph_proj.graph.models.metro.Station;

import java.util.List;
import java.util.Map;

public interface IMetroService {

    Map<String,Object> calculChemin(Metro metro, String source, String destination);

    List<Station> getAllStations(Metro metro);

}
