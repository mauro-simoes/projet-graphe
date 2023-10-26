package com.graph_proj.graph.services.metro;

import com.graph_proj.graph.global.Constants;
import com.graph_proj.graph.models.metro.Direction;
import com.graph_proj.graph.models.metro.Metro;
import com.graph_proj.graph.models.metro.Station;
import com.graph_proj.graph.models.pcc.Chemin;
import com.graph_proj.graph.services.pcc.PCCBellman;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MetroService implements IMetroService{

    private final static int MINUTE = 60;

    @Override
    public Map<String,Object> calculChemin(Metro metro, String source, String destination) {
        StringBuilder cheminString = new StringBuilder(String.format("- Vous êtes à %s.\n",metro.getStationFromName(source).getNom()));

        // calculer le chemin le plus court entre les stations
        Chemin chemin = getShortestPath(metro,source, destination);
        List<Integer> listeSommmetsChemin = chemin.getChemin();
        List<String> listeArretsChemin = new ArrayList<>();

        // recuperer la premiere ligne à prendre et dans quelle direction la prendre
        Direction direction = detectChange(metro,chemin.getSource(),listeSommmetsChemin.get(1),listeSommmetsChemin);
        String lastLine = direction.ligne();
        String lastDirection = direction.direction();
        cheminString.append(String.format("- Prenez la ligne %s direction %s.\n",direction.ligne(),direction.direction()));
        listeArretsChemin.add(source + ";" + lastLine + ";" + direction.direction());

        // detecter s'il y a des changements de ligne sur le chemin
//        for (int i = 1; i < listeSommmetsChemin.size()-2;i++){
//            direction = detectChange(metro,listeSommmetsChemin.get(i),listeSommmetsChemin.get(i+1),listeSommmetsChemin);
//            Station station = metro.getStationFromId(listeSommmetsChemin.get(i));
//            // si la ligne de metro n'est pas la meme que la precedente alors ajouter le changement de ligne
//            if (!direction.ligne().equals(lastLine)){
//                cheminString.append(String.format("- A %s changez et prenez la ligne %s direction %s.\n"
//                        ,metro.getStationFromId(listeSommmetsChemin.get(i)).getNom(),direction.ligne(),direction.direction()));
//                lastLine = direction.ligne();
//                lastDirection = direction.direction();
//            }
//            if(!listeArretsChemin.contains(station.getNom()))
//                listeArretsChemin.add(station.getNom() + ";" + lastLine + ";" + lastDirection);
//        }
        cheminString.append(getChanges(metro,listeSommmetsChemin,listeArretsChemin,lastLine,lastDirection));

        listeArretsChemin.add(metro.getStationFromId(listeSommmetsChemin.get(listeSommmetsChemin.size()-2)).getNom()+ ";" +
                metro.getLigne(listeSommmetsChemin.get(listeSommmetsChemin.size()-2)) + ";" + lastLine + ";" + lastDirection);
        listeArretsChemin.add(destination+ ";" + metro.getLigne(listeSommmetsChemin.get(listeSommmetsChemin.size()-1)) + ";" + lastDirection);
        // afficher le temps d'arrivee, si inferieur a une minute, afficher en secondes
        int tempsTrajet = chemin.getTemps();
        if (tempsTrajet < MINUTE)
            cheminString.append(String.format("- Vous devriez arriver à %s dans environ %d secondes.",
                    metro.getStationFromId(chemin.getDestination()).getNom(),tempsTrajet));
        else
            cheminString.append(String.format("- Vous devriez arriver à %s dans environ %d minutes.",
                    metro.getStationFromId(chemin.getDestination()).getNom(),tempsTrajet/MINUTE));


        return Map.ofEntries(
                Map.entry("chemin",listeArretsChemin),
                Map.entry("cheminText",cheminString.toString()),
                Map.entry("temps",tempsTrajet)
        );
    }

    @Override
    public List<Station> getAllStations(Metro metro) {
        Set<Station> set = new LinkedHashSet<>(metro.getStations());
        return new ArrayList<>(set);
    }

    private String getChanges(Metro metro,List<Integer> listeSommmetsChemin,List<String> listeArretsChemin,String lastLine,String lastDirection){
        StringBuilder cheminChanges = new StringBuilder();
        // detecter s'il y a des changements de ligne sur le chemin
        for (int i = 1; i < listeSommmetsChemin.size()-2;i++){
            Direction direction = detectChange(metro,listeSommmetsChemin.get(i),listeSommmetsChemin.get(i+1),listeSommmetsChemin);
            Station station = metro.getStationFromId(listeSommmetsChemin.get(i));
            // si la ligne de metro n'est pas la meme que la precedente alors ajouter le changement de ligne
            if (!direction.ligne().equals(lastLine)){
                cheminChanges.append(String.format("- A %s changez et prenez la ligne %s direction %s.\n"
                        ,metro.getStationFromId(listeSommmetsChemin.get(i)).getNom(),direction.ligne(),direction.direction()));
                lastLine = direction.ligne();
                lastDirection = direction.direction();
            }
            if(!listeArretsChemin.contains(station.getNom()))
                listeArretsChemin.add(station.getNom() + ";" + lastLine + ";" + lastDirection);
        }
        return cheminChanges.toString();
    }

    private Chemin getShortestPath(Metro metro,String source, String destination){
        Station sourceStation = metro.getStationFromName(source);
        Station destinationStation = metro.getStationFromName(destination);
        List<Chemin> cheminsPossibles = new ArrayList<>();
        for (Integer idSource : sourceStation.getIds()){
            for (Integer idDest : destinationStation.getIds()){
                cheminsPossibles.add(PCCBellman.pcc(metro.getPlan(),idSource,idDest));
            }
        }
        Collections.sort(cheminsPossibles);
        return cheminsPossibles.get(0);
    }


    private Direction detectChange(Metro metro,int station1, int station2, List<Integer> chemin){
        String line,direction;
        Station firstStop = Constants.METRO.getStationFromId(station1);
        Station secondStop = Constants.METRO.getStationFromId(station2);
        // si les deux identifiants de station representent la meme station alors prendre la deuxieme station
        //, car cela veut dire qu'il y a un changement de ligne
        // sinon prendre la ligne de la premiere station
        if(firstStop.getNom().equals(secondStop.getNom()))
            line = Constants.METRO.getLigne(station2);
        else
            line = Constants.METRO.getLigne(station1);

        // recuperer la direction dans laquelle il faut aller
        direction = getDirection(metro,chemin,line);
        return new Direction(line, direction);
    }

    private String getDirection(Metro metro,List<Integer> sommets, String line){
        String direction = "";
        for (int i = sommets.size()-1; i >=0; i--){
            if(metro.getLigne(sommets.get(i)).equals(line)){
                direction = direction(metro,sommets.get(i), sommets.get(i),line);
                break;
            }
        }
        return direction;
    }


    private String direction(Metro metro,int station1, int previous, String line){
        String direction = "";

        // parcourir tous les successeurs de la station jusqu'à tomber sur le terminus pour avoir la direction
        for(Integer idStation : metro.getPlan().getSuccesseurs(station1)){

            Station station = metro.getStationFromId(idStation);
            String ligne = Arrays.asList(station.getLignes().keySet().toArray())
                    .get(station.getIds().indexOf(idStation)).toString();

            // si la station est la station "source" ou si c'est la station qui a deja ete visite juste avant
            // ou si la station n'est pas sur la meme ligne, sauter l'iteration
            if(idStation == previous || !Objects.equals(ligne, line))
                continue;
            //System.out.println("source " + getStationFromId(station1).getNom() + " dest " + station.getNom() + " line " + ligne);
            // si la station est un terminus la fonction s'arrete, la direction est trouvee
            // sinon repeter la meme operation pour le successeur
            if(station.isTerminus(ligne)){
                System.out.println("here " + station.getNom());
                return station.getNom();
            }
            else
                direction = direction(metro,idStation,station1,line);
        }
        return direction;
    }

}
