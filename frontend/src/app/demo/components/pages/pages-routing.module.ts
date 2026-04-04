import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Routes } from '@angular/router';
import { ClientRegComponent } from './client-reg/client-reg.component';
import { EmployeeRegComponent } from './employee-reg/employee-reg.component';
import { AppointmentScheduleComponent } from './appointment-schedule/appointment-schedule.component';
import { StylistTaskManagementComponent } from './stylist-task-management/stylist-task-management.component';
import { InventoryComponent } from './inventory/inventory.component';
import { ServiceCategoryComponent } from './service-category/service-category.component';
import { ServiceComponent } from './service/service.component';
import { EmployeeScheduleComponent } from './employee-schedule/employee-schedule.component';
import { EmployeeLeaveComponent } from './employee-leave/employee-leave.component';
import { ClientNotesComponent } from './client-notes/client-notes.component';
import { ProductCategoryComponent } from './product-category/product-category.component';
import { ProductComponent } from './product/product.component';
import { TaxComponent } from './tax/tax.component';
import { SupplierComponent } from './supplier/supplier.component';
import { PurchaseOrderComponent } from './purchase-order/purchase-order.component';
import { PurchaseOrderDetailComponent } from './purchase-order-detail/purchase-order-detail.component';
import { InvoiceComponent } from './invoice/invoice.component';
import { ClientProfileComponent } from './client-profile/client-profile.component';
import { EmployeeProfileComponent } from './employee-profile/employee-profile.component';
import { EmployeeAttendanceComponent } from './employee-attendance/employee-attendance.component';
import { ReportClientRegComponent } from './report/report-client-reg/report-client-reg.component';
import { ProductSalesReportComponent } from './report/product-sales-report/product-sales-report.component';
import { ReportAppointmentStatusComponent } from './report/report-appointment-status/report-appointment-status.component';
import { ReportProcurementComponent } from './report/report-procurement/report-procurement.component';



export const PagesRoutes: Routes = [
    //export array holds routing definitions, unless you add the routing inside Imports array, this won't have any meaning
    //{ path:'form-demo',component:FormDemoComponent },

]

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'crud', loadChildren: () => import('./crud/crud.module').then(m => m.CrudModule) }, // localhost:4200/pages/crud
        { path: 'empty', loadChildren: () => import('./empty/emptydemo.module').then(m => m.EmptyDemoModule) },
        { path: 'timeline', loadChildren: () => import('./timeline/timelinedemo.module').then(m => m.TimelineDemoModule) },
        { path: 'client-reg', component: ClientRegComponent }, // need to include the path here for form demo component
        { path: 'client-profile/:id', component: ClientProfileComponent },
        { path: 'employee-reg', component: EmployeeRegComponent },
        { path: 'employee-profile/:id', component: EmployeeProfileComponent },

        { path: 'client-notes', component: ClientNotesComponent },
        { path: 'appointment-schedule', component: AppointmentScheduleComponent },
        { path: 'employee-attendance', component: EmployeeAttendanceComponent },
        { path: 'stylist-task-management', component: StylistTaskManagementComponent },
        { path: 'inventory', component: InventoryComponent },
        { path: 'service-category', component: ServiceCategoryComponent },
        { path: 'service', component: ServiceComponent },
        { path: 'employee-schedule', component: EmployeeScheduleComponent },
        { path: 'employee-leave', component: EmployeeLeaveComponent },
        { path: 'product-category', component: ProductCategoryComponent },
        { path: 'product', component: ProductComponent },
        { path: 'tax', component: TaxComponent },
        { path: 'supplier', component: SupplierComponent },
        { path: 'purchase-order', component: PurchaseOrderComponent },
        { path: 'purchase-order-detail/:id', component: PurchaseOrderDetailComponent },
        { path: 'purchase-order-profile/:id', component: PurchaseOrderDetailComponent },
        { path: 'invoice', component: InvoiceComponent },
        { path: 'report-client-reg', component: ReportClientRegComponent },
        { path: 'report-product-sales', component: ProductSalesReportComponent },
        { path: 'report-appointment-status', component: ReportAppointmentStatusComponent },
        { path: 'report-procurement', component: ReportProcurementComponent },
        { path: '**', redirectTo: '/notfound' },

    ])],
    exports: [RouterModule]
})
export class PagesRoutingModule { }

