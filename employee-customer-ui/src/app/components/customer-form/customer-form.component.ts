import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../../services/customer.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-customer-form',
  templateUrl: './customer-form.component.html',
  styleUrls: ['./customer-form.component.scss']
})
export class CustomerFormComponent implements OnInit {

  customer = {
    customerid: 0,
    firstname: '',
    lastname: '',
    email: '',
    phone: ''
  };

  constructor(
    private customerService: CustomerService,
    private router: Router
  ) { }

  ngOnInit(): void {}

  saveCustomer() {
    this.customerService.createCustomer(this.customer).subscribe({
      next: (res) => {
        console.log('Customer saved:', res);
        alert('Customer saved successfully');

        this.router.navigate(['/customers']);
      },
      error: (err) => {
        console.error('Error saving customer:', err);
      }
    });
  }
}
