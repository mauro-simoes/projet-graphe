import { Component, OnInit } from '@angular/core';
import { Station } from './Station';
import { MetroService } from 'src/services/MetroService';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers:[MetroService]
})
export class AppComponent implements OnInit{

  constructor(private metroService:MetroService){}

  source:string = '';
  displaySourceSearch:boolean=false;
  destination:string = '';
  displayDestinationSearch:boolean=false;
  stations!: Station[];
  
  displayError:boolean = false;
  errorMessage:string = '';

  displayChemin:boolean = false;
  displaySearch:boolean = true;

  chemin:string = '';
  listePointsChemin!:string[];
  temps:number = 0;

  startTime!:Date;
  arrivalTime!:Date;

  ngOnInit(): void {
    this.metroService.getStations().then(data => {
      this.stations = data;
    });
  }

  addSource(value:string){
    this.source = value;
    this.displaySourceSearch=false;
  }

  addDestination(value:string){
    console.log("value " + value);
    this.destination = value;
    this.displayDestinationSearch=false;
  }

  onFocusSource(){
    this.displaySourceSearch = true;
  }

  onBlurSource(){
    this.displaySourceSearch = false;
    if(!this.stationDoesNotExist(this.source)){
      this.addSource(this.source);
    }
  }

  onFocusDest(){
    this.displayDestinationSearch = true;
  }

  onBlurDest(){
    this.displayDestinationSearch = false;
    if(!this.stationDoesNotExist(this.destination)){
      this.addDestination(this.destination);
    }
  }

  stationDoesNotExist(value:string):boolean{
    return this.stations.filter(station => station.nom.toLowerCase() == value.toLowerCase()).length != 1;
  }

  getChemin():void{

    if(this.source =='' || this.destination ==''){
      this.displayError = true;
      this.errorMessage = 'Veuillez choisir une station de départ et une destination.'
    }if(this.stationDoesNotExist(this.source) || this.stationDoesNotExist(this.destination)){
      this.displayError = true;
      this.errorMessage = 'Veuillez choisir des stations valides.'
    }else{
      this.metroService.calculChemin(this.source,this.destination).then(data => {
        if(data.error){
          this.displayError = true;
          this.errorMessage = data.error;
        }else{
          this.displaySearch = false;
          this.displayError = false;
          this.source = '';
          this.destination = '';

          this.chemin = data.cheminText;
          this.listePointsChemin = data.chemin;
          this.temps = Math.floor(data.temps / 60);
          
          this.startTime =  new Date();
          this.arrivalTime = new Date();
          this.arrivalTime.setMinutes(this.arrivalTime.getMinutes() + this.temps);
          this.displayChemin = true;
        }
        
      })
    }

    
  }


}
