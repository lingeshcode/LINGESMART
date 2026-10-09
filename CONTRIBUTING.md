# Contributing to LingeshMart

## Local Setup Instructions

1. **Prerequisites**:
   - JDK 17 (LTS)
   - Apache Maven 3.8+
   - Apache Tomcat 9.0.x

2. **Clone and Build**:
   ```bash
   git clone https://github.com/your-username/LingeshMart.git
   cd LingeshMart
   mvn clean package
   ```

3. **Deploy to Tomcat**:
   - Copy `target/lingeshmart.war` to your Tomcat `webapps/` folder.
   - Start Tomcat: `./bin/startup.sh` (or `startup.bat`).
   - Access the application at: `http://localhost:8080/lingeshmart/`

4. **Default Accounts**:
   - **Buyer**: `buyer1@lingeshmart.com` / `password123`
   - **Seller**: `seller1@lingeshmart.com` / `password123`
   - **Admin**: `admin@lingeshmart.com` / `password123`

5. **Branching & Commit Standard**:
   - Use conventional commit formats: `feat:`, `fix:`, `test:`, `docs:`.
