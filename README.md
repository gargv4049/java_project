# Campus Lost & Found Management System

> **Enterprise-Grade Java Web Application for University & College Campuses**  
> Built with Java Servlets (Jakarta EE), JSP, JDBC, PostgreSQL, Bootstrap 5, Apache PDFBox, and ZXing.
> Directly importable and runnable in **IntelliJ IDEA**, **JDK 25**, and **Apache Tomcat 11**.

---

## 📌 Project Overview

The **Campus Lost & Found Management System** is a complete, production-grade web application tailored for college campuses. It bridges the gap between students, professors, and campus administration to systematically recover, identify, verify, and return lost possessions.

### 🌟 Key Highlights & Capabilities
- **Strict Jakarta EE 11 / Servlet 6.1 Compatibility**: Optimized for Apache Tomcat 11 and JDK 25 without any legacy `javax.*` dependencies.
- **Pure Java MVC Architecture**: No Spring Boot, Spring MVC, or Hibernate abstractions. Built with clean Controller &rarr; Service &rarr; DAO &rarr; Database separation of concerns.
- **Intelligent Algorithmic Matching Engine**: Combines custom **Levenshtein Text Edit Distance** (40%), **Haversine Geospatial Distance Formula** (30%), **Temporal Proximity** (20%), and **Category Correlation** (10%) to discover high-confidence matches automatically.
- **Two-Factor Claim Handover Protocol**: Employs cryptographically secure 6-digit time-based OTPs and QR code tokens to verify claimants before physical handover.
- **ACID Transactional Handover**: Item status transitions (`ACTIVE` &rarr; `CLAIMED` &rarr; `RETURNED`) execute inside atomic database transactions (`setAutoCommit(false)` with rollback protection).
- **Executive Administration & PDF Reporting**: Dynamic analytics dashboard with live Chart.js graphs, user role management, content moderation flags, unclaimed property disposal tracker, and automated downloadable PDF reports via Apache PDFBox.
- **Security Controls**: BCrypt password hashing (cost factor 12), parameterized `PreparedStatement` queries, session invalidation on logout, role/ownership checks, and allowlisted file-upload types.
- **Database Authentication**: Accounts register with a unique username or email identifier and BCrypt password; sign-in and password changes use PostgreSQL only. No external sign-in or email verification is required.

---

## 👥 Team Members

| S.No. | Name | Enrollment No. | Email | Contact | Module |
|---|---|---|---|---|---|
| 1 | Renuka Nagar | 24E1ARCSF30P132 | renukanagar.2007@gmail.com | 8005780667 | Authentication & User Accounts |
| 2 | Saloni Kumari | 24E1ARCSF10P144 | salonikri5617@gmail.com | 9661810983 | Item Inventory & Image Uploads |
| 3 | Simple Agrawal | 24E1ARCSF40P160 | Simpleagarwal3303@gmail.com | 7296973303 | Search Engine & Match Algorithms |
| 4 | Teena Soni | 24E1ARCSF30P175 | soniteena427@gmail.com | 9772912956 | Claims, OTP & Transactional Handover |
| 5 | Vivek Garg | 24E1ARCSM40P186 | gargv4049@gmail.com | 8005884935 | Dashboard, Reports & Analytics |

---

## 🗓️ 12-Week Project Work Plan

This is a suggested week-by-week development and academic reporting plan for the features in this repository. It describes project work areas, not dated evidence that each activity was completed in a particular week. Adjust the sequence and completion status to match the actual course schedule and project records.

| Week | Planned work | Deliverable / review point |
|---|---|---|
| 1 | Define the campus lost-and-found problem, users, goals, scope, and functional requirements. | Requirements and initial use cases for students, faculty, and administrators. |
| 2 | Design the application architecture, database entities, relationships, and development environment. | MVC/service/DAO design, PostgreSQL schema review, and a reproducible local build setup. |
| 3 | Implement account registration, login/logout, profile management, password hashing, and protected-route behavior. | Database-backed authentication and role-aware sessions. |
| 4 | Implement lost/found item reporting, editing, ownership checks, image uploads, and the item gallery. | Item lifecycle flows with validated metadata and safe image storage. |
| 5 | Add keyword, category, and location search and gallery filtering. | Search and filter flows returning relevant active listings. |
| 6 | Implement and review item matching using text, location, date, and category scores. | Match results with the documented component weights and score breakdown. |
| 7 | Implement ownership claims, claim review, and claimant/owner views. | Claim submission and review workflow with status validation. |
| 8 | Add OTP/QR verification and transactional item handover. | Verified handover flow that updates claim and item state consistently. |
| 9 | Implement administration workflows for user management, notifications, and community reports. | Role-protected moderation and administration pages. |
| 10 | Add administrative analytics, PDF reporting, audit history, and aged-item disposal review. | Operational dashboard and export/review features. |
| 11 | Integrate end-to-end flows; test validation, authorization, failure paths, uploads, database operations, and browser behavior. | Test results and a prioritized list of issues resolved or still open. |
| 12 | Prepare deployment, user/setup documentation, final demonstration, and maintenance handover. | Packaged WAR, deployment instructions, final review, and future-maintenance notes. |

