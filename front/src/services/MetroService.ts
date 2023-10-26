import { HttpClient } from "@angular/common/http";
import {Injectable } from "@angular/core";
import { Station } from "src/app/Station";

@Injectable({providedIn:"root"})
export class MetroService{


    urlBase: string = "http://localhost:8080"

    constructor(private http: HttpClient ){}


    async getStations(): Promise<Station[]>{
        const data = fetch(this.urlBase + "/metro/get-all-stations");
        return (await data).json()
    }

    async calculChemin(source:string, destination:string): Promise<any>{
        const data = fetch(this.urlBase + "/metro/calcul-chemin",{
            method: "POST",
            mode: "cors",
            cache: "no-cache",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({"source":source,"destination":destination}), // body data type must match "Content-Type" header
          });
        return (await data).json()
    }


}