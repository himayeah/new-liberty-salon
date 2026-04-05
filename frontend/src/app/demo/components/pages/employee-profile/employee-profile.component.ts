import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EmployeeRegServicesService } from 'src/app/services/employee-reg/employee-reg-services.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';

@Component({
    selector: 'app-employee-profile',
    templateUrl: './employee-profile.component.html',
    styleUrls: ['./employee-profile.component.scss']
})
export class EmployeeProfileComponent implements OnInit {
    employeeId: string | null = null;
    employee: any = null;
    loading: boolean = true;
    profileImage: string | null = null;

    constructor(
        private route: ActivatedRoute,
        private employeeRegService: EmployeeRegServicesService,
        private messageService: MessageServiceService
    ) { }

    ngOnInit(): void {
        this.route.paramMap.subscribe(params => {
            this.employeeId = params.get('id');
            if (this.employeeId) {
                this.fetchEmployeeDetails(this.employeeId);
            }
        });
    }

    fetchEmployeeDetails(id: string): void {
        this.loading = true;
        this.employeeRegService.getDataById(id).subscribe({
            next: (data: any) => {
                console.log('Employee data received:', data);
                this.employee = data;
                this.profileImage = data.profileImage || 'assets/demo/images/avatar/profile.jpg';
                this.loading = false;
            },
            error: (error: any) => {
                console.error('Error fetching employee details:', error);
                this.messageService.showError('Error fetching employee details: ' + (error.message || 'Unknown error'));
                this.loading = false;
            }
        });
    }

    onFileSelected(event: any): void {
        const file: File = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (e: any) => {
                this.profileImage = e.target.result;
                this.employee.profileImage = this.profileImage;
                
                this.employeeRegService.editData(this.employee.id, this.employee).subscribe({
                    next: () => this.messageService.showSuccess('Profile photo saved successfully!'),
                    error: (err: any) => this.messageService.showError('Error saving photo: ' + err.message)
                });
            };
            reader.readAsDataURL(file);
        }
    }

    triggerFileUpload(): void {
        const fileInput = document.getElementById('profilePhotoInput') as HTMLInputElement;
        fileInput.click();
    }
}
