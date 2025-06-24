import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CaptionsService } from './service/captions.service';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from './service/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {

  isDragging = false;
  selectedFile: File | null = null;
  previewUrl: string | ArrayBuffer | null = null;
  captions: string[] = [];
  errorMessage: string | null = null;
  isLoading = false;

  constructor(private captionsService: CaptionsService, private authService: AuthService) { }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) {
      this.previewUrl = null;
      this.selectedFile = null;
      return;
    }

    this.handleFile(input.files[0]);
  }

  onSubmit(event: Event): void {
    event.preventDefault();

    if (!this.selectedFile) return;

    this.errorMessage = null;
    this.captions = [];
    this.isLoading = true;

    const formData = new FormData();
    formData.append('image', this.selectedFile);

    this.authService.getTokens({ username: 'ashu123', password: 'pass123' }).subscribe({
      next: () => {
        this.captionsService.getCaptions(formData)
          .subscribe({
            next: (captions) => {
              this.captions = captions;
              this.isLoading = false;
            },
            error: (error: HttpErrorResponse) => {
              console.error('Error uploading image:', error);
              this.errorMessage = error.message || 'Unknown error';
              this.isLoading = false;
            }
          })
      },
      error: (error) => {
        this.errorMessage = 'Authentication failed: ' + error.message;
        this.isLoading = false;
      }
    });
  }

  onDragOver(event: DragEvent) {
    event.preventDefault();
    this.isDragging = true;
  }

  onDragLeave(event: DragEvent) {
    event.preventDefault();
    this.isDragging = false;
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    this.isDragging = false;
    if (event.dataTransfer?.files.length) {
      const file = event.dataTransfer.files[0];
      this.handleFile(file);
    }
  }

  handleFile(file: File) {
    this.selectedFile = file;

    // Preview
    const reader = new FileReader();
    reader.onload = () => {
      this.previewUrl = reader.result as string;
    };
    reader.readAsDataURL(file);
  }

}
