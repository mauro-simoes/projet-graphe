import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'path',
  templateUrl: './path.component.html',
  styleUrls: ['./path.component.css']
})
export class PathComponent implements OnInit{

    @Input()
    points!:string[];

    paths:any[] = [];

    ngOnInit(): void {
        console.log(this.points)
        let lastLine:number = parseInt(this.points[0].split(';')[1]);
        let source:string = this.points[0].split(';')[0];
        for(let i=0; i < this.points.length;i++){
            if(parseInt(this.points[i].split(';')[1]) != lastLine){
                this.paths.push({
                    "source":source,
                    "destination":this.points[i].split(';')[0],
                    "ligne":lastLine,
                    "direction":this.points[i-1].split(';')[2]
                });
                source = this.points[i].split(';')[0];
                lastLine = parseInt(this.points[i].split(';')[1]);
            }else if(i == this.points.length-1){
                this.paths.push({
                    "source":source,
                    "destination":this.points[i].split(';')[0],
                    "ligne":lastLine,
                    "direction":this.points[i].split(';')[2]
                });
            }
        }
        console.log(this.paths);
    }
}