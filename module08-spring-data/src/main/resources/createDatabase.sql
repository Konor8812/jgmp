CREATE TABLE IF NOT EXISTS events (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255),
                       date DATE,
                       price BIGINT
);
CREATE TABLE IF NOT EXISTS user_account_details (
                                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                    prepaid INT
);
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(255),
                                     email VARCHAR(255),
                                     user_account_details_id BIGINT,
                                     FOREIGN KEY (user_account_details_id) REFERENCES user_account_details(id)
);
CREATE TABLE IF NOT EXISTS tickets (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        eventId BIGINT,
                        userId BIGINT,
                        place INT,
                        category VARCHAR(50),
                        FOREIGN KEY (eventId) REFERENCES events(id),
                        FOREIGN KEY (userId) REFERENCES users(id)
);