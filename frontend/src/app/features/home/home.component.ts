import { Component } from '@angular/core';
import { FeedComponent } from "./feed/feed.component";
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [FeedComponent, RouterOutlet],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

}
