package com.graph_proj.graph.services.pcc;

import com.graph_proj.graph.models.graphe.IGraphe;
import com.graph_proj.graph.models.pcc.Chemin;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
public class PCCBellman{

    private final static int INFINI = Integer.MAX_VALUE;

    private PCCBellman(){}

    public static Chemin pcc(IGraphe graphe, int source, int destination){
        Chemin chemin = new Chemin(graphe,source,destination);
        if(source == destination)
            return chemin;
        return calcul(graphe,source,destination);
    }

    private static Chemin calcul(IGraphe graphe,int source, int destination) {
        int[] temps = new int[graphe.getNbSommets()];
        Arrays.fill(temps, INFINI);
        temps[source] = 0;

        Integer[] predecesseurs =  new Integer[graphe.getNbSommets()];
        Arrays.fill(predecesseurs, null);

        Chemin chemin = new Chemin(graphe,source,destination);

        List<Integer> sommets = graphe.getSommets();
        for(int i = 1; i < graphe.getNbSommets(); i++){
            for (int sommet : sommets)  {
                if(temps[sommet] == INFINI)
                    continue;

                List<Integer> successeurs =  graphe.getSuccesseurs(sommet);
                for (int successeur : successeurs) {
                    if (temps[successeur] > (temps[sommet] + graphe.getValuation(sommet,successeur))) {
                        temps[successeur] = temps[sommet] + graphe.getValuation(sommet,successeur);
                        predecesseurs[successeur] = sommet;
                    }
                }

            }
        }

        int sommet = predecesseurs[destination];
        while(sommet!=source){
            chemin.ajouterSommet(sommet);
            sommet=predecesseurs[sommet];
        }
        return chemin;
    }

}
