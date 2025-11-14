# High-Concurrency Online Shopping System

A scalable **Java Spring Boot** based online shopping system optimized for high-concurrency scenarios during large sales events. The project focuses on backend performance, distributed architecture.

---

## **Features & Highlights**

- **High-Concurrency Distributed System**  
  Built to handle massive simultaneous traffic during flash sale events without overselling.

- **Backend Architecture**
    - Java **Spring Boot** with **MyBatis** and **MySQL** for persistent storage.
    - CRUD operations and business logic efficiently handled.

- **Inventory & Oversell Prevention**
    - Real-time inventory stored in **Redis** with **Lua scripts**.
    - Integrated **RocketMQ** delay messages implementing the **try-confirm-cancel (TCC)** pattern to prevent overselling.

- **Peak Load Handling**
    - Burst traffic handled asynchronously via **RocketMQ**, improving throughput and system stability during peak sale periods.

- **CI/CD & Deployment**
    - Docker images managed with **AWS ECR**.
    - Automated build, test, deployment pipelines using **AWS CodeBuild** and **CodePipeline** via **AWS CDK**.
    - Deployed to **AWS ECS**, accelerating rollout efficiency by ~60%.

---

## **Tech Stack**

- **Backend:** Java, Spring Boot, MyBatis
- **Database:** MySQL
- **Cache / Message Queue:** Redis, RocketMQ
- **Infrastructure / DevOps:** Docker, AWS ECR, ECS, CodeBuild, CodePipeline, AWS CDK
- **Scripting / Optimization:** Lua (Redis scripts)


ROCKETMQ:
-bin folder
-mqnamesrv
-mqbroker -n localhost:9876 autoCreateTopic=true


