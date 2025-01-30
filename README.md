# 🎉 Event Wishing System 🎊

&#x20;   &#x20;

## 🌍 Overview

The Event Wishing System is a Spring Boot-based **GenAI 🤖**service designed to send personalized event wishes 🎂🎊 to users. It supports both personal & professional events and ensures accurate scheduling 🗓️ and delivery 📩 of messages.

## 🌟 Features

- **👥 User Management**: Stores users & their relations (family 👨‍👩‍👧, colleagues 🏢, friends 👫).
- **🎈 Event Handling**: Captures & processes yearly recurring events.
- **📄 JSON-Based Input**: Accepts event data via `UserController` in JSON format.
- **⚠️ Validation System**: Returns structured error messages ⚡ while saving valid data ✅.
- **⏳ Time Zone Conversion**: Converts event dates to UTC 🌍 for consistency.
- **🛢️ Database Architecture**: Uses MongoDB 🍃 for `User` & `UserRelation` storage and MySQL 🐬 for event tracking.
- **🤖 Automated Scheduling**:
    - 🗂️ Stores events in `events_master_MM_dd` daily.
    - 📌 Creates a snapshot in `events_YYYY_MM_DD` every night.
    - 📝 Fetches event messages via ChatGPT API at ⏰ 3 AM.
    - ✉️ Sends scheduled emails using Mailtrack at ⏰ 6 AM.

## 🏗️ Tech Stack

- **💻 Backend**: Spring Boot 🚀
- **📊 Database**: MySQL 🐬, MongoDB 🍃
- **📦 Containerization**: Docker 🐳
- **✉️ Messaging & Scheduling**: ChatGPT API 🤖, Mailtrack 📧
<p align="center">
  <img src="https://skillicons.dev/icons?i=java,spring,mysql,mongo,docker,git&theme=light" />

</p>

##
## 🔍 System Architecture



```
👤 User -> 🔗 UserRelation (Person to be wished) -> 🎉 Events
```

- **🔄 User Data Flow**:
    1. 📥 JSON data received via `UserController`.
    2. 🛠️ Validation ensures correctness & stores invalid records with error details ⚠️.
    3. 🔗 Events are linked via `UserRelation ID`.
    4. 🗃️ Events are stored in MySQL with a master record updated daily 📊.

## 🚀 Installation

### 🔧 Prerequisites

- 🐳 Docker
- ☕ Java 17+
- 🏗️ Maven
- 🐬 MySQL & 🍃 MongoDB

### 📌 Steps

1. 📂 Clone the repository:
   ```sh
   git clone https://github.com/yourusername/event-wishing-system.git
   cd event-wishing-system
   ```
2. 🚀 Start MySQL & MongoDB using Docker:
   ```sh
   docker-compose up -d
   ```
3. ⚙️ Configure `application.yml` with database credentials, **Mailtrack server details** 📧, and **ChatGPT API keys** 🤖.
4. 🏗️ Build the project:
   ```sh
   mvn clean install
   ```
5. ▶️ Run the application:
   ```sh
   mvn spring-boot:run
   ```

## 📌 Usage



1. **➕ Add User & Events**: Submit a JSON payload via the API 📥.
2. **✅ Validation**: Errors ⚠️ are returned for invalid records, partial saves enabled.
3. **🔄 Event Processing**: Data is transformed, stored & scheduled 🛠️.
4. **🤖 Message Generation**: AI-generated messages retrieved at ⏰ 3 AM.
5. **📧 Email Delivery**: Emails dispatched at ⏰ 6 AM based on event data.

## 🤝 Contributing

🛠️ Contributions are welcome! Submit pull requests 📤 to enhance functionality 🚀.

## 📝 License

📜 This project is open-source & available under the [MIT License](LICENSE).

## 📞 Contact

For queries ❓, reach out via GitHub issues 🐞 or email 📩 at [support@example.com](mailto\:support@example.com).

🎉 Happy Wishing! 🥳

