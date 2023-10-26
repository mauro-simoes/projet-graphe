package com.graph_proj.graph.services.graphe;

import com.graph_proj.graph.models.graphe.Arc;
import com.graph_proj.graph.models.graphe.IGraphe;
import com.graph_proj.graph.models.metro.Metro;
import com.graph_proj.graph.models.metro.Station;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class GrapheInitializr {

    private GrapheInitializr(){}

    public static void importer(Metro metro,String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner sc = new Scanner(file);
        String line;
        IGraphe g;
        if (! sc.hasNextLine()) {
            sc.close();
            throw new IllegalArgumentException("Pas de graphe dans "+ file);
        }
        g = metro.getPlan();
        Station stationPrecedente = new Station("");
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            if(line.charAt(0) == 'V'){

                String[] infosStation = line.replaceFirst("V","").split(";");
                int idSommet = Integer.parseInt(infosStation[0].split(" ")[1]);
                String nomStation = infosStation[0].replace(String.valueOf(idSommet),"")
                        .replace(String.valueOf(0),"").trim();
                String numLigne = infosStation[1].trim();
                boolean terminus = Boolean.parseBoolean(infosStation[2].split(" ")[0].trim().toLowerCase());
                g.addArc(idSommet,null);

                if (stationPrecedente.getNom().equals(nomStation)){
                    stationPrecedente.addId(idSommet);
                    stationPrecedente.addLigne(numLigne,terminus);
                }else{
                    Station station = new Station(nomStation);
                    station.addId(idSommet);
                    station.addLigne(numLigne,terminus);
                    stationPrecedente = station;
                }
                metro.addStation(stationPrecedente);

            }else{
                String[] infosArc = line.split(" ");
                g.addArc(Integer.parseInt(infosArc[1]),new Arc(Integer.parseInt(infosArc[2]),Integer.parseInt(infosArc[3])));
            }
        }

        //completeGraph(metro,file);

        sc.close();
    }

    private static void completeGraph(Metro metro,File file) {
        IGraphe graphe = metro.getPlan();
        for (Integer sommet : graphe.getSommets()){
            List<Integer> predecesseurs = graphe.getPredecesseurs(sommet);
            List<Integer> successeurs = graphe.getSuccesseurs(sommet);
            if (predecesseurs.size() != successeurs.size()){
                for (Integer predecesseur : predecesseurs){
                    if (!successeurs.contains(predecesseur)){
                        int valuation = graphe.getValuation(predecesseur,sommet);
                        graphe.addArc(sommet,new Arc(predecesseur,valuation));
                        try{
                            appendArcToFile(file,sommet,predecesseur,valuation);
                        }catch (IOException e){
                            System.out.println("Failed to append arc to file");
                        }
                    }
                }
                for (Integer successeur : successeurs){
                    if (!predecesseurs.contains(successeur)){
                        int valuation = graphe.getValuation(sommet,successeur);
                        graphe.addArc(successeur,new Arc(sommet,valuation));
                        try{
                            appendArcToFile(file,successeur,sommet,valuation);
                        }catch (IOException e){
                            System.out.println("Failed to append arc to file");
                        }
                    }
                }
            }
        }
    }

    private static void appendArcToFile(File file,int source, int destination,int valuation) throws IOException {
        FileWriter fw = new FileWriter(file.getName(), true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.newLine();
        bw.write("E " + source + " " + destination + " " + valuation);
        bw.close();
    }

}
