

---

# 🌊 Aqua Adventure App  

A seamless **Boat Ride Booking System** for tourists exploring **Zanzibar’s iconic destinations**, developed by **Resilient Matrix Technologies (RM TECH)**. Built with **PHP**, **MySQL**, and **Android (Java)**, this app connects travelers with boat operators for unforgettable aquatic experiences.  

---

## 🌟 Key Features  

### **Admin CMS Features**  
- **Dashboard Overview** - Booking analytics and quick actions  
- **Boat & Destination Management**  
  - Add/Edit boats (speedboats, dhows, etc.)  
  - Organize destinations (Mnemba Island, Prison Island, etc.)  
- **Booking Pipeline**  
  - Track reservations (Pending → Confirmed → Completed)  
- **Tourist Management** - View booking history and contact details  
- **Promotions** - Create seasonal offers or discounted packages  

### **Android App Features**  
- **Explore Zanzibar** - Filter destinations by type (snorkeling, beaches, islands)  
- **Real-Time Booking** - Check availability and schedule trips  
- **Secure Payments** - Mobile money/card integration (if implemented)  
- **Trip History** - View past bookings and upcoming adventures  

---

## 🚀 Quick Start  

### **1️⃣ Clone the repository**  
```bash
git clone https://github.com/Ramadhani-Yassin/AquaAdventures.git
cd AquaAdventures
```

### **2️⃣ Backend Setup (PHP/MySQL)**  
1. Import database:  
   ```bash
   mysql -u root -p aqua_adventures < aqua_adventures_db.sql
   ```
2. Configure database:  
   ```bash
   cp admin/includes/config.example.php admin/includes/config.php
   ```
   Edit with your credentials.

### **3️⃣ Android App Setup**  
1. Open `AquaAdventures/Android` in **Android Studio**  
2. Update API base URL in:  
   `app/src/main/java/com/rmtech/aqua/utils/ApiClient.java`  
   ```java
   public static final String BASE_URL = "http://your-domain.com/app/";
   ```
3. Build and run (Android 5.0+ required)  

---

## 💡 Contributing  

We welcome contributions! 🚀 If you'd like to enhance this aquatic adventure platform:  

✅ Submit a **Pull Request (PR)**  
✅ Open an **Issue** for bugs or feature requests  

---

## 📄 License  

MIT License © [Resilient Matrix Technologies](LICENSE)  

---

## � Developed by  

**Resilient Matrix Technologies (RM TECH)**  
**Empowering Businesses with Smart Tech & Financial Solutions | EST. 29 Nov 2022**  

<div align="center">
  <a href="https://github.com/Ramadhani-Yassin" target="_blank">
    <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white" alt="GitHub">
  </a>
  <a href="https://www.linkedin.com/in/ramadhani-yassin-ramadhani/" target="_blank">
    <img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" alt="LinkedIn">
  </a>
  <a href="mailto:yasynramah@gmail.com">
    <img src="https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white" alt="Email">
  </a>
  <a href="https://www.instagram.com/rm_tech.tz/" target="_blank">
    <img src="https://img.shields.io/badge/Instagram-E4405F?style=for-the-badge&logo=instagram&logoColor=white" alt="Instagram">
  </a>
</div>

---
