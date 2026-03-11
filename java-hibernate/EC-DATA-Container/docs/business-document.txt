# Customer–Employee Relationship (business-level)

Description:
A business bundle links an Employee and a Customer into a single business concept. At the business layer the pair is represented either as a
- BusinessRelationship (contains a relationshipType)
- BusinessContract (contains a contractId)

High-level diagram (ASCII):

    +-----------+      interacts with      +-----------+
    | Employee  | -----------------------> | Customer  |
    +-----------+                         +-----------+
           \                                 /
            \                               /
             \                             /
              v                           v
        +---------------------------------------+
        |           Business Bundle             |
        |  - BusinessRelationship (relationshipType)
        |  - BusinessContract     (contractId)  |
        +---------------------------------------+

(End of addition)

Concept:
A lightweight business abstraction that groups an employee and a customer so higher-level rules and presentation logic can operate without depending on persistence entities. Bundles expose only the data needed for decisions and summaries.

Summary & Business Intent:
- Summary: `RecordBundle.summary()` provides a short, human-readable one-line description combining the employee's and customer's names and the bundle-specific detail (relationship type or contract id). It's intended for logs, simple UIs, and quick inspections.

- Business intent: Bundles represent the business-level relationship between an Employee and a Customer without exposing persistence details. They exist so business rules, reporting, and presentation logic operate on a stable, focused data shape (the two records plus minimal metadata).

- Logic note: Business logic consumes the bundled records (for example `employee.salary()` and `customer.status()`) to decide which bundle to create or which downstream action to take — e.g., create a `BusinessContract` when salary exceeds a threshold, otherwise create a `BusinessRelationship`. The `summary()` method is then used for display/logging after the decision.

Logic note (expanded):
Business logic operates on the bundled records rather than on the raw persistence entities. This keeps rules simple, testable, and independent of Hibernate specifics. Common inputs to these rules are fields exposed by the records such as `employee.salary()`, `employee.title()`, or `customer.status()`.

Decision logic (concise):
A small, focused component maps an `EmployeeRecord` and `CustomerRecord` to a `RecordBundle`. This keeps rules testable and decoupled from persistence and presentation.

High-level design (ASCII):

    [EmployeeRecord]   [CustomerRecord]
           \               /
            \             /
             v           v
            +---------------------+
            |   BundleDecider     |  (implements rule, e.g., salary threshold)
            +---------------------+
                     |
           +---------+---------+
           |                   |
    [BusinessContract]   [BusinessRelationship]
           |                   |
        (contractId)       (relationshipType)
