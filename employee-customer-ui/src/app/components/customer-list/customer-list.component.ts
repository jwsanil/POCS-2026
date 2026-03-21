import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../../services/customer.service';

@Component({
  selector: 'app-customer-list',
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.scss']
})
export class CustomerListComponent implements OnInit {
  customers: any[] = [];

  constructor(private customerService: CustomerService) { }

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers() {
    this.customerService.getCustomers().subscribe((data: any) => {
      console.log('Customer API Response:', data);

      this.customers = data.data.map((c: any) => ({
        id: c.id,
        customerid: c.customerid,
        firstname: c.firstname,
        lastname: c.lastname,
        email: c.email,
        phone: c.phone
      }));

      console.log('Flattened Customers:', this.customers);
    });
  }
}
