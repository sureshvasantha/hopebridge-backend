-- ========================
-- ROLES
-- ========================
INSERT INTO roles (role_name) VALUES ('ADMIN');
INSERT INTO roles (role_name) VALUES ('DONOR');

-- ========================
-- USERS
-- ========================
INSERT INTO users (name, email, password, profile_picture)
VALUES 
('Suresh', 'suresh@hopebridge.org', '$2a$12$ihf7./gSgu0IzUarX1p8/egW3WQq.4iArtAH5wpB/YXXg8A1PrFUO', 'https://cdn.hopebridge.org/profiles/suresh.jpg'),
('Neha', 'neha@gmail.com', '$2a$12$FNbVDf0I3q0r3kq6NzC2j.ZrH6dmjrP8NtPk2tupHn4/4Brvii.ze', 'https://cdn.hopebridge.org/profiles/anita.png'),
('Vignesh', 'vignesh@gmail.com', '$2a$12$d7UGzDQohI/9LutIXuOKI.6rb4VncooOVz0/pKafYyT3R95rrPi2e', 'https://cdn.hopebridge.org/profiles/vignesh.jpg');

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (3, 2);

-- ========================
-- CAMPAIGNS
-- ========================
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

-- ========================
-- CAMPAIGN IMAGES
-- ========================
INSERT INTO campaign_images (image_url, description, campaign_id)
VALUES
('https://cdn.hopebridge.org/campaigns/asha_hospital.jpg', 'Asha at the hospital before surgery', 1),
('https://cdn.hopebridge.org/campaigns/asha_bill.jpg', 'Hospital bill for transparency', 1),
('https://cdn.hopebridge.org/campaigns/tribal_school.jpg', 'Tribal children in classroom', 2),
('https://cdn.hopebridge.org/campaigns/tree_drive.jpg', 'Volunteers planting saplings', 3);

-- ========================
-- DONATIONS (Stripe)
-- ========================
INSERT INTO donations 
(amount, currency, status, payment_session_id, payment_intent_id, payment_method, payment_method_type, transaction_id, receipt_url, donor_id, campaign_id)
VALUES
(10000, 'INR', 'SUCCESS', 'stripe_SESSION_201', 'pi_201', 'VISA ending in 4242', 'card', 'stripe_TXN_201', 'https://stripe.com/receipts/pi_201', 2, 2),
(500, 'INR', 'PENDING', 'stripe_SESSION_202', 'pi_202', NULL, NULL, NULL, NULL, 3, 2),
(2500, 'INR', 'SUCCESS', 'stripe_SESSION_301', 'pi_301', 'MasterCard ending in 5100', 'card', 'stripe_TXN_301', 'https://stripe.com/receipts/pi_301', 2, 3),
(1500, 'INR', 'FAILED', 'stripe_SESSION_401', 'pi_401', 'VISA ending in 4000', 'card', 'stripe_TXN_401', 'https://stripe.com/receipts/pi_401_failed', 3, 1);

-- ========================
-- IMPACT STORIES
-- ========================
INSERT INTO impact_stories (title, content, campaign_id)
VALUES
('Asha Successfully Operated', 
 'Thanks to your generous donations, Asha’s heart surgery was a success. She is recovering well and will return home soon.', 1),
('School Year Begins with Smiles', 
 'The children started their school year with new books and meals, made possible by your donations.', 2),
('Green Bengaluru Drive Progress', 
 '500 trees planted so far! Your contributions are making the city greener.', 3);

-- ========================
-- IMPACT IMAGES
-- ========================
INSERT INTO impact_images (image_url, story_id)
VALUES
('https://cdn.hopebridge.org/impact/asha_recovered.jpg', 1),
('https://cdn.hopebridge.org/impact/tribal_school_opening.jpg', 2),
('https://cdn.hopebridge.org/impact/tree_drive_halfway.jpg', 3);
