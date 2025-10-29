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
('Suresh', 'suresh@hopebridge.org', '$2a$12$ihf7./gSgu0IzUarX1p8/egW3WQq.4iArtAH5wpB/YXXg8A1PrFUO', 'https://images.pexels.com/photos/2379005/pexels-photo-2379005.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500'),
('Neha', 'neha@gmail.com', '$2a$12$FNbVDf0I3q0r3kq6NzC2j.ZrH6dmjrP8NtPk2tupHn4/4Brvii.ze', 'https://img.freepik.com/free-photo/young-beautiful-woman-pink-warm-sweater-natural-look-smiling-portrait-isolated-long-hair_285396-896.jpg'),
('Vignesh', 'vignesh@gmail.com', '$2a$12$d7UGzDQohI/9LutIXuOKI.6rb4VncooOVz0/pKafYyT3R95rrPi2e', 'https://img.freepik.com/free-photo/portrait-white-man-isolated_53876-40306.jpg?semt=ais_hybrid&w=740&q=80');

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
 500000, 0, '2025-10-01', '2025-12-31', 'ACTIVE', 'MEDICAL', 'Delhi', 1),

('Support Education for 50 Tribal Children',
 'Funds will provide uniforms, books, and nutritious meals for children in Odisha’s remote villages.',
 300000, 0, '2025-09-15', '2025-11-30', 'ACTIVE', 'EDUCATION', 'Odisha', 1),

('Plant 1000 Trees in Bengaluru',
 'Join our environmental drive to plant and maintain 1000 native trees in urban Bengaluru.',
 150000, 0, '2025-10-05', '2025-12-20', 'ACTIVE', 'ENVIRONMENT', 'Bengaluru', 1);

-- ========================
-- CAMPAIGN IMAGES
-- ========================
INSERT INTO campaign_images (image_url, description, campaign_id)
VALUES
('https://media.istockphoto.com/id/905899482/photo/sick-girl-in-hospital-bed.jpg?s=612x612&w=0&k=20&c=uOms2uTLzrhoC3eeaI6JTB34FqtO5BSGrwRZwkpWmZQ=', 'Asha at the hospital before surgery', 1),
('https://pbs.twimg.com/media/FBxT0_bakAAOn2L.jpg', 'Hospital bill for transparency', 1),
('https://media.assettype.com/tnm%2Fimport%2Fsites%2Fdefault%2Ffiles%2FKadambathurCoachingClass_1200.jpg?w=480&auto=format%2Ccompress&fit=max', 'Tribal children in classroom', 2),
('https://img.freepik.com/free-photo/volunteers-little-kid-planting-tree-covering-hole-ground_482257-91057.jpg?semt=ais_hybrid&w=740&q=80', 'Volunteers planting saplings', 3);

-- ========================
-- DONATIONS (Stripe)
-- ========================
-- INSERT INTO donations 
-- (amount, currency, status, payment_session_id, payment_intent_id, payment_method, payment_method_type, transaction_id, receipt_url, donor_id, campaign_id)
-- VALUES
-- (10000, 'INR', 'SUCCESS', 'stripe_SESSION_201', 'pi_201', 'VISA ending in 4242', 'card', 'stripe_TXN_201', 'https://stripe.com/receipts/pi_201', 2, 2),
-- (500, 'INR', 'PENDING', 'stripe_SESSION_202', 'pi_202', NULL, NULL, NULL, NULL, 3, 2),
-- (2500, 'INR', 'SUCCESS', 'stripe_SESSION_301', 'pi_301', 'MasterCard ending in 5100', 'card', 'stripe_TXN_301', 'https://stripe.com/receipts/pi_301', 2, 3),
-- (1500, 'INR', 'FAILED', 'stripe_SESSION_401', 'pi_401', 'VISA ending in 4000', 'card', 'stripe_TXN_401', 'https://stripe.com/receipts/pi_401_failed', 3, 1);

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
('https://media.istockphoto.com/id/1515280024/photo/recovery-time.jpg?s=612x612&w=0&k=20&c=jZCuZxA1QFbL3-dgBsIkTjPJ0oAath1gT08ftUS1vrI=', 1),
('https://cdnbbsr.s3waas.gov.in/s395192c98732387165bf8e396c0f2dad2/uploads/2024/11/202411182019343571-300x200.jpg', 2),
('https://www.shutterstock.com/image-photo/group-little-trees-growing-garden-260nw-453250882.jpg', 3);
