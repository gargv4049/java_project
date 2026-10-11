# Member 5 — Admin & Analytics Module

## Overview
This module provides administrative features for the Campus Lost & Found Portal, including dashboard statistics, user management, reports, notifications, and audit log monitoring.

## Controllers
- `AdminServlet.java` — Handles admin dashboard and related requests.
- `NotificationServlet.java` — Handles notification-related requests.
- `ReportServlet.java` — Handles report-related requests.

## Data Access Objects (DAO)
- `AdminDAO.java` and `AdminDAOImpl.java` — Handle admin data access.
- `ReportDAO.java` and `ReportDAOImpl.java` — Handle report data access.

## Services
- `AdminService.java` — Supports administrative operations.
- `NotificationService.java` — Supports notification operations.
- `ReportService.java` — Supports report operations.

## Utility
- `PdfExporter.java` — Provides PDF export functionality.

## Admin Pages
The web application includes JSP pages for:
- Dashboard
- User management
- Reports
- Analytics
- Unclaimed item disposal review
- Notifications

## Technology Stack
- Java
- Jakarta Servlets and JSP
- JDBC and MySQL
- Apache Maven
- Apache Tomcat
- Bootstrap

## Verification
The project packaging command `mvn clean package` completed successfully in the development environment. Individual admin features should be tested before claiming full functional verification.