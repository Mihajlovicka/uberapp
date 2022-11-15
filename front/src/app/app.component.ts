import { Component } from '@angular/core';
import { AppService } from './app.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'front';

  poruka: string = ""; 

  constructor(private appService: AppService) { 

  }

  ngOnInit(): void {
  
   
  }



}
