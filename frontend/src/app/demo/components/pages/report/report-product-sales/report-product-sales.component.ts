import { Component, OnInit } from '@angular/core';
import { ReportProductSalesService } from 'src/app/services/report-product-sales/report-product-sales.service';

@Component({
  selector: 'app-report-product-sales',
  templateUrl: './report-product-sales.component.html',
  styleUrl: './report-product-sales.component.scss'
})
export class ReportProductSalesComponent implements OnInit {

  productSalesData: any[] = [];

  constructor(private reportService: ReportProductSalesService) { }

  ngOnInit(): void {
    this.loadProductSalesData();
  }

  // product sales revenue table
  loadProductSalesData(): void {
    this.reportService.getProductSalesRevenue().subscribe({
      next: (data) => {
        console.log('product sales revenue:', data);
        this.productSalesData = data;
      },
      error: (err) => {
        console.error('Failed to load table data', err);
      }
    });
  }


}
