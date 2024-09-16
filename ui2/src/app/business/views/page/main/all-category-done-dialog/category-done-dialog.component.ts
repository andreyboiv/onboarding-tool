import { Component } from '@angular/core';
import {MatDialogActions, MatDialogContent, MatDialogRef} from "@angular/material/dialog";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-confirmation-dialog',
  standalone: true,
  imports: [
    MatDialogContent,
    MatButton,
    MatDialogActions
  ],
  templateUrl: './category-done-dialog.component.html',
  styleUrl: './category-done-dialog.component.css'
})
export class CategoryDoneDialogComponent {
  constructor(public dialogRef: MatDialogRef<CategoryDoneDialogComponent>) {
  }
}
