import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {ReactiveFormsModule} from "@angular/forms";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-info-page',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf, RouterLink],
  templateUrl: './info-page.component.html',
  styleUrl: '../auth.component.scss'
})
export class InfoPageComponent implements OnInit {
  msg: string | undefined;

  constructor(private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.msg = params["msg"];
    });
  }

  toIndex() {
    window.close();

    const url = this.router.serializeUrl(
      this.router.createUrlTree(['index'])
    );

    window.open(url, '_blank');
  }
}
