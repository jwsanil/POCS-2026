import { Component, OnInit } from '@angular/core';
import { EmployeeService } from '../../services/employee.service';

@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.scss']
})
export class EmployeeListComponent implements OnInit {
  employees: any[] = [];

  constructor(private employeeService: EmployeeService) { }

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees() {
    this.employeeService.getEmployees().subscribe({
      next: (data: any) => {
        console.log('API Response:', data); // Raw response from Strapi

        // Flatten each employee object for easy HTML access
        this.employees = data.data.map((e: any) => ({
          id: e.id,
          documentId: e.documentId,
          employeeid: e.employeeid,
          firstname: e.firstname,
          lastname: e.lastname,
          position: e.position,
          hiredate: e.hiredate,
          salary: e.salary
        }));

        console.log('Flattened Employees:', this.employees); // Check flattened data
      },
      error: (err) => {
        console.error('Error fetching employees:', err);
      }
    });
  }
}
