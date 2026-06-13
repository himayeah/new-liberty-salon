import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-stylist-workspace',
  templateUrl: './stylist-workspace.component.html',
  styleUrls: ['./stylist-workspace.component.scss']
})
export class StylistWorkspaceComponent implements OnInit {
  authenticatedStylist: any = null;

  constructor() { }

  ngOnInit(): void {
  }

  onStylistAuthenticated(stylist: any) {
    this.authenticatedStylist = stylist;
  }
}
