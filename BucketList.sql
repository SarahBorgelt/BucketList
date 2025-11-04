-- To create the correct database,
-- create a BucketList database in PostgreSQL and
-- run the below query to create the appropriate
-- table.

--If the table bucket_item already exists, it will
--be deleted. If it doesn’t exist, then it will
--be created.

DROP TABLE IF EXISTS bucket_item;

CREATE TABLE bucket_item (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    completed BOOLEAN DEFAULT FALSE
);
