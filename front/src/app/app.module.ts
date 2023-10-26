import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { MetroService } from 'src/services/MetroService';
import { FormsModule } from '@angular/forms';
import { PathComponent } from './path/path.component';

@NgModule({
  declarations: [
    AppComponent,
    PathComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [MetroService],
  bootstrap: [AppComponent]
})
export class AppModule { }
