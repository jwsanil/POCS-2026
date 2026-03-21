import { Component, OnInit } from '@angular/core';
import { EmployeeService } from '../../services/employee.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-employee-form',
  templateUrl: './employee-form.component.html',
  styleUrls: ['./employee-form.component.scss']
})
export class EmployeeFormComponent implements OnInit {

  employee = {
    employeeid: 0,
    firstname: '',
    lastname: '',
    position: '',
    hiredate: '',
    salary: 0
  };

  constructor(
    private employeeService: EmployeeService,
    private router: Router
  ) { }

  ngOnInit(): void {}

  saveEmployee() {
    this.employeeService.createEmployee(this.employee).subscribe({
      next: (res) => {
        console.log('Employee saved:', res);
        alert('Employee saved successfully');

        // Redirect to customer form
        this.router.navigate(['/customers/create']);
      },
      error: (err) => {
        console.error('Error saving employee:', err);
      }
    });
  }
}
