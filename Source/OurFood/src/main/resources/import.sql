-- Seed Data

-- Organization
INSERT INTO `organization` (id, description, is_active, is_pending, name,size , type, url, parent_id) VALUES (1, 'Our food Agri Platform', 1, 0, 'Our Food Pvt. Ltd.', '2000', 'Agri', 'http://ourfood.in', NULL), (2, 'Our food Partners', 1, 0, 'Our Food Partners Pvt. Ltd.', '2000', 'Agri', 'http://ourfoodpartners.in', NULL); 

-- Users
INSERT INTO `user` (id, primary_email, first_name, last_name, password, is_active, is_verified, organization_id, profile_id, facebook_id, google_id) VALUES (1, 'power.admin@ourfood.in', 'Power', 'Admin', '$2a$10$2ANcV7YGmQLNLPyeTUrtV.mfXOk5HvBoQBnnsLKXukjNZZfLANTE.', 1, 1, 1, NULL, NULL, NULL);
INSERT INTO `user` (id, primary_email, first_name, last_name, password, is_active, is_verified, organization_id, profile_id, facebook_id, google_id) VALUES (2, 'primary.admin@ourfood.in', 'Primary', 'Admin', '$2a$10$y7P/SyYD6oBLbE9l.FE4UeARXwueocZ7AZc.9CIzrkZW2P8zsSv9e', 1, 1, 1, NULL, NULL, NULL);
INSERT INTO `user` (id, primary_email, first_name, last_name, password, is_active, is_verified, organization_id, profile_id, facebook_id, google_id) VALUES (3, 'secondary.admin@ourfood.in', 'Secondary', 'Admin', '$2a$10$jAT.mKJqUY4FpGayJcifz.d3nmRZrCqMmJIkKhPSOb.juOLM65Ywq', 1, 1, 1, NULL, NULL, NULL);

INSERT INTO role(name, code) VALUES("Platform Power Admin", "ROLE_PLATFORM_POWER_ADMIN");
INSERT INTO role(name, code) VALUES("Platform Primary Admin", "ROLE_PLATFORM_PRI_ADMIN");

INSERT INTO permission(name, code) VALUES("General Update", "PERM_GENERAL_UPDATE");
INSERT INTO permission(name, code) VALUES("Platform Update", "PERM_PLATFORM_UPDATE");

INSERT INTO role_permission(role_id, permission_id) VALUES(1, 1);
INSERT INTO role_permission(role_id, permission_id) VALUES(1, 2);
INSERT INTO role_permission(role_id, permission_id) VALUES(2, 1);
INSERT INTO role_permission(role_id, permission_id) VALUES(2, 2);

INSERT INTO user_role(user_id, role_id) VALUES (1,1);
INSERT INTO user_role(user_id, role_id) VALUES (2,1);
INSERT INTO user_role(user_id, role_id) VALUES (3,1);

