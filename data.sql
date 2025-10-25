INSERT INTO roles (role_name) VALUES ('ADMIN');
INSERT INTO roles (role_name) VALUES ('DONOR');

INSERT INTO users (name, email, password, profile_picture)
VALUES 
('Suresh Kumar', 'suresh@hopebridge.org', 'admin123', 'https://cdn.hopebridge.org/profiles/suresh.jpg'),
('Anita Sharma', 'anita@gmail.com', 'donor123', 'https://cdn.hopebridge.org/profiles/anita.png'),
('Rahul Verma', 'rahul@gmail.com', 'donor123', 'https://cdn.hopebridge.org/profiles/rahul.jpg');

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (3, 2);

INSERT INTO campaigns (title, description, goal_amount, collected_amount, start_date, end_date, status, campaign_type, location, created_by)
VALUES 
('Help Asha Recover from Heart Surgery',
 'Asha, a 10-year-old girl from Delhi, needs urgent heart surgery. Your donations will cover hospital expenses and post-surgery care.',
 500000, 320000, '2025-10-01', '2025-12-31', 'ACTIVE', 'MEDICAL', 'Delhi', 1),

('Support Education for 50 Tribal Children',
 'Funds will provide uniforms, books, and nutritious meals for children in Odisha’s remote villages.',
 300000, 180000, '2025-09-15', '2025-11-30', 'ACTIVE', 'EDUCATION', 'Odisha', 1),

('Plant 1000 Trees in Bengaluru',
 'Join our environmental drive to plant and maintain 1000 native trees in urban Bengaluru.',
 150000, 90000, '2025-10-05', '2025-12-20', 'ACTIVE', 'ENVIRONMENT', 'Bengaluru', 1);

INSERT INTO campaign_images (image_url, description, campaign_id)
VALUES
('https://cdn.hopebridge.org/campaigns/asha_hospital.jpg', 'Asha at the hospital before surgery', 1),
('https://cdn.hopebridge.org/campaigns/asha_bill.jpg', 'Hospital bill for transparency', 1),
('https://cdn.hopebridge.org/campaigns/tribal_school.jpg', 'Tribal children in classroom', 2),
('https://cdn.hopebridge.org/campaigns/tree_drive.jpg', 'Volunteers planting saplings', 3);

INSERT INTO donations (amount, payment_id, status, donor_id, campaign_id)
VALUES
(5000, 'razorpay_TXN_101', 'SUCCESS', 2, 1),
(2000, 'razorpay_TXN_102', 'SUCCESS', 3, 1),
(10000, 'stripe_TXN_201', 'SUCCESS', 2, 2),
(500, 'stripe_TXN_202', 'PENDING', 3, 2),
(2500, 'stripe_TXN_301', 'SUCCESS', 2, 3);

INSERT INTO impact_stories (title, content, campaign_id)
VALUES
('Asha Successfully Operated', 
 'Thanks to your generous donations, Asha’s heart surgery was a success. She is recovering well and will return home soon.', 1),
('School Year Begins with Smiles', 
 'The children started their school year with new books and meals, made possible by your donations.', 2),
('Green Bengaluru Drive Progress', 
 '500 trees planted so far! Your contributions are making the city greener.', 3);

INSERT INTO impact_images (image_url, story_id)
VALUES
('https://cdn.hopebridge.org/impact/asha_recovered.jpg', 1),
('https://cdn.hopebridge.org/impact/tribal_school_opening.jpg', 2),
('https://cdn.hopebridge.org/impact/tree_drive_halfway.jpg', 3);