Use the **Comprehensive Testing Checklist** section to record the checks actually run. Mark an item complete only after verifying it; do not treat this proposed timeline as a test report.

---

## 🛠️ Technology Stack

| Layer | Technologies |
|---|---|
| **Backend Core** | Java 25, Jakarta Servlet 6.1, Jakarta JSP 4.0, Jakarta JSTL 3.0 |
| **Persistence** | JDBC, PostgreSQL JDBC Driver 42.7+, MySQL Connector/J 8.4+, PreparedStatement SQL |
| **Databases** | **PostgreSQL 14+** (default, local and cloud) & **MySQL 8.0+ / MariaDB 10.5+** (supported) |
| **Containerization** | Docker, Docker Compose (Multi-stage build, Tomcat 11 + PostgreSQL 16) |
| **Security & Hashing** | jBCrypt 0.4 (Blowfish salt rounds = 12), Secure HTTP Session Management |
| **Algorithms** | Custom Levenshtein Distance (Text), Custom Haversine Formula (GPS Proximity) |
| **Third-Party Libraries** | ZXing 3.5.3 (QR Codes), Apache PDFBox 3.0.4 (PDF Engine), Gson 2.11.0, JUnit 5 |
| **Frontend UI** | JSP, HTML5, CSS3, JavaScript (ES6), Bootstrap 5.3.3, Bootstrap Icons, Chart.js 4.4 |
| **Build Tool & Packaging** | Apache Maven 3.10+, WAR (Web Application Archive) |
| **Runtime Server** | Apache Tomcat 11.0.x (Jakarta EE 10/11) |

---

## 📂 Project Architecture & Directory Structure

