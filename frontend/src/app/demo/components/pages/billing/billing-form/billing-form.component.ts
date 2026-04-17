import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { ServiceService } from 'src/app/services/service/service.service';
import { ProductServiceService } from 'src/app/services/product/product-service.service';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatDividerModule } from '@angular/material/divider';
import { BillingService } from 'src/app/services/billing/billing.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { TaxServiceService } from 'src/app/services/tax/tax-service.service';
import { MatRadioModule } from '@angular/material/radio';
import { AppointmentSchedulingServiceService } from 'src/app/services/appointment_scheduling/appointment-scheduling-service.service';

@Component({
  selector: 'app-billing-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatIconModule,
    MatDividerModule,
    MatTableModule,
    MatAutocompleteModule,
    MatRadioModule
  ],
  templateUrl: './billing-form.component.html',
  styleUrl: './billing-form.component.scss'
})
export class BillingFormComponent implements OnInit {
  billingForm: FormGroup;
  mode: 'add' | 'edit' | 'view' = 'add';
  submitted = false;
  isButtonDisabled = false;

  billingCategories = [
    { value: 'SALON_SERVICE', viewValue: 'Salon Service' },
    { value: 'PRODUCT_SALE', viewValue: 'Product Sale' },
    { value: 'OTHER', viewValue: 'Other' }
  ];

  clientTypes = [
    { value: 1, viewValue: 'Registered' },
    { value: 2, viewValue: 'Walk-in' }
  ];

  purchaseColumns: string[] = ['category', 'name', 'quantity', 'price', 'action'];
  purchaseDataSource = new MatTableDataSource<any>([]);

  allServices: any[] = [];
  allProducts: any[] = [];
  filteredOptions: { [key: number]: any[] } = {};
  activeTaxRate: number = 0;

  constructor(
    private fb: FormBuilder,
    private billingService: BillingService,
    private messageService: MessageServiceService,
    private serviceService: ServiceService,
    private productService: ProductServiceService,
    private taxService: TaxServiceService,
    private appointmentService: AppointmentSchedulingServiceService,
    public dialogRef: MatDialogRef<BillingFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.mode = data?.mode || 'add';
    this.billingForm = this.fb.group({
      clientName: ['', [Validators.required]],
      billingCategory: ['', [Validators.required]],
      clientType: [1, [Validators.required]],
      billingDate: [new Date(), [Validators.required]],
      paymentMethod: ['CASH', [Validators.required]],
      handedAmount: [0],
      purchases: this.fb.array([])
    });
  }

  ngOnInit(): void {
    if ((this.mode === 'edit' || this.mode === 'view') && this.data.billing) {
      this.billingForm.patchValue(this.data.billing);
      if (this.data.billing.billingDate) {
        this.billingForm.get('billingDate')?.setValue(new Date(this.data.billing.billingDate));
      }

      // Load purchases if they existed on the object
      if (this.data.billing.purchases && Array.isArray(this.data.billing.purchases)) {
        this.purchases.clear();
        this.data.billing.purchases.forEach((p: any) => this.addPurchase(p));
      }

      if (this.mode === 'view') {
        this.billingForm.disable();
        this.isButtonDisabled = true;
      }
      // update the modal id data is prefilled (data is being sending through appointment Schedule)
    } else if (this.mode === 'add' && this.data.preFill) {
      const patchVal = { ...this.data.preFill };
      if (patchVal.billingDate) {
        patchVal.billingDate = new Date(patchVal.billingDate);
      }
      this.billingForm.patchValue(patchVal);

      if (this.data.preFill.clientType === 1 && this.data.preFill.serviceId) {
        this.loadServiceAndAddPurchase(this.data.preFill.serviceId, this.data.preFill.serviceName);
      }
    }
    this.populateSelectData();
    this.fetchActiveTax();
    this.setupCalculationSubscriptions();
  }

  fetchActiveTax(): void {
    this.taxService.getData().subscribe((res: any) => {
      const taxes = Array.isArray(res) ? res : (res?.data || []);
      const activeTax = taxes.find((t: any) => t.isActive);
      if (activeTax) {
        this.activeTaxRate = activeTax.taxRate || 0;
      }
    });
  }

  setupCalculationSubscriptions(): void {
    this.billingForm.get('purchases')?.valueChanges.subscribe(() => {
      this.calculateBalance();
    });
    this.billingForm.get('paymentMethod')?.valueChanges.subscribe(() => {
      this.calculateBalance();
    });
    this.billingForm.get('handedAmount')?.valueChanges.subscribe(() => {
      this.calculateBalance();
    });
  }

  get totalAmount(): number {
    return this.purchases.controls.reduce((sum, control) => {
      const qty = control.get('quantity')?.value || 0;
      const price = control.get('price')?.value || 0;
      return sum + (qty * price);
    }, 0);
  }

  get grossAmount(): number {
    return this.totalAmount * (1 + (this.activeTaxRate / 100));
  }

  calculateBalance(): void {
    const handedAmount = this.billingForm.get('handedAmount')?.value || 0;
    const balance = handedAmount - this.grossAmount;
    // We don't necessarily need to store balance in form if we use a getter,
    // but the user asked for it to be "calculated".
  }

  get balanceAmount(): number {
    const handedAmount = this.billingForm.get('handedAmount')?.value || 0;
    return handedAmount - this.grossAmount;
  }

  populateSelectData(): void {
    this.serviceService.getData().subscribe((res: any) => {
      this.allServices = Array.isArray(res) ? res : (res?.data || []);
    });
    this.productService.getData().subscribe((res: any) => {
      this.allProducts = Array.isArray(res) ? res : (res?.data || []);
    });
  }

