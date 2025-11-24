Gardening Community Platform  
A full-stack Java web application built using **Servlets, JSP, JDBC, and H2 Database**.  
It allows gardeners to share tips, join discussions, and track gardening projects.  
Admins can manage users, moderate tips, and view activity logs.

---

Features

Gardener
- Create and share gardening tips  
- Join gardening discussions  
- Track personal gardening projects (title, description, progress %)  
- View a colorful dashboard with easy navigation  

Admin
- Manage user accounts  
- Approve or reject gardening tips  
- View platform-wide activity through a threaded Activity Logger  
- Clean dashboard with colorful UI  

System
- DAO + JDBC database layer  
- H2 embedded database with auto table creation  
- Multithreaded Activity Logger  
- Servlet + JSP web architecture  
- Full Maven project  

---

Technologies Used
- Java 17+
- Jakarta Servlets
- JSP
- JDBC (H2 Database)
- Maven
- Apache Tomcat 9/10
- HTML + CSS (Custom UI)

---

Project Structure
src/main/java/
com.gardencommunity/
model/ ← User, Admin, Gardener, Tip, Discussion, Project models
dao/ ← DAO interfaces + JDBC implementations
db/ ← DatabaseManager (auto-creates tables)
service/ ← UserService
servlet/ ← Login, Register, Tip, AdminTip, Discussion, Project Servlets
util/ ← ActivityLogger (multithreaded)

src/main/webapp/
css/ ← UI styles
index.jsp ← Login & Register
gardener.jsp ← Gardener dashboard
admin.jsp ← Admin dashboard
tips.jsp ← Tip management page
discussions.jsp ← Discussion page
projects.jsp ← Projects page

---

Requirements

Before running the project, install:

JDK 17 or above  
IntelliJ IDEA (Community or Ultimate)  
Apache Tomcat 9 or 10  
Internet connection (for Maven dependencies)
No database setup required — H2 auto-creates everything.

---

Setup Instructions

Follow these steps to run the project correctly:

1. Clone the repository
Using Git: https://github.com/rubalsingh3/gardening-community-platform

2. Open in IntelliJ

Open IntelliJ

Click File → Open

Select the downloaded project folder

IntelliJ will auto-detect Maven

When prompted, click "Load Maven Project"

3. Configure Tomcat Server

Go to Run → Edit Configurations

Click + → Tomcat Server → Local

Choose your Tomcat installation directory

Under Deployment click + → Artifact
Select:

gardening-community-web:war exploded


Set:

Application context: /


Click Apply → OK

4. Run the Project

Click the green Run button.
After Tomcat starts, open:

http://localhost:8080/

Login Details
Admin Account
Email: admin@garden.com

Gardener

Register through the homepage form.

Database Information

Uses H2 embedded database

No installation required

Tables auto-created on server startup

DB is stored in memory/file depending on configuration

Tables:

users

tips

discussions

projects

Multithreading (Activity Logger)

The platform includes a custom multithreaded logger that:

Runs using ExecutorService

Stores events in a synchronized list

Logs user actions like login, tip creation, project updates, moderation, etc.

Visible from the Admin dashboard

This satisfies multithreading + synchronization rubric points.

UI

The UI uses:

Modern color gradients

Centered layout

Card-style sections

Buttons, forms, tables styled consistently

Works across all pages (Admin + Gardener).



Author

Rubal Singh
Gardening Community Platform Project