```
java_project-main/
│
├── Dockerfile                                   # Multi-stage production Docker image (Tomcat 11)
├── docker-compose.yml                           # Full-stack Docker orchestration (PostgreSQL + Web app)
├── pom.xml                                      # Maven configuration (Jakarta EE, Tomcat 11, JDK 25)
├── README.md                                    # Comprehensive system documentation
├── .env.example                                 # Sample environment variables configuration
├── .gitignore                                   # Version control exclusions
│
├── database/
│   ├── lost_found.sql                           # MySQL schema & development seed data (destructive reset)
│   ├── lost_found_mysql.sql                     # Dedicated MySQL 8.0+ / MariaDB setup script
│   └── lost_found_postgres.sql                  # PostgreSQL 14+ schema & development seed data (destructive reset)
│
├── src/
│   ├── test/                                    # Automated JUnit 5 Unit Tests (20 tests passed)
│   │   └── java/com/lostfound/                  # Tests for algorithms, auth, validation, config
│   │
│   └── main/
        ├── resources/
        │   └── db.properties                    # Non-secret defaults; credentials come from environment
        │
        ├── java/
        │   └── com/
        │       └── lostfound/
        │           ├── config/
        │           │   ├── AppConfig.java       # Properties reader and environment manager
        │           │   └── DatabaseConnection.java # JDBC connection factory & resource cleaner
        │           │
        │           ├── model/
        │           │   ├── User.java            # User entity POJO (Role, status, details)
        │           │   ├── Item.java            # Item entity POJO (Type, location, GPS, photo)
        │           │   ├── Category.java        # Item category POJO
        │           │   ├── Claim.java           # Ownership claim POJO (Status, OTP, QR token)
        │           │   ├── HandoverRecord.java  # Physical handover audit POJO
        │           │   ├── Notification.java    # User notification alert POJO
        │           │   ├── Report.java          # Flagged / moderation report POJO
        │           │   ├── AuditLog.java        # Security audit trail POJO
        │           │   └── MatchResult.java     # Match calculation breakdown & rating POJO
        │           │
        │           ├── member1_auth/            # MEMBER 1: Authentication & User Accounts
        │           │   ├── controller/
        │           │   │   ├── LoginServlet.java
        │           │   │   ├── RegisterServlet.java
        │           │   │   ├── LogoutServlet.java
        │           │   │   ├── ProfileServlet.java
        │           │   │   └── ChangePasswordServlet.java
        │           │   ├── service/
        │           │   │   ├── UserService.java
        │           │   │   └── UserServiceImpl.java
        │           │   ├── dao/
        │           │   │   ├── UserDAO.java
        │           │   │   └── UserDAOImpl.java
        │           │   └── util/
        │           │       ├── BCryptUtil.java
        │           │       └── ValidationUtil.java
        │           │
        │           ├── member2_items/           # MEMBER 2: Item Inventory & Image Uploads
        │           │   ├── controller/
        │           │   │   ├── ItemServlet.java
        │           │   │   └── UploadServlet.java
        │           │   ├── service/
        │           │   │   ├── ItemService.java
        │           │   │   └── ItemServiceImpl.java
        │           │   ├── dao/
        │           │   │   ├── ItemDAO.java
        │           │   │   └── ItemDAOImpl.java
        │           │   └── util/
        │           │       └── ImageUploadUtil.java
        │           │
        │           ├── member3_matching/        # MEMBER 3: Search Engine & Match Algorithms
        │           │   ├── controller/
        │           │   │   ├── SearchServlet.java
        │           │   │   └── MatchServlet.java
        │           │   ├── service/
        │           │   │   ├── SearchService.java
        │           │   │   └── MatchService.java
        │           │   ├── algo/
        │           │   │   ├── LevenshteinAlgorithm.java
        │           │   │   └── HaversineAlgorithm.java
        │           │   └── dao/
        │           │       └── SearchDAOImpl.java
        │           │
        │           ├── member4_claims/          # MEMBER 4: Claims, OTP & Transactional Handover
        │           │   ├── controller/
        │           │   │   ├── ClaimServlet.java
        │           │   │   ├── HandoverServlet.java
        │           │   │   └── VerificationServlet.java
        │           │   ├── service/
        │           │   │   ├── ClaimService.java
        │           │   │   ├── ClaimServiceImpl.java
        │           │   │   └── OtpService.java
        │           │   ├── dao/
        │           │   │   ├── ClaimDAO.java
        │           │   │   └── ClaimDAOImpl.java
        │           │   └── util/
        │           │       └── QRCodeUtil.java
        │           │
        │           └── member5_admin/           # MEMBER 5: Dashboard, Reports & Analytics
        │               ├── controller/
        │               │   ├── AdminServlet.java
        │               │   ├── ReportServlet.java
        │               │   └── NotificationServlet.java
        │               ├── service/
        │               │   ├── AdminService.java
        │               │   ├── ReportService.java
        │               │   └── NotificationService.java
        │               ├── dao/
        │               │   ├── AdminDAO.java
        │               │   └── ReportDAO.java
        │               └── util/
        │                   └── PdfExporter.java
        │
        └── webapp/
            ├── index.jsp                        # Homepage with live metrics & quick actions
            ├── login.jsp                        # User sign-in
            ├── register.jsp                     # Student & Faculty registration
            │
            ├── includes/
            │   ├── header.jsp                   # Meta tags, Bootstrap 5 CDN, Chart.js
            │   ├── navbar.jsp                   # Role-based navigation header
            │   ├── footer.jsp                   # Institutional footer & scripts
            │   └── alerts.jsp                   # Dismissible flash notification alerts
            │
            ├── auth/
            │   ├── profile.jsp                  # User profile viewer & editor
            │   └── change-password.jsp          # Credential update form
            │
            ├── items/
            │   ├── report-lost.jsp              # Lost property submission with GPS auto-fill
            │   ├── report-found.jsp             # Found property submission
            │   ├── item-details.jsp             # Item details with claim & flag triggers
            │   ├── my-items.jsp                 # Current user's reported inventory
            │   ├── gallery.jsp                  # Filterable grid of campus items
            │   └── edit-item.jsp                # Ownership-protected update form
            │
            ├── search/
            │   ├── search.jsp                   # Multi-parameter search entry
            │   ├── filters.jsp                  # Modular criteria filter bar
            │   ├── results.jsp                  # Filtered result cards
            │   └── match-results.jsp            # 4-factor algorithm score breakdown
            │
            ├── claims/
            │   ├── claim-item.jsp               # Ownership claim submission form
            │   ├── my-claims.jsp                # Filed vs. received claims tabs
            │   ├── claim-details.jsp            # QR code display, OTP status & reviews
            │   ├── otp-verification.jsp         # Multi-factor token validator
            │   └── handover.jsp                 # Final atomic handover confirmation
            │
            ├── admin/
            │   ├── dashboard.jsp                # Metric cards, alerts & live audit stream
            │   ├── users.jsp                    # Account search, role updates, blocking
            │   ├── reports.jsp                  # Community moderation review queue
            │   ├── notifications.jsp            # Alert notification center
            │   ├── analytics.jsp                # Dynamic Chart.js visualizations
            │   └── disposal.jsp                 # Unclaimed aging inventory review
            │
            ├── error/
            │   ├── 400.jsp                      # Bad Request
            │   ├── 401.jsp                      # Unauthorized
            │   ├── 403.jsp                      # Forbidden Access
            │   ├── 404.jsp                      # Resource Not Found
            │   └── 500.jsp                      # Internal Server Error (Sanitized)
            │
            ├── css/
            │   └── style.css                    # Professional college theme stylesheet
            ├── js/
            │   └── app.js                       # Geolocation, image previews, client validations
            │
            ├── WEB-INF/
            │   └── web.xml                      # Servlet 6.0 deployment descriptor
            │
            └── uploads/
                └── items/                       # Safe storage folder for item images
```

