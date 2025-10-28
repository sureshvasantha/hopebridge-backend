DROP TABLE IF EXISTS user_roles, impact_images, impact_stories, donations, campaign_images, campaigns, roles, users CASCADE;

CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    profile_picture VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
    role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
);

CREATE TABLE campaigns (
    campaign_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description CLOB,
    goal_amount DOUBLE NOT NULL,
    collected_amount DOUBLE DEFAULT 0,
    start_date DATE,
    end_date DATE,
    status VARCHAR(20) CHECK (status IN ('ACTIVE', 'COMPLETED', 'INACTIVE')),
    campaign_type VARCHAR(50) CHECK (campaign_type IN ('MEDICAL','EDUCATION','CHILDREN','ENVIRONMENT','WOMEN_EMPOWERMENT','ANIMAL_WELFARE')),
    location VARCHAR(255),
    created_by BIGINT,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL
);

CREATE TABLE campaign_images (
    image_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    image_url VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    campaign_id BIGINT,
    FOREIGN KEY (campaign_id) REFERENCES campaigns(campaign_id) ON DELETE CASCADE
);

CREATE TABLE donations (
    donation_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    amount DOUBLE NOT NULL,
    currency VARCHAR(10) DEFAULT 'INR',
    display_amount DOUBLE NOT NULL,
    display_currency VARCHAR(10) NOT NULL,
    status VARCHAR(20) CHECK (status IN ('SUCCESS', 'FAILED', 'PENDING')),
    payment_session_id VARCHAR(255),
    payment_intent_id VARCHAR(255),
    payment_method VARCHAR(100),
    payment_method_type VARCHAR(50),
    transaction_id VARCHAR(255),
    receipt_url VARCHAR(500),
    donation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    donor_id BIGINT,
    campaign_id BIGINT,
    FOREIGN KEY (donor_id) REFERENCES users(user_id) ON DELETE SET NULL,
    FOREIGN KEY (campaign_id) REFERENCES campaigns(campaign_id) ON DELETE SET NULL
);

CREATE TABLE impact_stories (
    story_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content CLOB,
    posted_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    campaign_id BIGINT,
    FOREIGN KEY (campaign_id) REFERENCES campaigns(campaign_id) ON DELETE CASCADE
);

CREATE TABLE impact_images (
    image_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    image_url VARCHAR(255) NOT NULL,
    story_id BIGINT,
    FOREIGN KEY (story_id) REFERENCES impact_stories(story_id) ON DELETE CASCADE
);
