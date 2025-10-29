/* ==========================================================
   🌉 HOPEBRIDGE: QUERY FILE
   Purpose: Verify data integrity and extract meaningful insights
   Database: H2 / MySQL / PostgreSQL
   ========================================================== */

/* 🧩 1️⃣ View all users and their roles */
SELECT 
    u.user_id,
    u.name,
    u.email,
    GROUP_CONCAT(r.role_name SEPARATOR ', ') AS roles,
    u.profile_picture,
    u.created_at
FROM users u
LEFT JOIN user_roles ur ON u.user_id = ur.user_id
LEFT JOIN roles r ON ur.role_id = r.role_id
GROUP BY u.user_id, u.name, u.email, u.profile_picture, u.created_at
ORDER BY u.user_id;

/* 💼 2️⃣ View all roles and their assigned users */
SELECT 
    r.role_name,
    COUNT(ur.user_id) AS total_users
FROM roles r
LEFT JOIN user_roles ur ON r.role_id = ur.role_id
GROUP BY r.role_name;

/* 🎯 3️⃣ Campaign Overview (Progress, Type, Location) */
SELECT 
    c.campaign_id,
    c.title,
    c.campaign_type,
    c.location,
    c.goal_amount,
    c.collected_amount,
    ROUND((c.collected_amount / c.goal_amount) * 100, 2) AS progress_percent,
    c.status,
    u.name AS created_by
FROM campaigns c
LEFT JOIN users u ON c.created_by = u.user_id
ORDER BY c.start_date DESC;

/* 🖼️ 4️⃣ Campaign Images */
SELECT 
    c.title AS campaign_title,
    ci.image_url,
    ci.description
FROM campaign_images ci
JOIN campaigns c ON ci.campaign_id = c.campaign_id
ORDER BY c.title;

/* 💰 5️⃣ Donation Details with Donor & Campaign Info */
SELECT 
    d.donation_id,
    u.name AS donor_name,
    c.title AS campaign_title,
    d.amount,
    d.payment_id,
    d.status,
    d.donation_date
FROM donations d
JOIN users u ON d.donor_id = u.user_id
JOIN campaigns c ON d.campaign_id = c.campaign_id
ORDER BY d.donation_date DESC;

/* 📊 6️⃣ Total Donations Summary per Campaign */
SELECT 
    c.title AS campaign_title,
    SUM(d.amount) AS total_collected,
    COUNT(d.donation_id) AS total_donations,
    ROUND((SUM(d.amount) / c.goal_amount) * 100, 2) AS completion_percentage
FROM donations d
JOIN campaigns c ON d.campaign_id = c.campaign_id
GROUP BY c.title, c.goal_amount
ORDER BY total_collected DESC;

/* 🧍‍♂️ 7️⃣ Donor Contribution Summary */
SELECT 
    u.name AS donor_name,
    u.email,
    SUM(d.amount) AS total_donated,
    COUNT(d.donation_id) AS donation_count
FROM donations d
JOIN users u ON d.donor_id = u.user_id
GROUP BY u.user_id, u.name, u.email
ORDER BY total_donated DESC;

/* 🕊️ 8️⃣ Active Campaigns by Location and Type */
SELECT 
    location,
    campaign_type,
    COUNT(campaign_id) AS total_campaigns,
    SUM(goal_amount) AS total_goal,
    SUM(collected_amount) AS total_collected
FROM campaigns
WHERE status = 'ACTIVE'
GROUP BY location, campaign_type
ORDER BY location, campaign_type;

/* 📜 9️⃣ Impact Stories with Images */
SELECT 
    s.story_id,
    s.title AS story_title,
    c.title AS campaign_title,
    s.content,
    s.posted_date,
    ii.image_url
FROM impact_stories s
LEFT JOIN campaigns c ON s.campaign_id = c.campaign_id
LEFT JOIN impact_images ii ON s.story_id = ii.story_id
ORDER BY s.posted_date DESC;

/* 📈 🔟 Campaign Type Summary (Performance) */
SELECT 
    campaign_type,
    COUNT(campaign_id) AS total_campaigns,
    SUM(goal_amount) AS total_goal,
    SUM(collected_amount) AS total_collected,
    ROUND((SUM(collected_amount) / SUM(goal_amount)) * 100, 2) AS avg_completion
FROM campaigns
GROUP BY campaign_type
ORDER BY avg_completion DESC;

/* 🧭 11️⃣ Filter Campaigns by Location */
SELECT 
    campaign_id,
    title,
    campaign_type,
    location,
    goal_amount,
    collected_amount,
    status
FROM campaigns
WHERE LOWER(location) = 'delhi';

/* 🔍 12️⃣ Search Campaigns by Type */
SELECT 
    campaign_id,
    title,
    goal_amount,
    collected_amount,
    status
FROM campaigns
WHERE campaign_type = 'EDUCATION'
ORDER BY collected_amount DESC;

/* 🧑‍🤝‍🧑 13️⃣ Verify User-Role Mapping */
SELECT 
    ur.user_id,
    u.name AS user_name,
    ur.role_id,
    r.role_name
FROM user_roles ur
JOIN users u ON ur.user_id = u.user_id
JOIN roles r ON ur.role_id = r.role_id
ORDER BY u.name;

/* 🧹 14️⃣ Find Campaigns That Have Reached or Exceeded Goal */
SELECT 
    campaign_id,
    title,
    goal_amount,
    collected_amount,
    ROUND((collected_amount / goal_amount) * 100, 2) AS completion_percent
FROM campaigns
WHERE collected_amount >= goal_amount;

/* ⏳ 15️⃣ Donations Pending or Failed */
SELECT 
    d.donation_id,
    u.name AS donor_name,
    c.title AS campaign_title,
    d.amount,
    d.status
FROM donations d
JOIN users u ON d.donor_id = u.user_id
JOIN campaigns c ON d.campaign_id = c.campaign_id
WHERE d.status IN ('PENDING', 'FAILED')
ORDER BY d.donation_date DESC;
