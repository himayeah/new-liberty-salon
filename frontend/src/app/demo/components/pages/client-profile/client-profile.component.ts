import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ClientRegServiceService } from 'src/app/services/client-reg/client-reg-service.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';

@Component({
  selector: 'app-client-profile',
  templateUrl: './client-profile.component.html',
  styleUrls: ['./client-profile.component.scss']
})
export class ClientProfileComponent implements OnInit {
  clientData: any = null;
  clientId: string | null = null;
  photoUrl: string | ArrayBuffer | null = null;
  
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private clientRegService: ClientRegServiceService,
    private messageService: MessageServiceService
  ) {}

  ngOnInit(): void {
    this.clientId = this.route.snapshot.paramMap.get('id');
    if (this.clientId) {
      this.fetchClientDetails(this.clientId);
    }
  }

  fetchClientDetails(id: string): void {
    this.clientRegService.getDataById(id).subscribe({
      next: (res: any) => {
        this.clientData = res;
        this.photoUrl = res.photo || null;
      },
      error: (err: any) => {
        this.messageService.showError('Error loading client details: ' + err.message);
      }
    });
  }

  onPhotoUpload(event: any): void {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        this.photoUrl = reader.result;
        this.clientData.photo = this.photoUrl;
        
        this.clientRegService.editData(this.clientData.id, this.clientData).subscribe({
          next: () => this.messageService.showSuccess('Photo uploaded fully!'),
          error: (err: any) => this.messageService.showError('Database link failed: ' + err.message)
        });
      };
      reader.readAsDataURL(file);
    }
  }

  goBack(): void {
    this.router.navigate(['/pages/client-reg']);
  }
}