---

## 🔐 Authentication and password recovery

- Registration asks for a full name, unique username/email identifier, password, and confirmation. The identifier is stored in the existing unique `users.email` column for compatibility with the rest of the application; it is normalized to lowercase and is not verified or used for email.
- Passwords are stored only as BCrypt hashes (cost 12). Login checks the identifier and password against PostgreSQL. Unauthenticated requests to protected application routes are redirected to sign in; login invalidates any prior session before establishing a fresh one, and logout invalidates it.
- Signed-in users change passwords at **Change Password**; their current password is required.
- There is no public password-reset endpoint. If a user is locked out, an administrator verifies the person's identity in person, generates a strong one-time temporary password, hashes it with the application's BCrypt utility, and updates only that account through a restricted database administrator account. The temporary password is provided privately and the user immediately signs in and changes it using the normal current-password-required flow. Do not email passwords or perform resets based only on an online request.

Example SQL after generating a BCrypt hash securely:
```sql
UPDATE users
SET password = '<BCrypt hash of a strong temporary password>'
WHERE user_id = <verified user id>;
```
Confirm exactly one row was affected. Never put a plaintext password or production database credential in SQL committed to source control.

---

## 🧮 Algorithm Matching Engine Details

When an item is inspected, clicking **"Run Algorithm Match"** triggers `MatchService.java`:

$$\text{Total Score} = (\text{Text Score} \times 0.40) + (\text{Location Score} \times 0.30) + (\text{Date Score} \times 0.20) + (\text{Category Score} \times 0.10)$$

### 1. Text Similarity (40% Weight) — `LevenshteinAlgorithm.java`
- Manually implemented matrix edit distance algorithm with normalization (lowercase, trim, whitespace collapse).
- Augmented with token overlap / Jaccard containment so matching keywords (e.g., *"Wallet"* vs. *"Black Leather Tommy Hilfiger Wallet"*) receive accurate correlation.

### 2. Geospatial Proximity (30% Weight) — `HaversineAlgorithm.java`
- Calculates the spherical great-circle distance $d$ in kilometers using earth radius $R = 6371.0 \text{ km}$:
  $$a = \sin^2\left(\frac{\Delta\text{lat}}{2}\right) + \cos(\text{lat}_1)\cos(\text{lat}_2)\sin^2\left(\frac{\Delta\text{lon}}{2}\right)$$
  $$c = 2 \cdot \arctan2\left(\sqrt{a}, \sqrt{1-a}\right), \quad d = R \cdot c$$
