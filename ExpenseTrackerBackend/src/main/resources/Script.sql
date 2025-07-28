-- Insert Role if not exists (SUPER_ADMIN)
INSERT INTO roles (id, role_name, created_at, updated_at)
VALUES (1, 'SUPER_ADMIN', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Insert Tenant
INSERT INTO tenants (id, name, max_users_allowed, license_expiry, created_at, updated_at)
VALUES (1, 'GlobalTenant', 1000, '2099-12-31', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Insert SUPER_ADMIN User
INSERT INTO users (
    id,
    first_name,
    last_name,
    email,
    password,
    phone_number,
    gendar,
    tenant_id,
    created_by_id,
    created_at,
    updated_at
) VALUES (
    2,
    'Karunakar',
    'Thurlu',
    'superadmin@gmail.com',
    '$2a$10$djP2xj2pQL/qr.hSp6OOXu499OTWRr32NCN/F8luam1OekzZPXNDO', -- password: superadmin123
    '+911234567890',
    'MALE',
    1,         -- tenant_id
    NULL,      -- created_by_id is NULL for root SUPER_ADMIN
    NOW(),
    NOW()
);

-- Associate User with Role
INSERT INTO User_Roles (user_id, role_id)
VALUES (1, 1)
ON CONFLICT DO NOTHING;



