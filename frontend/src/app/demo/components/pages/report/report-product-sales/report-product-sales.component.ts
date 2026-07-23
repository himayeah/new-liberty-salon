import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-report-product-sales',
  templateUrl: './report-product-sales.component.html',
  styleUrl: './report-product-sales.component.scss'
})
export class ReportProductSalesComponent implements OnInit {

  productSalesData: any[] = [];

  ngOnInit(): void {
    this.loadproductSalesData();
  }

  loadproductSalesData(): void {
  }


}