- Threshold Scoring:
  - $0 \le d \le 0.10 \text{ km} \rightarrow 100\%$
  - $0.10 < d \le 0.50 \text{ km} \rightarrow 90\%$
  - $0.50 < d \le 1.00 \text{ km} \rightarrow 75\%$
  - $1.00 < d \le 2.00 \text{ km} \rightarrow 60\%$
  - $2.00 < d \le 5.00 \text{ km} \rightarrow 40\%$
  - $> 5.00 \text{ km} \rightarrow 20\%$
- Automatic text fallback if GPS coordinates are missing.

### 3. Date Proximity (20% Weight)
- Exact same day: $100\%$
- Within 3 days: $85\%$
- Within 7 days: $70\%$
- Within 14 days: $50\%$
- Within 30 days: $30\%$
- Beyond 30 days: $15\%$

### 4. Category Alignment (10% Weight)
- Identical category ID: $100\%$ | Different category: $0\%$

---

## 🗄️ Database Setup (PostgreSQL 14+)

Install PostgreSQL and ensure its `bin` directory is on `PATH`. Create the application database (PowerShell):

```powershell
createdb -U postgres lost_found_db
```

The application initializes its tables and development seed data automatically the first time it connects to an empty database. To initialize manually, use the PostgreSQL script:

```powershell
psql -U postgres -d lost_found_db -f .\database\lost_found_postgres.sql
```

This script drops and recreates the application's tables before adding demo data. **Only run it against a new or disposable database**; it will delete existing application data. For an existing database, let the application apply its non-destructive schema checks, or back up and migrate the data separately. The seed includes demo accounts; change their credentials before using a public deployment.

Switching `DATABASE_URL` from an existing MySQL service to PostgreSQL does not migrate its data. Export and import the real rows separately, preserving primary and foreign keys, then reset PostgreSQL identity sequences if IDs were imported explicitly. Test against a PostgreSQL copy before switching production traffic; do not use the reset script on a production database.

Development demo accounts (created only when the schema is first initialized):

| Role | Login ID | Password |
| --- | --- | --- |
| Administrator | `admin@college.com` | `Admin@123` |
| Student | `student@college.com` | `Student@123` |
| Faculty | `faculty@college.com` | `Faculty@123` |

### 🗄️ Database Configuration & Credentials

PostgreSQL is the default for local development, Docker, and cloud hosting. MySQL and MariaDB remain supported when explicitly configured.

Set the database connection in `.env` or as environment variables:
- **For local PostgreSQL**:
  ```env
  DB_TYPE=postgres
  DB_HOST=localhost
  DB_PORT=5432
  DB_NAME=lost_found_db
  DB_USER=postgres
  DB_PASSWORD=your_password
  DB_SSLMODE=prefer
  ```
- **For hosted databases**, use the complete connection URL provided by your database host. PostgreSQL providers generally require SSL; their URL should include `sslmode=require`.
  - MySQL: `DATABASE_URL=mysql://user:password@host:3306/lost_found_db`
  - PostgreSQL: `DATABASE_URL=postgresql://user:password@host:5432/lost_found_db`

---

## 🔨 Maven Build & Test Commands

Run automated tests and generate the production WAR from PowerShell:

```powershell
# Run all 20 automated unit tests
mvn test

# Package the production WAR
mvn clean package
```

The resulting deployable WAR file is generated at:
```
target/LostFoundManagementSystem.war
```

---

## 🐳 Quickstart with Docker Compose (Recommended)

Run the complete stack (**PostgreSQL 16 Database** + **Tomcat 11 Web Application**) with a single command:

```bash
docker compose up --build -d
```

- **Web Portal**: `http://localhost:8080/` or `http://localhost:8080/LostFoundManagementSystem/`
- **PostgreSQL Database**: `localhost:5432` (Database: `lost_found_db`, User: `lostfound`, Password: `lostfound123`)
- **Database Schema**: Auto-imported on first startup from `database/lost_found_postgres.sql`. The script drops and recreates app tables, so Docker only runs it when initializing a new volume.
- **Logs**: `docker compose logs -f`
- **Stop**: `docker compose down`

---

## 🌐 Step-by-Step Free Deployment Guide: Database & Website

You can deploy both the database and the web application **100% free** without requiring a credit card using the following tested platforms:

### 1. Free Database Setup (PostgreSQL)

