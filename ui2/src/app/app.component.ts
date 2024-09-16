import {Component, OnInit} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NgIf],
  templateUrl: './app.component.html'
})

export class AppComponent implements OnInit {
  cookieEnabled: boolean | undefined;

  ngOnInit(): void {
    this.cookieEnabled = navigator.cookieEnabled;

    if (!this.cookieEnabled) {
      document.cookie = "testcookie";
      this.cookieEnabled = document.cookie.indexOf('testcookie') !== -1;
    }

  }
}