  get purchases() {
    return this.billingForm.get('purchases') as FormArray;
  }

  addPurchase(data?: any) {
    // If we have data from backend, 'name' is a string. 
    // We keep it as a string or object; getItemLabel handles both.
    const purchaseGroup = this.fb.group({
      id: [data?.id || null],
      category: [data?.category || 'SERVICE', Validators.required],
      name: [data?.name || '', Validators.required],
      quantity: [data?.quantity || 1, [Validators.required, Validators.min(1)]],
      price: [data?.price || 0, [Validators.required, Validators.min(0)]]
    });

    const index = this.purchases.length;
    this.filteredOptions[index] = [];

    // Listen for category changes to reset name and update options
    purchaseGroup.get('category')?.valueChanges.subscribe(() => {
      purchaseGroup.get('name')?.setValue('');
      this.updateFilteredOptions(index, purchaseGroup);
    });

    // Listen for name changes to filter options
    purchaseGroup.get('name')?.valueChanges.subscribe(val => {
      const filterValue = typeof val === 'string' ? val : (this.getItemLabel(val));
      this.updateFilteredOptions(index, purchaseGroup, filterValue);
    });

    this.purchases.push(purchaseGroup);
    this.purchaseDataSource.data = [...this.purchases.controls];
    this.billingForm.updateValueAndValidity();

    // Initial update
    this.updateFilteredOptions(index, purchaseGroup, data?.name || '');
  }

  updateFilteredOptions(index: number, group: FormGroup, filterValue: string = '') {
    const category = group.get('category')?.value;
    const items = category === 'SERVICE' ? this.allServices : this.allProducts;
    const filter = filterValue ? (typeof filterValue === 'string' ? filterValue.toLowerCase() : '') : '';

    this.filteredOptions[index] = items.filter(item => {
      const name = item.name || item.serviceName || item.productName || '';
      return name.toLowerCase().includes(filter);
    });
  }

  onOptionSelected(event: any, index: number) {
    const selectedItem = event.option.value;
    const group = this.purchases.at(index) as FormGroup;

    // Check all common price fields used in the system for services and products
    // products use sellingPrice, services typically use price or servicePrice
    const price = selectedItem.sellingPrice !== undefined ? selectedItem.sellingPrice : 
                  (selectedItem.price !== undefined ? selectedItem.price : 
                  (selectedItem.servicePrice !== undefined ? selectedItem.servicePrice : 0));

    group.patchValue({
      name: selectedItem, // Save the whole object; getItemLabel and submission logic handle it
      price: price
    });
  }

  getItemLabel(item: any): string {
    if (!item) return '';
    if (typeof item === 'string') return item;
    return item.name || item.serviceName || item.productName || '';
  }

  removePurchase(index: number) {
    this.purchases.removeAt(index);
    // Re-index filteredOptions
    const newFilteredOptions: { [key: number]: any[] } = {};
    for (let i = 0; i < this.purchases.length; i++) {
      newFilteredOptions[i] = this.filteredOptions[i >= index ? i + 1 : i];
    }
    this.filteredOptions = newFilteredOptions;

    this.purchaseDataSource.data = [...this.purchases.controls];
    this.billingForm.updateValueAndValidity();
  }

  loadServiceAndAddPurchase(serviceId: number, serviceName: string) {
    this.serviceService.getData().subscribe((response: any) => {
      const services = Array.isArray(response) ? response : (response?.data || []);
      const service = services.find((s: any) => s.id === serviceId);
      const price = service ? service.price || service.servicePrice || 0 : 0;
      this.addPurchase({
        category: 'SERVICE',
        name: serviceName,
        quantity: 1,
        price: price
      });
    });
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.billingForm.invalid) return;

    this.isButtonDisabled = true;
    const formValue = { 
      ...this.billingForm.value,
      paymentStatus: 'Completed' // Automatically set status to Completed as requested
    };

    // If there's an associated appointment, update its status to Completed as well
    const appointmentId = this.data.preFill?.appointmentId || this.data.billing?.appointmentId;
    const appointmentObj = this.data.preFill?.appointment || this.data.billing?.appointment;

    if (appointmentId && appointmentObj) {
      const updatedAppointment = { 
        ...appointmentObj, 
        appointmentStatus: 'Completed' 
      };
      this.appointmentService.editData(appointmentId, updatedAppointment).subscribe({
        next: () => console.log('Appointment status updated to Completed'),
        error: (err) => console.error('Error updating appointment status:', err)
      });
    }

    // Convert 'name' from object/string to string for backend
    if (formValue.purchases && Array.isArray(formValue.purchases)) {
      formValue.purchases = formValue.purchases.map((p: any) => ({
        ...p,
        name: typeof p.name === 'string' ? p.name : this.getItemLabel(p.name)
      }));
    }

    // Convert date to timestamp for backend
    if (formValue.billingDate instanceof Date) {
      formValue.billingDate = formValue.billingDate.getTime();
    }

    if (this.mode === 'add') {
      this.billingService.serviceCall(formValue).subscribe({
        next: (response) => {
          this.messageService.showSuccess('Billing added successfully!');
          this.dialogRef.close(response);
        },
        error: (error) => {
          this.messageService.showError('Error adding billing: ' + (error.message || 'Unknown error'));
          this.isButtonDisabled = false;
        }
      });
    } else {
      this.billingService.editData(this.data.billing.id, formValue).subscribe({
        next: (response) => {
          this.messageService.showSuccess('Billing updated successfully!');
          this.dialogRef.close(response);
        },
        error: (error) => {
          this.messageService.showError('Error updating billing: ' + (error.message || 'Unknown error'));
          this.isButtonDisabled = false;
        }
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