#### Option A: Supabase or Neon (PostgreSQL)
1. Create a PostgreSQL project with your chosen provider.
2. Initialize a new database by running `database/lost_found_postgres.sql` in its SQL editor, or let the application initialize an empty database on first connection.
3. Copy the provider's PostgreSQL connection URL, including the credentials and SSL mode, into `DATABASE_URL`.
4. **Do not run the SQL script against a database containing data**: it drops and recreates the application tables.

#### Option B: Aiven for MySQL (MySQL alternative)
1. Visit **[Aiven.io](https://aiven.io/)** and sign up for a free account.
2. In the console, click **Create Service** &rarr; select **MySQL**.
3. Choose the **Free Plan** (Includes 5 GB storage, 1 CPU, 1 GB RAM).
4. Select a cloud region near you and click **Create Service**.
5. Once running (takes 1-2 minutes):
   - Copy the **Service URI** (e.g. `mysql://avnadmin:xxxx@mysql-xxx.aivencloud.com:12345/defaultdb?ssl-mode=REQUIRED`).
   - Create the database `lost_found_db` in the Aiven console **Databases** tab, or import `database/lost_found.sql`.
   - Your connection string is:
     ```
     DATABASE_URL=mysql://avnadmin:YOUR_PASSWORD@YOUR_HOST:YOUR_PORT/lost_found_db
     ```

#### Option C: Clever Cloud (MySQL alternative)
1. Sign up for free at **[Clever-Cloud.com](https://www.clever-cloud.com/)**.
2. Click **Create** &rarr; **an add-on** &rarr; choose **MySQL**.
3. Select the free **Dev** plan (5 concurrent connections, 20 MB storage).
4. Name your database `lost_found_db` and click **Create**.
5. Under the add-on's dashboard, copy the Host, Port, User, Password, and Database Name.
6. Use phpMyAdmin (accessible from Clever Cloud dashboard) to import `database/lost_found.sql`.

---

### 2. Free Web App Hosting (Render.com via Docker)

**Render.com** allows hosting the Dockerized Tomcat application completely free:

1. **Push your code to GitHub**:
   ```bash
   git add .
   git commit -m "Configure production Docker and PostgreSQL deployment"
   git push origin main
   ```
2. **Create Web Service on Render**:
   - Go to **[Render.com](https://render.com/)** and sign in.
   - Click **New +** &rarr; **Web Service**.
   - Select **Build and deploy from a Git repository** and connect your repository `java_project`.
3. **Configure Service Details**:
   - **Name**: `campus-lost-found` (or any custom name)
   - **Region**: Choose the region closest to your database
   - **Branch**: `main`
   - **Runtime**: **Docker** (Render detects the root `Dockerfile` automatically)
   - **Instance Type**: **Free**
4. **Configure Environment Variables**:
   Click **Advanced &rarr; Add Environment Variable**:
   | Key | Value | Notes |
   |---|---|---|
   | `PORT` | `10000` | Port injected by Render |
   | `DATABASE_URL` | *Your Cloud DB URL* | e.g. `mysql://user:pass@host:port/lost_found_db` or PostgreSQL URL |
   | `DB_SSLMODE` | `require` | Required when using PostgreSQL host variables; include SSL mode in `DATABASE_URL` when using a URL |
   | `APP_URL` | `https://your-service-name.onrender.com` | Your live Render URL |
5. **Click "Create Web Service"**:
   - Render will automatically trigger `docker build` using the multi-stage Dockerfile.
   - In 2-3 minutes, your application will be live at:
     ```
     https://campus-lost-found.onrender.com/
     ```
   - Both `/` and `/LostFoundManagementSystem/` routes work out of the box!

---

### 3. Alternative Free Web App Hosting (Railway or Koyeb)

#### Railway.app:
1. Sign up on **[Railway.app](https://railway.app/)**.
2. Click **New Project** &rarr; **Deploy from GitHub repo**.
3. Railway automatically builds using Dockerfile.
4. In **Variables**, add `DATABASE_URL` and `PORT=8080`.
5. Under **Settings &rarr; Networking**, click **Generate Domain**.

---

## 💻 PowerShell deployment to Tomcat 11

Use a dedicated PostgreSQL account rather than a superuser when possible. This example prompts for the password without writing it to a project file, copies the WAR, and starts Tomcat in the foreground:

```powershell
$env:CATALINA_HOME = 'C:\apache-tomcat-11.0.x'
$env:DB_USERNAME = 'lostfound_app'
$securePassword = Read-Host 'PostgreSQL password' -AsSecureString
$passwordPointer = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($securePassword)
try {
    $env:DB_PASSWORD = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($passwordPointer)
    mvn clean package
    if ($LASTEXITCODE -ne 0) { throw 'Maven build failed.' }
    Copy-Item .\target\LostFoundManagementSystem.war "$env:CATALINA_HOME\webapps\LostFoundManagementSystem.war" -Force
    & "$env:CATALINA_HOME\bin\catalina.bat" run
}
finally {
    [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($passwordPointer)
    Remove-Item Env:DB_PASSWORD -ErrorAction SilentlyContinue
}
```

Open `http://localhost:8080/LostFoundManagementSystem/`; use HTTPS in production so the session cookie is transported over TLS. Stop the foreground server with Ctrl+C.

## 💻 IntelliJ IDEA & Tomcat 11 Setup Guide

Follow these exact steps to run the application inside IntelliJ IDEA with Apache Tomcat 11:

### 1. Open the Project in IntelliJ IDEA
1. Launch **IntelliJ IDEA**.
2. Select **File &rarr; Open...**
3. Navigate to the project directory containing `pom.xml` and select it (or select `pom.xml` directly).
4. Click **Open as Project**.

### 2. Verify Project SDK (JDK 25)
1. Go to **File &rarr; Project Structure &rarr; Project**.
2. Set **SDK** to **JDK 25**.
3. Set **Language Level** to **25** (or SDK default).
4. Click **Apply &rarr; OK**.

### 3. Configure Apache Tomcat 11 Local Server
1. Go to **Run &rarr; Edit Configurations...**
2. Click the **+** (Add New Configuration) icon.
3. Select **Tomcat Server &rarr; Local** (If not visible, click *More items...*).
4. Name the configuration: `Tomcat 11`.
5. Under **Application server**, click **Configure...** and point to your Apache Tomcat 11 installation folder (e.g. `C:\apache-tomcat-11.0.x`).
6. Click the **Deployment** tab:
   - Click **+ &rarr; Artifact...**
   - Choose `LostFoundManagementSystem:war exploded`.
   - In **Application context**, enter: `/LostFoundManagementSystem`
7. Click the **Server** tab:
   - URL: `http://localhost:8080/LostFoundManagementSystem/`
   - HTTP port: `8080`
   - JRE: Select your JDK 25.
8. Click **Apply &rarr; OK**.

### 4. Run the Application
1. Click the green **Run** or **Debug** button in IntelliJ IDEA.
2. Tomcat will start and automatically open your default browser to:
   ```
   http://localhost:8080/LostFoundManagementSystem/
   ```

---

## 📋 Comprehensive Testing Checklist

### 1. Authentication & Security
- [ ] Register a new user with a username (not an email), matching passwords, then verify the account is stored with a BCrypt hash.
- [ ] Register another account using the same identifier with different letter casing; verify the unique identifier is rejected.
- [ ] Sign in using the registered identifier/password; verify the session is established and the profile/role is shown.
- [ ] Try a wrong password and verify the request is rejected without establishing a session.
- [ ] Request a protected URL while signed out and verify redirection to login; verify public search/gallery remains available.
- [ ] Change password while signed in; confirm an incorrect current password fails and the correct current password succeeds.
- [ ] Log out, then verify the session is invalidated and protected URLs redirect to login.

### 2. Item Inventory
- [ ] **Report Lost**: Submit lost item with campus location, date, and image. Verify redirect to details page.
- [ ] **Report Found**: Submit found item with custody details.
- [ ] **GPS Auto-Fill**: Click "GPS" button on reporting forms to populate coordinates via browser API.
- [ ] **Image Upload**: Upload `.jpg`, `.jpeg`, `.jfif`, `.png`, or `.webp` file (`< 5MB`). Verify image preview, safe UUID renaming, item detail display, and persistence after a Tomcat redeploy.
- [ ] **Ownership Protection**: Log in as student B. Verify cannot edit or delete student A's reported item.
- [ ] **Gallery Filters**: Filter gallery by "Lost Only", "Found Only", or "All Items".

### 3. Search & Matching
- [ ] **Multi-Criteria Search**: Search with keyword "Wallet", category "Wallet", and location "Library".
- [ ] **Levenshtein Scoring**: Open an item with a matching counterpart and verify the text score.
- [ ] **Haversine Distance**: Verify GPS distance percentage calculation.
- [ ] **Composite Score**: Verify total score calculation $(40\% + 30\% + 20\% + 10\%)$ and rating badge.

### 4. Claims & Handover
- [ ] **File Claim**: As a student, view a found item reported by another user. Click "Claim This Item". Provide reason and proof.
- [ ] **Self-Claim Prevention**: Attempt to claim an item you reported yourself. Verify security policy blocks the request.
- [ ] **Review Claim**: Log in as the found item reporter or Admin. View claim details and click "Approve Claim".
- [ ] **OTP & QR Generation**: Verify 6-digit OTP code and QR code generated on claim details page.
- [ ] **OTP Verification**: Enter the OTP on the verification page. Verify transition to `VERIFIED`.
- [ ] **Transactional Handover**: Submit the verified handover. Verify item status updates to `RETURNED` and claim status to `COMPLETED`.

### 5. Administration & Reporting
- [ ] **Dashboard Metrics**: Sign in as an administrator. Verify live counts for users, items, claims, and reports.
- [ ] **User Management**: Search users, toggle Block/Unblock, and change roles. Verify protection of the last remaining admin.
- [ ] **Content Moderation**: Flag an item from the details page. View and resolve the report from `/admin/reports`.
- [ ] **Analytics Charts**: Visit `/admin/analytics`. Verify dynamic Chart.js rendering of category counts and status distributions.
- [ ] **PDF Audit Export**: Export the PDF report and verify the download and included data.

---

## 🛠️ Common Errors & Troubleshooting

| Issue / Error | Cause | Resolution |
|---|---|---|
| **PostgreSQL connection refused** | PostgreSQL is stopped, or the host/port is incorrect. | Start local PostgreSQL or run `docker compose up -d postgres_db`; check `DB_HOST` and `DB_PORT=5432`. |
| **PostgreSQL password authentication failed** | The configured username or password is incorrect. | Verify `DB_USER` and `DB_PASSWORD` with the database provider; for Compose, use `lostfound` and `lostfound123`. |
| **MySQL access denied / communications failure** | A configured MySQL service is stopped or the credentials are incorrect. | Check `DB_TYPE=mysql`, `DB_HOST`, `DB_PORT=3306`, and MySQL credentials. |
| **`Database 'lost_found_db' does not exist`** | The database itself has not been created. | Create an empty PostgreSQL database (`createdb -U postgres lost_found_db`) or use the database provider's console. The app initializes an empty database on first connection. |
| **`java.lang.ClassNotFoundException: com.mysql.cj.jdbc.Driver`** | Missing MySQL driver in classpath. | Run `mvn clean package` to bundle the updated `mysql-connector-j` into the WAR. |
| **`404 Not Found` when opening application** | Application context path mismatch. | Both `/` and `/LostFoundManagementSystem/` are now deployed simultaneously to prevent this. Ensure Tomcat 11 is running. |
| **`500 - java.lang.NoClassDefFoundError: jakarta/servlet/...`** | Older Tomcat version (Tomcat 9 or 8) used. | Apache Tomcat 11 is required for Jakarta EE 11 / Servlet 6.1 specification. |
| **`File upload size exceeds limit`** | Uploaded image $> 5 \text{ MB}$. | Choose an image file smaller than 5 MB or compress the picture before upload. |
| **`OTP Expired` error during handover** | 5-minute security timeout elapsed. | Ask the item reporter or an administrator to open the approved claim and select **Regenerate OTP / QR**. |

---

## 📄 License & Academic Attribution
Developed for university and college property recovery systems. Free to use and extend for academic projects, institutional operations, and portfolio demonstrations.
 Saloni


## 🧹 Keeping This README Current

- Keep setup commands relative to the repository root; do not add personal machine paths, local passwords, or real `.env` values.
- Update the architecture, dependencies, database steps, and deployment instructions whenever the corresponding code or configuration changes.
- Keep the 12-week plan as a planning outline unless actual dated progress records are available.
- Mark testing checklist entries as complete only after a test has been run and verified; update troubleshooting entries when a root cause or fix changes.
- Before publishing, check links, command paths, Tomcat context names, and Maven artifact names against the current project.
- main
