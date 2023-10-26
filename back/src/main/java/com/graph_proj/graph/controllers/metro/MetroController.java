package com.graph_proj.graph.controllers.metro;

import com.graph_proj.graph.global.Constants;
import com.graph_proj.graph.models.metro.Station;
import com.graph_proj.graph.models.requests.CalculCheminRequest;
import com.graph_proj.graph.services.metro.IMetroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(path = "/metro")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class MetroController {

    private final IMetroService imetroService;

    @PostMapping("/calcul-chemin")
    public ResponseEntity<Map<String,Object>> calculChemin(@RequestBody CalculCheminRequest request){
        if (request.isInvalid())
            return ResponseEntity.badRequest().body(Map.of("Error","Invalid request"));

        Map<String,Object> chemin;
        try{
            chemin = imetroService.calculChemin(Constants.METRO,request.source(), request.destination());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of("Exception",e.getMessage()));
        }

        return ResponseEntity.ok().body(chemin);
    }

    @GetMapping("/get-all-stations")
    public ResponseEntity<List<Map<String,Object>>> getAllStations(){
        List<Map<String,Object>> stations = new ArrayList<>();

        for (Station station : imetroService.getAllStations(Constants.METRO)){
            stations.add(Map.ofEntries(
                    Map.entry("nom",station.getNom()),
                    Map.entry("lignes",station.getLignes().keySet())
                )
            );
        }

        return ResponseEntity.ok().body(stations);
    }

}
