import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PagesRoutingModule } from './pages-routing.module';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatIconModule } from '@angular/material/icon';
import { MatSortModule } from '@angular/material/sort';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDialogModule } from '@angular/material/dialog';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { MatTabsModule } from '@angular/material/tabs';
import { TableModule } from 'primeng/table';
import { ChartModule } from 'primeng/chart';

import { ClientRegComponent } from './client-reg/client-reg.component';
import { ClientFormComponent } from './client-reg/client-form/client-form.component';
import { EmployeeRegComponent } from './employee-reg/employee-reg.component';
import { AppointmentScheduleComponent } from './appointment-schedule/appointment-schedule.component';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { InventoryComponent } from './inventory/inventory.component';
import { InventoryFormComponent } from './inventory/inventory-form/inventory-form.component';
import { ServiceCategoryComponent } from './service-category/service-category.component';
import { ServiceComponent } from './service/service.component';
import { EmployeeScheduleComponent } from './employee-schedule/employee-schedule.component';
import { EmployeeLeaveComponent } from './employee-leave/employee-leave.component';
import { ClientNotesComponent } from './client-notes/client-notes.component';
import { ProductCategoryComponent } from './product-category/product-category.component';
import { ProductComponent } from './product/product.component';
import { PurchaseOrderComponent } from './purchase-order/purchase-order.component';
import { PurchaseOrderDetailComponent } from './purchase-order-detail/purchase-order-detail.component';
import { InvoiceComponent } from './invoice/invoice.component';
import { InvoiceItemModalComponent } from './invoice/invoice-item-modal/invoice-item-modal.component';
import { InvoicePaymentModalComponent } from './invoice/invoice-payment-modal/invoice-payment-modal.component';
import { ClientProfileComponent } from './client-profile/client-profile.component';
import { ClientAdditionalNotesComponent } from './client-profile/client-additional-notes/client-additional-notes.component';
import { EmployeeFormComponent } from './employee-reg/employee-form/employee-form.component';
import { EmployeeProfileComponent } from './employee-profile/employee-profile.component';
import { EmployeeLeaveFormComponent } from './employee-leave/employee-leave-form/employee-leave-form.component';
import { EmployeeScheduleFormComponent } from './employee-schedule/employee-schedule-form/employee-schedule-form.component';
import { AppointmentFormComponent } from './appointment-schedule/appointment-form/appointment-form.component';
import { EmployeeAttendanceComponent } from './employee-attendance/employee-attendance.component';
import { EmployeeAttendanceFormComponent } from './employee-attendance/employee-attendance-form/employee-attendance-form.component';
import { ServiceCategoryFormComponent } from './service-category/service-category-form/service-category-form.component';
import { ServiceFormComponent } from './service/service-form/service-form.component';
import { ProductCategoryFormComponent } from './product-category/product-category-form/product-category-form.component';
import { ProductFormComponent } from './product/product-form/product-form.component';
import { PurchaseOrderDetailFormComponent } from './purchase-order-detail/purchase-order-detail-form/purchase-order-detail-form.component';
import { GrnFormComponent } from './grn/grn-form/grn-form.component';
import { ReportClientRegComponent } from './report/report-client-reg/report-client-reg.component';
import { ProductSalesReportComponent } from './report/product-sales-report/product-sales-report.component';
import { ReportAppointmentStatusComponent } from './report/report-appointment-status/report-appointment-status.component';

@NgModule({
  declarations: [
    ClientRegComponent,
    EmployeeRegComponent,
    AppointmentScheduleComponent,
    InventoryComponent,
    InventoryFormComponent,
    ServiceCategoryComponent,
    ServiceComponent,
    EmployeeScheduleComponent,
    EmployeeLeaveComponent,
    ClientNotesComponent,
    ProductCategoryComponent,
    ProductComponent,
    PurchaseOrderDetailComponent,
    InvoiceComponent,
    InvoiceItemModalComponent,
    InvoicePaymentModalComponent,
    ClientFormComponent,
    ClientProfileComponent,
    ClientAdditionalNotesComponent,
    EmployeeFormComponent,
    EmployeeProfileComponent,
    EmployeeLeaveFormComponent,
    EmployeeScheduleFormComponent,
    AppointmentFormComponent,
    EmployeeAttendanceComponent,
    EmployeeAttendanceFormComponent,
    ServiceCategoryFormComponent,
    ServiceFormComponent,
    ProductCategoryFormComponent,
    ProductFormComponent,
    PurchaseOrderDetailFormComponent,
    ReportClientRegComponent,
    ProductSalesReportComponent,
    ReportAppointmentStatusComponent
  ],

  imports: [
    CommonModule,
    PagesRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatTableModule,
    MatPaginatorModule,
    MatIconModule,
    MatSortModule,
    MatTooltipModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSelectModule,
    MatSlideToggleModule,
    MatCheckboxModule,
    MatDialogModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatDividerModule,
    MatTabsModule,
    TableModule,
    ChartModule,
    GrnFormComponent

  ]
})
export class PagesModule { } // Trigger re-compilation