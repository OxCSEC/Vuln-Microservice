INSERT INTO users (username, email, password, full_name)
VALUES
    ('admin', 'admin@dvwa.local', 'password', 'DVWA Admin'),
    ('gordon', 'gordon@dvwa.local', 'abc123456', 'Gordon Brown'),
    ('1337', '1337@dvwa.local', 'charley123', 'Hack Me')
ON CONFLICT (username) DO NOTHING;
